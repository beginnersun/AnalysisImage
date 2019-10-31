package com.example.analysisimage

import android.app.DownloadManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
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
        setContentView(R.layout.activity_main)
        /**
         * 声明一个匿名类  object:xxxxx{    override fun .....(){ 覆写的方法}    }
         */
//        first.setOnClickListener(object :View.OnClickListener{
//            override fun onClick(v: View?) {
//
//            }
//        })
        first.setOnClickListener {first.text = "222"}
        var result = getImageRecogintionToken(apiKey,secretKey)

    }

    fun getImageRecogintionToken(apiKey:String ,secretKey:String):String{
        var url = "https://aip.baidubce.com/oauth/2.0/token?" + "grant_type=client_credentials"+
                "&client_id=" + apiKey + "&client_secrey=" + secretKey
        var result = ""
        val request : Request = Request.Builder().url(url).get().build()
        val response: Response = client.newCall(request).execute()
        result = response.body.toString()
        return result;
    }

    fun analysisToken(result:String):String{
        val jsonObject:JSONObject = JSONObject(result);
        var token = ""
        var expires:Long;
        expires = jsonObject.optLong("expires_in")
        token = jsonObject.optString("access_token")
        SharedPreferenceUtil.getInstance().setToken(token)
        return token;
    }
}
