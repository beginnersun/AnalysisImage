package com.example.kotlinmvvm.bind_adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.example.base_module.util.TimeUtil

object BindAdapters {
    @BindingAdapter("loadPic")
    @JvmStatic
    fun setUrl(imageView: ImageView, url: String) {
        Glide.with(imageView.context).load(url).into(imageView)
    }

    @BindingAdapter("differenceTime")
    @JvmStatic
    fun setUrl(textView: TextView, time: String) {
        textView.text = TimeUtil.getTimeDifferenceWithCurrent(time)
    }
}