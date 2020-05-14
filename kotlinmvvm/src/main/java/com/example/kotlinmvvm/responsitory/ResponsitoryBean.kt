package com.example.kotlinmvvm.responsitory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.DataSource
import androidx.paging.PagedList
import com.example.kotlinmvvm.bean.NewsBean

class ResponsitoryBean(val pagedList:LiveData<PagedList<NewsBean>>,val newtWorkState:LiveData<Int>,val initialLoad:LiveData<Int>,val retry:(()->Any?),val refresh:()->Unit) {
}