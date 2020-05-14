package com.example.base_module.widget

import android.animation.ObjectAnimator
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.WindowManager
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.annotation.GlideModule
import com.bumptech.glide.module.AppGlideModule
import com.example.base_module.R
import kotlinx.android.synthetic.main.loading_view.*


class LoadingView(context: Context) :Dialog(context, R.style.Show_LoadingView) {

    private var bar:ProgressBar? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        init()

    }

    private fun init(){
//        Glide.with(context).load("").
        setCancelable(true)
        setCanceledOnTouchOutside(false)
        val view = LayoutInflater.from(context).inflate(R.layout.loading_view,null)
        setContentView(view)
        view.findViewById<TextView>(R.id.loading_text).isSelected = true
        val params = window!!.attributes
        params.width = WindowManager.LayoutParams.WRAP_CONTENT
        params.height = WindowManager.LayoutParams.WRAP_CONTENT
        window!!.attributes = params
    }

    override fun show() {
        super.show()
        window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

    /**
     * cancel 与 dismiss  关系   cancel会先发送一条消息 然后再执行dismiss
     */
    override fun dismiss() {
        super.dismiss()
        ObjectAnimator.ofPropertyValuesHolder()
        window!!.setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE, WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE)
    }

}