package com.example.kotlinmvvm.vm

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.StzbFellowGalleryBean
import com.example.kotlinmvvm.model.StzbResponsitory
import com.example.kotlinmvvm.view.stzb.StzbFellowGalleryActivity
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class StzbGalleryViewModel(private val stzbResponsitory: StzbResponsitory):BaseViewModel() {

    val fellowGalleryData:MutableLiveData<List<StzbFellowGalleryBean>> by lazy {
        MutableLiveData<List<StzbFellowGalleryBean>>()
    }

    fun getFellowGallery(page:Int){
        viewModelScope.launch(CoroutineExceptionHandler{_,e ->
            e.printStackTrace()
            listener?.error(e.message!!)
        }) {
            listener?.startLoad()
            fellowGalleryData.value = stzbResponsitory.getFellowGallery(page)
            listener?.endLoad()
            cancel()
        }
    }

}