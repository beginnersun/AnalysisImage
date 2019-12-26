package com.example.kotlinmvvm.view.news.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.databinding.ItemNewsBinding

class NewsAdapter(val context: Context, val datas: List<out Any>) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.create(parent, LayoutInflater.from(context))

    override fun getItemCount(): Int =
        datas?.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(datas[position] as NewsBean)
    }

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun create(parent: ViewGroup,inflater: LayoutInflater):ViewHolder =
                ViewHolder(ItemNewsBinding.inflate(inflater,parent,false))
        }

        fun bindData(bean:NewsBean){
            binding.item = bean
            binding.executePendingBindings()
        }
    }

}