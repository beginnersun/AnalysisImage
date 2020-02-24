package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.NoticeBean
import com.example.kotlinmvvm.databinding.ItemStzbNoticeBinding

class NoticeAdapter(private var context: Context, private var datas: List<NoticeBean>) :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder =
        NoticeViewHolder.createViewHolder(parent, LayoutInflater.from(context))

    override fun getItemCount(): Int =
        datas.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bindView(position,datas[position])
    }


    class NoticeViewHolder(var binding: ItemStzbNoticeBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater) =
                NoticeViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_stzb_notice, parent, false))!!)

        }

        fun bindView(position:Int,bean: NoticeBean){
            binding.item = bean
            binding.executePendingBindings()
        }

    }

}