package com.example.kotlinmvvm.model

import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Streaming
import retrofit2.http.Url

interface DownLoadFileService {

    @Streaming
    @GET
    suspend fun downLoadFile(@Url url:String)

}