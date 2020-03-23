package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.util.AttributeSet
import android.util.Log
import android.util.LruCache
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import com.example.base_module.util.BitmapUtil
import java.io.InputStream

class BitImageView : View {

    private var mDecoder: BitmapRegionDecoder? = null
    private var mImageWidth = 0
    private var mImageHeight = 0
    private var mImageCurrentWidth = 0f   //截取图片的宽度
    private var mImageCurrentHeight = 0f  //截取图片的长度
    private var oldImageWidth = 0f
    private var oldImageHeight = 0f
    private var mScale = 1f
    private var mMatrix = Matrix()
    @Volatile
    private var mRect = RectF()
    private val options: BitmapFactory.Options = BitmapFactory.Options()
    private var gestureDetector: GestureDetector? = null
    private var scaleGestureDetector: ScaleGestureDetector? = null
    private var maxScale = 5f
    private var minScale = 0.2f

    private val blockSize = 200  //每 200 * 200 为一个块
    private val drawRect = Rect() //当前View的绘制区域  （计算出当前区域能有几块blockSize）

    private var imageCache: LruCache<Point, Bitmap>? = null

    init {
        options.inPreferredConfig = Bitmap.Config.RGB_565
    }

    private var startScroll = 0L

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        setData(context!!.assets.open("girl.jpg"))
    }


    private fun init() {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
                startScroll = System.currentTimeMillis()
//                Log.e("onScroll变化","$mImageCurrentWidth     $mImageWidth     $mImageCurrentHeight     $mImageHeight         $distanceX       $distanceY")
                if (mImageCurrentWidth < mImageWidth) {
                    mRect.left = mRect.left + distanceX / mScale
                    mRect.right = mRect.right + distanceX / mScale
                    checkRectWidth()
                }
                if (mImageCurrentHeight < mImageHeight) {
                    mRect.top = mRect.top + distanceY / mScale
                    mRect.bottom = mRect.bottom + distanceY / mScale
                    checkRectHeight()
                }
                if (mImageCurrentWidth < mImageWidth || mImageCurrentHeight < mImageHeight) {
                    invalidate()
                }
                return super.onScroll(e1, e2, distanceX, distanceY)
            }

            override fun onFling(e1: MotionEvent?, e2: MotionEvent?, velocityX: Float, velocityY: Float): Boolean {
                return super.onFling(e1, e2, velocityX, velocityY)
            }

        })

        scaleGestureDetector =
            ScaleGestureDetector(context, object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScale(detector: ScaleGestureDetector?): Boolean {
                    var tmpScale = detector!!.scaleFactor
                    val newScale = mScale * tmpScale
                    Log.e("onScale缩放", "$tmpScale    $mScale     $newScale     $maxScale    $minScale")
                    if (newScale > maxScale) {
                        tmpScale = maxScale / mScale
                    }
                    if (newScale < minScale) {
                        tmpScale = minScale / mScale
                    }
                    mScale *= tmpScale
                    updateScale(tmpScale)
                    return super.onScale(detector)
                }

                override fun onScaleBegin(detector: ScaleGestureDetector?): Boolean {
                    return true
                }
            })

        imageCache = object : LruCache<Point, Bitmap>(5) {
            override fun sizeOf(key: Point?, value: Bitmap?): Int =
                BitmapUtil.getBitmapSize(value!!)

            override fun create(key: Point?): Bitmap {
                val rect = createBlockRect(key!!.x,key!!.y)
                return super.create(key)
            }

            private fun createBlockRect(x: Int, y: Int) =
                Rect(x * blockSize, y * blockSize, (x + 1) * blockSize, (y + 1) * blockSize)

        }
    }

    private fun updateScale(tmpScale: Float) {
        oldImageWidth = mImageCurrentWidth
        oldImageHeight = mImageCurrentHeight

        mImageCurrentWidth /= tmpScale
        mImageCurrentHeight /= tmpScale

        mRect.left = mRect.left - (mImageCurrentWidth - oldImageWidth) / 2   //当前状态减去要向左边延伸的长度
        mRect.right = mRect.right + (mImageCurrentWidth - oldImageWidth) / 2
        mRect.top = mRect.top - (mImageCurrentHeight - oldImageHeight) / 2
        mRect.bottom = mRect.bottom + (mImageCurrentHeight - oldImageHeight) / 2

        if (mImageCurrentWidth < mImageWidth) {
            checkRectWidth()
        }
        if (mImageCurrentHeight < mImageHeight) {
            checkRectHeight()
        }
        invalidate()
    }

    private fun checkRectWidth() {

        if (mRect.left < 0) {
            mRect.left = 0f
            mRect.right = mRect.left + mImageCurrentWidth
        }
        if (mRect.right > mImageWidth) {
            mRect.right = mImageWidth * 1f
            mRect.left = mRect.right - mImageCurrentWidth
        }
    }

    private fun checkRectHeight() {

        if (mRect.top < 0) {
            mRect.top = 0f
            mRect.bottom = mRect.top + mImageCurrentHeight
        }

        if (mRect.bottom > mImageHeight) {
            mRect.bottom = mImageHeight * 1f
            mRect.top = mRect.bottom - mImageCurrentHeight
        }
    }


    fun setData(inputStream: InputStream) {
        mDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
        val tmpOptions = BitmapFactory.Options()
        tmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, tmpOptions)
        mImageWidth = mDecoder!!.width
        mImageHeight = mDecoder!!.height
        matrix.setScale(mScale, mScale)
        invalidate()
        inputStream.close()

    }

    fun setData(uri: Uri) {
        val inputStream = context.contentResolver.openInputStream(uri)
        mDecoder = BitmapRegionDecoder.newInstance(inputStream, false)
        val tmpOptions = BitmapFactory.Options()
        tmpOptions.inJustDecodeBounds = true
        BitmapFactory.decodeStream(inputStream, null, tmpOptions)
        mImageWidth = mDecoder!!.width
        mImageHeight = mDecoder!!.height
        invalidate()
        inputStream?.close()
    }

    override fun onDraw(canvas: Canvas?) {
        getDrawingRect(drawRect)
        if (mDecoder != null) {
            val startTime = System.currentTimeMillis()
//            Log.e("切割位置","${mRect.left}    ${mRect.right}    ${mRect.top}  ${mRect.bottom}     ${width}    ${height}")
            val rect = Rect().apply {
                left = mRect.left.toInt()
                right = mRect.right.toInt()
                top = mRect.top.toInt()
                bottom = mRect.bottom.toInt()
            }
            mRect.isEmpty
            val bitmap = mDecoder!!.decodeRegion(rect, options)
            mMatrix.setScale(mScale, mScale)
            val middle = System.currentTimeMillis()
            canvas!!.drawBitmap(bitmap, mMatrix, Paint())
            val endTime = System.currentTimeMillis()
            if (bitmap != null && !bitmap.isRecycled) {
                bitmap.recycle()
            }
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

        if (maxScale != -1f && (mImageCurrentWidth != 0f && mImageCurrentHeight != 0f)) {
            maxScale =
                if (mImageWidth / mImageCurrentWidth > mImageHeight / mImageCurrentHeight) mImageWidth / mImageCurrentWidth else mImageHeight / mImageCurrentHeight
            maxScale *= 2
        }
        if (minScale != -1f && (mImageCurrentWidth != 0f && mImageCurrentHeight != 0f)) {
            minScale =
                if (mImageCurrentWidth / mImageWidth < mImageCurrentHeight / mImageHeight) mImageCurrentWidth / mImageWidth else mImageCurrentHeight / mImageHeight
            minScale /= 2
        }
        Log.e("区域大小与缩放范围", "$mImageCurrentWidth    $mImageCurrentHeight     $minScale    $maxScale")

        mRect.left = mImageWidth * 1f / 2 - mImageCurrentWidth * 1f / 2
        mRect.right = mRect.left + mImageCurrentWidth
        mRect.top = mImageHeight * 1f / 2 - mImageCurrentHeight * 1f / 2
        mRect.bottom = mRect.top + mImageCurrentHeight
    }

    private fun preDrawBitmap() {
        val rect = blocks()
        for (i in rect.left..rect.right) {
            for (j in rect.top..rect.bottom) {

            }
        }
    }

    private fun blocks() = Rect(
        floor(drawRect.left / blockSize), floor(drawRect.top / blockSize),
        ceil(drawRect.right / blockSize), ceil(drawRect.bottom / blockSize)
    )

    private fun floor(value: Int) =
        Math.floor(value.toDouble()).toInt()

    private fun ceil(value: Int) =
        Math.ceil(value.toDouble()).toInt()

}