package com.example.kotlinmvvm.widget

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextPaint
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ItemDecoration


class ContactDecoration constructor() : ItemDecoration() {

    private var map: Map<Int, String>? = null
    private var titleHeight: Int = 100
    private val backGroundPaint = Paint()
    private val textPaint = TextPaint()
    private var mTextHeight = 0
    private var mTextBaselineOffset = 0
    private var mTextStartMargin = 0

    fun setMap(map: Map<Int, String>) {
        this.map = map
    }

    constructor(map: Map<Int, String>) : this() {
        this.map = map
        map[0] to "X"
        backGroundPaint.isAntiAlias = true
        backGroundPaint.color = Color.parseColor("#504426")

        textPaint.color = Color.parseColor("#E0C864")
        textPaint.textSize = 40f
        val fm = textPaint.fontMetrics
        mTextHeight = (fm.bottom - fm.top).toInt()
        mTextBaselineOffset = fm.bottom.toInt()
        mTextStartMargin = 50
    }

    /**
     * 先对所有的子View调用此方法
     */
    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as RecyclerView.LayoutParams).viewAdapterPosition
        val height = if (map!!.keys.contains(position)) titleHeight else 0
        Log.e("判断你是否需要", "$height           $position")
        outRect.set(0, height, 0, 0)
//        outRect.set(0,50,0,20)
    }

    /**
     * 然后执行绘制
     * 通过getItemOffsets方法将每个子View所占据的大小重新分配
     * 所以item不是属于子View尺寸中的一部分 只是对应的item上方留出来的空白区域
     */
    override fun onDraw(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDraw(c, parent, state)
        val left = parent.paddingLeft
        val right = parent.width - parent.paddingRight
        for (i in 0 until parent.childCount) {
            val child = parent.getChildAt(i)
            val params = (child.layoutParams as RecyclerView.LayoutParams)
            val position = params.viewAdapterPosition
            if (map?.keys!!.contains(position)) {
                val bottom = child.top - params.topMargin
                val top = bottom - titleHeight
//                val title:String = map!!
                val title = map!![position]
                Log.e("准备执行绘制", "$title    $left    $top    $right     $bottom    $position")
                drawTitle(c, title!!, left, top, right, bottom)
            }
        }
    }

    private fun drawTitle(canvas: Canvas, title: String, left: Int, top: Int, right: Int, bottom: Int) {
        canvas.drawRect(left.toFloat(), top.toFloat(), right.toFloat(), bottom.toFloat(), backGroundPaint)
        canvas.drawText(
            title, (left + mTextStartMargin).toFloat(),
            (bottom - (titleHeight - mTextHeight) / 2 - mTextBaselineOffset).toFloat(), textPaint
        )
    }

    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
        super.onDrawOver(c, parent, state)
        val first = (parent.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        if (first == RecyclerView.NO_POSITION) {
            return
        }
        var flag = false
        val child = parent.findViewHolderForAdapterPosition(first)!!.itemView
        val tag = getTag(first)

        if (TextUtils.equals(tag, getTag(first + 1))) {  //当前页面第一个item与下一个item的tag一样说明还没有碰撞

        }else{  //如果碰撞 产生位移动画
            Log.e("碰撞动画","${child.height}   ${child.top}    ${titleHeight}   $first")
            if (child.height + child.top < titleHeight){   //child.top  +  child.height  (就是当前屏幕上他还剩余的大小  当child有一部分被遮挡时他的top就是遮挡高度的负值 而height不会变所以想加就是剩余大小)
                // 当剩余大小与titleHeight相等时代表即将相撞
                c.save()  //保存当前画布的信息然后重新生成一个位图
                flag = true
                c.translate(0f, (child.height+child.top - titleHeight).toFloat()) //改变当前坐标系的原点的位置为0,(child.height+child.top - titleHeight)
            }
        }
        //如果执行过 translate  那么坐标系的原点会改变  虽然绘drawRect与drawText方法中参数没变 但是实际在整个画布中已经改变了
        c.drawRect(parent.paddingLeft.toFloat(),
            parent.paddingTop.toFloat(),
            (parent.right - parent.paddingRight).toFloat(),
            (parent.paddingTop + titleHeight).toFloat(),backGroundPaint)

        c.drawText(tag,
            (child.paddingLeft+mTextStartMargin).toFloat(),
            (parent.paddingTop+titleHeight - (titleHeight - mTextHeight) / 2 - mTextBaselineOffset).toFloat()
            ,textPaint)

        if (flag){   //如果 画布上移过 那么 还原画布之前的状态然后就会产生一个上移的效果  (也可以说还原为原来的坐标系  如果不执行restore那么相当于没变）
            // 以上效果仅仅是为了将文字内容绘制的位置上移    也可以直接调用drawText改变绘制位置 但是比较麻烦  如果直接使用translate会比较简单
            c.restore()
        }
    }

    private fun getTag(index: Int): String {
        var position = index
        while (position >= 0) {
            if (map?.keys!!.contains(position)) {
                val value = map!![position]
                return value!!
            }
            position--
        }
        return ""
    }
}
