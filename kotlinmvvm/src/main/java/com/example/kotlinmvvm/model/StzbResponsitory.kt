package com.example.kotlinmvvm.model

import android.util.Log
import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.bean.*
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
        Log.e("请求数据","收到数据$result")
        val json = JSONObject(result)
        val list = json.optJSONObject("Variables").optJSONArray("forum_threadlist")
        Log.e("请求数据",list.toString())
        return Gson().fromJson<List<NoticeBean>>(list.toString(),object :TypeToken<List<NoticeBean>>(){}.type)
    }

    suspend fun getStzbDetail(tid:String):List<NoticeDetailsBean>{
        Log.e("获取到的stzbNoticeService",stzbNoticeService.toString())
        val result = stzbNoticeService.getStzbNotice(1,module = "viewthread",tid = tid)
        val json = JSONObject(result)
        val list = json.optJSONObject("Variables").optJSONArray("postlist")
        val listBeans =  Gson().fromJson<List<NoticeDetailsBean>>(list.toString(),object :TypeToken<List<NoticeDetailsBean>>(){}.type)
        return listBeans
    }

    suspend fun getStzbNewArea(page:Int):List<NoticeAreaBean>{
        val result = stzbNoticeService.getStzbNewArea(page)
        val json = JSONObject(result)
        val list = json.optJSONObject("Variables").optJSONArray("forum_threadlist")
        return Gson().fromJson<List<NoticeAreaBean>>(list.toString(),object : TypeToken<List<NoticeAreaBean>>(){}.type)
    }

    suspend fun getFellowGallery():List<StzbFellowGalleryBean>{
        val result = stzbNoticeService.getStzbNotice(1,fid = 1537)
        val json = JSONObject(result)
        val list = json.optJSONObject("Variables").optJSONArray("forum_threadlist")
        return Gson().fromJson<List<StzbFellowGalleryBean>>(list.toString(),object : TypeToken<List<StzbFellowGalleryBean>>(){}.type)
    }

}