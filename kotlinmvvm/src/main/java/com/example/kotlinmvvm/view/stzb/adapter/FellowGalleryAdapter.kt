package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomViewTarget
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.StzbFellowGalleryBean
import com.example.kotlinmvvm.databinding.ItemFellowGalleryBinding

class FellowGalleryAdapter(private val context: Context,private val datas:MutableList<StzbFellowGalleryBean> ):RecyclerView.Adapter<FellowGalleryAdapter.ViewHolder>() {
    init {
        for (item in datas){
            Glide.with(context).load(item.coverpath).into(object :SimpleTarget<Drawable>(){
                override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                    val scale = resource.intrinsicHeight.toFloat() / resource.intrinsicWidth.toFloat()
                    item.scale = scale
                }
            })
        }
    }

    var onItemClickListener:OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.createViewHolder(
        LayoutInflater.from(context),parent)

    override fun getItemCount(): Int =
        datas.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position,datas[position])
        if (onItemClickListener != null){
            holder.bindItemClick(onItemClickListener!!)
        }
    }

    class ViewHolder private constructor(private val binding:ItemFellowGalleryBinding):RecyclerView.ViewHolder(binding.root){

        companion object{
            fun createViewHolder(inflate:LayoutInflater,parent:ViewGroup) =
                ViewHolder(DataBindingUtil.bind(inflate.inflate(R.layout.item_fellow_gallery,parent,false))!!)
        }

        fun bindData(position:Int,bean:StzbFellowGalleryBean){
            binding.data = bean
            binding.executePendingBindings()
        }

        fun bindItemClick(onItemClick:OnItemClickListener){
            binding.root.setOnClickListener {
                onItemClick.onItemClick(layoutPosition)
            }
        }

    }
}