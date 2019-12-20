package com.example.base_module

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin

open class BaseApplication : Application() {

    override fun onCreate() {
        super.onCreate()
    }

}
