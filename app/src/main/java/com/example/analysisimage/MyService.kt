package com.example.analysisimage

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat

class MyService: Service() {

    private lateinit var notificationChannelId:String

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
//        val builder = NotificationCompat.Builder(this, "").apply {
//            setContentTitle("Picture Download")
//            setContentText("Download in progress")
//            setSmallIcon(R.drawable.ic_launcher_background)
//            priority = NotificationCompat.PRIORITY_LOW
//        }
//        NotificationManagerCompat.from(this).notify(1,builder.build())
//
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            var channel = NotificationChannel(notificationChannelId,"channelName",NotificationManager.IMPORTANCE_DEFAULT).apply {
                description = "描述内容"
            }
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
        NotificationManagerCompat.from(this).notify(1,getNotification())
    }

    private fun getNotification(): Notification =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationCompat.Builder(this,notificationChannelId).build()
        }else{
            NotificationCompat.Builder(this).build()
        }

}