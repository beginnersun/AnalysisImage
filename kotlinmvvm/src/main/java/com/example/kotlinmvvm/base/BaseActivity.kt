package com.example.kotlinmvvm.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.base_module.widget.LoadingView
import com.example.kotlinmvvm.vm.vminterface.VmStateListener

abstract class BaseActivity:AppCompatActivity() {

    private var baseViewModel:BaseViewModel? =  null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val loadingView = LoadingView(this)
        baseViewModel = setViewModel()
        baseViewModel?.listener = object:VmStateListener{
            override fun error(message: String) {
                loadingView.cancel()
                onError(message)
            }

            override fun startLoad() {
                loadingView.show()
                onStartLoad()
            }

            override fun endLoad() {
                loadingView.cancel()
                onEndLoad()
            }

            override fun onSuccess() {
                loadingView.dismiss()
                onLoadSucess()
            }

        }
    }

    class OnVmStateListener:VmStateListener{
        override fun error(message: String) {

        }

        override fun startLoad() {

        }

        override fun endLoad() {

        }

        override fun onSuccess() {

        }

    }

    abstract fun setViewModel():BaseViewModel

    protected fun onError(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    protected fun onStartLoad(){
    }

    protected fun onEndLoad(){

    }

    protected fun onLoadSucess(){

    }
}