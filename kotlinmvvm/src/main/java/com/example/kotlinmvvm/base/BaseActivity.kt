package com.example.kotlinmvvm.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmvvm.vminterface.VmStateListener

abstract class BaseActivity:AppCompatActivity() {

    private var baseViewModel:BaseViewModel? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = setViewModel()
        baseViewModel?.listener = object:VmStateListener{
            override fun error(message: String) {

            }

            override fun startLoad() {

            }

            override fun endLoad() {

            }

            override fun onSuccess() {

            }

        }
    }

    abstract fun setViewModel():BaseViewModel
}