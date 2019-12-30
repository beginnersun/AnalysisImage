package com.example.kotlinmvvm.base

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.kotlinmvvm.vminterface.VmStateListener

abstract class BaseViewModel:ViewModel() {

    var listener:VmStateListener? = null
    set(value) {
        Log.e("setListener","执行Listener赋值语句")
        field = value
    }

}