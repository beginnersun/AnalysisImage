package com.example.kotlinmvvm.model

import android.util.Log
import com.example.kotlinmvvm.bean.ServerBean
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.bean.UnionBean
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class StzbServerInfoResponsitory(private val service:StzbServerInfoService) {

    suspend fun getServerList():List<ServerBean>{
        val result = service.getServerList()
        val list = JSONObject(result).optJSONArray("servers")
        return Gson().fromJson<List<ServerBean>>(list.toString(),object :TypeToken<List<ServerBean>>(){}.type)
    }

    suspend fun getServerRank(server_id:String,date:String):List<UnionBean>{
        val result = service.getServerRank(server_id,date)
        Log.e("排名结果result","$result")
        val list = JSONObject(result).optJSONArray("allies")
        return Gson().fromJson<List<UnionBean>>(list.toString(),object :TypeToken<List<UnionBean>>(){}.type)
    }

    suspend fun getServerCityInfo(server_id: String,date: String):List<ServerCityBean>{
        val result = service.getServerCityInfo(server_id,date)
        Log.e("显示结果","$result")
        val list = JSONObject(result).optJSONArray("city_info")
        return Gson().fromJson<List<ServerCityBean>>(list.toString(),object :TypeToken<List<ServerCityBean>>(){}.type)
    }

}