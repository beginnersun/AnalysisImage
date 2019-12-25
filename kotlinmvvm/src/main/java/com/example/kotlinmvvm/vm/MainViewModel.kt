package com.example.kotlinmvvm.vm

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.example.base_module.bean.UserBean
import com.example.kotlinmvvm.model.UserRepository
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import io.reactivex.ObservableOnSubscribe
import io.reactivex.ObservableSource
import io.reactivex.annotations.NonNull
import io.reactivex.functions.Function
import java.util.concurrent.Callable
import javax.xml.datatype.DatatypeConstants.SECONDS
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import com.example.kotlinmvvm.base.BaseViewModel
import io.reactivex.functions.BiFunction
import io.reactivex.functions.Consumer
import java.util.concurrent.TimeUnit


class MainViewModel(val userRepositroy: UserRepository) : BaseViewModel() {

    private val userLiveData by lazy {
        MutableLiveData<UserBean>().also {

        }
    }



    private fun loadUser() {
        userLiveData.value = UserBean("name", "password", "phoneNumber", "headImage")
    }

    @SuppressLint("CheckResult")
    public fun login(username: String, password: String) {
        userRepositroy.login(username, password).doOnSubscribe {
            listener?.startLoad()
        }
            .doOnSuccess {
                userLiveData.value = it
            }
            .doOnError {
                listener?.error(it.message.toString())
            }  //doOnError发送错误事件时调用
    }

}