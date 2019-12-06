package com.example.analysisimage

import android.app.DownloadManager
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import com.example.analysisimage.activity.PlantAnalysisActivity
import com.example.analysisimage.activity.camerax.PlantAnalysisActivityCameraX
import com.example.analysisimage.util.Father
import com.example.analysisimage.util.PreferenceManager
import com.example.analysisimage.util.PreferenceManagerUtil
import com.example.analysisimage.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    val apiKey:String = "PdszjNdLcEG4rMpQR2ELHbIl"
    val secretKey:String = "ph4d4TOqnArWF5i8Z6GmBIYk6fowTwxI"
    val client:OkHttpClient = OkHttpClient();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferenceUtil.init(applicationContext)
        setContentView(R.layout.activity_main)
        /**
         * 声明一个匿名类  object:xxxxx{    override fun .....(){ 覆写的方法}    }
         */
        first.setOnClickListener{ view -> startActivity(Intent(this@MainActivity,PlantAnalysisActivityCameraX::class.java))}
//        first.setOnClickListener {first.text = "222"}
        Thread(){
            kotlin.run {
                var result = getImageRecogintionToken(apiKey,secretKey)
                Log.e("TokenResult",result)
                analysisToken(result)
            }
        }.start()
    }

    fun getImageRecogintionToken(apiKey:String ,secretKey:String):String{
        var url = "https://aip.baidubce.com/oauth/2.0/token?" + "grant_type=client_credentials"+
                "&client_id=" + apiKey + "&client_secret=" + secretKey
        var result = ""
        val request : Request = Request.Builder().url(url).build()
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            Log.d("kwwl", "response.code()==" + response.code)
            Log.d("kwwl", "response.message()==" + response.message)
            result = response.body!!.string()
        }
        else
        {
            Log.d("Fail","get请求失败")
        }
        return result
    }

    fun analysisToken(result:String):String{
        val jsonObject = JSONObject(result)
        var token:String
        var expires:Long
        expires = jsonObject.optLong("expires_in")
        token = jsonObject.optString("access_token")
        Log.e("上传图片",token)
        SharedPreferenceUtil.getInstance().setToken(token)
        return token
    }
}
