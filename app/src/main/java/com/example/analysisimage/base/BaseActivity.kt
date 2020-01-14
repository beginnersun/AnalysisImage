package com.example.analysisimage.base

import android.os.Bundle
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.example.analysisimage.R
import kotlin.jvm.internal.Ref

open abstract class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_plant)
        initView()
        Log.e(TAG, javaClass.simpleName)
    }

    companion object {
        private val TAG = "BaseActivity"
    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutId():Int
}
