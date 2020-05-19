package com.example.analysisimage

import android.app.Service
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyService: Service() {

    private var aidl:testAidl.Stub = object: testAidl.Stub(){
        override fun add(a: Int, b: Int) {
            a+b
        }
    }

    override fun onBind(intent: Intent?): IBinder? {
        return aidl
    }

    fun test(){
        stopSelf()
        val builder = NotificationCompat.Builder(this, "").apply {
            setContentTitle("Picture Download")
            setContentText("Download in progress")
            setSmallIcon(R.drawable.ic_launcher_background)
            priority = NotificationCompat.PRIORITY_LOW
        }
        NotificationManagerCompat.from(this).notify(1,builder.build())

    }
}