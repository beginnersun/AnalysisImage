package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.responsitory.NewsResponsitory

class NewsPageViewModel(val newsResponsitory:NewsResponsitory):BaseViewModel() {

    private val repoResult = newsResponsitory.getNewsList()


    fun refresh(){

    }

}