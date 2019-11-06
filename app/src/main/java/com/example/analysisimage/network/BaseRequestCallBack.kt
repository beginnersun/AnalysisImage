package com.example.analysisimage.network

import android.os.Handler
import android.os.Message
import okhttp3.Call
import okhttp3.Callback;
import okhttp3.Response
import java.io.IOException

abstract class BaseRequestCallBack:Callback {

    private val handler = object:Handler(){
        override fun handleMessage(msg: Message?) {
            super.handleMessage(msg)
            when(msg?.what){
                0 -> onNetworkFaild()
                1 -> onSucceed(msg?.obj as String)
                2 -> onFailed(msg?.obj as String)
            }
        }
    }

    override fun onResponse(call: Call, response: Response) {
        if (response.body == null){
            handler.sendEmptyMessage(0)
        }else{
            val message = Message()
            message.what = 1
            message.obj = response.body!!.string()
            handler.sendMessage(message)
        }
    }

    override fun onFailure(call: Call, e: IOException) {
        if (e!=null){
            val message = Message()
            message.what = 2
            message.obj = e.message
            handler.sendMessage(message)
        }
    }

    abstract fun onSucceed(result:String)

    abstract fun onNetworkFaild()

    abstract fun onFailed(result:String)
}