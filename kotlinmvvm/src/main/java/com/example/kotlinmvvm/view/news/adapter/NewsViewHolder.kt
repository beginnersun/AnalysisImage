package com.example.kotlinmvvm.view.news.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.ViewDataBinding
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseRecyclerAdapter
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.databinding.ItemNewsBinding

class NewsViewHolder(root: View,private val itemClick:OnNewsBtnClick? = null) : BaseRecyclerAdapter.BaseViewHolder<NewsBean, ItemNewsBinding>(root,itemClick) {

    override fun bind(bean: NewsBean?) {
        binding.item = bean!!
        binding.itemClick = itemClick
        super.bind(bean)
    }

    companion object {
        fun create(parent: ViewGroup) = NewsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_news, parent)
        )
    }

    interface OnNewsBtnClick:BaseRecyclerAdapter.ItemClick{
        fun like(bean:NewsBean)
    }

}