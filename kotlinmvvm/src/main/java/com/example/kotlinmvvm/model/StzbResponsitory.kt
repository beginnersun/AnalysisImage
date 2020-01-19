package com.example.kotlinmvvm.model

import android.util.Log
import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.bean.VideoBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject

class StzbResponsitory(val stzbService: StzbService) {

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


}