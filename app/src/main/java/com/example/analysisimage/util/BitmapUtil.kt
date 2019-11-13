package com.example.analysisimage.util

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Matrix
import android.renderscript.ScriptIntrinsicYuvToRGB
import android.text.AndroidCharacter.mirror
import id.zelory.compressor.Compressor
import okio.Okio
import java.io.ByteArrayOutputStream
import kotlin.concurrent.thread

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