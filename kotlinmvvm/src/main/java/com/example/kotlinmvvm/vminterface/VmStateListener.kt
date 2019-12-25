package com.example.kotlinmvvm.vminterface

interface VmStateListener {

    fun error(message:String)

    fun startLoad()

    fun endLoad()

    fun onSuccess()

}