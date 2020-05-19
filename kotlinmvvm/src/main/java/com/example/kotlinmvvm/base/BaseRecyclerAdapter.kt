package com.example.kotlinmvvm.base

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.paging.PagedListAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.databinding.ItemNewsBinding
import java.util.*

/**
 * V 数据类型  H ViewHolder类型
 * @itemClick 默认为空,item点击方法
 */
open abstract class BaseRecyclerAdapter<V,H:BaseRecyclerAdapter.BaseViewHolder<V,out ViewDataBinding>>(itemCallback:DiffUtil.ItemCallback<V>,var itemClick:ItemClick? = null) :
    PagedListAdapter<V, H>(itemCallback) {

    override fun onBindViewHolder(holder: H, position: Int) {
        holder.bind(getItem(position))
    }

    open abstract class BaseViewHolder<V,B:ViewDataBinding> constructor(root: View,private val itemClick:ItemClick? = null) : RecyclerView.ViewHolder(root) {
        protected val binding: B = DataBindingUtil.bind(root)!!
        private var bean:V? = null
        init {
            root.setOnClickListener {
                itemClick?.onItemClick(bean)
            }
        }

        open fun bind(bean: V?){
            this.bean = bean
            binding.executePendingBindings()

        }
    }

    interface ItemClick{
        fun <V> onItemClick(v:V)
    }
}