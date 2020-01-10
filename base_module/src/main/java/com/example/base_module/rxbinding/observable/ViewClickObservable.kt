package com.example.base_module.rxbinding.observable

import android.os.Looper
import android.util.Log
import android.view.View
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class ViewClickObservable constructor(private val view: View): Observable<Any>(){


    override fun subscribeActual(observer: Observer<in Any>?) {
        val listener = Listener(view,observer)
        observer?.onSubscribe(listener)
        view.setOnClickListener(listener)
    }

    inner class Listener(private val view:View,private var observer:Observer<in Any>?): MainThreadDisposable(),View.OnClickListener{
        override fun onDispose() {
            view.setOnClickListener(null)
        }

        override fun onClick(v: View?) {
            Log.e("准备发送事件","执行onClick方法"+System.currentTimeMillis())
            if (!isDisposed) {
                Log.e("发送事件","执行onClick方法"+System.currentTimeMillis())
                observer?.onNext(view.id)
            }
        }

    }
    
}