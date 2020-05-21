package com.example.analysisimage.dagger;

import javax.inject.Inject;

public class Student {

    private String name;
    private String num;

    @Inject
    public Student(){
        this.name = "Test_Student";
        this.num = "66666666";
    }

}
