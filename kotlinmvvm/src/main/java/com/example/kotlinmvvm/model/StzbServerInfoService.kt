package com.example.kotlinmvvm.model

import retrofit2.http.GET

interface StzbServerInfoService {

    @GET("server_list?_=1583307101540&callback=jsonp2")
    suspend fun getServerList()

}