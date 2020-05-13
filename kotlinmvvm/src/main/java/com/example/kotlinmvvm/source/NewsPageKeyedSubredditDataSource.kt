package com.example.kotlinmvvm.source

import androidx.paging.PageKeyedDataSource
import com.example.kotlinmvvm.bean.NewsBean

class NewsPageKeyedSubredditDataSource:PageKeyedDataSource<String,NewsBean>() {

    /**
     * 通过网络初始化值 并且调用callBack方法执行onResult 以便通知数据更新
     * 加载初始化数据 参数只有size
     */
    override fun loadInitial(params: LoadInitialParams<String>, callback: LoadInitialCallback<String, NewsBean>) {
        val items = mutableListOf<NewsBean>()
        callback.onResult(items,"preKey","afterKey")
        params.requestedLoadSize

    }

    /**
     * 根据afterKey加载数据
     */
    override fun loadAfter(params: LoadParams<String>, callback: LoadCallback<String, NewsBean>) {
        params.key
    }

    /**
     * 根据preKey加载数据
     */
    override fun loadBefore(params: LoadParams<String>, callback: LoadCallback<String, NewsBean>) {
        params.key
    }
}