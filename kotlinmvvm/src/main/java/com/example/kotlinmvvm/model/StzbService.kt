package com.example.kotlinmvvm.model

import com.example.kotlinmvvm.bean.VideoBean
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface StzbService {

    //callback=jQuery111108998118018689065_1578639222250&
//    @GET("/mrecord/get_records?page={page}&page_size={page_size}&sort=0&tag_name=&gametype=478&_=1578639222251")
//    suspend fun getStzbVideoInfo(@Path("page")page:Int,@Path("page_size")size:Int):String


    @GET("mrecord/get_records")
    suspend fun getStzbVideoInfo(@Query("page")page:Int,@Query("page_size")page_size:Int,@Query("sort")sort:Int = 0,
                                  @Query("gametype")gametype:Int = 478):String



}