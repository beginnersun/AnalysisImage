package com.example.kotlinmvvm.view.stzb.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.bean.NoticeBean
import com.example.kotlinmvvm.databinding.FragmentStzbNoticeBinding
import com.example.kotlinmvvm.view.stzb.StzbDetailsActivity
import com.example.kotlinmvvm.view.stzb.adapter.NoticeAdapter
import com.example.kotlinmvvm.vm.StzbViewModel
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class NoticeFragment:BaseFragment(),XRecyclerView.LoadingListener ,OnItemClickListener{
    override fun onItemClick(position: Int) {
        var intent = Intent(activity,StzbDetailsActivity::class.java)
        intent.putExtra("tid",data[position].tid)
        startActivity(intent)
    }

    override fun onRefresh() {
        page = 1
        getNoticeList()
    }

    override fun onLoadMore() {
        page++
        getNoticeList()
    }

    var binding:FragmentStzbNoticeBinding? = null
    private val viewModel: StzbViewModel by viewModel(named("stzb"))
    private var page:Int = 1
    private val data = mutableListOf<NoticeBean>()
    private var noticeAdapter:NoticeAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_stzb_notice,container,false)
        binding = DataBindingUtil.bind(view)

        noticeAdapter = NoticeAdapter(context!!,data)
        binding?.recyclerNotice!!.run {
            setLoadingMoreEnabled(true)
            setPullRefreshEnabled(true)
            setLoadingListener(this@NoticeFragment)
            layoutManager = LinearLayoutManager(context)
            adapter = noticeAdapter
        }
        noticeAdapter?.setOnItemClick(this)
        return view
    }

    private fun getNoticeList(){
        viewModel.getStzbNotice(page)
    }

    override fun onFirstVisible() {
        viewModel.run {
            getStzbNotice(page)
            videoNotice.observe(this@NoticeFragment, Observer {
                Log.e("收到数据",it.toString())
                binding?.recyclerNotice!!.reset()
                if (page == 1) {
                    data.clear()
                }
                data.addAll(it)
                Log.e("收到数据","${data.size}")
                noticeAdapter?.notifyDataSetChanged()
            })
        }
    }


}