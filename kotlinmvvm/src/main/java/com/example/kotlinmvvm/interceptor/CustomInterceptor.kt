package com.example.kotlinmvvm.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val  request = chain.request()
//        Log.e("新闻请求链接为1",request.url.toUri().host)
//        Log.e("新闻请求链接为2",request.url.toUri().path)
//        Log.e("新闻请求链接为3",request.url.toUri().rawQuery)
//        Log.e("新闻请求链接为4",request.url.toUri().query)
//        Log.e("新闻请求链接为5",request.url.toUri().rawPath)
//        Log.e("新闻请求链接为6",request.url.toUri().scheme)
//        Log.e("新闻请求链接为7",request.url.toUri()?.authority)
//        Log.e("新闻请求链接为8",request.url.toUri()?.fragment)
        return chain.proceed(request)
    }
}