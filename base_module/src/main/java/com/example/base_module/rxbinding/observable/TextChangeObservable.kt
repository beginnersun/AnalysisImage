package com.example.base_module.rxbinding.observable

import android.text.Editable
import android.text.TextWatcher
import android.widget.EditText
import io.reactivex.Observable
import io.reactivex.Observer
import io.reactivex.android.MainThreadDisposable

class TextChangeObservable constructor(private val view: EditText): Observable<Any>(){


    override fun subscribeActual(observer: Observer<in Any>?) {
        val listener = Listener(view,observer)
        observer?.onSubscribe(listener)
        view.addTextChangedListener(listener)
        observer?.onNext(view.text)
    }

    inner class Listener(private val view:EditText,private var observer:Observer<in Any>?): MainThreadDisposable(),TextWatcher{

        override fun afterTextChanged(s: Editable?) {
            if (!isDisposed) {
                observer?.onNext(s.toString())
            }
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
        }

        override fun onDispose() {
            view.removeTextChangedListener(this)
        }

    }
    
}