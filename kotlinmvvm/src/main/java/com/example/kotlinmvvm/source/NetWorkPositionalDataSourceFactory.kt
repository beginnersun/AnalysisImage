package com.example.kotlinmvvm.source

import androidx.paging.DataSource
import com.example.kotlinmvvm.base.BaseNetWorkPositionalDataSource


class NetWorkPositionalDataSourceFactory<Value>: DataSource.Factory<Int,Value>() {



    override fun create(): BaseNetWorkPositionalDataSource<Value> = NewsPositionalDataSource()

}