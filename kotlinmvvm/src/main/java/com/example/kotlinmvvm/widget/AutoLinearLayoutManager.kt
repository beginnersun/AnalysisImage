package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.util.DisplayMetrics
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView

class AutoLinearLayoutManager : LinearLayoutManager {

    constructor(context: Context) : super(context)

    constructor(context: Context, orientation: Int, reverseLayout: Boolean) : super(
        context,
        orientation,
        reverseLayout
    )

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(
        context,
        attrs,
        defStyleAttr,
        defStyleRes
    )

    override fun smoothScrollToPosition(recyclerView: RecyclerView?, state: RecyclerView.State?, position: Int) {
        val linerScroller = SlowLinearScroller(recyclerView!!.context)
        linerScroller.targetPosition = position
        startSmoothScroll(linerScroller)
    }

    inner class SlowLinearScroller(context: Context):LinearSmoothScroller(context){

        override fun computeScrollVectorForPosition(targetPosition: Int): PointF? {
            return this@AutoLinearLayoutManager.computeScrollVectorForPosition(targetPosition)
        }

        override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics?): Float {
            return 15f / displayMetrics!!.density
        }
    }

}