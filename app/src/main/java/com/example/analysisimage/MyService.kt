package com.example.analysisimage

import android.app.Service
import android.content.Intent
import android.os.IBinder

class MyService: Service() {

    private var aidl:testAidl.Stub = object: testAidl.Stub(){
        override fun add(a: Int, b: Int) {
            a+b
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return aidl
    }
}