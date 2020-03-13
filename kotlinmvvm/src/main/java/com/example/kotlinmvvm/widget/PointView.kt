package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View

class PointView : View {

    private val pointDatas: MutableList<PointF> = mutableListOf()
    private val duration: Int = 500  //一个点的变大 变下控制在500ms
    private var index = 1 //当前绘制几个点
    private var dRadiusValue = 0  // 半径变化的差值
    private val pointPaint: Paint = Paint()
    private val pointColor: MutableList<Int> = mutableListOf()
    private var radiusLarge: Float = 0f
    private var radiusSmall: Float = 0f
    private var bigger = true
    private var auto = false

    companion object {
        const val START_ANIMATOR = 0x01
        const val END_ANIMATOR = 0x02
    }

    constructor(context: Context) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        pointPaint.isAntiAlias = true
        pointPaint.style = Paint.Style.FILL_AND_STROKE
        pointPaint.color = Color.RED
    }

    fun start() {
        auto = false
        handler.sendEmptyMessage(START_ANIMATOR)
    }

    fun nextPoint() {
        auto = false
        if (index == pointDatas.size){  //跳过0这个数字
            index = 1
        }else {
            index++
        }
        dRadiusValue = 0
        bigger = true
        handler.sendEmptyMessage(START_ANIMATOR)
    }

    fun resetPoint() {
        handler.sendEmptyMessage(END_ANIMATOR)
    }

    override fun onDraw(canvas: Canvas?) {
//        Log.e("描点","绘制${index}个点")
        if (pointDatas != null && pointDatas.size != 0) {
            for (i in 0 until index) {
//                Log.e("描点","绘制${i}个")
                if (i != index - 1) {
                    drawPointSmall(canvas!!, i)
                } else {
                    drawPointLarge(canvas!!, i)
                }
            }
            if (bigger) {
                dRadiusValue++
            } else {
                dRadiusValue--
            }
            if (bigger && dRadiusValue == 6) {
                bigger = false
            }
            if (auto) {
                if (!bigger && dRadiusValue == -1) {
                    index++
                    dRadiusValue = 0
                    bigger = true
                }
                if (index != pointDatas.size) { //判断是否是最后一波  不是发送START
                    Log.e("描点", "绘制状态${dRadiusValue}")
                    handler.sendEmptyMessageDelayed(START_ANIMATOR, (duration / 11).toLong())
                } else {  //是最后一波发送END
                    handler.sendEmptyMessageDelayed(END_ANIMATOR, (duration / 11).toLong())
                }
            }else{
                if (bigger || dRadiusValue != -1){
                    handler.sendEmptyMessageDelayed(START_ANIMATOR, (duration / 11).toLong())
                }
            }
        }
    }

    private fun drawPointLarge(canvas: Canvas, id: Int) {
        if (id < pointDatas.size && id >= 0) {
            pointPaint.color = pointColor[id]
            canvas.drawCircle(pointDatas[id].x, pointDatas[id].y, radiusSmall + dRadiusValue, pointPaint)
        }
    }

    private fun drawPointSmall(canvas: Canvas, id: Int) {
        pointPaint.color = pointColor[id]
        canvas.drawCircle(pointDatas[id].x, pointDatas[id].y, radiusSmall, pointPaint)
    }

    fun startAuto() {
        auto = true
        handler.sendEmptyMessage(START_ANIMATOR)
    }

    private val handler = object : Handler() {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                START_ANIMATOR -> {
                    invalidate()
                }
                else -> {
                    index = 1
                    dRadiusValue = 0
                    bigger = true
                }
            }
        }
    }

    fun setPointColor(colorDatas: MutableList<Int>) {
        this.pointColor.clear()
        this.pointColor.addAll(colorDatas)
    }

    fun setDatas(data: MutableList<PointF>) {
        pointDatas.clear()
        pointDatas.addAll(data)
    }

    fun setRadiusLarge(radius: Float) {
        radiusLarge = radius
    }

    fun setRadiusSmall(radius: Float) {
        radiusSmall = radius
    }
}