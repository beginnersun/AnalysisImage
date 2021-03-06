package com.example.kotlinmvvm.model

import retrofit2.http.GET
import retrofit2.http.Query

interface StzbServerInfoService {

    @GET("server_list")
    suspend fun getServerList(@Query("_")test:String = "1583307101540"):String

    @GET("city")
    suspend fun getServerCityInfo(@Query("server_id")server_id:String,@Query("date")date:String,@Query("_")test:String = "1583307291083"):String
//    ,@Query("callback")callback:String

    @GET("allies_top_10")
    suspend fun getServerRank(@Query("server_id")server_id:String,@Query("date")date:String,@Query("_")test:String = "1583308047151"):String

}