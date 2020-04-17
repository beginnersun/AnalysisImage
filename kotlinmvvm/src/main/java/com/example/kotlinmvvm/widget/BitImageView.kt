package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Handler
import android.os.HandlerThread
import android.os.Message
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

    private val blockSize = 300  //每 200 * 200 为一个块
    private val drawRect = Rect() //当前View的绘制区域  （计算出当前区域能有几块blockSize）

    private var imageCache: LruCache<Point, Bitmap>? = null
    private val drawables = mutableListOf<ImageDrawable>()
    private var oldBlocks = Rect()

    private val handlerThread:HandlerThread = HandlerThread("handlerThread")
    private var mHandler:Handler? = null

    init {
        options.inPreferredConfig = Bitmap.Config.RGB_565

    }

    private var startScroll = 0L

    constructor(context: Context?) : this(context, null, 0)

    constructor(context: Context?, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
        setData(context!!.assets.open("beautiful_girl.jpg"))
    }


    private fun init() {
        gestureDetector = GestureDetector(context, object : GestureDetector.SimpleOnGestureListener() {

            override fun onScroll(e1: MotionEvent?, e2: MotionEvent?, distanceX: Float, distanceY: Float): Boolean {
//                startScroll = System.currentTimeMillis()
//                scrollBy(distanceX.toInt(), distanceY.toInt())
//                invalidate()
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
//                    mHandler!!.sendEmptyMessage(1)
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

//        handlerThread.start()
//        mHandler = object: Handler(handlerThread.looper){
//            override fun handleMessage(msg: Message) {
//                super.handleMessage(msg)
//                if (msg.what == 1){
//                    val rect = Rect().apply {
//                        left = mRect.left.toInt()
//                        right = mRect.right.toInt()
//                        top = mRect.top.toInt()
//                        bottom = mRect.bottom.toInt()
//                    }
//                    preDrawBitmap(rect)
//                    postInvalidate()
//                }
//            }
//        }

//
        val maxSize = (context.resources.displayMetrics.widthPixels *
                context.resources.displayMetrics.heightPixels) shl 4
//            Runtime.getRuntime().maxMemory() / 1024  / 2
        Log.e("大小",maxSize.toString())
        imageCache = object : LruCache<Point, Bitmap>(maxSize) {
            override fun sizeOf(key: Point?, value: Bitmap?): Int =
                BitmapUtil.getBitmapSize(value!!)

            override fun create(key: Point?): Bitmap {
                val rect = createBlockRect(key!!.x,key!!.y)
                Log.e("大小对比","$rect       ${mDecoder!!.width}    ${mDecoder!!.height}")
                return mDecoder!!.decodeRegion(rect,options)
            }

            private fun createBlockRect(x: Int, y: Int) =
                Rect(x * blockSize, y * blockSize,
                    if((x + 1) * blockSize > mDecoder!!.width) mDecoder!!.width else (x + 1) * blockSize,
                    if((y + 1) * blockSize > mDecoder!!.height) mDecoder!!.height else (y + 1) * blockSize )

//            override fun entryRemoved(evicted: Boolean, key: Point?, oldValue: Bitmap?, newValue: Bitmap?) {
//                oldValue?.recycle()
//            }
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
//        getDrawingRect(drawRect)
//        Log.e("translate后的变化","$drawRect")
        if (mDecoder != null) {
            val rect = Rect().apply {
                left = mRect.left.toInt()
                right = mRect.right.toInt()
                top = mRect.top.toInt()
                bottom = mRect.bottom.toInt()
            }
//            val startTime = System.currentTimeMillis()
            preDrawBitmap(rect)
//            val middle = System.currentTimeMillis()
//            Log.e("时间差1","${middle - startTime}")
            for (item in drawables){
                canvas!!.drawBitmap(item.bitmap,item.src,item.dst,null)
            }
//            val endTime = System.currentTimeMillis()
//            Log.e("时间差2","${endTime - middle}      总的差值  ${endTime - startTime}")
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
        mHandler!!.sendEmptyMessage(1)
    }

    private fun preDrawBitmap(rect: Rect) {
        Log.e("初始切割","$mImageCurrentWidth    $mImageCurrentHeight    $rect")
        val blocks = blocks(rect)
        val offsetX = rect.left % blockSize
        val offsetY = rect.top % blockSize
        val offsetRightX = rect.right % blockSize
        val offsetBottomY = rect.bottom % blockSize
        var count = 1
        for (i in blocks.top .. blocks.bottom) {
            for (j in blocks.left .. blocks.right) {
                val start = System.currentTimeMillis()
                val bitmap = imageCache!!.get(Point(j,i))
                val end = System.currentTimeMillis()
                if (bitmap!= null) {
//                    Log.e("读取切割原图$count","${bitmap.width}      ${bitmap.height}     $offsetX     $offsetY")
                    val src = createBitmapRect(bitmap)
                    if (j == blocks.left) {
                        src.left = offsetX
                    }
                    if (j == blocks.right) {
                        src.right = offsetRightX
                    }
                    if (i == blocks.top) {
                        src.top = offsetY
                    }
                    if (i == blocks.bottom) {
                        src.bottom = offsetBottomY
                    }
//                    Log.e("读取切割区域$count","$src")
                    var right =
                        (j - blocks.left) * blockSize + blockSize - offsetX   //或者说 (i-blocks.left+1)*blockSize - offsetX
                    right = if (right > mImageCurrentWidth) mImageCurrentWidth.toInt() else right

                    var bottom = (i - blocks.top) * blockSize + blockSize - offsetY
                    bottom = if (bottom > mImageCurrentHeight ) mImageCurrentHeight.toInt() else bottom

                    var left = right - (src.right - src.left)
                    left = if (left < 0) 0 else left

                    var top = bottom - (src.bottom - src.top)
                    top = if (top < 0) 0 else top

                    val dst = Rect(left, top, right, bottom)
//                    Log.e("绘制切割区域$count","$dst")
                    val drawable = ImageDrawable(bitmap, src, dst)
                    drawables.add(drawable)
                    count++
                }
            }
        }
    }

    private fun blocks(rect:Rect) = Rect(
        floor(rect.left / blockSize), floor(rect.top / blockSize),
        ceil(rect.right / blockSize), ceil(rect.bottom / blockSize)
    )

    private fun floor(value: Int) =
        Math.floor(value.toDouble()).toInt()

    private fun ceil(value: Int) =
        Math.ceil(value.toDouble()).toInt()

    private fun createBitmapRect(bitmap:Bitmap) = Rect(0,0,bitmap.width,bitmap.height)

    data class ImageDrawable(var bitmap:Bitmap,var src:Rect,var dst:Rect)
}