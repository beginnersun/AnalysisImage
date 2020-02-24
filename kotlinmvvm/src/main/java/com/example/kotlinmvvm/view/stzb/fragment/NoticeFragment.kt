package com.example.kotlinmvvm.view.stzb.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.databinding.FragmentStzbNoticeBinding
import com.example.kotlinmvvm.vm.StzbViewModel
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class NoticeFragment:BaseFragment(),XRecyclerView.LoadingListener {
    override fun onRefresh() {

    }

    override fun onLoadMore() {

    }

    var binding:FragmentStzbNoticeBinding? = null
    private val viewModel: StzbViewModel by viewModel(named("stzb"))
    private var page:Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view = inflater.inflate(R.layout.fragment_stzb_notice,container,false)
        binding = DataBindingUtil.bind(view)

        binding?.recyclerNotice!!.run {
            setLoadingMoreEnabled(true)
            setPullRefreshEnabled(true)
            setLoadingListener(this@NoticeFragment)
        }

        return view
    }

    private fun getNoticeList(){
    }

    override fun onFirstVisible() {
        viewModel.run {
            getStzbNotice(page)
            videoNotice.observe(this@NoticeFragment, Observer {

            })
        }
    }


}