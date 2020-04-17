package com.example.analysisimage.hook

import android.app.Instrumentation
import java.lang.reflect.Proxy

class HookHelper {

    companion object {

        fun hookStartActivity() {
            var activityThreadClass = Class.forName("android.app.ActivityThread")
            var method = activityThreadClass.getDeclaredMethod("currentActivityThread")   //获取当前ActivityThread
            method.isAccessible = true
            var activityThread = method.invoke(null)

//            activityThread
            var instrumentationField = activityThreadClass.getDeclaredField("mInstrumentation")
            instrumentationField.isAccessible = true
            var default = instrumentationField.get(activityThread) //得到原有ActivityThread的instrumentation属性

            var instrumentationClass = Class.forName("android.app.Instrumentation")
            var instrumentationProxy = Proxy.newProxyInstance(
                instrumentationClass.classLoader,
                (default as Instrumentation).javaClass.interfaces,
                InstrumentationHookHandler(default)
            )
            instrumentationField.set(activityThread,instrumentationProxy)  //替代回去
        }

        fun hookActivityManager(){
            var activityManagerClass = Class.forName("android.app.ActivityTaskManager")
            var activityTaskManagerField = activityManagerClass.getDeclaredField("IActivityTaskManagerSingleton")
            activityTaskManagerField.isAccessible = true
            var default = activityTaskManagerField.get(null)  //静态的所以直接获取就行

            var singleTonClass = Class.forName("android.util.Singleton")
            var instanceField = singleTonClass.getDeclaredField("mInstance")
            instanceField.isAccessible = true
            var oldIActivityManager = instanceField.get(default)

            var iActivityManagerInterface = Class.forName("android.app.IActivityTaskManager")
            var proxy = Proxy.newProxyInstance(iActivityManagerInterface.classLoader,iActivityManagerInterface.interfaces,ActivityManagerHookHandler(oldIActivityManager))
            instanceField.set(default,proxy)
        }
    }


}