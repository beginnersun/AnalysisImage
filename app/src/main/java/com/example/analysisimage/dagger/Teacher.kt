package com.example.analysisimage.dagger

import javax.inject.Inject

class Teacher {

    lateinit var name:String
    lateinit var num:String

    @Inject constructor(){
        this.name = "TestTeacher"
        this.num = "TestNum"
    }
}