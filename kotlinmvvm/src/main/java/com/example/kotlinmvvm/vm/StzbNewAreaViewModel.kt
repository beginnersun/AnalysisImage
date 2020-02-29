package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NoticeAreaBean
import com.example.kotlinmvvm.model.StzbResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StzbNewAreaViewModel(private val stzbResponsitory: StzbResponsitory):BaseViewModel() {

    val areaData:MutableLiveData<List<NoticeAreaBean>> by lazy {
        MutableLiveData<List<NoticeAreaBean>>()
    }

    fun getStzbNewAreaList(){
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            e.printStackTrace()
            listener?.error(e.message!!)
        }) {
            listener?.startLoad()
            areaData.value = stzbResponsitory.getStzbNewArea(1)
            listener?.endLoad()
            cancel()
        }
    }

}