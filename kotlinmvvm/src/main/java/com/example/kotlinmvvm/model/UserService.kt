package com.example.kotlinmvvm.model

import com.example.base_module.bean.UserBean
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface UserService {
//
//    @GET("")
    fun login(@Query("username")username:String,@Query("password")password:String): Single<UserBean>
}