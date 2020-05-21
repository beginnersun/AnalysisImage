package com.example.analysisimage.dagger

import android.app.Application

class DaggerApplication:Application() {

    override fun onCreate() {
        super.onCreate()
        injectAppModule()
    }

    fun injectAppModule(){
        val component = DaggerAppComponent.builder().appModule(AppModule(this)).build()
        ComponentHolder.setComponent(component)
    }

}