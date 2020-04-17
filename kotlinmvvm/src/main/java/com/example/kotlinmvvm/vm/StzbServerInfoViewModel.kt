package com.example.kotlinmvvm.vm

import android.util.Log
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
            Log.e("执行前:当前线程","${Thread.currentThread().name}")
            serverData.value = responsitory.getServerList()
            Log.e("执行后:当前线程","${Thread.currentThread().name}")
        })
    }

    override fun onCleared() {
        Log.e("销毁ViewModel","onCleared")
        super.onCleared()
    }

}