package com.example.kotlinmvvm.view.stzb.adapter

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.databinding.ItemServerCityInfoBinding

class CityAdapter(private val context: Context, private val datas: MutableList<ServerCityBean>) :
    RecyclerView.Adapter<CityAdapter.ViewHolder>() {

    var oldPosition: Int = -1
    private var choosePosition: Int = 0
    private val map: MutableMap<Int, ViewHolder> = mutableMapOf()

    fun setChoosePosition(choosePosition: Int) {
        this.choosePosition = choosePosition
        if (oldPosition != choosePosition) {
            map[choosePosition]?.onSelected()
            map[oldPosition]?.unSeleted()
            oldPosition = choosePosition
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder = ViewHolder.createViewHolder(
        LayoutInflater.from(context), parent
    )

    override fun getItemCount(): Int =
        datas.size + 12
//        Integer.MAX_VALUE


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        if (datas.size != 0 && position < datas.size) {
            map[position] = holder
            holder.bindData(position, datas[position % datas.size])
        }else{
            holder.bindData(position,ServerCityBean("",0,"",0,"","",0, mutableListOf()))
        }
    }


    class ViewHolder private constructor(private val binding: ItemServerCityInfoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        companion object {
            fun createViewHolder(inflater: LayoutInflater, parent: ViewGroup) =
                ViewHolder(DataBindingUtil.bind(inflater.inflate(R.layout.item_server_city_info, parent, false))!!)

        }

        fun bindData(position: Int, bean: ServerCityBean) {
            binding?.item = bean
            binding?.executePendingBindings()
        }

        fun onSelected() {
            binding?.tvArea.setTextColor(Color.parseColor("#EAB53C"))
            binding?.tvCityName.setTextColor(Color.parseColor("#EAB53C"))
            binding?.tvLevel.setTextColor(Color.parseColor("#EAB53C"))
            binding?.tvTime.setTextColor(Color.parseColor("#EAB53C"))
            binding?.tvUnionName.setTextColor(Color.parseColor("#EAB53C"))
        }

        fun unSeleted() {
            binding?.tvArea.setTextColor(Color.parseColor("#ffffff"))
            binding?.tvCityName.setTextColor(Color.parseColor("#ffffff"))
            binding?.tvLevel.setTextColor(Color.parseColor("#ffffff"))
            binding?.tvTime.setTextColor(Color.parseColor("#ffffff"))
            binding?.tvUnionName.setTextColor(Color.parseColor("#ffffff"))

        }


    }

}