package com.example.analysisimage

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.base_module.Constants
import com.example.base_module.util.SharedPreferenceUtil
import com.example.base_module.util.main
import com.example.kotlinmvvm.vm.MainViewModel
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

@Route(path = "app/main")
class MainActivity : AppCompatActivity() {

    private val apiKey: String = "PdszjNdLcEG4rMpQR2ELHbIl"
    private val secretKey: String = "ph4d4TOqnArWF5i8Z6GmBIYk6fowTwxI"
    private val client: OkHttpClient = OkHttpClient();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AppTheme)
        SharedPreferenceUtil.init(applicationContext)
        setContentView(R.layout.activity_main)
        /**
         * 声明一个匿名类  object:xxxxx{    override fun .....(){ 覆写的方法}    }
         */
        first.setOnClickListener { view ->
            ARouter.getInstance().build("/kotlinmvvm/news").navigation()
        }

        baseContext!!.getSystemService(Context.ACTIVITY_SERVICE)


        GlobalScope.launch(Dispatchers.Main) {
            println("Hello 线程${Thread.currentThread().name}")
            val result = test()
            Log.e("TokenResult", result)
            analysisToken(result)
            println("End 线程${Thread.currentThread().name}")
        }




//        Thread {
//            kotlin.run {
//                var result = getImageRecognitionToken(apiKey, secretKey)
//                Log.e("TokenResult", result)
//                analysisToken(result)
//            }
//        }.start()
//
//        getSystemService(Context.ACTIVITY_SERVICE)


        var outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        var widthPixels = outMetrics.widthPixels
        var heightPixels = outMetrics.heightPixels
        Constants.SCREEN_WIDTH = widthPixels
        Constants.SCREEN_HEIGHT = heightPixels
    }

    suspend fun test() =
        withContext(Dispatchers.IO){
            println("World 线程1${Thread.currentThread().name}")
            var result = getImageRecognitionToken(apiKey, secretKey)
            println("World 线程2${Thread.currentThread().name}")
            result
        }


    private fun getImageRecognitionToken(apiKey: String, secretKey: String): String {
        var url = "https://aip.baidubce.com/oauth/2.0/token?" + "grant_type=client_credentials" +
                "&client_id=" + apiKey + "&client_secret=" + secretKey
        var result = ""
        val request: Request = Request.Builder().url(url).build()
        val response: Response = client.newCall(request).execute()
        if (response.isSuccessful) {
            result = response.body!!.string()
        } else {
            Log.d("Fail", "get请求失败")
        }
        return result
    }

    fun analysisToken(result: String): String {
        val jsonObject = JSONObject(result)
        var token: String
        token = jsonObject.optString("access_token")
        Log.e("上传图片", token)
        SharedPreferenceUtil.getInstance().setToken(token)
        return token
    }
}
