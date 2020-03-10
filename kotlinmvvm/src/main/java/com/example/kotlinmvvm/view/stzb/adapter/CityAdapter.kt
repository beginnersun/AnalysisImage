package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.databinding.ItemServerCityInfoBinding

class CityAdapter(private val context: Context,private val datas:MutableList<ServerCityBean>):RecyclerView.Adapter<CityAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.createViewHolder(
        LayoutInflater.from(context),parent)

    override fun getItemCount(): Int =
        datas.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(position,datas[position])
    }


    class ViewHolder private constructor(private val binding:ItemServerCityInfoBinding):RecyclerView.ViewHolder(binding.root){

        companion object{
            fun createViewHolder(inflater: LayoutInflater,parent: ViewGroup) =
                ViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_server_city_info,parent,false))!!)

        }

        fun bindData(position:Int,bean:ServerCityBean){
            binding?.item = bean
            binding?.executePendingBindings()
        }

    }

}