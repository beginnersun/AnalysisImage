package com.example.base_module.util

import android.graphics.Bitmap
import android.graphics.Matrix

object BitmapUtil {

    fun convertBitmap(src:Bitmap):Bitmap{
        val m = Matrix()
            m.setScale(-1f, 1f)
        val w = src.getWidth()
        val h = src.getHeight()
        //生成的翻转后的bitmap
        val desc = Bitmap.createBitmap(src, 0, 0, w, h, m, true)
        return desc
    }

}