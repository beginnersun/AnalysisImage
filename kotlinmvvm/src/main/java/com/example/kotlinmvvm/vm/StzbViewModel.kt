package com.example.kotlinmvvm.vm

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NoticeBean
import com.example.kotlinmvvm.bean.VideoBean
import com.example.kotlinmvvm.model.StzbResponsitory
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

class StzbViewModel constructor(private val responsitory: StzbResponsitory) : BaseViewModel() {

    val videoData: MutableLiveData<List<VideoBean>> by lazy {
        MutableLiveData<List<VideoBean>>()
    }
    val videoNotice:MutableLiveData<List<NoticeBean>> by lazy {
        MutableLiveData<List<NoticeBean>>()
    }

    fun getStzbVideo(page: Int, size: Int) {
        viewModelScope.launch(CoroutineExceptionHandler { _, e ->
            Log.e("网络请求出错$page $size","请求")
            e.printStackTrace()
            listener?.error(e.message!!)
        })
        {
//            listener?.startLoad()
            Log.e("准备开始网络请求$page $size","请求")
            var beans = responsitory.getStzbVideoInfo(page,size)
            Log.e("加载结果${beans.size}","请求")
            videoData.value = beans
//            listener?.endLoad()
        }
    }

    fun getStzbNotice(page:Int){
        viewModelScope.launch(CoroutineExceptionHandler{ _,e ->
            e.printStackTrace()
            listener?.error(e.message!!)
        }) {
            var notices = responsitory.getStzbNotice(page)
            videoNotice.value = notices
        }
    }

}