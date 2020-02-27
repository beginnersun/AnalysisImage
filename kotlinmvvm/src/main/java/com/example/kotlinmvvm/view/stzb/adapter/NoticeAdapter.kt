package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.NoticeBean
import com.example.kotlinmvvm.databinding.ItemStzbNoticeBinding

class NoticeAdapter(private var context: Context, private var datas: List<NoticeBean>) :
    RecyclerView.Adapter<NoticeAdapter.NoticeViewHolder>() {

    private var onItemCLick:OnItemClickListener? = null

    fun setOnItemClick(onItemClickListener: OnItemClickListener){
        this.onItemCLick = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder =
        NoticeViewHolder.createViewHolder(parent, LayoutInflater.from(context))

    override fun getItemCount(): Int =
        datas.size

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.bindView(position,datas[position])
        if (onItemCLick!=null) {
            holder.setItemClickListener(onItemCLick!!)
        }
    }

    class NoticeViewHolder(var binding: ItemStzbNoticeBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun createViewHolder(parent: ViewGroup, inflater: LayoutInflater) =
                NoticeViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_stzb_notice, parent, false))!!)
        }

        fun bindView(position:Int,bean: NoticeBean){
            if (bean!=null) {
                binding.item = bean
                Log.e("imageUrl", bean.imageHeadUrl)
                Log.e("View Station", bean.showImage.toString() + "   " + bean.showNew)
                binding.executePendingBindings()
            }
        }

        fun setItemClickListener(itemClickListener: OnItemClickListener){
            binding?.root.setOnClickListener {
//                Log.e("点击位置1","$position")
//                Log.e("点击位置2","$layoutPosition")
//                Log.e("点击位置3","$adapterPosition")
//                Log.e("点击位置4","$oldPosition")
                itemClickListener.onItemClick(layoutPosition)
            }
        }

    }

}