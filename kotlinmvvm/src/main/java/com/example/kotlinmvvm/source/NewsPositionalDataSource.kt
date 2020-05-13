package com.example.kotlinmvvm.source

import androidx.annotation.WorkerThread
import androidx.paging.PositionalDataSource
import com.example.kotlinmvvm.base.BaseNetWorkPositionalDataSource
import com.example.kotlinmvvm.bean.NewsBean

/**
 * 作为数据源 根据需求加载网络数据并提示PageList刷新数据
 * 所以需要进行网络请求的工具 入 api、Retrofit(OkHttp)
 * 可选 错误处理工具参考链接{@Link https://github.com/qingmei2/android-architecture-components/tree/master/PagingWithNetworkSample}
 */
class NewsPositionalDataSource<T> : BaseNetWorkPositionalDataSource<T>() {

    /**
     * 通过给定的加载范围加载数据
     * @workerThread 代表在工作线程（非UI线程）所以最好不要再开启子线程
     */
    @WorkerThread
    override fun loadRange(params: LoadRangeParams, callback: LoadRangeCallback<T>) {
        params.loadSize
        params.startPosition
        callback.onResult(mutableListOf())
    }

    /**
     * 加载初始化数据
     */
    @WorkerThread
    override fun loadInitial(params: LoadInitialParams, callback: LoadInitialCallback<T>) {
        try {

        }catch (e:Exception){ //当加载失败或者出现其他问题时，利用retry保留加载调用
            retry = {
                loadInitial(params,callback)
            }
        }
    }
}