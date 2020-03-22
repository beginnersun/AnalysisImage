package com.example.base_module.util

import android.graphics.Bitmap
import android.graphics.Matrix
import android.os.Build

object BitmapUtil {

    fun convertBitmap(src: Bitmap): Bitmap {
        val m = Matrix()
        m.setScale(-1f, 1f)
        val w = src.getWidth()
        val h = src.getHeight()
        //生成的翻转后的bitmap
        val desc = Bitmap.createBitmap(src, 0, 0, w, h, m, true)
        return desc
    }


    fun getBitmapSize(bitmap: Bitmap) =
        when (Build.VERSION.SDK_INT) {
            in 0..12 -> bitmap.rowBytes * bitmap.height
            in 13..19 -> bitmap.byteCount
            else -> bitmap.allocationByteCount
        }


}