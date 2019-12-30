package com.example.kotlinmvvm.vm

import android.app.usage.NetworkStats
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.dao.NewsDao
import com.example.kotlinmvvm.dao.NewsDatabase
import com.example.kotlinmvvm.model.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository,private val newsDao: NewsDao):BaseViewModel() {

    private val newsDatas by lazy {
        mutableMapOf<String,MutableLiveData<List<NewsBean>>>()
    }

    fun getDatas(type:String):LiveData<List<NewsBean>> {
        if (newsDatas.keys.contains(type))
            return newsDatas[type]!!
        else {
            newsDatas[type] = MutableLiveData()
            return newsDatas[type]!!
        }
    }

    fun getNewsList(type:String,start:Int,step:Int){
        if (NetWorkState.isConnected()){
            newsRepository.getNewsList(type,start,step)
                .doOnSubscribe {
                    Log.e("网络请求","start")
                    if (listener == null){
                        Log.e("网络请求","but listener is null")
                    }
                    listener?.startLoad()
                }
                .doOnSuccess{
                    Log.e("网络请求","success")
                    newsDatas[type]!!.value = it
                    listener?.endLoad()
                }
                .doOnError {
                    Log.e("网络请求","error")
                    listener?.error(it.message.toString())
                }.subscribe()
        }else{
            newsDao.getNewsList(type,start,step)
        }

    }

}