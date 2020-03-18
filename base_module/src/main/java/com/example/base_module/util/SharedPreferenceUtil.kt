package com.example.base_module.util

import android.content.Context
import android.content.SharedPreferences
import android.util.Log

class SharedPreferenceUtil private constructor(){
    private var  editor:SharedPreferences.Editor? = null
    init {
        editor = sharedPreference?.edit()
    }
    companion object{
        private const val ACCESS_TOKEN:String = "ACCESS_TOKEN"
        private const val TOKEN_EXPIRSE:String = "TOKEN_EXPIRSE"


        private var instance:SharedPreferenceUtil? = null
        private var sharedPreference:SharedPreferences? = null

        @Synchronized
        fun getInstance():SharedPreferenceUtil{
            if (instance == null){
                instance = SharedPreferenceUtil()
            }
            return instance!!
        }
        fun init(context:Context){
            sharedPreference = context.getSharedPreferences("",Context.MODE_PRIVATE);
        }
    }

    fun setToken(token:String){
        editor?.putString(ACCESS_TOKEN,token)
        editor?.apply()
    }

    fun getToken():String? {
        Log.e("上传图片","获取token"+sharedPreference?.getString(ACCESS_TOKEN,""))
        return sharedPreference?.getString(ACCESS_TOKEN,"")
    }

    fun setExpirse(expirst_time:String){
        editor?.putString(TOKEN_EXPIRSE,expirst_time)
        editor?.apply()
    }

    fun getExpirse() = { sharedPreference?.getString(TOKEN_EXPIRSE,"")}

}