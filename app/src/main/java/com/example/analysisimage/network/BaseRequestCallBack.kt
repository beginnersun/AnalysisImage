package com.example.analysisimage.network

import okhttp3.Call
import okhttp3.Callback;
import okhttp3.Response
import java.io.IOException

abstract class BaseRequestCallBack:Callback {

    override fun onResponse(call: Call, response: Response) {
        if (response.body == null){
            onNetworkFaild()
        }else{
            onSucceed(response.body.toString())
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        if (e!=null){
            onFailed(e.toString())
        }
    }

    abstract fun onSucceed(result:String)

    abstract fun onNetworkFaild()

    abstract fun onFailed(result:String)
}