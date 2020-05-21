package com.example.analysisimage.dagger

import dagger.Module
import dagger.Provides
import javax.inject.Scope
import javax.inject.Singleton

/**
 * 如果有Module注解 + @Provides 就不需要生成XXX_Factory
 */
@Module
class TeacherModule(val activit:TeacherActivity) {

    @Provides
    @ActivityScope
    fun provideTeacher() = Teacher()

}