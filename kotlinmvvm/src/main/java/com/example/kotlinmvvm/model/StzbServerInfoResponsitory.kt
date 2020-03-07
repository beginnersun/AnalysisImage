package com.example.kotlinmvvm.model

import com.example.kotlinmvvm.bean.ServerBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class StzbServerInfoResponsitory(private val service:StzbServerInfoService) {

    suspend fun getServerList():List<ServerBean>{
        val result = service.getServerList()
        val list = JSONObject(result).optJSONArray("servers")
        return Gson().fromJson<List<ServerBean>>(list.toString(),object :TypeToken<List<ServerBean>>(){}.type)
    }

}