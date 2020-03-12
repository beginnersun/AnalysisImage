package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.*
import android.os.Handler
import android.os.Message
import android.util.AttributeSet
import android.util.Log
import android.view.View

class PointView:View {

    private val pointDatas:MutableList<PointF> = mutableListOf()
    private val duration:Int = 500  //一个点的变大 变下控制在500ms
    private var index = 1 //当前绘制几个点
    private var currentState = 0  //0 代表小点 1 代表大点 2代表小点  绘制到2则切换到下一个点
    private val pointPaint:Paint = Paint()
    private val pointColor:MutableList<Color> = mutableListOf()
    private var radiusLarge:Float = 0f
    private var radiusSmall:Float = 0f

    companion object{
        const val START_ANIMATOR = 0x01
        const val END_ANIMATOR = 0x02
    }

    constructor(context:Context):super(context)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs,0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        pointPaint.isAntiAlias = true
        pointPaint.style = Paint.Style.FILL_AND_STROKE
        pointPaint.color = Color.RED
    }


    override fun onDraw(canvas: Canvas?) {
        Log.e("描点","绘制${index}个点")
        if (pointDatas!= null && pointDatas.size != 0) {
            for (i in 0 until index) {
                Log.e("描点","绘制${i}个")
                if (i != index-1) {
                    drawPointSmall(canvas!!, i)
                } else {
                    if (currentState == 1) {  //只有处于最后一个点 切currentSate == 1 时才绘制大点
                        drawPointLarge(canvas!!, i)
                    } else {
                        drawPointSmall(canvas!!, i)
                    }
                }
            }
            currentState++
            if (currentState == 3) {  //大小点波动完毕
                index++
                currentState = 0
            }
            if (index != pointDatas.size) { //判断是否是最后一波  不是发送START
                Log.e("描点","绘制状态${currentState}")
                handler.sendEmptyMessageDelayed(START_ANIMATOR, (2000f).toLong())
            } else {  //是最后一波发送END
                handler.sendEmptyMessageDelayed(END_ANIMATOR, (2000f).toLong())
            }
        }
    }

    private fun drawPointLarge(canvas: Canvas,id:Int){
        if (id < pointDatas.size && id >= 0) {
            canvas.drawCircle(pointDatas[id].x,pointDatas[id].y,radiusLarge,pointPaint)
        }
    }

    private fun drawPointSmall(canvas: Canvas,id:Int){
        canvas.drawCircle(pointDatas[id].x,pointDatas[id].y,radiusSmall,pointPaint)
    }

    fun start(){
        handler.sendEmptyMessage(START_ANIMATOR)
    }

    private val handler = object: Handler(){
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when(msg.what){
                START_ANIMATOR ->{
                    invalidate()
                }
                else ->{
                    index = 0
                }
            }
        }
    }

    fun setDatas(data:MutableList<PointF>){
        pointDatas.clear()
        pointDatas.addAll(data)
    }

    fun setRadiusLarge(radius:Float){
        radiusLarge = radius
    }

    fun setRadiusSmall(radius:Float){
        radiusSmall = radius
    }
}