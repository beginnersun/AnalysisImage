package com.example.kotlinmvvm

import com.example.base_module.BaseApplication
import com.example.kotlinmvvm.vm.MainViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.dsl.module

class MvvmApplication: BaseApplication() {

    val cc: (KoinApplication) -> Unit = { it ->
        it.androidLogger()
        it.androidContext(this@MvvmApplication)
        it.modules(listOf(remoteModule, mainViewModel))
    }

    val koin:KoinApplication.() -> Unit = {
        androidContext(this@MvvmApplication)
        logger(AndroidLogger())
        androidFileProperties("koin.properties")
        modules(listOf(remoteModule, mainViewModel))
    }

    override fun onCreate() {
        super.onCreate()
        startKoin {cc}
    }
}