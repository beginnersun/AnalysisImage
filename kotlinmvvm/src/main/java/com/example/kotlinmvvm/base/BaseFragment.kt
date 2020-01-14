package com.example.kotlinmvvm.base

import android.util.Log
import androidx.fragment.app.Fragment

abstract class BaseFragment:Fragment() {

    private var onResumed = false

    abstract fun onFirstVisible()

    override fun onResume() {
        if (!onResumed){
            onResumed = true
            onFirstVisible()
        }
        super.onResume()
    }

    override fun onStop() {
        super.onStop()
        Log.e("onStop","生命周期")
    }

    override fun onPause() {
        Log.e("onPause","生命周期")
        super.onPause()
    }

    override fun onDestroyView() {
        onResumed = false
        Log.e("onDestroyView","生命周期")
        super.onDestroyView()
    }

    override fun onDetach() {
        Log.e("onDetach","生命周期")
        super.onDetach()
    }

    override fun onDestroy() {
        Log.e("onDestroy","生命周期")
        super.onDestroy()
    }
}