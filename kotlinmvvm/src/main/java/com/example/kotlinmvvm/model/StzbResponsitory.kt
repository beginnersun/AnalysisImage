package com.example.kotlinmvvm.model

import android.util.Log
import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.bean.NoticeBean
import com.example.kotlinmvvm.bean.VideoBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class StzbResponsitory(val stzbService: StzbService,val stzbNoticeService: StzbNoticeService) {

    suspend fun getStzbVideoInfo(page: Int, size: Int): List<VideoBean> =
            if (NetWorkState.isConnected()) {
                var value:String = stzbService.getStzbVideoInfo(page, size)
                val json = JSONObject(value)
                val array = json.optJSONObject("data").optJSONArray("records")
                Gson().fromJson<List<VideoBean>>(array.toString(),object :TypeToken<List<VideoBean>>(){}.type)
            }else {
                stzbService.getStzbVideoInfo(page, size)//因为本地条件问题 无论是否有网。进行远程访问
                mutableListOf<VideoBean>()
            }

    suspend fun getStzbNotice(page:Int):List<NoticeBean>{
        val result = stzbNoticeService.getStzbNotice(page)
        val json = JSONObject(result)
        val list = json.optJSONObject("Variables").optJSONArray("forum_threadlist")
        return Gson().fromJson<List<NoticeBean>>(list.toString(),object :TypeToken<List<NoticeBean>>(){}.type)
    }


}