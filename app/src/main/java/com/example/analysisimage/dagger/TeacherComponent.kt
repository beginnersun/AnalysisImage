package com.example.analysisimage.dagger

import dagger.Component
import javax.inject.Singleton

/**
 * dependencies 代表添加依赖注解  如果需要同时Inject注入TeacherModule与APPModule中的值 那么需要将AppComponent放到dependencies里面
 * 为什么将appComponent写入dependencies中是因为此时他属于依赖 在写注入程序时也会利用这个依赖并且不需要传入Module只需要将AppModule对应的AppComponent实例传入就OK了
 */
@ActivityScope
@Component(modules = [TeacherModule::class], dependencies = [AppComponent::class])
interface TeacherComponent {

    fun inject(activity: TeacherActivity)

}