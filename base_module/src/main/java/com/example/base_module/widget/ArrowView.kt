package com.example.base_module.widget

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

class ArrowView : View {

    //  arrow  16,10     dot   5,5
    //  留给周围触摸区域的大小 所以宽度总共为 16 + 17*2  = 50   高wei  5 * 3 + 10 + 4 * 5 = 45


    private val arrowPaint: Paint = Paint()
    private val dotPaint: Paint = Paint()
    private var mWidthMode = MeasureSpec.AT_MOST  //默认View的尺寸模式为wrap_content
    private var mHeightMode = MeasureSpec.AT_MOST
    private var mWidth = 0
    private var mHeight = 0
    private val defaultWidth = 50
    private val defaultHeight = 45
    private var radius = 2.5f //点的半径
    private var space = 5f  //间距
    private var arrowTop = 0f
    private var arrowRadius = 3f
    private var arrowHeiht = 10f

    private var centerX = 0f

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = MeasureSpec.getSize(widthMeasureSpec)
        mHeight = MeasureSpec.getSize(heightMeasureSpec)
        mWidthMode = MeasureSpec.getMode(widthMeasureSpec)
        mHeight = MeasureSpec.getMode(heightMeasureSpec)

        setMeasuredDimension(
            if (mWidthMode == MeasureSpec.EXACTLY) mWidth else defaultWidth,
            if (mHeightMode == MeasureSpec.EXACTLY) mHeight else defaultHeight
        )
    }

    private fun init() {
        arrowPaint.isAntiAlias = true
        arrowPaint.color = Color.WHITE
        arrowPaint.style = Paint.Style.FILL_AND_STROKE

        dotPaint.isAntiAlias = true
        dotPaint.color = Color.WHITE
        dotPaint.strokeWidth = radius
    }

    private fun initSizeInfo(){
        centerX = (mWidth / 2).toFloat()
        if (defaultWidth == mWidth || defaultHeight == mHeight){
            arrowTop = mHeight - arrowHeiht
        }else{
            var scale = 0f
            if ( mWidth / mHeight > defaultWidth / defaultHeight ){
                scale = mHeight*1f / defaultHeight
            }
            space *= scale
            arrowHeiht *= scale
            radius *= scale
            arrowRadius *= scale
            arrowTop = mHeight - arrowHeiht
        }
    }

    override fun onDraw(canvas: Canvas?) {
        initSizeInfo()
        for (i in 1..3){
            drawDot(canvas!!,i)
        }
        drawArrow(canvas!!)
    }

    private fun drawDot(canvas: Canvas,i:Int){
        canvas.drawCircle(centerX,i*space + (i-1)*radius*2 + radius,radius,dotPaint)
    }

    private fun drawArrow(canvas: Canvas){
        val path = Path()
        path.moveTo(centerX - arrowRadius,arrowTop)
        path.lineTo(centerX,mHeight.toFloat())
        path.lineTo(centerX + arrowRadius,arrowTop)
        canvas.drawPath(path,arrowPaint)
    }
}