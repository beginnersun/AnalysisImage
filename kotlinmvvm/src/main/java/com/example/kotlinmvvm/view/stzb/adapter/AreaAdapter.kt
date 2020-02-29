package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.AreaInfo
import com.example.kotlinmvvm.databinding.ItemStzbAreaBinding

class AreaAdapter(private var context:Context,private var datas:MutableList<AreaInfo>):RecyclerView.Adapter<AreaAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.createViewHolder(LayoutInflater.from(context),parent)

    var itemClickListener:OnItemClickListener? = null

    override fun getItemCount(): Int =
        datas.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(datas[position])
        if (itemClickListener!=null){
            holder.setItemClickListener(itemClickListener!!)
        }
    }

    class ViewHolder(private val binding:ItemStzbAreaBinding):RecyclerView.ViewHolder(binding.root){

        companion object{
            fun createViewHolder(inflater:LayoutInflater,parent:ViewGroup) =
                ViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_stzb_area,parent,false))!!)
        }

        fun bindData(data:AreaInfo){
            binding.data = data
            binding.executePendingBindings()
        }

        fun setItemClickListener(itemClickListener: OnItemClickListener){
            binding?.root.setOnClickListener {
                itemClickListener.onItemClick(layoutPosition)
            }
        }
    }
}