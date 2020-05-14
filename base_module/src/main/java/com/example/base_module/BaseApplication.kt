package com.example.base_module

import android.app.Application
import com.alibaba.android.arouter.launcher.ARouter
import com.tencent.smtt.sdk.QbSdk

open class BaseApplication : Application() {

    private val isDebug = true

    companion object{
        private var instance:BaseApplication? = null

        fun getInstance():BaseApplication = instance!!
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
        if (isDebug){
            ARouter.openLog()
            ARouter.openDebug()
        }
        ARouter.init(this)
        initWebX5()
    }

    private fun initWebX5(){
        QbSdk.setDownloadWithoutWifi(true)
        QbSdk.initX5Environment(this,object:QbSdk.PreInitCallback{
            override fun onCoreInitFinished() {

            }
            override fun onViewInitFinished(p0: Boolean) {

            }
        })
    }

}
