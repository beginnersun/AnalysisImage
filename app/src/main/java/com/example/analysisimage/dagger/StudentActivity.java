package com.example.analysisimage.dagger;

import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.analysisimage.R;
import dagger.internal.DaggerCollections;

import javax.inject.Inject;

public class StudentActivity extends AppCompatActivity {

    @Inject
    Student student;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student);

        DaggerStudentComponent.builder()
                .build()
                .inject(this);
    }
}
