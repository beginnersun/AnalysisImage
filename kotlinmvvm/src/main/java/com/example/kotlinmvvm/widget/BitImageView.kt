package com.example.kotlinmvvm.widget

import android.content.Context
import android.graphics.BitmapRegionDecoder
import android.util.AttributeSet
import android.view.View

class BitImageView : View {

    private var mDecoder:BitmapRegionDecoder? = null

    constructor(context: Context?) : super(context)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {

    }

    public fun setData(){
//        mDecoder = BitmapRegionDecoder.newInstance()
    }


}