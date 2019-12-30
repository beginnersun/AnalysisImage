package com.example.kotlinmvvm.model

import io.reactivex.Observable
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface NewsService {
//0-0
    @GET("list/{type}/{start}-{step}.html")
    fun getNewsList(@Path("type")type:String,@Path("start")start:Int,@Path("step")step:Int): Single<ResponseBody>

}