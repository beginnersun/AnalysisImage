package com.example.kotlinmvvm.view.stzb.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.Observable
import androidx.lifecycle.Observer
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.bean.VideoBean
import com.example.kotlinmvvm.databinding.FragmentStzbBinding
import com.example.kotlinmvvm.vm.StzbViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

class VideoFragment : BaseFragment() {

    private var page = 1
    private val page_size = 10

    override fun onFirstVisible() {

    }


    private val viewModel: StzbViewModel by viewModel(named("stzb"))

    private var binding: FragmentStzbBinding? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater.inflate(R.layout.fragment_stzb, container, false)

        binding = DataBindingUtil.bind(view)
        viewModel
            .apply {
                getStzbVideo(page, page_size)
                videoData.observe(this@VideoFragment, Observer<List<VideoBean>> {
                    Log.e("信息",it.toString())
                    binding?.recyclerStzb!!.reset()
                    if (page == 1){
                    }

                })
            }

        return view
    }
}