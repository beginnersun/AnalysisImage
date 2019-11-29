package com.example.analysisimage.util

import android.graphics.PointF
import androidx.camera.core.MeteringPointFactory

class TextureMeteringPointFactory:MeteringPointFactory() {
    override fun translatePoint(x: Float, y: Float): PointF {
        return PointF(x,y)
    }
}