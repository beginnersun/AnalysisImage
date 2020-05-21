package com.example.analysisimage.base

import android.content.Intent
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Bundle
import androidx.annotation.IntDef
import androidx.annotation.LayoutRes
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import android.view.View
import com.example.analysisimage.R
import kotlin.jvm.internal.Ref

open abstract class BaseActivity : AppCompatActivity() {

    protected var saturation:Float = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(getLayoutId())
        initView()
        changeSkin()
        Log.e(TAG, javaClass.simpleName)
    }

    fun getCC():Float = saturation

    private fun changeSkin(){
        val paint = Paint()
        val matrix = ColorMatrix()
        matrix.setSaturation(getCC())
        paint.colorFilter = ColorMatrixColorFilter(matrix)
        window.decorView.setLayerType(View.LAYER_TYPE_HARDWARE,paint)
    }

    companion object {
        private val TAG = "BaseActivity"
    }

    abstract fun initData()

    abstract fun initView()

    abstract fun getLayoutId():Int

}
