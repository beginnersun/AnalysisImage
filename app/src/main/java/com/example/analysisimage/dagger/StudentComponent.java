package com.example.analysisimage.dagger;

import dagger.Component;

@Component(modules = StudentModule.class)
public interface StudentComponent {

    void inject(StudentActivity activity);

}
