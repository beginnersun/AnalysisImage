package com.example.kotlinmvvm.view.stzb

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.AreaInfo
import com.example.kotlinmvvm.bean.NoticeAreaBean
import com.example.kotlinmvvm.databinding.ActivityStzbNewAreaBinding
import com.example.kotlinmvvm.view.stzb.adapter.AreaAdapter
import com.example.kotlinmvvm.vm.StzbNewAreaViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StzbNewAreaActivity:BaseActivity(), OnItemClickListener {
    /**
     * 跳转到详情页
     */
    override fun onItemClick(position: Int) {

    }

    override fun setViewModel(): BaseViewModel = viewModel

    private val viewModel:StzbNewAreaViewModel by viewModel()
    private var binding:ActivityStzbNewAreaBinding? = null
    private val areaBeans:MutableList<AreaInfo> = mutableListOf<AreaInfo>()
    private val areaAdapter:AreaAdapter = AreaAdapter(this,areaBeans)

    private fun getStznNewAreaList(){
        viewModel.getStzbNewAreaList()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stzb_new_area)

        binding?.recyclerArea!!.run {
            layoutManager = LinearLayoutManager(this@StzbNewAreaActivity)
            adapter = areaAdapter
        }

        viewModel.areaData.observe(this,  Observer{
            areaBeans.clear()
            for (item in it){
                areaBeans.add(item.areaInfo)
            }
            areaAdapter.notifyDataSetChanged()
        })

        areaAdapter.itemClickListener = this

        getStznNewAreaList()
    }
}