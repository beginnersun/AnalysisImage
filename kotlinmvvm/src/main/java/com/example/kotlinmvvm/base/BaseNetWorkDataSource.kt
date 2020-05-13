package com.example.kotlinmvvm.base

import androidx.lifecycle.MutableLiveData
import androidx.paging.PositionalDataSource

open abstract class BaseNetWorkPositionalDataSource<Value>:PositionalDataSource<Value>(){

    /**
     * 用于发送网络状态与数据加载状态
     */
    val netWorkState = MutableLiveData<Int>()

    val initialLoad = MutableLiveData<Int>()
    /**
     * 保留重新加载功能  提供一个重新刷新数据的函数调用
     */
    var retry:(()->Any?)? = null

    /**
     * 让数据源失效 达到刷新数据的目的
     */
    val refresh = ::invalidate

}