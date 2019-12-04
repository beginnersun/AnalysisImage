package com.example.analysisimage.activity.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.ImageFormat
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.os.*
import android.util.*
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.analysisimage.Constants
import com.example.analysisimage.R
import com.example.analysisimage.bean.PlantBean
import com.example.analysisimage.network.BaseRequestCallBack
import com.example.analysisimage.network.OkHttpManager
import com.example.analysisimage.util.FileUtil
import com.example.analysisimage.util.SharedPreferenceUtil
import com.example.analysisimage.util.TextureMeteringPointFactory
import kotlinx.android.synthetic.main.activity_camerax.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_plant.*
import okhttp3.FormBody
import okhttp3.internal.format
import org.json.JSONArray
import java.io.File
//, LifecycleOwner
class PlantAnalysisActivityCameraX : AppCompatActivity() {

    private lateinit var textureView: TextureView
    private var preview: Preview? = null
    private var imageCapture: ImageCapture? = null
    private var analysis: ImageAnalysis? = null
    private val framgent = CameraXFragment()
//    private var lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)
    private var camerarFacing: CameraX.LensFacing? = null

    private var glsurafce: GLSurfaceView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camerax)
        textureView = findViewById(R.id.texture_view)

        camerarFacing = CameraX.LensFacing.BACK

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

        textureView.setOnTouchListener { v, event ->

            val meteringPoint = TextureMeteringPointFactory().createPoint(event.getX(), event.getY())
            val action = FocusMeteringAction.Builder.from(meteringPoint).addPoint(meteringPoint).build()
            CameraX.getCameraControl(camerarFacing).startFocusAndMetering(action)
            return@setOnTouchListener false    //单纯写个false 也行
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
        val a = "string"
        analysis?.setAnalyzer { image, rotationDegrees ->

            val rect = image.cropRect
            val format = image.format
            val width = image.width
            val height = image.height
            val planes = image.planes
            val buffer = image.planes[0].buffer
            val data = ByteArray(buffer.remaining())
            buffer.get(data)
            if (format == ImageFormat.YUV_444_888 || format == ImageFormat.YUV_420_888 || format == ImageFormat.YUV_422_888){
                Log.e("图片格式","YUV")
            } else{
                Log.e("图片格式","NV $format")
            }
            val result = String(Base64.encode(data, Base64.DEFAULT))
            updateImage(result)
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
            }

            override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
                return false
            }

            override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
                initPreView()
            }
        }
    }


    private fun updateImage(imageCode:String) {
        analysis?.removeAnalyzer() //图片开始上传就取消
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

    fun dealAnalysisResult(result: String): PlantBean? {
        val array = JSONArray(result)
        var plantBean = PlantBean("", "", "", "")
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
}