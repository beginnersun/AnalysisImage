package com.example.analysisimage.dagger

import android.content.SharedPreferences
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(modules = [AppModule::class])
interface AppComponent {

    fun sharedPreferences():SharedPreferences

    fun myApplication():DaggerApplication

}