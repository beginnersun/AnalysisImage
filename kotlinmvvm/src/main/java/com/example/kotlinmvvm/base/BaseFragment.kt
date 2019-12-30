package com.example.kotlinmvvm.base

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
        onResumed = false
    }
}