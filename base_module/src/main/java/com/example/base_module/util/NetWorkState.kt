package com.example.base_module.util

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import com.example.base_module.BaseApplication

class NetWorkState {

    companion object{
        fun isConnected():Boolean{
            val manager:ConnectivityManager = BaseApplication.getInstance().applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= 29){
                return manager.getNetworkCapabilities(manager.activeNetwork).let {
                    capabilities ->
                    return@let capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)&&capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)  //第一个参数确保连上网络，第二个参数确保能访问网络
                }
            }else {
                return manager.activeNetworkInfo.isAvailable
            }
        }
    }

}