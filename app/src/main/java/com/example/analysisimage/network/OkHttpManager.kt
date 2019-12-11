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
        /**
         *
         * by 代表instance属性被委托给lazy方法的返回值（Lazy<T>这个类委托实现（通过Lazy的getValue方法获得值）
         *
         * lazy 方法 表示懒加载（只有在调用时才会进行计算并且值不会改变
         * 传入的参数SYNCHRONIZED表示在计算式会进行双重枷锁（也就是能在多个线程中返回同一个值）   (貌似默认值就是SYNCHRONIZED)
         * 其他的NONE代表只会用于同一个线程
         * PUBLICATION 表示会在多个线程中计算但是只会返回第一个计算的结果(不确定性）
         */
        val instance:OkHttpManager by lazy(LazyThreadSafetyMode.SYNCHRONIZED){
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