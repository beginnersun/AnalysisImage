package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.example.kotlinmvvm.R
import java.io.InputStream
import kotlin.math.max

class BitImageView : View {

    private var mDecoder:BitmapRegionDecoder? = null
    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mImageCurrentWidth = 0f   //截取图片的宽度
    private var mImageCurrentHeight = 0f  //截取图片的长度
    private var oldImageWidth = 0f
    private var oldImageHeight = 0f
    private var mScale = 1f
    private var mMatrix = Matrix()
    @Volatile private var mRect = RectF()
    private val options:BitmapFactory.Options = BitmapFactory.Options()
    private var gestureDetector: GestureDetector? = null
    private var scaleGestureDetector:ScaleGestureDetector? = null
    private var maxScale = 5f
    private var minScale = 0.2f
    init {
        options.inPreferredConfig = Bitmap.Config.RGB_565
    }

    constructor(context: Context?) : super(context){
    }

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr){
        init()
        setData(context!!.assets.open("big_bg.jpg"))
//        setData(R.mipmap.big_bg)
    }

    private fun init(){
        gestureDetector = GestureDetector(context,object :GestureDetector.SimpleOnGestureListener(){

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                Log.e("onScroll位置","${e1!!.x}   ${e1!!.y}     ${e2!!.x}   ${e2!!.y}      $distanceX       $distanceY")
                Log.e("onScroll变化","$mImageCurrentWidth     $mImageWidth     $mImageCurrentHeight     $mImageHeight")
                if (mImageCurrentWidth < mImageWidth) {
                    mRect.left = mRect.left + distanceX
                    mRect.right = mRect.right + distanceX
                    checkRectWidth()
                }
                if (mImageCurrentHeight < mImageHeight){
                    mRect.top = mRect.top + distanceY
                    mRect.bottom = mRect.bottom + distanceY
                    checkRectHeight()
                }
                if (mImageCurrentWidth < mImageWidth || mImageCurrentHeight < mImageHeight){
                    invalidate()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return super.onFling(e1, e2, velocityX, velocityY)
            }

        })

        scaleGestureDetector = ScaleGestureDetector(context,object :ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                var tmpScale = detector!!.scaleFactor
                val newScale = mScale * tmpScale
                if (newScale > maxScale){
                    tmpScale = maxScale/mScale
                }
                if (newScale < minScale){
                    tmpScale = minScale / mScale
                }
//                if (mScale < maxScale && mScale > minScale){
//                    mScale *=tmpScale
//                    updateScale(tmpScale)
//                }
                mScale *=tmpScale
                updateScale(tmpScale)
//                if (mScale >=maxScale){
//                    val newScale = maxScale/(mScale/tmpScale)
//                    mScale = maxScale
//                }
//                if (mScale <= minScale){
//                    val newScale = minScale/(mScale/tmpScale)
//                    mScale = minScale
//                    updateScale(newScale)
//                    return super.onScale(detector)
//                }
//                updateScale(tmpScale)
                return super.onScale(detector)
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }
        })
    }

    private fun updateScale(tmpScale : Float){
        Log.e("缩放的值","$tmpScale")
        oldImageWidth = mImageCurrentWidth
        oldImageHeight = mImageCurrentHeight

        mImageCurrentWidth /= tmpScale
        mImageCurrentHeight /= tmpScale

        mRect.left = mRect.left - (mImageCurrentWidth-oldImageWidth)/2   //当前状态减去要向左边延伸的长度
        mRect.right = mRect.right + (mImageCurrentWidth-oldImageWidth)/2
        mRect.top = mRect.top - (mImageCurrentHeight - oldImageHeight)/2
        mRect.bottom = mRect.bottom + (mImageCurrentHeight - oldImageHeight)/2

        if (mImageCurrentWidth < mImageWidth) {
            checkRectWidth()
        }
        if (mImageCurrentHeight < mImageHeight){
            checkRectHeight()
        }
        Log.e("生成位置","${mRect.left}    ${mRect.right}    ${mRect.top}  ${mRect.bottom}     ${mImageCurrentWidth}    ${mImageCurrentHeight}")
        invalidate()
    }

    private fun checkRectWidth(){

        if (mRect.left < 0){
            mRect.left = 0f
            mRect.right = mRect.left + mImageCurrentWidth
        }
        if (mRect.right > mImageWidth){
            mRect.right = mImageWidth*1f
            mRect.left = mRect.right - mImageCurrentWidth
        }
    }

    private fun checkRectHeight(){

        if (mRect.top < 0){
            mRect.top = 0f
            mRect.bottom = mRect.top + mImageCurrentHeight
        }

        if (mRect.bottom > mImageHeight){
            mRect.bottom = mImageHeight*1f
            mRect.top = mRect.bottom - mImageCurrentHeight
        }
    }


    fun setData(inputStream: InputStream){
        mDecoder = BitmapRegionDecoder.newInstance(inputStream,false)
        val tmpOptions = BitmapFactory.Options()
        tmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream,null,tmpOptions)
        mImageWidth = mDecoder!!.width
        mImageHeight = mDecoder!!.height
        matrix.setScale(mScale,mScale)
        invalidate()
        inputStream.close()
    }

    fun setData(uri: Uri){
        val inputStream = context.contentResolver.openInputStream(uri)
        mDecoder = BitmapRegionDecoder.newInstance(inputStream,false)
        val tmpOptions = BitmapFactory.Options()
        tmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream,null,tmpOptions)
        mImageWidth = mDecoder!!.width
        mImageHeight = mDecoder!!.height
        invalidate()
        inputStream?.close()
    }

    fun setData(resource:Int){
        Log.e("路径","android.resource://${context.packageName}/${resource}")
        setData(Uri.parse("android.resource://${context.packageName}/${resource}"))
    }

    override fun onDraw(canvas: Canvas?) {
        if(mDecoder!=null) {
            Log.e("切割位置","${mRect.left}    ${mRect.right}    ${mRect.top}  ${mRect.bottom}     ${width}    ${height}")
            val bitmap = mDecoder!!.decodeRegion(Rect().apply {
                left = mRect.left.toInt()
                right = mRect.right.toInt()
                top = mRect.top.toInt()
                bottom = mRect.bottom.toInt()
            }, options)
            mMatrix.setScale(mScale,mScale)
            canvas!!.drawBitmap(bitmap, mMatrix, Paint())
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector!!.onTouchEvent(event)
        gestureDetector!!.onTouchEvent(event)
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mImageCurrentWidth = measuredWidth.toFloat()
        mImageCurrentHeight = measuredHeight.toFloat()

        if (maxScale != -1f && (mImageCurrentWidth != 0f && mImageCurrentHeight != 0f)){
            maxScale = if (mImageWidth /mImageCurrentWidth > mImageHeight / mImageCurrentHeight) mImageWidth / mImageCurrentWidth else mImageHeight / mImageCurrentHeight
            maxScale *= 2
        }
        if (minScale != -1f && (mImageCurrentWidth != 0f && mImageCurrentHeight != 0f)){
            minScale = if (mImageWidth /mImageCurrentWidth < mImageHeight / mImageCurrentHeight) mImageWidth / mImageCurrentWidth else mImageHeight / mImageCurrentHeight
            minScale /= 2
        }
        Log.e("缩放的范围","$maxScale     $minScale")
//
        mRect.left = mImageWidth*1f / 2 - mImageCurrentWidth*1f /2
        mRect.right = mRect.left + mImageCurrentWidth
        mRect.top = mImageHeight*1f / 2 - mImageCurrentHeight*1f / 2
        mRect.bottom = mRect.top + mImageCurrentHeight
    }

}