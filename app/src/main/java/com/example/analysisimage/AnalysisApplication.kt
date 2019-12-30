package com.example.analysisimage

import android.util.Log
import com.example.base_module.BaseApplication
import com.example.kotlinmvvm.localModule
import com.example.kotlinmvvm.mainViewModel
import com.example.kotlinmvvm.newsViewModel
import com.example.kotlinmvvm.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

class AnalysisApplication : BaseApplication() {

    val cc: (KoinApplication) -> Unit = { it ->
        Log.e("测试1","初始化Module")
        it.androidLogger()
        it.androidContext(this@AnalysisApplication)
        it.modules(listOf(remoteModule, mainViewModel,newsViewModel, localModule))
    }

    override fun onCreate() {
        Log.e("测试","开始")
        super.onCreate()
        Log.e("测试","加载Module")
        startKoin(cc)
    }


//
//    val koin:KoinApplication.() -> Unit = {
//        androidContext(this@MvvmApplication)
//        logger(AndroidLogger())
//        androidFileProperties("koin.properties")
//        modules(listOf(remoteModule, mainViewModel))
//    }

}