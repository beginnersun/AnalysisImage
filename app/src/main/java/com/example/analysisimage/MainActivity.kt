package com.example.analysisimage

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.base_module.Constants
import com.example.base_module.util.SharedPreferenceUtil
import kotlinx.android.synthetic.main.activity_main.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import org.json.JSONObject

@Route(path = "app/main")
class MainActivity : AppCompatActivity() {

    val apiKey: String = "PdszjNdLcEG4rMpQR2ELHbIl"
    val secretKey: String = "ph4d4TOqnArWF5i8Z6GmBIYk6fowTwxI"
    val client: OkHttpClient = OkHttpClient();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        SharedPreferenceUtil.init(applicationContext)
        setContentView(R.layout.activity_main)
        /**
         * 声明一个匿名类  object:xxxxx{    override fun .....(){ 覆写的方法}    }
         */
        first.setOnClickListener { view ->
            ARouter.getInstance().build("/kotlinmvvm/server").navigation()
        }

        Thread {
            kotlin.run {
                var result = getImageRecognitionToken(apiKey, secretKey)
                Log.e("TokenResult", result)
                analysisToken(result)
            }
        }.start()


        var outMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(outMetrics)
        var widthPixels = outMetrics.widthPixels
        var heightPixels = outMetrics.heightPixels
        Constants.SCREEN_WIDTH = widthPixels
        Constants.SCREEN_HEIGHT = heightPixels
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
