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
                onError(message)
            }

            override fun startLoad() {
                onStartLoad()
            }

            override fun endLoad() {
                onEndLoad()
            }

            override fun onSuccess() {
                onLoadSucess()
            }

        }
    }

    abstract fun setViewModel():BaseViewModel

    protected fun onError(message:String){

    }

    protected fun onStartLoad(){

    }

    protected fun onEndLoad(){

    }

    protected fun onLoadSucess(){

    }
}