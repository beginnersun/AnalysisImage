package com.example.analysisimage.base

import android.os.Bundle
import android.support.annotation.IntDef
import android.support.annotation.LayoutRes
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.example.analysisimage.R
import kotlin.jvm.internal.Ref

open abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_plant)
        Log.e(TAG, javaClass.simpleName)
    }

    companion object {
        private val TAG = "BaseActivity"
    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutId():Int
}
