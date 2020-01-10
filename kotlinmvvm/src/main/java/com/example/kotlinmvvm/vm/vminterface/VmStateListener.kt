package com.example.kotlinmvvm.vm.vminterface

interface VmStateListener {

    fun error(message:String)

    fun startLoad()

    fun endLoad()

    fun onSuccess()

}