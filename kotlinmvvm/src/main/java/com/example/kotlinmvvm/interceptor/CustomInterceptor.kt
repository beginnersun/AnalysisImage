package com.example.kotlinmvvm.interceptor

import android.util.Log
import okhttp3.Interceptor
import okhttp3.Response

class CustomInterceptor:Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val  request = chain.request()
        Log.e("新闻请求链接为1",request.url.toUri().host)
        Log.e("新闻请求链接为2",request.url.toUri().path)
        return chain.proceed(request)
    }
}