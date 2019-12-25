package com.example.kotlinmvvm.model

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.POST
import retrofit2.http.Path

interface NewsService {

    @POST("list/{type}/{start}-{step}.html")
    fun getNewsList(@Path("type")type:String,@Path("start")start:Int,@Path("step")step:Int): Single<String>

}