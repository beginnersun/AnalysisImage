package com.example.base_module.widget

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager

class GalleryItemDecoration(val verticalDistance:Int,val horizontalDistance:Int):RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        super.getItemOffsets(outRect, view, parent, state)
        val position = (view.layoutParams as StaggeredGridLayoutManager.LayoutParams).spanIndex
        if (position%2 == 1){
            outRect.left = horizontalDistance/2
        }else{
            outRect.right = horizontalDistance/2
        }
        outRect.bottom = verticalDistance
    }


}