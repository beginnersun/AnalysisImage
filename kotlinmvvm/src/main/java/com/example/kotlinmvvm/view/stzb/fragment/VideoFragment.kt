package com.example.kotlinmvvm.view.stzb.fragment

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.bean.VideoBean
import com.example.kotlinmvvm.databinding.FragmentStzbBinding
import com.example.kotlinmvvm.view.stzb.StzbDetailsActivity
import com.example.kotlinmvvm.view.stzb.adapter.VideoAdapter
import com.example.kotlinmvvm.vm.StzbViewModel
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class VideoFragment : BaseFragment(),VideoAdapter.OnAdapterItemClick,XRecyclerView.LoadingListener{
    override fun onLoadMore() {
        page++
        viewModel.getStzbVideo(page,page_size)
    }

    override fun onRefresh() {
        page = 1
        viewModel.getStzbVideo(page,page_size)
    }


    private var page = 1
    private val page_size = 10

    override fun onFirstVisible() {

    }

    private val datas:MutableList<VideoBean> = mutableListOf()

    private var videoAdapter:VideoAdapter? = null

    private val viewModel: StzbViewModel by viewModel(named("stzb"))

    private var binding: FragmentStzbBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_stzb, container, false)
        binding = DataBindingUtil.bind(view)

        videoAdapter = VideoAdapter(activity!!,datas)
        videoAdapter?.itemClick = this
        binding?.recyclerStzb!!.apply {
            layoutManager = GridLayoutManager(activity!!,2)
            setPullRefreshEnabled(true)
            setLoadingMoreEnabled(true)
            setLoadingListener(this@VideoFragment)
            adapter = videoAdapter
        }
        viewModel
            .apply {
                getStzbVideo(page, page_size)
                videoData.observe(this@VideoFragment, Observer<List<VideoBean>> {
                    Log.e("请求信息",it.toString())
                    binding?.recyclerStzb!!.reset()
                    if (page == 1){
                        datas.clear()
                    }
                    datas.addAll(it)
                    videoAdapter?.notifyDataSetChanged()
                })
            }
        return view
    }

    override fun onItemClick(position: Int) {
        startActivity(Intent(activity!!,StzbDetailsActivity::class.java))
    }
}