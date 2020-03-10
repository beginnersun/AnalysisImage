package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.ServerBean
import com.example.kotlinmvvm.model.StzbServerInfoResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StzbServerInfoViewModel(private val responsitory: StzbServerInfoResponsitory) : BaseViewModel() {

    val serverData: MutableLiveData<List<ServerBean>> by lazy {
        MutableLiveData<List<ServerBean>>()
    }

    fun getServerList() {
        launch({
            serverData.value = responsitory.getServerList()
        })
    }

}