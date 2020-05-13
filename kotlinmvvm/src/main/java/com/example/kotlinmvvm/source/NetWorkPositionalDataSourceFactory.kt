package com.example.kotlinmvvm.source

import androidx.paging.DataSource


class NetWorkPositionalDataSourceFactory<Value>: DataSource.Factory<Int,Value>() {

    override fun create(): DataSource<Int, Value> = NewsPositionalDataSource<Value>()

}