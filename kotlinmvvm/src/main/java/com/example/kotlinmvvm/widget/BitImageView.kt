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

class BitImageView : View {

    private var mDecoder:BitmapRegionDecoder? = null
    private var mImageWidth = 0
    private var mImageHeight = 0
    @Volatile private var mRect = Rect()
    private val options:BitmapFactory.Options = BitmapFactory.Options()
    private var gestureDetector: GestureDetector? = null
    private var scaleGestureDetector:ScaleGestureDetector? = null
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
                mRect.left = (mRect.left + distanceX).toInt()
                mRect.top = mRect.top + distanceY.toInt()
                if (width < mImageWidth) {
                    checkRectWidth()
                }
                if (height < mImageHeight){
                    checkRectHeight()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                Log.e("onFling位置","${e1!!.x}   ${e1!!.y}     ${e2!!.x}   ${e2!!.y}      $velocityX       $velocityY")
                return super.onFling(e1, e2, velocityX, velocityY)
            }

        })

        scaleGestureDetector = ScaleGestureDetector(context,object :ScaleGestureDetector.SimpleOnScaleGestureListener(){
            override fun onScale(detector: ScaleGestureDetector?): Boolean {
                Log.e("onScale缩放位置","${detector!!.focusX}  ${detector!!.focusY}       ${detector!!.currentSpan}    ${detector!!.currentSpanX}    ${detector!!.currentSpanY}")
                return super.onScale(detector)
            }

            override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                return true
            }
        })
    }

    private fun checkRectWidth(){

        if (mRect.top < 0){
            mRect.top = 0
            mRect.bottom = mRect.top + height
        }
        if (mRect.bottom > mImageHeight){
            mRect.bottom = mImageHeight
            mRect.top = mRect.bottom - height
        }
    }

    private fun checkRectHeight(){

        if (mRect.top < 0){
            mRect.top = 0
            mRect.bottom = mRect.top + height
        }
        if (mRect.bottom > mImageHeight){
            mRect.bottom = mImageHeight
            mRect.top = mRect.bottom - height
        }
    }


    fun setData(inputStream: InputStream){
        mDecoder = BitmapRegionDecoder.newInstance(inputStream,false)
        val tmpOptions = BitmapFactory.Options()
        tmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream,null,tmpOptions)
        mImageWidth = mDecoder!!.width
        mImageHeight = mDecoder!!.height
        Log.e("option Size","$mImageWidth   $mImageHeight")
        Log.e("Decoder Size","${mDecoder!!.width}   ${mDecoder!!.height}")
        requestLayout()
        invalidate()
        if (inputStream!=null){
            inputStream.close()
        }
    }

    fun setData(uri: Uri){
        val inputStream = context.contentResolver.openInputStream(uri)
        if (inputStream!=null){
            Log.e("图片大小","${inputStream}")
        }
        mDecoder = BitmapRegionDecoder.newInstance(inputStream,false)
        val tmpOptions = BitmapFactory.Options()
        tmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream,null,tmpOptions)
        mImageWidth = mDecoder!!.width
        mImageHeight = mDecoder!!.height
        Log.e("option Size","$mImageWidth   $mImageHeight")
        Log.e("Decoder Size","${mDecoder!!.width}   ${mDecoder!!.height}")
//        requestLayout()
        invalidate()
        if (inputStream!=null){
            inputStream.close()
        }
    }

    fun setData(resource:Int){
        Log.e("路径","android.resource://${context.packageName}/${resource}")
        setData(Uri.parse("android.resource://${context.packageName}/${resource}"))
    }

    override fun onDraw(canvas: Canvas?) {
        if(mDecoder!=null) {
            val bitmap = mDecoder!!.decodeRegion(mRect, options)
            canvas!!.drawBitmap(bitmap, 0f, 0f, null)
        }
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        scaleGestureDetector!!.onTouchEvent(event)
        gestureDetector!!.onTouchEvent(event)
        return true
//        scaleGestureDetector!!.
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        val width = measuredWidth
        val height = measuredHeight
        Log.e("测量大小1","$mImageWidth    $mImageHeight    ${mRect.left}  ${mRect.right}      ${mRect.top}  ${mRect.bottom}" )

        mRect.left = mImageWidth / 2 - width /2
        mRect.right = mRect.left + width
        mRect.top = mImageHeight / 2 - height / 2
        mRect.bottom = mRect.top + height
        Log.e("测量大小2","$mImageWidth    $mImageHeight    ${mRect.left}  ${mRect.right}      ${mRect.top}  ${mRect.bottom}" )

    }

}