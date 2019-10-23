package com.example.analysisimage

import android.app.DownloadManager
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response

class MainActivity : AppCompatActivity() {

    val apiKey:String = "PdszjNdLcEG4rMpQR2ELHbIl"
    val secretKey:String = "ph4d4TOqnArWF5i8Z6GmBIYk6fowTwxI"
    val client:OkHttpClient = OkHttpClient();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        getImageRecogintionToken(apiKey,secretKey);
    }

    fun getImageRecogintionToken(apiKey:String ,secretKey:String):String{
        var url = "https://aip.baidubce.com/oauth/2.0/token?" + "grant_type=client_credentials"+
                "&client_id=" + apiKey + "&client_secrey=" + secretKey
        var result = ""
        val request : Request = Request.Builder().url(url).get().build()
        val response: Response = client.newCall(request).execute()
        result = response.body.toString();
        return result;
    }

    fun analysisToken(){
        var token = ""
        var expires:Long

    }
}
