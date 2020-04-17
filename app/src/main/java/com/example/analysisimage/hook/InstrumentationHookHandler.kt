package com.example.analysisimage.hook

import android.app.Instrumentation
import android.util.Log
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method

class InstrumentationHookHandler(val mInstrumentation: Instrumentation):InvocationHandler {



    override fun invoke(proxy: Any?, method: Method?, args: Array<out Any>?): Any {
        if (method!!.name.compareTo("execStartActivity") == 0){
            Log.e("执行跳转","hook住")

        }
        return method!!.invoke(mInstrumentation,args)
    }
}