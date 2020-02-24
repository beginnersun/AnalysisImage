package com.example.kotlinmvvm.view.stzb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.bean.NoticeBean
import com.example.kotlinmvvm.databinding.FragmentStzbNoticeBinding
import com.example.kotlinmvvm.view.stzb.adapter.NoticeAdapter
import com.example.kotlinmvvm.vm.StzbViewModel
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class NoticeFragment:BaseFragment(),XRecyclerView.LoadingListener {
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
    private var adapter:NoticeAdapter = NoticeAdapter(context!!,data)

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_stzb_notice,container,false)
        binding = DataBindingUtil.bind(view)

        binding?.recyclerNotice!!.run {
            setLoadingMoreEnabled(true)
            setPullRefreshEnabled(true)
            setLoadingListener(this@NoticeFragment)
            adapter = adapter
        }

        return view
    }

    private fun getNoticeList(){
        viewModel.getStzbNotice(page)
    }

    override fun onFirstVisible() {
        viewModel.run {
            getStzbNotice(page)
            videoNotice.observe(this@NoticeFragment, Observer {
                binding?.recyclerNotice!!.reset()
                if (page == 1) {
                    data.clear()
                }
                data.addAll(it)
                adapter.notifyDataSetChanged()
            })
        }
    }


}