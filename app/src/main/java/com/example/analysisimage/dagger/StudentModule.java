package com.example.analysisimage.dagger;

import dagger.Module;

@Module
public class StudentModule {

    private StudentActivity activity;

    public StudentModule(StudentActivity activity) {
        this.activity = activity;
    }
}
