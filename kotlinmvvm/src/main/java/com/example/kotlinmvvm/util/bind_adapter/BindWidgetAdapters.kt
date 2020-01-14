package com.example.kotlinmvvm.util.bind_adapter

import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.base_module.rxbinding.RxView
import com.example.base_module.util.TimeUtil

object BindAdapters {
    @BindingAdapter("loadPic")
    @JvmStatic
    fun setUrl(imageView: ImageView, url: String) {
        val requestOptions = RequestOptions().transform(RoundedCorners(10))
        if (!TextUtils.isEmpty(url)) {
            Glide.with(imageView.context).load(url.replace("http", "https")).apply(requestOptions).into(imageView)
        }else{

        }
    }

    @BindingAdapter("differenceTime")
    @JvmStatic
    fun setUrl(textView: TextView, time: String) {
        textView.text = TimeUtil.getTimeDifferenceWithCurrent(time)
    }


    @BindingAdapter("click")
    @JvmStatic
    fun View.onClick(method:()->Unit){
        Log.e("BindAdapter","执行onClick方法")
        RxView.bindClick(this).subscribe{
            if (it!=null) {
                method()
            }
        }
    }

    @BindingAdapter("info", "time")
    @JvmStatic
    fun TextView.setInfo(count:String,time:String){
        this.text = "你好$count $time"
    }
}