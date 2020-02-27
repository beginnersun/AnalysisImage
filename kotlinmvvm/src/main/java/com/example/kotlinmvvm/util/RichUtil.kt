package com.example.kotlinmvvm.util

import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import android.text.Html
import android.text.Spanned
import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.URL

class RichUtil {




    companion object{

        suspend fun delayText(htmlInfo:String,width:Int = 1,height:Int = 1): Spanned =
            withContext(Dispatchers.IO) {
                if(Build.VERSION.SDK_INT >= 24){
                    Html.fromHtml(htmlInfo, Html.FROM_HTML_MODE_LEGACY, ImageGetter(width,height), null)
                }else {
                    Html.fromHtml(htmlInfo, ImageGetter(width,height), null)
                }
            }

        //这里面的resource就是fromhtml函数的第一个参数里面的含有的url
        private val imgGetter: Html.ImageGetter = Html.ImageGetter { source ->
            var drawable: Drawable?
            val url: URL
            try {
                url = URL(source)
                drawable = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
            } catch (e: Exception) {
                Log.e("出错开始", "1")
                e.printStackTrace()
                Log.e("出错结束", "2")
                return@ImageGetter null
            }
            drawable!!.setBounds(
                0, 0, drawable!!.minimumWidth,
                drawable!!.minimumHeight
            )
            drawable
        }


    }

    class ImageGetter(var width:Int = 1,var height: Int = 1):Html.ImageGetter{
        override fun getDrawable(source: String?): Drawable {
            var drawable: Drawable? = null
            val url: URL
            try {
                url = URL(source)
                drawable = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
            } catch (e: Exception) {
                Log.e("出错开始", "1")
                e.printStackTrace()
                Log.e("出错结束", "2")
                return ColorDrawable()
            }
            if (width != 1 && height != 1){
                var scale = drawable!!.intrinsicHeight*1f / drawable!!.intrinsicWidth
                drawable!!.setBounds(
                    0, 0, (width*0.8f).toInt(),
                    (width*scale*0.8f).toInt()
                )
            }else {
                drawable!!.setBounds(
                    0, 0, drawable!!.minimumWidth,
                    drawable!!.minimumHeight
                )
            }
            return drawable
        }

    }

}