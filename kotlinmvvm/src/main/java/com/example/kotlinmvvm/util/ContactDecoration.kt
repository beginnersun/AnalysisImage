package com.example.kotlinmvvm.util

import androidx.recyclerview.widget.RecyclerView.ItemDecoration

class ContactDecoration constructor() : ItemDecoration() {

    private var map:Map<Int,String>? = null
    fun setMap(map:Map<Int,String>){
        this.map = map
    }

    constructor(map: Map<Int, String>):this() {
        this.map = map
    }


}
