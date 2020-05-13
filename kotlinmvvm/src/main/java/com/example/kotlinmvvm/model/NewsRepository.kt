package com.example.kotlinmvvm.model

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import androidx.paging.PagedList
import androidx.room.Dao
import androidx.room.Query
import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.dao.NewsDao
import com.example.kotlinmvvm.util.asyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.*
import org.json.JSONObject
class Bean{

    var name:String

    constructor(){
        this.name = "5"
    }

    constructor(name:String){
        this.name = name
    }
}
class NewsRepository(private val newsService: NewsService) {

    fun getNewsList(type: String, start: Int, step: Int = 10): Single<List<NewsBean>> {
            return newsService.getNewsList(
                type, start, if (step > 20) 20 else {
                    step
                }
            ).flatMap {
                val body = it.string()
                var result: String
                if (body.contains("artiList(")) {
                    result = body.replace("artiList(", "")
                    result = result.removeRange(result.length - 1, result.length)
                } else {
                    result = body
                }
                Log.e("信息", "$result")
                val list: List<NewsBean> = Gson().fromJson(JSONObject(result).optJSONArray(type).toString(),
                    object : TypeToken<List<NewsBean>>() {}.type)
                for (item in list) {
                    item.type = type
                }
                Single.just(list)
            }.asyncTask()
    }

    fun getNewsList1(type: String, start: Int, step: Int = 10) = liveData<String> {

        val disposable = emitSource(
            MutableLiveData(Bean()).map {
                it.name
            }
        )

//        Contig


        val bean = Bean("nnn")

        disposable.dispose()


    }
}