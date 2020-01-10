package com.example.kotlinmvvm.vm


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.model.NewsRepository

class NewsViewModel(private val newsRepository: NewsRepository) : BaseViewModel() {

    private val newsDatas by lazy {
        mutableMapOf<String, MutableLiveData<List<NewsBean>>>()
    }

    fun getDatas(type: String): LiveData<List<NewsBean>> {
        if (newsDatas.keys.contains(type))
            return newsDatas[type]!!
        else {
            newsDatas[type] = MutableLiveData()
            return newsDatas[type]!!
        }
    }

    fun getNewsList(type: String, start: Int, step: Int) {
        newsRepository.getNewsList(type, start, step)
            .doOnSubscribe {
                listener?.startLoad()
            }
            .doOnSuccess {
                newsDatas[type]!!.value = it
                listener?.endLoad()
            }
            .doOnError {
                listener?.error(it.message.toString())
            }.subscribe()

    }

}