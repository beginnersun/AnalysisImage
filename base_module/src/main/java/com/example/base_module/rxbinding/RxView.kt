package com.example.base_module.rxbinding

import android.view.View
import android.widget.EditText
import com.example.base_module.rxbinding.observable.TextChangeObservable
import com.example.base_module.rxbinding.observable.ViewClickObservable
import java.util.concurrent.TimeUnit

class RxView private constructor(){

    companion object{
        fun bindClick(view: View) = bindClick(view,500)

        fun bindClick(view:View,time:Long) = ViewClickObservable(view).throttleFirst(time,TimeUnit.MILLISECONDS)

        fun bindTextChange(editText: EditText) = bindTextChange(editText,500)

        fun bindTextChange(editText:EditText,time:Long) = TextChangeObservable(editText).debounce(time,TimeUnit.MILLISECONDS)

    }

}