package com.example.kotlinmvvm.model

import retrofit2.http.GET
import retrofit2.http.Query

interface StzbNoticeService {

    @GET("api/mobile/index.php")
    suspend fun getStzbNotice(
        @Query("page") page: Int,
        @Query("version") version: Int = 163, @Query("module") module: String = "forumdisplay", @Query("charset") charset: String = "utf-8",
        @Query("fid") fid: Int = 565, @Query("tid") tid: String = ""
    ): String

    @GET("api/mobile/index.php")
    suspend fun getStzbNewArea(@Query("page")page:Int,@Query("version")version: Int = 163,@Query("module")module:String = "forumdisplay",
                               @Query("charset")charset:String = "utf-8",@Query("fid")fid:Int = 567,@Query("tpp")tpp:Int = 20,@Query("filter")filter:String = "typeid",
                               @Query("typeid")typeid:Int = 4013,@Query("get_post_detail")get_post_detail:Int = 1):String
}