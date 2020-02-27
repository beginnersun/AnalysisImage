package com.example.analysisimage.activity

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.*
import android.hardware.camera2.*
import android.media.ImageReader
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import com.bumptech.glide.Glide
import com.example.base_module.Constants
import com.example.analysisimage.R
import com.example.analysisimage.bean.PlantBean
import com.example.analysisimage.network.BaseRequestCallBack
import com.example.analysisimage.network.OkHttpManager
import com.example.base_module.util.isHardwareLevelSupported
import kotlinx.android.synthetic.main.activity_plant.*
import okhttp3.FormBody
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.util.concurrent.Executors
import com.example.base_module.util.BitmapUtil
import com.example.base_module.util.FileUtil
import com.example.base_module.util.SharedPreferenceUtil

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
    private var cameraId: String = ""
    private var cameraCharacteristics: CameraCharacteristics? = null
    private var previewSurface: Surface? = null
    private lateinit var mCameraManager: CameraManager
    private var cameraDevice: CameraDevice? = null
    private var imageReader: ImageReader? = null
    private var mCameraSession: CameraCaptureSession? = null

    private var count = 58
    private val cameraHandler:Handler
    private val handlerThread:HandlerThread
    private var useFront = true

    init {
        handlerThread = HandlerThread("CameraThread")
        handlerThread.start()
        cameraHandler = Handler(handlerThread.looper)
        handlerThread.quitSafely()
    }

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
            releaseCamera()
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
        if(useFront){
            cameraId = frontCameraId
        }else{
            cameraId = backCameraId
        }
        cameraCharacteristics = mCameraManager.getCameraCharacteristics(cameraId)

        val previewSize =
            getOptimalSize(texture_view.width,texture_view.height, cameraCharacteristics)  //获取最合适的预览大小
        //设置预览界面的大小      surfaceTexture 就是TextureView的getSurfaceTexture方法返回了mSurface
        if (previewSize!=null) {
            texture_view.surfaceTexture.setDefaultBufferSize(
                previewSize.width,
                previewSize.height
            )
        }
        imageReader = ImageReader.newInstance(texture_view.width, texture_view.height, ImageFormat.JPEG, 3)
        imageReader?.setOnImageAvailableListener(OnMyImageAvailableListener(), handler)

        openCamera()
    }

    private fun captureImage() {
        val requestBuilder = cameraDevice?.createCaptureRequest(CameraDevice.TEMPLATE_STILL_CAPTURE)
        requestBuilder?.addTarget(imageReader?.surface!!)  //再加上ImageReader的surface 用来拍摄照片（或者获取单帧图片）
//        requestBuilder?.set(CaptureRequest.CONTROL_AF_MODE, CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE) //自动对焦
        requestBuilder?.set(CaptureRequest.CONTROL_AF_TRIGGER, CameraMetadata.CONTROL_AF_TRIGGER_START) //锁定焦点
        requestBuilder?.set(CaptureRequest.JPEG_ORIENTATION,getJpegOrientation(cameraCharacteristics!!,windowManager.defaultDisplay.rotation))
        requestBuilder?.set(CaptureRequest.JPEG_QUALITY,100)
        mCameraSession?.capture(requestBuilder?.build()!!, null, handler)
    }

    private var degrees = 0

    private fun getJpegOrientation(cameraCharacteristics: CameraCharacteristics, deviceOrientation: Int): Int {
        var myDeviceOrientation = deviceOrientation
        if (myDeviceOrientation == android.view.OrientationEventListener.ORIENTATION_UNKNOWN) {
            return 0
        }
        val sensorOrientation = cameraCharacteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)!!

        // Round device orientation to a multiple of 90
        myDeviceOrientation = (myDeviceOrientation + 45) / 90 * 90

        // Reverse device orientation for front-facing cameras
        val facingFront = cameraCharacteristics.get(CameraCharacteristics.LENS_FACING) == CameraCharacteristics.LENS_FACING_FRONT
        if (facingFront) {
            myDeviceOrientation = -myDeviceOrientation
        }

        // Calculate desired JPEG orientation relative to camera orientation to make
        // the image upright relative to the device orientation
        return (sensorOrientation + myDeviceOrientation + 360) % 360
    }

    @RequiresApi(21)
    private inner class OnMyImageAvailableListener : ImageReader.OnImageAvailableListener {
        override fun onImageAvailable(reader: ImageReader?) { //捕获到单帧图片
            val image = reader?.acquireNextImage()
            val buffer = image!!.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            val rawBitmap = BitmapFactory.decodeByteArray(data, 0, data.size)
            val newBitmap = BitmapUtil.convertBitmap(rawBitmap)
            pre_imageView.setImageBitmap(newBitmap)
            image?.close()
//
//            Image mImage = reader.acquireNextImage();
//            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
//            byte[] data = new byte[buffer.remaining()];
//            buffer.get(data);
//            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
//            Camera2Util.createSavePath(Camera2Config.PATH_SAVE_PIC);//判断有没有这个文件夹，没有的话需要创建
//            picSavePath = Camera2Config.PATH_SAVE_PIC + "IMG_" + timeStamp + ".jpg";
//            FileOutputStream fos = null;
//            try {
//                fos = new FileOutputStream(picSavePath);
//                fos.write(data, 0, data.length);
//
//                Message msg = new Message();
//                msg.what = CAPTURE_OK;
//                msg.obj = picSavePath;
//                mCameraHandler.sendMessage(msg);
//            } catch (IOException e) {
//                e.printStackTrace();
//            } finally {
//                if (fos != null) {
//                    try {
//                        fos.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//            mImage.close()

//            val image = reader?.acquireLatestImage()
//
//            if (null!=image){
//                val plane_y = image!!.planes[0]
//                val plane_u = image!!.planes[1]
//                val plane_b = image!!.planes[2]
//            }
//
//
//            val width = image?.width
//            val height = image?.height
//            val byteArray = getDataFromImage(image, 1) //从Image中读取图片数据（注意此时数据是YUV格式的）
//            var rawBitmap = getBitmapImageFromYUV(
//                I420Tonv21(byteArray, width!!, height!!),
//                width!!,
//                height!!
//            ) //I420 转为21（也就是YUV转为jpg） 然后再转为bitmap
//            rawBitmap = mirror(rawBitmap)
//            val out_Image = BitmapUtil.CompressBitmap(rawBitmap)//进行质量压缩  减小位深 但是不影响图片质量 适用于二进制数据传递
//            var imageBase64Code = String(Base64.encode(out_Image, Base64.DEFAULT)) //编码为Base64
//            val file = File(Environment.getExternalStorageDirectory().absolutePath + "/$count.txt")
//            if (!file.exists()) {
//                file.createNewFile()
//            }
//            val out = FileOutputStream(file)
//            imageBase64Code = imageBase64Code.replace("\n", "").replace("\r", "").replace("\r\n", "")  //移除内容中的换行
//            out.write(imageBase64Code.toByteArray())
//            out.close()
//            Toast.makeText(this@PlantAnalysisActivity,"图片保存成功 地址为$count.txt",Toast.LENGTH_LONG).show()
//            count = count + 5;
//            pre_imageView.setImageBitmap(rawBitmap)
//            image?.close()
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
                    session.setRepeatingRequest(requestBuilder?.build()!!, mCaptureCallback(), handler)  //发起预览请求
                }

                override fun onConfigureFailed(session: CameraCaptureSession) {
                }
            },
            handler
        )
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
            if (permissions.size > 1 && permissions[0] == Manifest.permission.READ_EXTERNAL_STORAGE
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
                val cursor: Cursor = contentResolver.query(uri!!, arrayOf(MediaStore.Images.Media.DATA), null, null, null)!!
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
        releaseCamera()
        releaseTask()
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
        val supportSize = streamConfigurationMap!!.getOutputSizes(SurfaceTexture::class.java)
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

    fun releaseCamera() {
        mCameraSession?.close()
        mCameraSession = null

        cameraDevice?.close()
        cameraDevice = null

        imageReader?.close()
        imageReader = null

    }

    fun releaseTask(){
        cameraHandler?.removeCallbacksAndMessages(null)
        handlerThread.quitSafely()
    }

    fun exchangeCamera(){
        releaseCamera()
        useFront = !useFront
        initCamera()
    }

}