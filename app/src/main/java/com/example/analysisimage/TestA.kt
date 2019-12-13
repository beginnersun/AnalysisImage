package com.example.analysisimage

import android.app.Fragment
import android.graphics.Bitmap

class TestA {

    fun rawByteArray2RGBABitmap2(data: ByteArray, width: Int, height: Int): Bitmap {

        val frameSize = width * height
        val rgba = IntArray(frameSize)
        for (i in 0 until height)
            for (j in 0 until width) {
                var y = 0xff and data[i * width + j].toInt()
                val u = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 0].toInt()
                val v = 0xff and data[frameSize + (i shr 1) * width + (j and 1.inv()) + 1].toInt()
                y = if (y < 16) 16 else y
                var r = Math.round(1.164f * (y - 16) + 1.596f * (v - 128))
                var g = Math.round(1.164f * (y - 16) - 0.813f * (v - 128) - 0.391f * (u - 128))
                var b = Math.round(1.164f * (y - 16) + 2.018f * (u - 128))
                r = if (r < 0) 0 else if (r > 255) 255 else r
                g = if (g < 0) 0 else if (g > 255) 255 else g
                b = if (b < 0) 0 else if (b > 255) 255 else b
                rgba[i * width + j] = -0x1000000 + (b shl 16) + (g shl 8) + r
            }
        val bmp = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        bmp.setPixels(rgba, 0, width, 0, 0, width, height)
        return bmp
    }

}
