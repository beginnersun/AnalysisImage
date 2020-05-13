package com.example.base_module.down

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.arialyy.annotations.M3U8
import com.arialyy.aria.core.Aria
import com.arialyy.aria.core.AriaManager
import com.arialyy.aria.core.download.m3u8.M3U8VodOption
import com.arialyy.aria.core.task.DownloadTask

class TestDownActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Aria.download(this).register()
    }

    @M3U8.onPeerStart fun onM3U8PreStart(task: DownloadTask){
        task.downloadEntity
    }

    @M3U8.onPeerComplete fun onM3U8Complete(task:DownloadTask){

    }

    @M3U8.onPeerFail fun onM3U8Fail(task:DownloadTask){

    }

    override fun onDestroy() {
        Aria.download(this).unRegister()
        var id = Aria.download(this).load("").m3u8VodOption(M3U8VodOption()).create()


        super.onDestroy()
    }

}