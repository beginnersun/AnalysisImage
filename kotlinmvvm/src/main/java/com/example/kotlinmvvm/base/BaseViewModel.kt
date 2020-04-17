package com.example.kotlinmvvm.base

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.kotlinmvvm.vm.vminterface.VmStateListener
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

abstract class BaseViewModel:ViewModel() {

    var listener:VmStateListener? = null
    set(value) {
        Log.e("setListener","执行Listener赋值语句")
        field = value
    }

    protected fun launch(block:suspend CoroutineScope.() -> Unit,onError:(message:String) ->Unit = {},OnComplete : () -> Unit = {}){
        viewModelScope.launch(
//            CoroutineExceptionHandler{ _,e ->
//            e.printStackTrace()
//            onError(e.message!!)
//            listener?.error(e.message!!)
//        }
        )
        {
            listener?.startLoad()
            this.block()
            listener?.endLoad()
            cancel()
        }
    }

}