package com.example.kotlinmvvm.responsitory

import android.media.Image
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.paging.LivePagedListBuilder
import androidx.paging.PagedList
import com.example.kotlinmvvm.base.BaseNetWorkPositionalDataSource
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.source.NetWorkPositionalDataSourceFactory
import com.example.kotlinmvvm.source.NewsPositionalDataSource
import java.util.*
import java.util.concurrent.Executors

class NewsResponsitory {

    fun getNewsList():LiveData<PagedList<NewsBean>>{
        val newsPositionalDataSource: LiveData<PagedList<NewsBean>> = LivePagedListBuilder(NetWorkPositionalDataSourceFactory<NewsBean>(),50).build()

        (newsPositionalDataSource as BaseNetWorkPositionalDataSource<NewsBean>).refresh()

        val data = mutableListOf<NewsBean>()

        ResponsitoryBean(
            newsPositionalDataSource,
            newsPositionalDataSource.netWorkState,
            newsPositionalDataSource.initialLoad,
            newsPositionalDataSource.retry!!,
            newsPositionalDataSource.refresh)
        return newsPositionalDataSource
    }

}