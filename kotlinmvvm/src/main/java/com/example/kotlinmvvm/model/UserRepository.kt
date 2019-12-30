package com.example.kotlinmvvm.model

import com.example.base_module.bean.UserBean
import com.example.base_module.util.SharedPreferenceUtil
import com.example.kotlinmvvm.util.asyncTask
import io.reactivex.Single
import java.util.*

/**
 * 在Repository层进行数据的处理 将数据处理成ViewModel层需要的样子
 */
class UserRepository(val userService: UserService) {

    fun login(name: String, password: String): Single<UserBean> = userService.login(name, password).map {
        it.headImage = "www.baidu.com${it.headImage}"
        it
    }
//        .asyncTask()

}