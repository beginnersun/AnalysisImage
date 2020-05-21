package com.example.analysisimage.dagger

import android.content.Context
import android.content.SharedPreferences
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
class AppModule {

    private var application:DaggerApplication

    constructor(application: DaggerApplication){
        this.application = application
    }

    @Provides
    @Singleton
    fun provideSharedPreference(): SharedPreferences = application.getSharedPreferences("setting", Context.MODE_PRIVATE)

    @Provides
    @Singleton
    fun provideApplication() = application

}