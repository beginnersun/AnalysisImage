package com.example.kotlinmvvm.util.bind_adapter

import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.example.base_module.Constants
import com.example.base_module.rxbinding.RxView
import com.example.base_module.util.TimeUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.util.RichUtil
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.net.URL

object BindAdapters {
    @BindingAdapter("loadPic")
    @JvmStatic
    fun setUrl(imageView: ImageView, url: String?) {
        val requestOptions = RequestOptions().transform(RoundedCorners(10)).placeholder(R.mipmap.ic_launcher)
        if (!TextUtils.isEmpty(url)) {
            if (!url!!.contains("https")){
                Glide.with(imageView.context).load(url.replace("http", "https")).apply(requestOptions).into(imageView)
            }else{
                Glide.with(imageView.context).load(url).apply(requestOptions).into(imageView)
            }
        }else{

        }
    }

    @BindingAdapter("differenceTime")
    @JvmStatic
    fun setUrl(textView: TextView, time: String?) {
        textView.text = TimeUtil.getTimeDifferenceWithCurrent(time!!)
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
    fun TextView.setInfo(count:String?,time:String?){
        text = "你好$count $time"
    }

    @BindingAdapter("rich")
    @JvmStatic
    fun TextView.setRich(rich:String?){
        if (!TextUtils.isEmpty(rich)) {
            GlobalScope.launch(Dispatchers.Main) {
                Log.e("富文本图片大小","$width $height ${View.MeasureSpec.getSize(measuredWidth)} ${View.MeasureSpec.getSize(measuredHeight)}")
                var spanned = RichUtil.delayText(rich!!, Constants.SCREEN_WIDTH,height = height)
                text = spanned
            }
        }
    }

}