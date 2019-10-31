package com.example.analysisimage.network

import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.lang.StringBuilder

class OkHttpManager private constructor(){

    private var client:OkHttpClient

    init{
        client = OkHttpClient()
    }


    companion object{
        val instance:OkHttpManager by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED){
            OkHttpManager()
        }
    }

    fun syncPlantAnalysisPost(url:String,body:RequestBody,callBack:BaseRequestCallBack){
        val builder = Request.Builder()
        builder.addHeader("Content-Type","application/x-www-form-urlencode")
        builder.post(body).url(url)
        client.newCall(builder.build()).enqueue(callBack)
    }

}