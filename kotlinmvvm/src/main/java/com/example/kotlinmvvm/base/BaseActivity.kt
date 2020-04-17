package com.example.kotlinmvvm.base

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.base_module.widget.LoadingView
import com.example.kotlinmvvm.vm.vminterface.VmStateListener
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

abstract class BaseActivity:AppCompatActivity() {

    private var baseViewModel:BaseViewModel? =  null
    private var loadingView:LoadingView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        baseViewModel = setViewModel()
        loadingView = LoadingView(this)
        baseViewModel?.listener = object:VmStateListener{
            override fun error(message: String) {
                cancelLoading()
                onError(message)
            }

            override fun startLoad() {
                showLoading()
            }

            override fun endLoad() {
                endLoading()
            }

            override fun success() {
                onSuccess()
            }

        }

        if (isChangingConfigurations){

        }else{

        }

    }

    abstract fun setViewModel():BaseViewModel

    protected fun onError(message:String){
        Toast.makeText(this,message,Toast.LENGTH_LONG).show()
    }

    protected fun onSuccess(){

    }

    override fun onDestroy() {
        loadingView = null
        super.onDestroy()
    }

    protected fun showLoading(){
        loadingView?.show()
    }

    protected fun endLoading(){
        loadingView?.dismiss()
    }

    protected fun cancelLoading(){
        loadingView?.cancel()
    }

}