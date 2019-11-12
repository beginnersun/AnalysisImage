package com.example.analysisimage.activity

import android.Manifest
import android.app.Activity
import android.app.usage.ExternalStorageStats
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.database.Cursor
import android.graphics.*
import android.hardware.camera2.*
import android.location.Location
import android.location.LocationManager
import android.media.ImageReader
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.annotation.LayoutRes
import android.support.annotation.MainThread
import android.support.annotation.RequiresApi
import android.support.annotation.WorkerThread
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Base64
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.example.analysisimage.Constants
import com.example.analysisimage.R
import com.example.analysisimage.activity.BitmapUtil.*
import com.example.analysisimage.base.BaseActivity
import com.example.analysisimage.bean.PlantBean
import com.example.analysisimage.network.BaseRequestCallBack
import com.example.analysisimage.network.OkHttpManager
import com.example.analysisimage.util.BitmapUtil
import com.example.analysisimage.util.FileUtil
import com.example.analysisimage.util.SharedPreferenceUtil
import com.example.analysisimage.util.isHardwareLevelSupported
import kotlinx.android.synthetic.main.activity_plant.*
import okhttp3.FormBody
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.lang.StringBuilder
import java.net.URLEncoder
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.jvm.internal.Ref

@RequiresApi(21)
class PlantAnalysisActivity : AppCompatActivity() {

    companion object {
        private const val TAG = "PlantAnalysisActivity"
    }

    private val requestSelectImage = 1
    private val requestTakePhoto = 2
    private val requestCamera = 3
    private val requestPhoto = 4
    private val updateImageToBaidu = 5
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private var imageCode: String = ""
    private val REQUIRED_SUPPORTED_HARDWARE_LEVEL: Int =
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY

    private var frontCameraId: String = ""
    private var backCameraId: String = ""
    private var backCameraCharacteristics: CameraCharacteristics? = null
    private var previewSurface: Surface? = null
    private lateinit var mCameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var imageReader: ImageReader? = null
    private var mCameraSession: CameraCaptureSession? = null
    private var mCameraSensorOrientation = 0        //摄像头方向

    private var count = 58;


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant)

        tv_select_image.setOnClickListener { selectImage() }
        tv_crop_image.setOnClickListener { captureImage() }
        if (Build.VERSION.SDK_INT >= 23) {
            texture_view.surfaceTextureListener = PreviewSurfaceTextureListener()
        }
    }


    /**
     * 设置TextureView的监听回调
     */
    @RequiresApi(21)
    private inner class PreviewSurfaceTextureListener : TextureView.SurfaceTextureListener {
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        /**
         * 预览界面可用时
         * 这时初始化Camera相机并设置好参数
         * @param surface: 和TextureView.mSurface 是同一个
         */
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            initCamera()
        }
    }

    @RequiresApi(21)
    private fun initCamera() {
        mCameraManager = getSystemService(Context.CAMERA_SERVICE) as CameraManager
        val cameraIdList = mCameraManager.cameraIdList
        if (cameraIdList.isEmpty()) {
            Log.e(TAG, "没有发现可用相机")
            return
        }
        //遍历所有相机 然后找到后置相机 并且保存后置相机ID
        cameraIdList.forEach { cameraId ->
            val cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId)
            if (cameraCharacteristics.isHardwareLevelSupported(REQUIRED_SUPPORTED_HARDWARE_LEVEL)) {
                //这里的数组cameraCharacteristics[CameraCharacteristics.LENS_FACING] 其实是调用了
                // cameraCharacteristics.get(key:Key)方法 key就是CameraCharacteristics.LENS_FACING
                if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_FRONT) {  //判断是否有前置相机
                    Log.e("前置","有")
                    frontCameraId = cameraId
                } else if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK) {  //判断是否有后置
                    backCameraId = cameraId
                }
            }
        }
        backCameraCharacteristics = mCameraManager.getCameraCharacteristics(frontCameraId)
        mCameraSensorOrientation =
            backCameraCharacteristics?.get(CameraCharacteristics.SENSOR_ORIENTATION)!!  //获取摄像头方向  用于自动对焦
        val exchange = exchangeWidthAndHeight(windowManager.defaultDisplay.rotation, mCameraSensorOrientation)

        val previewSize =
            getOptimalSize(texture_view.width,texture_view.height, backCameraCharacteristics)  //获取最合适的预览大小
        //设置预览界面的大小      surfaceTexture 就是TextureView的getSurfaceTexture方法返回了mSurface
        if (previewSize!=null) {
            texture_view.surfaceTexture.setDefaultBufferSize(
                previewSize.width,
                previewSize.height
            )
        }

        if (exchange){
            imageReader = ImageReader.newInstance(texture_view.height, texture_view.width, ImageFormat.YUV_420_888, 5)
        }else {
            imageReader = ImageReader.newInstance(texture_view.width, texture_view.height, ImageFormat.YUV_420_888, 5)
        }
        imageReader?.setOnImageAvailableListener(OnMyImageAvailableListener(), handler)

        //调整方向
//        val orientation = resources.configuration.orientation
//        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
//            texture_view
//        }
        openCamera()
    }

    private fun captureImage() {
        val requestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        requestBuilder?.addTarget(imageReader?.surface)  //再加上ImageReader的surface 用来拍摄照片（或者获取单帧图片）
        requestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) //自动对焦
//        mCameraSensorOrientation = getDisplayRotation(backCameraCharacteristics!!)
        Log.e("屏幕方向","角度为${mCameraSensorOrientation}")
        requestBuilder?.set(CaptureRequest.JPEG_ORIENTATION, mCameraSensorOrientation)
        requestBuilder?.set(CaptureRequest.JPEG_QUALITY,100)
        mCameraSession?.capture(requestBuilder?.build(), null, handler)
    }

//    @WorkerThread
//    @RequiresApi(21)
//    private fun getLocation(): Location? {
//        val locationManager = getSystemService(LocationManager::class.java)
//        if (locationManager != null && ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            return locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER)
//        }
//        return null
//    }

    private var degrees = 0
    /**
     * 获取当前摄像头角度
     */
    private fun getDisplayRotation(cameraCharacteristics: CameraCharacteristics): Int {
        val rotation = windowManager.defaultDisplay.rotation
        degrees = when (rotation) {
            Surface.ROTATION_0 -> 0
            Surface.ROTATION_90 -> 90
            Surface.ROTATION_180 -> 180
            Surface.ROTATION_270 -> 270
            else -> 0
        }

        Log.e("屏幕方向","屏幕角度为${degrees}")
        val sensorOrientation = cameraCharacteristics[CameraCharacteristics.SENSOR_ORIENTATION]!!
        return if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_FRONT) {
            (360 - (sensorOrientation + degrees) % 360) % 360
        } else {
            (sensorOrientation - degrees + 360) % 360
        }
    }
    private fun exchangeWidthAndHeight(displayRotation: Int, sensorOrientation: Int): Boolean {
        var exchange = false
        when (displayRotation) {
            Surface.ROTATION_0, Surface.ROTATION_180 ->
                if (sensorOrientation == 90 || sensorOrientation == 270) {
                    exchange = true
                }
            Surface.ROTATION_90, Surface.ROTATION_270 ->
                if (sensorOrientation == 0 || sensorOrientation == 180) {
                    exchange = true
                }
            else -> Log.e("屏幕方向","Display rotation is invalid: $displayRotation")
        }

        Log.e("屏幕方向","屏幕方向  $displayRotation")
        degrees = sensorOrientation
        Log.e("屏幕方向","相机方向  $sensorOrientation")
        return exchange
    }

    fun mirror(rawBitmap: Bitmap): Bitmap {
        var matrix = Matrix()
        if (degrees == 270) {
            Log.e("屏幕方向","asdfasdf镜像")
            matrix.postScale(-1f,1f)
            matrix.setRotate(-90f)
        }else{
            matrix.setRotate(90f)
        }
        return Bitmap.createBitmap(rawBitmap, 0, 0, rawBitmap.width, rawBitmap.height, matrix, true)
    }

    @RequiresApi(21)
    private inner class OnMyImageAvailableListener : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader?) { //捕获到单帧图片
            val image = reader?.acquireLatestImage()

            if (null!=image){
                val plane_y = image!!.planes[0]
                val plane_u = image!!.planes[1]
                val plane_b = image!!.planes[2]
            }


            val width = image?.width
            val height = image?.height
            val byteArray = getDataFromImage(image, 1) //从Image中读取图片数据（注意此时数据是YUV格式的）
            var rawBitmap = getBitmapImageFromYUV(
                I420Tonv21(byteArray, width!!, height!!),
                width!!,
                height!!
            ) //I420 转为21（也就是YUV转为jpg） 然后再转为bitmap
            rawBitmap = mirror(rawBitmap)
            val out_Image = BitmapUtil.CompressBitmap(rawBitmap)//进行质量压缩  减小位深 但是不影响图片质量 适用于二进制数据传递
            var imageBase64Code = String(Base64.encode(out_Image, Base64.DEFAULT)) //编码为Base64
            val file = File(Environment.getExternalStorageDirectory().absolutePath + "/$count.txt")
            if (!file.exists()) {
                file.createNewFile()
            }
//            val out = FileOutputStream(file)
            imageBase64Code = imageBase64Code.replace("\n", "").replace("\r", "").replace("\r\n", "")  //移除内容中的换行
//            out.write(imageBase64Code.toByteArray())
//            out.close()
            Toast.makeText(this@PlantAnalysisActivity,"图片保存成功 地址为$count.txt",Toast.LENGTH_LONG).show()
            count = count + 5;
            pre_imageView.setImageBitmap(rawBitmap)
            image?.close()
        }
    }

    @RequiresApi(21)
    private fun openCamera() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
            mCameraManager.openCamera(frontCameraId, CameraStateCallBack(), handler) //打开相机 传入相机ID与回掉接口
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), requestCamera)
        }
    }

    @RequiresApi(21)
    private inner class CameraStateCallBack : CameraDevice.StateCallback() {
        override fun onError(camera: CameraDevice, error: Int) {
            Log.e("CameraCallBack:onError", Thread.currentThread().name)
        }

        override fun onDisconnected(camera: CameraDevice) {
        }

        override fun onOpened(camera: CameraDevice) {
            Log.e("CameraCallBack:onOpened", Thread.currentThread().name)
            cameraDevice = camera
            createCaptureSession()  //打开成功 保存相机设备Device 并且创建会话
        }
    }


    @RequiresApi(21)
    private fun createCaptureSession() {
        val requestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        val surface = Surface(texture_view.surfaceTexture)
        requestBuilder?.addTarget(surface)
        requestBuilder?.set(CaptureRequest.JPEG_QUALITY,100)
        requestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) // 自动对焦
        cameraDevice?.createCaptureSession(
            arrayListOf(surface, imageReader?.surface),
            object : CameraCaptureSession.StateCallback() {
                /**
                 * 会话创建成功  此时意味着两端链接成功 然后发起预览请求
                 */
                override fun onConfigured(session: CameraCaptureSession) {
                    mCameraSession = session
                    session.setRepeatingRequest(requestBuilder?.build(), mCaptureCallback(), handler)  //发起预览请求
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {

                }

            },
            handler
        )
    }

    override fun onResume() {
        super.onResume()
    }

    override fun onPause() {
        super.onPause()
//        mCameraSession?.stopRepeating()
    }

    /**
     * 预览请求回调接口
     */
    @RequiresApi(21)
    private inner class mCaptureCallback : CameraCaptureSession.CaptureCallback() {
        /**
         * 预览成功
         */
        override fun onCaptureCompleted(
            session: CameraCaptureSession,
            request: CaptureRequest,
            result: TotalCaptureResult
        ) {
            ImageFormat.JPEG
            super.onCaptureCompleted(session, request, result)  //这个result 里面没有实际的像素信息

        }

        override fun onCaptureFailed(session: CameraCaptureSession, request: CaptureRequest, failure: CaptureFailure) {
            super.onCaptureFailed(session, request, failure)
        }
    }

    /**
     * 判断是否有读取存储的条件 有->进行选择图片 无->申请权限
     */
    private fun selectImage() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0包括6.0拍照权限 或者读取手机存储 权限都要要手动申请
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED && ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.WRITE_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {  //判断是否有权限   有权限
                val intent = Intent()
                intent.action = Intent.ACTION_PICK
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
                startActivityForResult(intent, requestSelectImage)
            } else { //无权限
                ActivityCompat.requestPermissions(
                    this@PlantAnalysisActivity,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE),
                    requestPhoto
                )
            }
        } else {
            val intent = Intent()
            intent.action = Intent.ACTION_PICK
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*")
            startActivityForResult(intent, requestSelectImage)
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == requestCamera) {
            if (permissions != null && permissions.size > 0 && permissions[0] == Manifest.permission.CAMERA) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    openCamera()
                } else {

                }
            }
        } else if (requestCode == requestPhoto) {
            if (permissions != null && permissions.size > 1 && permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE
                && permissions[1] == Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                    selectImage()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestSelectImage) {
            if (resultCode == Activity.RESULT_OK) {
                val uri: Uri? = data?.data
                Glide.with(this).load(uri).into(imageView)
                val cursor: Cursor = contentResolver.query(uri, arrayOf(MediaStore.Images.Media.DATA), null, null, null)
                if (cursor.moveToFirst()) {
                    var filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    encodeImageByBase64(filepath)
                }
                cursor.close()
            }
        }
    }

    fun encodeImageByBase64(filepath: String) {
        singleThreadExecutor.execute(object : Runnable {
            override fun run() {
                val imageBase64Code = FileUtil.FileToBaseEncode(filepath)
                imageCode = imageBase64Code
                val file = File(Environment.getExternalStorageDirectory().absolutePath + "/11.txt")
                if (!file.exists()) {
                    file.createNewFile()
                }
                val out = FileOutputStream(file)
                out.write(imageBase64Code.toByteArray())
                out.close()
                val message = Message()
                message.what = updateImageToBaidu
                handler.sendMessage(message)
            }
        })
        singleThreadExecutor.shutdown()
    }

    private fun updateImage() {
        val builder = FormBody.Builder()
        builder.add("image", imageCode)
        OkHttpManager.instance.syncPlantAnalysisPost(
            Constants.ANALYSIS_IMAGE_FOR_PLANT + "?access_token=" + SharedPreferenceUtil.getInstance().getToken(),
            builder.build(),
            object : BaseRequestCallBack() {
                override fun onFailed(result: String) {
                    Log.e("上传图片失败", result)
                }

                override fun onSucceed(result: String) {
                    Log.e("上传图片有回调", result)
                    val plantBean = dealAnalysisResult(result)
                    tv_analysis_result_name.text = "名称:" + plantBean?.name
                    tv_analysis_result_description.text = "描述:" + plantBean?.description
                }

                override fun onNetworkFaild() {
                    Log.e("上传图片", "网络问题")
                }
            })
    }

    override fun onDestroy() {
        super.onDestroy()
        singleThreadExecutor.shutdownNow()
    }

    val handler = object : Handler() {
        override fun handleMessage(msg: Message?) {

            Log.e("上传图片", "3")
            super.handleMessage(msg)
            when (msg?.what) {
                updateImageToBaidu -> {
                    Log.e("上传图片", "4")
                    updateImage()
                }
                else -> {
                }
            }
        }
    }

    fun dealAnalysisResult(result: String): PlantBean? {
        val array = JSONArray(result)
        var plantBean: PlantBean = PlantBean("", "", "", "")
        for (index in 0..array.length()) {
            val json = array.optJSONObject(index)
            plantBean.name = json.optString("name")
            if (null != json.optJSONObject("baike_info")) {
                val baike = json.optJSONObject("baike_info")
                plantBean.image = baike.optString("image_url")
                plantBean.description = baike.optString("description")
                plantBean.baike = baike.optString("baike_url")
                return plantBean
            }
        }
        return plantBean
    }

    /**
     * 获取最佳的大小
     */
    @RequiresApi(21)
    fun getOptimalSize(maxWidth: Int, maxHeight: Int, cameraCharacteristics: CameraCharacteristics?): Size? {
        val aspectRatio = maxWidth.toFloat() / maxHeight
        val streamConfigurationMap = cameraCharacteristics!![CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
        //根据类型返回支持的尺寸数组
        // 类型有SurfaceHolder  SurfaceTexture 用来预览
        // ImageReader 用来拍照或者接收YUV数据
        // MediaRecorder 用来录制视频
        // MediaCodec 用来录制视频
        val supportSize = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
        var me_size:Size? = null
        if (supportSize != null) {
            supportSize.forEach { size ->
                Log.e("屏幕分辨率","width:${size.width}  height${size.height}")
                // && size.width <= maxWidth && size.height <= maxHeight
                if (size.width.toFloat() / size.height == aspectRatio) {
                    Log.e("选中分辨率为","width:${size.width}  height${size.height}")
                    me_size = size
                }
            }
        }
        return me_size
    }
//
//    override fun getLayoutId():Int {
//        return R.layout.activity_plant
//    }
}