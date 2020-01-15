package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.bean.VideoBean
import com.example.kotlinmvvm.databinding.ItemStzbvideoBinding

class VideoAdapter(private val context:Context): RecyclerView.Adapter<VideoAdapter.ViewHolder>(){

    private val datas:List<VideoBean> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.create(parent, LayoutInflater.from(context))
    }

    override fun getItemCount() =
        datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(datas[position])
    }


    class ViewHolder(private val binding:ItemStzbvideoBinding):RecyclerView.ViewHolder(binding.root){

        companion object{
            fun create(parent: ViewGroup,inflater: LayoutInflater) =
                ViewHolder(ItemStzbvideoBinding.inflate(inflater,parent,false))
        }

        fun bindData(data:VideoBean){
            binding.data = data
            binding.executePendingBindings()
        }

    }

}