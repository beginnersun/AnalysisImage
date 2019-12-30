package com.example.kotlinmvvm.bind_adapter

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.base_module.util.TimeUtil

object BindAdapters {
    @BindingAdapter("loadPic")
    @JvmStatic
    fun setUrl(imageView: ImageView, url: String) {
        val requestOptions = RequestOptions().transform(RoundedCorners(10))
        Glide.with(imageView.context).load(url.replace("http","https")).apply(requestOptions).into(imageView)
    }

    @BindingAdapter("differenceTime")
    @JvmStatic
    fun setUrl(textView: TextView, time: String) {
        textView.text = TimeUtil.getTimeDifferenceWithCurrent(time)
    }

}