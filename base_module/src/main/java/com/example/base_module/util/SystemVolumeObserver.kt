package com.example.base_module.util

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.AudioManager
import com.example.base_module.BaseApplication

class SystemVolumeObserver {

    private constructor(context:Context?){
        mContext = context
        audioManager = mContext!!.getSystemService(Service.AUDIO_SERVICE) as AudioManager
    }

    companion object{
        val instance:SystemVolumeObserver by lazy(LazyThreadSafetyMode.SYNCHRONIZED) {
            SystemVolumeObserver(BaseApplication.getInstance())
        }

    }

    private var mContext:Context? = null
    private var onVolumeChangedListener:OnVolumeChangedListener? = null
    private var audioManager:AudioManager? = null
    private var volumeReceiver:VolumeBroadCastReceiver? = null
    private var mRegister:Boolean = false

    fun registerVolumeBroadCast(volumeChangedListener:OnVolumeChangedListener){
        this.onVolumeChangedListener = volumeChangedListener
        volumeReceiver = VolumeBroadCastReceiver()
        var intentFilter = IntentFilter()
        intentFilter.addAction("android.media.VOLUME_CHANGED_ACTION")
        mContext?.registerReceiver(volumeReceiver,intentFilter)
        mRegister = true
    }

    fun unregisterVolume(){
        if(mRegister){
            mContext?.unregisterReceiver(volumeReceiver)
            mRegister = false
        }
    }

    fun getMaxVolume(type:Int = AudioManager.STREAM_MUSIC) = audioManager?.getStreamMaxVolume(type)


    fun getCurrentVolume(type: Int = AudioManager.STREAM_MUSIC) = audioManager?.getStreamVolume(type)


    private inner class VolumeBroadCastReceiver:BroadcastReceiver(){
        override fun onReceive(context: Context?, intent: Intent?) {
            onVolumeChangedListener?.onVolumeChanged(getCurrentVolume()!!)
        }
    }

    interface OnVolumeChangedListener{
        fun onVolumeChanged(volume:Int)
    }

}