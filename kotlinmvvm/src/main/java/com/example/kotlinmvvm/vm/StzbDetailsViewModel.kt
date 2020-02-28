package com.example.kotlinmvvm.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NoticeDetailsBean
import com.example.kotlinmvvm.model.StzbResponsitory
import kotlinx.coroutines.*

class StzbDetailsViewModel(private var stzbResponsitory:StzbResponsitory):BaseViewModel() {

    private var job: Job? = null

    val detailData:MutableLiveData<List<NoticeDetailsBean>> by lazy {
        MutableLiveData<List<NoticeDetailsBean>>()
    }

    fun getDetail(tid:String){
        Log.e("获取到的Responsitory",stzbResponsitory.toString())
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            if (e!=null) {
                e.printStackTrace()
                listener?.error(e.message!!)
            }
        }) {
            listener?.startLoad()
            detailData.value = stzbResponsitory.getStzbDetail(tid)
            listener?.endLoad()
            cancel()
        }
    }

    fun refresh(){
            job = viewModelScope.launch {
                detailData.value = stzbResponsitory.getStzbDetail("7402903")
            }
//        }

    }

}