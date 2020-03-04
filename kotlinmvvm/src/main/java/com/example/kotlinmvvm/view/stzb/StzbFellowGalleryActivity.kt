package com.example.kotlinmvvm.view.stzb

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_module.widget.GalleryItemDecoration
import com.example.kotlinmvvm.OnItemClickListener
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.StzbFellowGalleryBean
import com.example.kotlinmvvm.databinding.ActivityStzbFellowGalleryBinding
import com.example.kotlinmvvm.databinding.ActivityStzbNewAreaBinding
import com.example.kotlinmvvm.view.stzb.adapter.FellowGalleryAdapter
import com.example.kotlinmvvm.vm.StzbGalleryViewModel
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = "/kotlinmvvm/gallery")
class StzbFellowGalleryActivity : BaseActivity(), XRecyclerView.LoadingListener ,OnItemClickListener{
    override fun onItemClick(position: Int) {
        var intent = Intent(this,StzbDetailsActivity::class.java)
        intent.putExtra("tid",fellowGalleryList[position-1].tid)
        startActivity(intent)
    }

    override fun onLoadMore() {
        page++
        load()
    }

    override fun onRefresh() {
        page = 1
        load()
    }

    override fun setViewModel(): BaseViewModel = viewModel

    private var binding: ActivityStzbFellowGalleryBinding? = null
    private val viewModel: StzbGalleryViewModel by viewModel()
    private val fellowGalleryList: MutableList<StzbFellowGalleryBean> = mutableListOf()
    private val galleryAdapter: FellowGalleryAdapter = FellowGalleryAdapter(this, fellowGalleryList)

    private var page: Int = 1

    private fun load() {
        viewModel.getFellowGallery(page)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stzb_fellow_gallery)

        galleryAdapter.onItemClickListener = this

        load()
        viewModel.fellowGalleryData.observe(this, Observer {
            if (page == 1) {
                fellowGalleryList.clear()
            }
            val start = fellowGalleryList.size
            binding?.recyclerGallery!!.reset()
            fellowGalleryList.addAll(it)
            galleryAdapter.notifyItemRangeChanged(start, fellowGalleryList.size)
        })

        binding?.recyclerGallery!!.top
        binding?.recyclerGallery!!.height

        binding?.recyclerGallery!!.run {
            setPullRefreshEnabled(true)
            setLoadingMoreEnabled(true)
            setLoadingListener(this@StzbFellowGalleryActivity)
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                .apply {
                    gapStrategy = StaggeredGridLayoutManager.GAP_HANDLING_NONE
                }
            (itemAnimator as DefaultItemAnimator).supportsChangeAnimations = false
            (itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            setHasFixedSize(true)
            itemAnimator?.changeDuration = 0
            addItemDecoration(GalleryItemDecoration(50, 50))
            adapter = galleryAdapter
        }
    }

}