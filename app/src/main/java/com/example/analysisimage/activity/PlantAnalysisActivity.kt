package com.example.analysisimage.activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.database.Cursor
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Message
import android.provider.MediaStore
import android.support.annotation.LayoutRes
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.TextView
import com.bumptech.glide.Glide
import com.example.analysisimage.Constants
import com.example.analysisimage.R
import com.example.analysisimage.base.BaseActivity
import com.example.analysisimage.network.BaseRequestCallBack
import com.example.analysisimage.network.OkHttpManager
import com.example.analysisimage.util.FileUtil
import kotlinx.android.synthetic.main.activity_plant.*
import okhttp3.FormBody
import java.net.URLEncoder
import java.util.concurrent.Executors
import kotlin.jvm.internal.Ref

class PlantAnalysisActivity :AppCompatActivity(){
//    override fun initData() {
//    }
//
//    override fun initView() {
//    }

    private val requestSelectImage = 1
    private val requestTakePhoto = 2
    private val updateImageToBaidu = 3
    private val singleThreadExecutor = Executors.newSingleThreadExecutor()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant)
        tv_select_image.setOnClickListener { selectImage() }


    }

    private fun selectImage(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //6.0包括6.0拍照权限 或者读取手机存储 权限都要要手动申请
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.READ_EXTERNAL_STORAGE
                ) == PackageManager.PERMISSION_GRANTED
            ) {  //判断是否有权限   有权限
                val intent = Intent()
                intent.action =Intent.ACTION_PICK
                intent.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,"image/*")
                startActivityForResult(intent,requestSelectImage)
            }
            else { //无权限
                ActivityCompat.requestPermissions(this@PlantAnalysisActivity, arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),1)
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
                Log.e("上传图片","开始")
                val imageBase64Code = FileUtil.FileToBaseEncode(filepath)
                val builder = FormBody.Builder()
                Log.e("上传图片","0")
                builder.add("image",
                    URLEncoder.encode(imageBase64Code))
                Log.e("上传图片","1")
                val message = Message()
                message.what = updateImageToBaidu
                message.obj = builder
                handler.sendMessage(message)

                Log.e("上传图片","2")
            }
        })
        singleThreadExecutor.shutdown()
    }

    private fun updateImage(builder : FormBody.Builder){
        Log.e("上传图片","5")
        OkHttpManager.instance.syncPlantAnalysisPost(Constants.ANALYSIS_IMAGE_FOR_PLANT,builder.build(),object:BaseRequestCallBack(){
            override fun onFailed(result: String) {
                Log.e("上传图片失败",result)
            }

            override fun onSucceed(result: String) {

                Log.e("上传图片有回调",result)
                tv_analysis_result.text = result
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
                    val builder:FormBody.Builder = msg?.obj as FormBody.Builder
                    updateImage(builder)
                }
                else -> {}
            }
        }
    }
//
//    override fun getLayoutId():Int {
//        return R.layout.activity_plant
//    }
}