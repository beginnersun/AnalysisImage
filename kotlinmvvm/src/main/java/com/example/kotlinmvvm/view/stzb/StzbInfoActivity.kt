package com.example.kotlinmvvm.view.stzb

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityStzbBinding
import com.example.kotlinmvvm.view.news.adapter.FragmentManagerdapter
import com.example.kotlinmvvm.view.news.fragment.NewsFragment
import com.example.kotlinmvvm.view.stzb.fragment.VideoFragment
import com.example.kotlinmvvm.vm.StzbViewModel
import com.google.android.material.tabs.TabLayout
import org.koin.core.qualifier.named
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = "/kotlinmvvm/stzb")
class StzbInfoActivity:BaseActivity() {

    private val viewModel:StzbViewModel by viewModel(named("stzb"))

    override fun setViewModel(): BaseViewModel = viewModel

    private var binding:ActivityStzbBinding? = null

    private val fragmentList = mutableListOf<BaseFragment>()

    private val titleList = listOf("视频"
//        ,"公告"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_stzb)

        initFragment()

        binding?.viewpager!!.apply {
            adapter = FragmentManagerdapter(fragmentList,titleList,supportFragmentManager)
        }
        binding?.tablayout!!.run {
            for (item in titleList){
                addTab(newTab().setText(item))
            }
            setupWithViewPager(binding?.viewpager)
        }

    }

    private fun initFragment(){
        fragmentList.add(VideoFragment())
//        fragmentList.add(VideoFragment())
    }

}