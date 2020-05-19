package com.example.kotlinmvvm.view.stzb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.ServerBean
import com.example.kotlinmvvm.databinding.ActivityServerInfoBinding
import com.example.kotlinmvvm.widget.ContactDecoration
import com.example.kotlinmvvm.view.stzb.adapter.ServerAdapter
import com.example.kotlinmvvm.vm.StzbServerInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = "/kotlinmvvm/server")
class StzbServerActivity:BaseActivity() ,OnItemClickListener{
    override fun onItemClick(position: Int) {
        startActivity(Intent(this,StzbServerDetailActivity::class.java).putExtra("id","${serverBeans[position].server_id}"))
    }

    override fun setViewModel(): BaseViewModel = viewModel

    private val viewModel:StzbServerInfoViewModel by viewModel()
    private var binding:ActivityServerInfoBinding? = null
    private val serverBeans:MutableList<ServerBean> = mutableListOf()
    private val serverAdapter = ServerAdapter(this,serverBeans)
    private val map:MutableMap<Int,String> = mutableMapOf()
    private fun load(){
        viewModel.getServerList()
        Pair(1,"fd")
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_info)

        binding?.recyclerServer!!.run {
            layoutManager = LinearLayoutManager(this@StzbServerActivity)
            addItemDecoration(ContactDecoration(map))
            adapter = serverAdapter
        }

        load()

        viewModel.serverData.observe(this, Observer {
            serverBeans.clear()
            serverBeans.addAll(it)
            initServerInfo(serverBeans)
            serverAdapter.notifyDataSetChanged()
        })

        serverAdapter.onItemClick = this
    }

    private fun initServerInfo(list:List<ServerBean>){
        for ((index,item) in list.withIndex()){
            when{
                item.name.startsWith("S2") -> {
                    if (!map.values.contains("S2")){
                        map[index] = "S2"
                    }
                }
                item.name.startsWith("S3") -> {
                    if (!map.values.contains("S3")){
                        map[index] = "S3"
                    }
                }
                item.name.startsWith("X") -> {
                    if (!map.values.contains("X")){
                        map[index] = "X"
                    }
                }
                else -> {
                    if (!map.values.contains("S1")){
                        map[index] = "S1"
                    }
                }
            }
        }
    }

    override fun onStop() {
        Log.e("StzbServerInfoActivity","onStop")
        super.onStop()
    }

    override fun onDestroy() {
        Log.e("StzbServerInfoActivity","onDestroy")
        super.onDestroy()
    }

}