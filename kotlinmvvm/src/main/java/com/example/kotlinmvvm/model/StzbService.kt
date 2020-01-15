package com.example.kotlinmvvm.model

import com.example.kotlinmvvm.bean.VideoBean
import io.reactivex.Single
import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Path

interface StzbService {

    //callback=jQuery111108998118018689065_1578639222250&
    @GET("//mshare.cc.163.com/mrecord/get_records?page={page}&page_size={page_size}&sort=0&tag_name=&gametype=478&_=1578639222251")
    suspend fun getStzbVideoInfo(@Path("page")page:Int,@Path("page_size")size:Int): List<VideoBean>



}