package com.example.analysisimage.widget

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.TextView
import com.example.analysisimage.R

class LoadingView(context: Context) :Dialog(context, R.style.Show_LoadingView) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()
    }

    private fun init(){
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context).inflate(R.layout.loading_view,null)
        view.findViewById<TextView>(R.id.loading_text).isSelected = true
        val params = window!!.attributes;
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = params
    }

    override fun show() {
        super.show()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * cancel 与 dismiss  关系   cancel会先发送一条消息 然后再执行dismiss
     */
    override fun dismiss() {
        super.dismiss()
        ObjectAnimator.ofPropertyValuesHolder()
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}