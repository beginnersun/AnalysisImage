package com.example.base_module

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

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
