package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.bean.UnionBean
import com.example.kotlinmvvm.model.StzbServerInfoResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StzbServerDetailsViewModel(private val stzbServerInfoResponsitory: StzbServerInfoResponsitory):BaseViewModel() {

    val serverData:MutableLiveData<List<UnionBean>> by lazy {
        MutableLiveData<List<UnionBean>>()
    }

    val cityData:MutableLiveData<List<ServerCityBean>> by lazy {
        MutableLiveData<List<ServerCityBean>>()
    }

    private fun testNetwork(block : () ->Unit){
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            e.printStackTrace()
            listener?.error(e.message!!)
        }) {
            listener?.startLoad()
            block()
            listener?.endLoad()
            cancel()
        }
    }

    fun getServerDetails(server_id:String,date:String){
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            e.printStackTrace()
            listener?.error(e.message!!)
        }) {
            listener?.startLoad()
            serverData.value = stzbServerInfoResponsitory.getServerRank(server_id,date)
            listener?.endLoad()
            cancel()
        }
    }

    fun getCityInfo(server_id: String,date: String){
        testNetwork {
//            serverData.value = stzbServerInfoResponsitory.getServerRank(server_id,date)
        }
//        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
//            e.printStackTrace()
//            listener?.error(e.message!!)
//        }) {
//            listener?.startLoad()
//            serverData.value = stzbServerInfoResponsitory.getServerRank(server_id,date)
//            listener?.endLoad()
//            cancel()
//        }
    }

}