package com.example.analysisimage.activity.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.os.*
import android.text.method.ScrollingMovementMethod
import android.util.*
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.analysisimage.Constants
import com.example.analysisimage.R
import com.example.analysisimage.bean.PlantBean
import com.example.analysisimage.network.BaseRequestCallBack
import com.example.analysisimage.network.OkHttpManager
import com.example.base_module.util.FileUtil
import com.example.base_module.util.SharedPreferenceUtil
import com.example.analysisimage.util.TextureMeteringPointFactory
import com.example.base_module.util.ImageUtil
import com.example.base_module.widget.LoadingView
import kotlinx.android.synthetic.main.activity_camerax.*
import okhttp3.FormBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.File
class PlantAnalysisActivityCameraX : AppCompatActivity() {

    private lateinit var textureView: TextureView
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var analysis: ImageAnalysis? = null
    private var camerarFacing: CameraX.LensFacing? = null

    private val UPDATE_IMAGE_TO_ANALYSISI = 1
    private val RESTART_ANALYSIS_IMAGE = 2
    private var start_analysis = false
    private var count = 0

    private var mPd = LoadingView(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax)
        textureView = findViewById(R.id.texture_view)

        camerarFacing = CameraX.LensFacing.BACK
        tv_analysis_result.movementMethod = ScrollingMovementMethod.getInstance()

        if (this.let {
                ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.CAMERA
                )
            } == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            takePreView()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE), 1)
        }

        textureView.setOnTouchListener { v, event ->   //点击 聚焦
            val meteringPoint = TextureMeteringPointFactory()
                .createPoint(event.getX(), event.getY())
            val action = FocusMeteringAction.Builder.from(meteringPoint).addPoint(meteringPoint).build()
            CameraX.getCameraControl(camerarFacing).startFocusAndMetering(action)
            return@setOnTouchListener false    //单纯写个false 也行
        }

        tv_analysis.setOnClickListener {
            start_analysis = false  //重新开启图像分析
            CameraX.bindToLifecycle(this,analysis)
        }

    }

    fun initPreView() {
        val metrix = DisplayMetrics().also { textureView.display.getRealMetrics(it) }
        val screenSize = Size(metrix.widthPixels, metrix.heightPixels)
        val screenAspectRatio = Rational(metrix.widthPixels, metrix.heightPixels)
        val handlerThread = HandlerThread("ImageAnalysis").apply { start() }

        val previewConfig = PreviewConfig.Builder()
            .setLensFacing(camerarFacing!!)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetResolution(screenSize).build()

        val imageCaptureConfig = ImageCaptureConfig.Builder()
            .setCaptureMode(ImageCapture.CaptureMode.MAX_QUALITY)
            .setLensFacing(camerarFacing!!)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(textureView.display.rotation)
            .build()

        val imageAnalysisConfig = ImageAnalysisConfig.Builder()
            .setTargetResolution(screenSize)
            .setTargetAspectRatio(screenAspectRatio)
            .setTargetRotation(textureView.display.rotation)
            .setImageReaderMode(ImageAnalysis.ImageReaderMode.ACQUIRE_LATEST_IMAGE)
            .setCallbackHandler(Handler(handlerThread.looper))

            .build()

        preview = Preview(previewConfig)
        imageCapture = ImageCapture(imageCaptureConfig)
        analysis = ImageAnalysis(imageAnalysisConfig)

        preview?.setOnPreviewOutputUpdateListener {
            textureView.surfaceTexture = it.surfaceTexture
//            textureView.surfaceTexture.setOnFrameAvailableListener { surfaceTexture: SurfaceTexture? ->
//
//            }

        }

        capture.setOnClickListener {
            val imageFile = FileUtil.createTempFile(System.currentTimeMillis().toString() + "", ".jpg")
            imageCapture?.takePicture(imageFile, object : ImageCapture.OnImageSavedListener {
                override fun onError(
                    imageCaptureError: ImageCapture.ImageCaptureError,
                    message: String,
                    cause: Throwable?
                ) {
                }

                override fun onImageSaved(file: File) {
                    Toast.makeText(
                        this@PlantAnalysisActivityCameraX,
                        "图片保存成功，地址为${file.absolutePath}",
                        Toast.LENGTH_LONG
                    ).show()
                }
            })
        }
        analysis?.setAnalyzer { image, rotationDegrees ->

            image.image
            val format = image.format
            var result = ""
            if(!start_analysis) {
                start_analysis = true
                if (format == ImageFormat.YUV_444_888 || format == ImageFormat.YUV_420_888 || format == ImageFormat.YUV_422_888) {
                    val byteArray = ImageUtil.getJpegImageForYUVData(Constants.APPString.APP_DIRECTORY_ABSOLUTEPATH + "xxxyyy${count}.jpeg",image?.image!!)
                    result = String(Base64.encode(byteArray, Base64.DEFAULT))
                } else {
                    Log.e("图片格式", "NV $format")
                }
                val message = Message()
                message.what = UPDATE_IMAGE_TO_ANALYSISI
                message.obj = result
                handler.sendMessage(message)
            }
        }
        CameraX.bindToLifecycle(this, preview, imageCapture, analysis)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1) {
            if (grantResults!![0] == PackageManager.PERMISSION_GRANTED) {
                takePreView()
            }
        }
    }

    fun takePreView() {
        textureView.surfaceTextureListener = object:TextureView.SurfaceTextureListener{
            override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
            }

            override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
//                val canvas = textureView.lockCanvas()

//                textureView.unlockCanvasAndPost(canvas)
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                initPreView()
            }
        }
    }


    private fun analysisImage(imageCode:String) {
        val builder = FormBody.Builder()
        builder.add("image", imageCode)
        mPd.show()
        OkHttpManager.instance.syncPlantAnalysisPost(
            Constants.ANALYSIS_IMAGE_FOR_PLANT + "?access_token=" + SharedPreferenceUtil.getInstance().getToken(),
            builder.build(),
            object : BaseRequestCallBack() {
                override fun onFailed(result: String) {
                    mPd.cancel()
                    start_analysis = false
                    Log.e("上传图片失败", result)
                }

                override fun onSucceed(result: String) {
                    mPd.cancel()
                    Log.e("上传图片有回调", result)
                    val jsonObject = JSONObject(result)
                    if(jsonObject.optJSONArray("result")!=null) {
                        analysis?.removeAnalyzer()
                        val plantList = dealAnalysisResult(jsonObject.optString("result")!!)
                        tv_analysis_result.text = "" //清空内容
                        for (plantBean in plantList!!.iterator()) {
                            tv_analysis_result.append("名称: ${plantBean?.    name} \n描述: ${plantBean?.description} \n\n")
                        }
                        start_analysis = true
                        CameraX.unbind(analysis) // 解除识别（分析）
                    }else{
                        start_analysis = false
                    }
                }

                override fun onNetworkFaild() {
                    mPd.cancel()
                    Log.e("上传图片", "网络问题")
                }
            })
    }

    fun findDetectForImage(imageCode:String,isPerson:Boolean){
        val builder = FormBody.Builder()
        builder.add("image",imageCode)
        builder.add("with_face",(if (!isPerson) 0 else { 1 }) as String)
        mPd.show()
        OkHttpManager.instance.syncPlantAnalysisPost(Constants.ANALYSIS_IMAGE_DETECT,builder.build(),object :BaseRequestCallBack(){
            override fun onSucceed(result: String) {
                mPd.cancel()
                Log.e("上传图片回调",result)
                val jsonObject = JSONObject(result)
                val result = jsonObject.optJSONObject("result")
                if (result != null){
                    val width = result.optInt("width")
                    val height = result.optInt("height")
                    val left = result.optInt("left")
                    val top = result.optInt("top")
                }else{

                }
            }

            override fun onNetworkFaild() {
                mPd.cancel()
                Log.e("上传图片", "网络问题")
            }

            override fun onFailed(result: String) {
                mPd.cancel()
                Log.e("上传图片失败", result)
            }

        })
    }

    fun dealAnalysisResult(result: String): List<PlantBean>? {
        val array = JSONArray(result)
        val plantList = ArrayList<PlantBean>()
        for (index in 0 until array.length()) {
            val json = array.optJSONObject(index)
            val plantBean = PlantBean("", "", "", "")
            plantBean.name = json.optString("name")
            if (null != json.optJSONObject("baike_info")) {
                val baike = json.optJSONObject("baike_info")
                plantBean.image = baike.optString("image_url")
                plantBean.description = baike.optString("description")
                plantBean.baike = baike.optString("baike_url")
            }
            plantList.add(plantBean)
        }
        return plantList
    }

    val handler = object: Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                UPDATE_IMAGE_TO_ANALYSISI -> analysisImage(msg?.obj as String)
                RESTART_ANALYSIS_IMAGE -> {
                    start_analysis = false
                    sendEmptyMessageDelayed(RESTART_ANALYSIS_IMAGE,1000)
                }
            }
        }
    }

    /**
     * 防止浪费资源   识别功能由用户手动点击开始
     */
    fun startAnalysis(){

    }
}