package com.example.kotlinmvvm.base

import androidx.lifecycle.ViewModel
import com.example.kotlinmvvm.vminterface.VmStateListener

open abstract class BaseViewModel:ViewModel() {

    var listener:VmStateListener? = null

}