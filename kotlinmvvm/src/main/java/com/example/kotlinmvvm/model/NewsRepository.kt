package com.example.kotlinmvvm.model

import android.util.Log
import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.dao.NewsDao
import com.example.kotlinmvvm.util.asyncTask
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.reactivex.*
import org.json.JSONObject

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
}