package com.example.kotlinmvvm.model

import retrofit2.http.GET
import retrofit2.http.Query

interface StzbNoticeService {

    @GET("api/mobile/index.php")
    suspend fun getStzbNotice(
        @Query("page") page: Int,
        @Query("version") version: Int = 163, @Query("module") module: String = "forumdisplay", @Query("charset") charset: String = "utf-8", @Query(
            "fid"
        ) fid: Int = 565
        ): String

}