package com.example.kotlinmvvm.model

import com.example.kotlinmvvm.util.asyncTask
import io.reactivex.*
import org.json.JSONObject

class NewsRepository(val newsService: NewsService) {

    fun getNewsList(type: String, start: Int, step: Int): Single<out Any> =
        newsService.getNewsList(type, start, if(step>20) 20 else {step}).flatMap {
            val result = it.replace("artiList(", "").apply {
                this.removeRange(this.length - 1, this.length)
            }
            Single.just(JSONObject(result))
        }.asyncTask()
}