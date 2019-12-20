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

class MainViewModel(val userRepositroy:UserRepository) : ViewModel() {

    private val userLiveData by lazy {
        MutableLiveData<UserBean>().also {

        }
    }

    private fun loadUser(){
        userLiveData.value = UserBean("name","password","phoneNumber","headImage")
    }

    @SuppressLint("CheckResult")
    public fun login(username:String, password:String){
        var observable =  Observable.create(ObservableOnSubscribe<UserBean>(){
            it.onNext(UserBean("1","","",""))
            it.onNext(UserBean("22","","",""))
            it.onNext(UserBean("333","","",""))
        })
        var observable1 = Observable.just(UserBean("","","",""))
        Observable.defer{
            Observable.just(1)
        }
        
        Observable.defer(object:Callable<Observable<out Any>>{
            override fun call(): Observable<out Any> {
                return Observable.just(1)
            }
        })

        observable.map {
            t -> t.headImage.length
        }
        observable.map(object:Function<UserBean,String>{
            override fun apply(t: UserBean): String {
                return t.headImage
            }
        })
//        observable.buffer()

        observable.flatMap {
            val list = mutableListOf<String>()
            for (i in 0..9){
                list.add("${it.name}$i")
            }
            return@flatMap Observable.just(list)
        }


//        userRepositroy.login(username,password)
    }

}