package com.example.kotlinmvvm.vm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.base_module.bean.UserBean
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.model.NewsRepository
import kotlinx.coroutines.Dispatchers
import org.json.JSONObject

class NewsViewModel(val newsRepository: NewsRepository):BaseViewModel() {

    private val newsDatas by lazy {
        mutableMapOf<String,MutableLiveData<List<NewsBean>>>()
    }

    fun getDatas(type:String):LiveData<List<NewsBean>> {
        if (newsDatas.keys.contains(type))
            return newsDatas[type]!!
        else {
            newsDatas[type] = MutableLiveData<List<NewsBean>>()
            return newsDatas[type]!!
        }
    }

    fun getNewsList(type:String,start:Int,step:Int){
        newsRepository.getNewsList(type,start,step).doOnSuccess{
            var jsonObject:JSONObject = it as JSONObject
            var jsonArray = jsonObject.optJSONArray(type)
//            newsDatas[type]!!.value = jsonArray as List<NewsBean>
            listener?.onSuccess()
        }.doOnError {
            listener?.error(it.message.toString())
        }
    }

}