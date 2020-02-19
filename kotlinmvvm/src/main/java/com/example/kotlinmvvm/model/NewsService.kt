package com.example.kotlinmvvm.model

import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface NewsService {

    @GET("article/list/{type}/{start}-{step}.html")
    fun getNewsList(@Path("type")type:String,@Path("start")start:Int,@Path("step")step:Int): Single<ResponseBody>

}