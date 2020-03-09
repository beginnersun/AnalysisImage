package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.UnionBean
import com.example.kotlinmvvm.model.StzbServerInfoResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class StzbServerDetailsViewModel(private val stzbServerInfoResponsitory: StzbServerInfoResponsitory):BaseViewModel() {

    val serverData:MutableLiveData<List<UnionBean>> by lazy {
        MutableLiveData<List<UnionBean>>()
    }

    fun getServerDetails(server_id:String,date:String){
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            e.printStackTrace()
            listener?.error(e.message!!)
        }) {
            listener?.startLoad()
            serverData.value = stzbServerInfoResponsitory.getServerRank(server_id,date)
            listener?.endLoad()
        }
    }

}