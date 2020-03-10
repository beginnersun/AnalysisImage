package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.ServerBean
import com.example.kotlinmvvm.databinding.ItemServerInfoBinding

class ServerAdapter(private val context: Context,private val datas:MutableList<ServerBean>):RecyclerView.Adapter<ServerAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.createViewHolder(LayoutInflater.from(context),parent)

    var onItemClick:OnItemClickListener? = null

    override fun getItemCount(): Int =
        datas.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position,datas[position])
        if (onItemClick!=null){
            holder.bindItemClick(onItemClick!!)
        }
    }

    class ViewHolder(private val binding:ItemServerInfoBinding):RecyclerView.ViewHolder(binding.root){

        companion object{
            fun createViewHolder(inflater: LayoutInflater,parent:ViewGroup) =
                ViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_server_info,parent,false))!!)
        }

        fun bindData(position: Int,data:ServerBean){
            binding.data = data
            binding.executePendingBindings()
        }

        fun bindItemClick(onClick:OnItemClickListener){
            binding.root.setOnClickListener {
                onClick.onItemClick(layoutPosition)
            }
        }
    }
}