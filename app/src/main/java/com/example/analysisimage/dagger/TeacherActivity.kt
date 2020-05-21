package com.example.analysisimage.dagger

import android.content.SharedPreferences
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.analysisimage.R
import javax.inject.Inject

class TeacherActivity : AppCompatActivity() {

    @Inject
    lateinit var teacher: Teacher
    @Inject
    lateinit var teacher2:Teacher
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher)

        DaggerTeacherComponent.builder()
            .appComponent(ComponentHolder.getComponent())
            .teacherModule(TeacherModule(this))
            .build().inject(this)
//        DaggerTeacherComponent.builder()
//            .teacherModule(TeacherModule(this))
//            .build()
//            .inject(this)
    }

}