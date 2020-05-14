package com.example.kotlinmvvm.view.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseRecyclerAdapter
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.databinding.ItemNewsBinding

class NewsPageAdapter():BaseRecyclerAdapter<NewsBean,NewsViewHolder>(comparableDiffer) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsViewHolder =
        NewsViewHolder.create(parent)

    override fun onBindViewHolder(holder: NewsViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
    }

    companion object{
        val comparableDiffer = object : DiffUtil.ItemCallback<NewsBean>(){
            override fun areItemsTheSame(oldItem: NewsBean, newItem: NewsBean): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: NewsBean, newItem: NewsBean): Boolean =
                oldItem.docid == newItem.docid

            override fun getChangePayload(oldItem: NewsBean, newItem: NewsBean): Any? {
                return super.getChangePayload(oldItem, newItem)
            }
        }
    }
}