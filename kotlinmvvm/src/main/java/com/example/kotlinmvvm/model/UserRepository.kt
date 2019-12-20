package com.example.kotlinmvvm.model

import com.example.base_module.bean.UserBean
import com.example.base_module.util.SharedPreferenceUtil
import com.example.kotlinmvvm.util.asyncTask
import io.reactivex.Single
import java.util.*

class UserRepository(val userService: UserService) {

    fun login(name:String,password:String): Single<UserBean>
            =  userService.login(name,password).asyncTask().doOnSuccess{}


}