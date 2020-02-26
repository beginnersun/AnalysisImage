package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.NoticeDetailsBean
import com.example.kotlinmvvm.databinding.ItemStzbCommentBinding

class CommentAdapter(private var context: Context,private var datas: MutableList<NoticeDetailsBean>) :
    RecyclerView.Adapter<CommentAdapter.CommentViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommentViewHolder =
        CommentViewHolder.createViewHolder(LayoutInflater.from(context), parent)


    override fun getItemCount(): Int =
        datas.size


    override fun onBindViewHolder(holder: CommentViewHolder, position: Int) {
        holder.bindData(datas[position],position)
    }


    class CommentViewHolder private constructor(private var binding: ItemStzbCommentBinding) :
        RecyclerView.ViewHolder(binding.root) {
        companion object {
            fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                CommentViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_stzb_comment, parent, false))!!)
        }

        fun bindData(bean:NoticeDetailsBean,position: Int){
            binding.item = bean
            var level = when(position) {
                0 -> "#沙发"
                1 -> "#板凳"
                2 -> "#地板"
                else -> "#$position"
            }
            binding.level = level
            binding.executePendingBindings()
        }
    }
}