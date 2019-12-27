package com.example.kotlinmvvm.base

import androidx.lifecycle.ViewModel
import com.example.kotlinmvvm.vminterface.VmStateListener

abstract class BaseViewModel:ViewModel() {

    var listener:VmStateListener? = null

}