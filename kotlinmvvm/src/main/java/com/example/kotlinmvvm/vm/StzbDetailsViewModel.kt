package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NoticeDetailsBean
import com.example.kotlinmvvm.model.StzbResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class StzbDetailsViewModel(private var stzbResponsitory:StzbResponsitory):BaseViewModel() {

    val detailData:MutableLiveData<List<NoticeDetailsBean>> by lazy {
        MutableLiveData<List<NoticeDetailsBean>>()
    }

    suspend fun getDetail(tid:String){
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            if (e!=null) {
                e.printStackTrace()
                listener?.error(e.message!!)
            }
        }) {
            listener?.startLoad()
            detailData.value = stzbResponsitory.getStzbDetail(tid)

        }
    }

}