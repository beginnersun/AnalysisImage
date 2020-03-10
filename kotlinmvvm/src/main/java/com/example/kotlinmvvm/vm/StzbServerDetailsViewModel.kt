package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.bean.UnionBean
import com.example.kotlinmvvm.model.StzbServerInfoResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StzbServerDetailsViewModel(private val stzbServerInfoResponsitory: StzbServerInfoResponsitory):BaseViewModel() {

    val serverData:MutableLiveData<List<UnionBean>> by lazy {
        MutableLiveData<List<UnionBean>>()
    }

    val cityData:MutableLiveData<List<ServerCityBean>> by lazy {
        MutableLiveData<List<ServerCityBean>>()
    }

    fun getServerDetails(server_id:String,date:String){
        launch({
            serverData.value = stzbServerInfoResponsitory.getServerRank(server_id,date)
        })
    }

    fun getCityInfo(server_id: String,date: String){
        launch({
            cityData.value = stzbServerInfoResponsitory.getServerCityInfo(server_id,date)
        })
    }

}