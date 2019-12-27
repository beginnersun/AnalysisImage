package com.example.base_module

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter

open class BaseApplication : Application() {

    private val isDebug = true

    override fun onCreate() {
        super.onCreate()
        if (isDebug){
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
    }

}
