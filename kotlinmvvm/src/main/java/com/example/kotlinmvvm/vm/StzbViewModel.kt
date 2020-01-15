package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.VideoBean
import com.example.kotlinmvvm.model.StzbResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class StzbViewModel constructor(private val responsitory:StzbResponsitory):BaseViewModel() {

    val videoData:MutableLiveData<List<VideoBean>> by lazy {
        MutableLiveData<List<VideoBean>>()
    }

    fun getStzbVideo(page:Int,size:Int){
        viewModelScope.launch(CoroutineExceptionHandler{_,e ->
            listener?.error(e.message!!)
        }){
            listener?.startLoad()
            videoData.value = responsitory.getStzbVideoInfo(page,size)
            listener?.endLoad()
        }
    }

}