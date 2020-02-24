package com.example.base_module.widget

import android.app.Service
import android.content.Context
import android.graphics.SurfaceTexture
import android.media.AudioFocusRequest
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.util.AttributeSet
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.Group
import com.example.base_module.R
import com.example.base_module.util.SystemVolumeObserver
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.util.*
import kotlin.math.absoluteValue


class VideoPlayer : FrameLayout, TextureView.SurfaceTextureListener ,SystemVolumeObserver.OnVolumeChangedListener{


    override fun onVolumeChanged(volume: Int) {
        volumeSeek = true
        volume_SeekBar?.progress = volume
    }

    constructor(context: Context) : super(context) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }

    private var group: Group? = null
    private var mediaPlayer: IjkMediaPlayer? = null
    private var textureView: TextureView? = null
    private var surfaceTexture: SurfaceTexture? = null
    private var surface: Surface? = null
    private var audioManager: AudioManager? = null
    private var maxVolume: Int = 0
    private var mVolume: Int = 0
    private var mUrl: String = ""

    private var volume_SeekBar: SeekBar? = null
    private var video_SeekBar: SeekBar? = null
    private var tvTitle: TextView? = null
    private var tvTime: TextView? = null
    private var ivPlay: ImageView? = null
    private var ivVolume: ImageView? = null
    private var ivFullScreen: ImageView? = null
    var callBack: VideoListenerCallBack? = null
    private var durationTime:String = ""

    private var mCurrentState: Int = 0
    private var VIDEO_STATE_READY = 0x00  //视频源准备好
    private var VIDEO_STATE_START = 0x01  //开始播放
    private var VIDEO_STATE_PAUSE = 0x02  //暂停
    private var VIDEO_STATE_STOP = 0x03  //停止
    private var VIDEO_STATE_ERROR = 0x04  //停止

    private var gestureDetector: GestureDetector? = null
    private var timer = Timer()

    private var timerSeek = false
    private var volumeSeek = false
    private var fullScreen = false

    fun setUrl(url: String) {
        mUrl = url
        release()
        prepareMedia()
    }

    fun start(){
        if (mediaPlayer!=null && mCurrentState != VIDEO_STATE_ERROR){
            mediaPlayer!!.start()
        }
    }

    private fun initView() {
        Log.e("VideoPlayer","initView")
        audioManager = context.getSystemService(Service.AUDIO_SERVICE) as AudioManager?
        var view = LayoutInflater.from(context).inflate(R.layout.widget_video_player_view, this)
        group = view.findViewById(R.id.group)
        textureView = view.findViewById(R.id.texture_view)
        volume_SeekBar = view.findViewById(R.id.volume_seekbar)
        video_SeekBar = view.findViewById(R.id.seek_bar)
        tvTitle = view.findViewById(R.id.tv_title)
        tvTime = view.findViewById(R.id.tv_time)
        ivPlay = view.findViewById(R.id.iv_play)
        ivVolume = view.findViewById(R.id.iv_volume)
        ivFullScreen = view.findViewById(R.id.iv_full_screen)

        video_SeekBar?.setOnSeekBarChangeListener(object :SeekBar.OnSeekBarChangeListener{
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }

            override fun onStopTrackingTouch(seekBar: SeekBar?) {

            }

            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!timerSeek) {
                    seekTo(progress.toLong())
                }else{
                    timerSeek = false
                }
            }

        })

        initGestureDetector()
        textureView!!.setOnTouchListener { _, event ->
            gestureDetector?.onTouchEvent(event)
            true
        }

        ivPlay!!.setOnClickListener{
            if (mCurrentState == VIDEO_STATE_PAUSE){
                ivPlay!!.setImageResource(R.mipmap.play)
                start()
                timer?.schedule(task,0,1000)
            }else if (mCurrentState == VIDEO_STATE_START){
                ivPlay!!.setImageResource(R.mipmap.pause)
                mediaPlayer?.pause()
                timer.cancel()
            }
        }

        ivVolume!!.setOnClickListener {
            volume_SeekBar?.visibility = if(volume_SeekBar?.visibility == View.VISIBLE) View.GONE else View.VISIBLE
        }

        ivFullScreen!!.setOnClickListener {
            fullScreen = !fullScreen
            if (callBack != null) {
                callBack!!.fullScreen(fullScreen)
            }
            if (fullScreen){
                ivFullScreen!!.setImageResource(R.mipmap.fullscreen)
            }else{
                ivFullScreen!!.setImageResource(R.mipmap.fullscreen_exit)
            }
        }

        group!!.postDelayed({
            group?.visibility = GONE
        },3000)


    }

    private var mediaPositionHandler = Handler{
        if (it.what == 1){
            tvTime?.text = "${getCurrentTime()}/${durationTime}"
            timerSeek = true
            video_SeekBar!!.progress = mediaPlayer!!.currentPosition.toInt()
        }
        false
    }

    var task:TimerTask = object :TimerTask(){
        override fun run() {
            mediaPositionHandler.sendEmptyMessage(1)
        }
    }

    private fun initGestureDetector() {
        Log.e("VideoPlayer","initGestureDetector")
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {
            override fun onShowPress(e: MotionEvent?) {

            }

            override fun onSingleTapUp(e: MotionEvent?): Boolean {  //轻触屏幕  单击（抬手便会执行）
                group!!.visibility = if (group!!.visibility == View.GONE) View.VISIBLE else View.GONE
                group!!.requestLayout()
                return true
            }

            override fun onDown(e: MotionEvent?): Boolean {
                return true
            }

            /**
             * 在拖动时 触发  distanceX 与  distanceY 代表x y方向上移动的距离
             * @param e1  起始位置
             * @param e2  结束位置
             * 如果起点位于左半边那么 上下滑动改变亮度  右半边改变音量
             */
            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                Log.e("VideoPlayer","参数值 e1: ${e1?.x} ${e1?.y}")
                Log.e("VideoPlayer","参数值 e2: ${e2?.x} ${e2?.y}")
                Log.e("VideoPlayer","参数值 distanceX:${distanceX}")
                Log.e("VideoPlayer","参数值 distanceY:${distanceY}")
                if (e1!!.x > textureView!!.width/2){    //大于表示在右半边 改变音量
                    if (distanceY.absoluteValue >= 20){
                        if (distanceY > 0){
                            addAdjustVolume(AudioManager.ADJUST_RAISE,AudioManager.FLAG_SHOW_UI)
                        }else{
                            addAdjustVolume(AudioManager.ADJUST_LOWER,AudioManager.FLAG_SHOW_UI)
                        }
                    }
                }else{

                }

                return true
            }

            override fun onLongPress(e: MotionEvent?) {

            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return true
            }

            /**
             * 单击屏幕 如果再触发一次 会变成双击（并且这个方法不会执行）
             */
            override fun onSingleTapConfirmed(e: MotionEvent?): Boolean {
                return false
            }

            override fun onDoubleTap(e: MotionEvent?): Boolean {
                if (mCurrentState == VIDEO_STATE_PAUSE) {
                    mediaPlayer?.start()
                    timer.schedule(task,0,1000)
                } else if (mCurrentState == VIDEO_STATE_START) {
                    mediaPlayer?.pause()
                    timer.cancel()
                }
                return false
            }
        })
    }

    /**
     * 开始播放
     */
    private fun prepareMedia() {
        Log.e("VideoPlayer","prepareMedia")
        initAudioManager()
        initMediaPlayer()
        initTexTureView()
        openMediaPlayer()
    }

    /**
     * 跳转进度
     */
    fun seekTo(progress: Long) {
        mediaPlayer?.seekTo(progress)
    }

    /**
     * 调整音量
     * @param direction  AudioManager.ADJUST_LOWER 调小
     *                    AudioManager.ADJUST_RAISE 调大
     * @param flags AudioManager.FLAG_FLAG_SHOW_UI 显示进度
     * @param flags AudioManager.FLAG_SHOW_UI 播放声音
     */
    fun addAdjustVolume(direction: Int, flags: Int) {
        audioManager?.adjustStreamVolume(AudioManager.STREAM_MUSIC, direction, flags)
        mVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)!!
        Log.e("VideoPlayer","当前音量${mVolume}")
    }

    /**
     * 直接设置音量大小
     */
    fun setStreamVolume(volume: Int, flags: Int) {
        mVolume = volume
        timerSeek = true
        volume_SeekBar?.progress = mVolume
        audioManager?.setStreamVolume(AudioManager.STREAM_MUSIC, volume, flags)
        Log.e("VideoPlayer","当前音量set${mVolume}")
    }

    fun addAdjustBrightness(){

    }

    /**
     * 初始化音量信息
     */
    fun getVolume() = mVolume

    private fun initAudioManager() {
        Log.e("VideoPlayer","initAudioManager")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            audioManager?.requestAudioFocus(AudioFocusRequest.Builder(AudioManager.AUDIOFOCUS_GAIN).build())
        } else {
            audioManager?.requestAudioFocus(null, AudioManager.STREAM_MUSIC, AudioManager.AUDIOFOCUS_GAIN)
        }
        maxVolume = audioManager?.getStreamMaxVolume(AudioManager.STREAM_MUSIC)!!
        volume_SeekBar?.max = maxVolume
        mVolume = audioManager?.getStreamVolume(AudioManager.STREAM_MUSIC)!!
        volume_SeekBar?.progress = mVolume


        volume_SeekBar?.setOnSeekBarChangeListener(object:SeekBar.OnSeekBarChangeListener{
            override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                if (!volumeSeek) {
                    setStreamVolume(progress, AudioManager.FLAG_SHOW_UI)
                }else{
                    volumeSeek = false
                }
            }
            override fun onStartTrackingTouch(seekBar: SeekBar?) {
            }
            override fun onStopTrackingTouch(seekBar: SeekBar?) {
            }
        })
        SystemVolumeObserver.instance.registerVolumeBroadCast(this)
    }

    /**
     * 初始化视图渲染
     */
    private fun initTexTureView() {
        if (textureView != null) {
            Log.e("VideoPlayer","initTexTureView")
            textureView!!.apply {
                surfaceTextureListener = this@VideoPlayer
            }
        }
    }

    /**
     * 初始化多媒体资源
     */
    private fun initMediaPlayer() {
        if (mediaPlayer == null) {
            Log.e("VideoPlayer","initMediaPlayer")
            mediaPlayer = IjkMediaPlayer()
            mediaPlayer?.setAudioStreamType(AudioManager.STREAM_MUSIC)

            mediaPlayer!!.setOnPreparedListener {
                Log.e("VideoPlayer状态", "准备完成")
                initVideoSeekBar()
                timer.schedule(task,0,1000)
                callBack?.stationChanged(VIDEO_STATE_START,null)
            }

            mediaPlayer!!.setOnBufferingUpdateListener { _, precent ->
                var info = "缓冲了$precent 的数据"
                Log.e("VideoPlayer状态", info)
            }

            mediaPlayer!!.setOnTimedTextListener { _, text ->
                Log.e("VideoPlayer TimedText",text.text)
            }

            mediaPlayer!!.setOnSeekCompleteListener {
                Log.e("VideoPlayer","播放进度完成")
            }

            mediaPlayer!!.setOnCompletionListener {
                Log.e("VideoPlayer","播放完成")
            }
        }
    }

    /**
     * 初始化视频信息
     */
    private fun initVideoSeekBar() {
        video_SeekBar?.max = getDuration()!!.toInt()
        durationTime = getDurationTime()
        tvTime?.text = "${getCurrentTime()}/${durationTime}"
    }

    private fun getDuration() =
        mediaPlayer?.duration

    private fun getDurationTime(): String {
        val totalSeconds = getDuration()!!.div(1000)
        val seconds = totalSeconds % 60
        val minutes = totalSeconds / 60 % 60
        val hours = totalSeconds / 3600
        return if (hours > 0)
            String.format("%02d:%02d:%02d", hours, minutes, seconds)
        else String.format(
            "%02d:%02d",
            minutes,
            seconds
        )
    }

    private fun getCurrentTime(): String {
        if (mediaPlayer!=null) {
            val totalSeconds = mediaPlayer?.currentPosition!!.div(1000)
            val seconds = totalSeconds % 60
            val minutes = totalSeconds / 60 % 60
            val hours = totalSeconds / 3600
            return if (hours > 0)
                String.format("%02d:%02d:%02d", hours, minutes, seconds)
            else String.format(
                "%02d:%02d",
                minutes,
                seconds
            )
        }else{
           return "00:00"
        }
    }

    /**
     * TexTureView 状态发生改变
     */
    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {

    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {

    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return surfaceTexture == null
    }

    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        if (surfaceTexture == null) {
            surfaceTexture = surface
            openMediaPlayer()
        } else {
            textureView?.surfaceTexture = surfaceTexture
        }
    }

    /**
     * 打开并准备播放
     */
    private fun openMediaPlayer() {
        if (TextUtils.isEmpty(mUrl) || surfaceTexture == null){
            return
        }
        keepScreenOn = true   //屏幕常亮
        mediaPlayer?.setDataSource(context, Uri.parse(mUrl))
        if (surface == null) {
            surface = Surface(surfaceTexture)
        }
        mediaPlayer?.setSurface(surface)
        mediaPlayer?._prepareAsync()
        ivPlay!!.setImageResource(R.mipmap.pause)

    }

    /**
     * 停止播放
     */
    fun stopPlayback() {
        if (mediaPlayer != null) {
            mediaPlayer!!.stop()
            mediaPlayer!!.release()
            mediaPlayer = null
            mCurrentState = VIDEO_STATE_STOP
            audioManager!!.abandonAudioFocus(null)
        }
        timer.cancel()
        SystemVolumeObserver.instance.unregisterVolume()
        IjkMediaPlayer.native_profileEnd()
    }

    /**
     * 释放资源
     */
    private fun release() {

        Log.e("VideoPlayer","release")
        if (mediaPlayer != null) {
            mediaPlayer!!.reset()
            mediaPlayer!!.release()
            mediaPlayer = null
            audioManager?.abandonAudioFocus(null)
        }
    }

    /**
     * 视频播放状态回调
     */
    interface VideoListenerCallBack {

        /**
         * VIDEO_STATE_START = 0x01  //开始播放
         * VIDEO_STATE_PAUSE = 0x02  //暂停
         * VIDEO_STATE_STOP = 0x03   //停止
         * VIDEO_STATE_ERROR = 0x04  //出错
         */
        fun stationChanged(station: Int, message: Message?)

        /**
         * 跳转到全屏
         */
        fun fullScreen(full:Boolean)


        fun share()
    }
}
