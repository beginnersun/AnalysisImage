package com.example.analysisimage.hook

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class ActivityManagerHookHandler(private val activityTaskManager: Any) : InvocationHandler {
    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        if (method!!.name.compareTo("") == 0){

        }
        return method!!.invoke(activityTaskManager, args)
    }
}