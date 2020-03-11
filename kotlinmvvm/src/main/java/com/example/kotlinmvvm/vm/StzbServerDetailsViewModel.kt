package com.example.kotlinmvvm.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.bean.UnionBean
import com.example.kotlinmvvm.model.StzbServerInfoResponsitory
import kotlinx.coroutines.*

class StzbServerDetailsViewModel(private val stzbServerInfoResponsitory: StzbServerInfoResponsitory):BaseViewModel() {

    val serverData:MutableLiveData<List<UnionBean>> by lazy {
        MutableLiveData<List<UnionBean>>()
    }

    val cityData:MutableLiveData<List<ServerCityBean>> by lazy {
        MutableLiveData<List<ServerCityBean>>()
    }

    fun getServerDetails(server_id:String,date:String){
        launch({
            Log.e("网络请求","获取1$server_id")
            val value1 = async { stzbServerInfoResponsitory.getServerRank(server_id,date) }
            Log.e("网络请求","获取2$server_id")
            val value2 = async { stzbServerInfoResponsitory.getServerCityInfo(server_id,date) }
            serverData.value = value1.await()
            cityData.value = value2.await()
        })
    }

//    fun getCityInfo(server_id: String,date: String){
//        launch({
//        })
//    }

}