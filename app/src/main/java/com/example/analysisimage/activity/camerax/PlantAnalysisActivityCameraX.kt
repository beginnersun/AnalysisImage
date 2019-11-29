package com.example.analysisimage.activity.camerax

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.SurfaceTexture
import android.opengl.GLSurfaceView
import android.os.*
import android.util.DisplayMetrics
import android.util.Log
import android.util.Rational
import android.util.Size
import android.view.TextureView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.*
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry
import com.example.analysisimage.R
import com.example.analysisimage.util.FileUtil
import com.example.analysisimage.util.TextureMeteringPointFactory
import kotlinx.android.synthetic.main.activity_camerax.*
import kotlinx.android.synthetic.main.activity_main.*
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




//        textureView.surfaceTexture.setOnFrameAvailableListener { surfaceTexture ->
//
//        }

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
        analysis?.setAnalyzer { image, rotationDegrees ->

            val rect = image.cropRect
            val format = image.format
            val width = image.width
            val height = image.height
            val planes = image.planes
            Log.e("Analysis图片", 1.toString())

        }
        CameraX.bindToLifecycle(this, preview, imageCapture, analysis)
        Environment.getExternalStorageDirectory()
//        Environment.getExternalStoragePublicDirectory(int type)  根据传入参数获取对应的SD中对应的目录
        getExternalFilesDir(null)   //获取应用目录 （卸载后会随之删除）
//        context.externalCacheDir()  获取 应用  缓存路径

        openFileInput("").bufferedReader().useLines { lines ->
            lines.fold("") { some, text ->
                "$some\n$text"
            }
        }
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


//    override fun getLifecycle(): Lifecycle {
//        return lifecycleRegistry
//    }

}