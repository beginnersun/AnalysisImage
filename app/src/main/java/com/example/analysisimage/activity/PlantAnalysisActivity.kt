package com.example.analysisimage.activity

import android.Manifest
import android.app.Activity
import android.app.usage.ExternalStorageStats
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import android.net.Uri
import android.os.*
import android.provider.MediaStore
import android.support.annotation.LayoutRes
import android.support.annotation.RequiresApi
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.util.Size
import android.view.Surface
import android.view.TextureView
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.analysisimage.Constants
import com.example.analysisimage.R
import com.example.analysisimage.base.BaseActivity
import com.example.analysisimage.bean.PlantBean
import com.example.analysisimage.network.BaseRequestCallBack
import com.example.analysisimage.network.OkHttpManager
import com.example.analysisimage.util.FileUtil
import com.example.analysisimage.util.SharedPreferenceUtil
import com.example.analysisimage.util.isHardwareLevelSupported
import kotlinx.android.synthetic.main.activity_plant.*
import okhttp3.FormBody
import org.json.JSONArray
import java.io.File
import java.io.FileOutputStream
import java.net.URLEncoder
import java.util.concurrent.Executors
import java.util.concurrent.ThreadPoolExecutor
import kotlin.jvm.internal.Ref

class PlantAnalysisActivity :AppCompatActivity(){

    private val requestSelectImage = 1
    private val requestTakePhoto = 2
    private val updateImageToBaidu = 3
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()
    private var imageCode:String = ""
    private val REQUIRED_SUPPORTED_HARDWARE_LEVEL:Int =
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY

    private var frontCameraId:String = ""
    private var backCameraId:String = ""
    private var backCameraCharacteristics : CameraCharacteristics? =null
    private var previewSurface:Surface? =null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant)

        tv_select_image.setOnClickListener { selectImage() }

        if (Build.VERSION.SDK_INT >= 23) {
            texture_view.surfaceTextureListener = PreviewSurfaceTextureListener()

            val cameraManager: CameraManager by lazy { getSystemService(CameraManager::class.java) }
            val cameraIdList = cameraManager.cameraIdList
            cameraIdList.forEach { cameraId ->
                val cameraCharacteristics = cameraManager.getCameraCharacteristics(cameraId)
                if (cameraCharacteristics.isHardwareLevelSupported(REQUIRED_SUPPORTED_HARDWARE_LEVEL)){
                    //这里的数组cameraCharacteristics[CameraCharacteristics.LENS_FACING] 其实是调用了
                    // cameraCharacteristics.get(key:Key)方法 key就是CameraCharacteristics.LENS_FACING
                    if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_FRONT) {  //判断是否有前置相机
                        frontCameraId = cameraId
                    }else if (cameraCharacteristics[CameraCharacteristics.LENS_FACING] == CameraCharacteristics.LENS_FACING_BACK){  //判断是否有后置
                        backCameraId = cameraId
                    }
                }
            }
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                backCameraCharacteristics = cameraManager.getCameraCharacteristics(backCameraId)
                cameraManager.openCamera(backCameraId, CameraStateCallBack(), handler)
            }
        }
    }

    @RequiresApi(21)
    fun getOptimalSize(maxWidth:Int,maxHeight:Int,cameraCharacteristics: CameraCharacteristics?):Size?{
        val aspectRatio = maxWidth.toFloat() / maxHeight
        val streamConfigurationMap = cameraCharacteristics!![CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP]
        val supportSize = streamConfigurationMap.getOutputSizes(SurfaceTexture::class.java)
        if (supportSize != null){
            supportSize.forEach { size->
                if(size.width.toFloat() / size.height == aspectRatio && size.width <= maxWidth && size.height <= maxHeight){
                    return size
                }
            }
        }
        return null
    }

    @RequiresApi(21)
    private inner class CameraStateCallBack : CameraDevice.StateCallback(){
        override fun onError(camera: CameraDevice, error: Int) {
            Log.e("CameraCallBack:onError",Thread.currentThread().name)

        }

        override fun onDisconnected(camera: CameraDevice) {
        }

        override fun onOpened(camera: CameraDevice) {
            Log.e("CameraCallBack:onOpened",Thread.currentThread().name)
            runOnUiThread {  }
        }
    }

    @RequiresApi(21)
    private inner class PreviewSurfaceTextureListener:TextureView.SurfaceTextureListener{
        override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
        }

        override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
        }

        override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
            return false
        }

        /**
         * 可用时回调
         */
        override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
            val previewSize = getOptimalSize(200,200,backCameraCharacteristics)
            surface?.setDefaultBufferSize(previewSize!!.width,previewSize!!.height)
            previewSurface = Surface(surface)
        }

    }

    /**
     * 判断是否有读取存储的条件 有->进行选择图片 无->申请权限
     */
    private fun selectImage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0包括6.0拍照权限 或者读取手机存储 权限都要要手动申请
            if (ContextCompat.checkSelfPermission(this,Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED  && ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {  //判断是否有权限   有权限
                val intent = Intent()
                intent.action =Intent.ACTION_PICK
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
                startActivityForResult(intent,requestSelectImage)
            }
            else { //无权限
                ActivityCompat.requestPermissions(this@PlantAnalysisActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.WRITE_EXTERNAL_STORAGE),1)
            }
        }else{
            val intent = Intent()
            intent.action =Intent.ACTION_PICK
            intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
            startActivityForResult(intent,requestSelectImage)
        }
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == requestSelectImage){
            if (resultCode == Activity.RESULT_OK){
                val uri: Uri? = data?.data
                Glide.with(this).load(uri).into(imageView)
                val cursor:Cursor = contentResolver.query(uri,arrayOf(MediaStore.Images.Media.DATA),null,null,null)
                if (cursor.moveToFirst()){
                    var filepath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA))
                    encodeImageByBase64(filepath)
                }
                cursor.close()
            }
        }
    }

    fun encodeImageByBase64(filepath:String){
        singleThreadExecutor.execute(object :Runnable{
            override fun run() {
                Log.e("上传图片","开始22")
                val imageBase64Code = FileUtil.FileToBaseEncode(filepath)
                val builder = FormBody.Builder()
                imageCode = imageBase64Code
                val file = File(Environment.getExternalStorageDirectory().absolutePath + "/11.txt")
                Log.e("上传图片地址",file.absolutePath)
                if(!file.exists()){
                    file.createNewFile()
                }
                val out = FileOutputStream(file)
                out.write(imageBase64Code.toByteArray())
                out.close()
                Log.e("上传图片","1" + URLEncoder.encode(imageBase64Code,"utf-8"))
                val message = Message()
                message.what = updateImageToBaidu
//                message.obj = builder
                handler.sendMessage(message)
                Log.e("上传图片","2")
            }
        })
        singleThreadExecutor.shutdown()
    }

    private fun updateImage(){
        val builder = FormBody.Builder()
        Log.e("上传图片","5")
        builder.add("image",imageCode)
        builder.add("baike_num","2")
        Log.e("上传图片" , SharedPreferenceUtil.getInstance().getToken()!!)
        OkHttpManager.instance.syncPlantAnalysisPost(Constants.ANALYSIS_IMAGE_FOR_PLANT + "?access_token=" + SharedPreferenceUtil.getInstance().getToken(),builder.build(),object:BaseRequestCallBack(){
            override fun onFailed(result: String) {
                Log.e("上传图片失败",result)
            }

            override fun onSucceed(result: String) {
                Log.e("上传图片有回调",result)
                val plantBean = dealAnalysisResult(result)
                tv_analysis_result_name.text = "名称:" + plantBean?.name
                tv_analysis_result_description.text = "描述:" + plantBean?.description
            }

            override fun onNetworkFaild() {
                Log.e("上传图片","网络问题")
            }
        })
    }

    override fun onDestroy() {
        super.onDestroy()
        singleThreadExecutor.shutdownNow()
    }

    val handler = object:Handler(){
        override fun handleMessage(msg: Message?) {

            Log.e("上传图片","3")
            super.handleMessage(msg)
            when(msg?.what){
                updateImageToBaidu -> {
                    Log.e("上传图片","4")
                    updateImage()
                }
                else -> {}
            }
        }
    }

    fun dealAnalysisResult(result:String):PlantBean?{
        val array = JSONArray(result)
        var plantBean:PlantBean = PlantBean("","","","")
        for (index in 0 .. array.length()){
            val json = array.optJSONObject(index)
            plantBean.name = json.optString("name")
            if(null != json.optJSONObject("baike_info")){
                val baike = json.optJSONObject("baike_info")
                plantBean.image = baike.optString("image_url")
                plantBean.description = baike.optString("description")
                plantBean.baike = baike.optString("baike_url")
                return  plantBean
            }
        }
        return plantBean
    }
//
//    override fun getLayoutId():Int {
//        return R.layout.activity_plant
//    }
}