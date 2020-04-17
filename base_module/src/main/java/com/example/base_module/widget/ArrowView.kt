package com.example.base_module.widget

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import com.example.base_module.Constants

class ArrowView : View {

    //  arrow  16,10     dot   5,5
    //  留给周围触摸区域的大小 所以宽度总共为 16 + 17*2  = 50   高wei  5 * 3 + 10 + 4 * 5 = 45


    private val arrowPaint: Paint = Paint()
    private val dotPaint: Paint = Paint()
    private var mWidthMode = MeasureSpec.AT_MOST  //默认View的尺寸模式为wrap_content
    private var mHeightMode = MeasureSpec.AT_MOST
    private var mWidth = 0
    private var mHeight = 0
    private val defaultWidth = 25
    private val defaultHeight = 45
    private var radius = 2.5f //点的半径
    private var space = 5f  //间距
    private var arrowTop = 0f
    private var arrowRadius = 8.2f
    private var arrowHeight = 10f

    private var centerX = 0f
    private var mPaddingTop = 0f

    private var oldTransotionY = 0f
    private val animatorSet = AnimatorSet()

    var duration = 1000L

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
        mHeightMode = MeasureSpec.getMode(heightMeasureSpec)

        setMeasuredDimension(
            if (mWidthMode == MeasureSpec.EXACTLY) mWidth else defaultWidth,
            if (mHeightMode == MeasureSpec.EXACTLY) mHeight else defaultHeight
        )
    }

    private fun init() {
        arrowPaint.isAntiAlias = true
        arrowPaint.color = Color.WHITE
        arrowPaint.strokeWidth = radius
        arrowPaint.style = Paint.Style.STROKE

        dotPaint.isAntiAlias = true
        dotPaint.color = Color.WHITE
        dotPaint.style = Paint.Style.FILL_AND_STROKE
//        context!!.getDir()
    }

    private fun initSizeInfo(){
        centerX = (mWidth / 2).toFloat()
        if (defaultWidth == mWidth || defaultHeight == mHeight){
        } else{
            var scale = 0f
            scale = (mWidth * 1f / defaultWidth).coerceAtMost(mHeight * 1f / defaultHeight)
            Log.e("绘制比例大小","$scale   ${mWidth * 1f / defaultWidth}       ${mHeight * 1f / defaultHeight}")
            space = 5f * scale
            arrowHeight = 10f * scale
            radius = 2.5f * scale
            arrowRadius = 8.2f* scale

            mPaddingTop = mHeight - space * 4 - radius*6 - arrowHeight
            arrowPaint.strokeWidth = radius
        }
        arrowTop = mHeight - arrowHeight
        Log.e("绘制数据大小:"," $centerX    $space     $radius     $arrowRadius      $arrowTop     $arrowHeight")
    }

    override fun onDraw(canvas: Canvas?) {
        Log.e("绘制数据大小","$mWidth      $mHeight")
        initSizeInfo()
        for (i in 1..3){
            drawDot(canvas!!,i)
        }
        drawArrow(canvas!!)
    }

    private fun drawDot(canvas: Canvas,i:Int){
        Log.e("绘制点","$i    $centerX     ${i*space + (i-1)*radius*2 + radius}    $radius")
        canvas.drawCircle(centerX,mPaddingTop+i*space + (i-1)*radius*2 + radius,radius,dotPaint)
    }

    fun start(){
        if (animatorSet.isPaused){
            animatorSet.start()
            return
        }
        oldTransotionY = translationY
        val animatorStart = ObjectAnimator.ofFloat(this,"translationY",translationY, 0f)
        val animatorEnd = ObjectAnimator.ofFloat(this,"translationY",0f, oldTransotionY)
        animatorStart.interpolator = AccelerateInterpolator(2f)
        animatorEnd.interpolator = DecelerateInterpolator(2f)
        animatorSet.play(animatorStart).before(animatorEnd)
        animatorSet.duration = duration
        animatorSet.start()
        animatorSet.addListener(object:Animator.AnimatorListener{
            override fun onAnimationEnd(animation: Animator?) {
                animatorSet.start()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationStart(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    fun cancel(){
        animatorSet.cancel()
        animatorSet.reverse()
        animatorSet.removeAllListeners()
    }

    fun pause(){
        animatorSet.pause()
    }

    private fun drawArrow(canvas: Canvas){
        val path = Path()
        arrowPaint.strokeJoin = Paint.Join.ROUND
        path.moveTo(centerX - arrowRadius,arrowTop)
        path.lineTo(centerX,mHeight.toFloat()-radius)
        path.lineTo(centerX + arrowRadius,arrowTop)
        canvas.drawPath(path,arrowPaint)
        canvas.drawCircle(centerX - arrowRadius,arrowTop,radius/2.5f,dotPaint)
        canvas.drawCircle(centerX + arrowRadius,arrowTop,radius/2.5f,dotPaint)
    }


}