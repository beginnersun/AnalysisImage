package com.example.kotlinmvvm.view.news.adapter

import android.content.Context
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.Imgextra
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.databinding.ItemNewsBinding

class NewsAdapter(private val context: Context) : RecyclerView.Adapter<NewsAdapter.ViewHolder>() {

    private val datas = mutableListOf<NewsBean>()
    private var count = 0;

    constructor(context: Context,datas: List<NewsBean>):this(context){
        this.datas.clear()
        this.datas.addAll(datas)
        notifyDataSetChanged()
    }

    fun removeAndAddList(news:List<NewsBean>){
        this.datas.clear()
        addAllList(news)
    }

    fun addAllList(news:List<NewsBean>){
        this.datas.addAll(news)
        notifyDataSetChanged()
    }

    fun removeAndAdd(vararg newsBean:NewsBean){
        this.datas.clear()
        this.datas.addAll(newsBean)
        notifyDataSetChanged()
    }

    fun addAll(vararg newsBean:NewsBean){
        this.datas.addAll(newsBean)
        notifyDataSetChanged()
    }

    private var onItemCLickListener:OnItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder =
        ViewHolder.create(parent, LayoutInflater.from(context)).apply {
            Log.e("创建${count++}次",this.toString())
        }

    override fun getItemCount(): Int =
        datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(datas[position])
    }

    class ViewHolder(val binding: ItemNewsBinding) : RecyclerView.ViewHolder(binding.root) {

        companion object{
            fun create(parent: ViewGroup,inflater: LayoutInflater):ViewHolder =
                ViewHolder(ItemNewsBinding.inflate(inflater,parent,false))
        }

        fun bindData(bean:NewsBean){
            binding.item = bean
            View.VISIBLE
            if (bean.imgextra!= null && TextUtils.equals(bean.imgsrc3gtype,"2")) {
                binding.images = bean.imgextra
                Log.e("图集",bean.imgextra.toString())
            }
            binding.executePendingBindings()
        }
    }

    interface OnItemClickListener{
        fun onNoInterestClick(id:Int)

        fun onShieldClick(id:Int)
    }

}