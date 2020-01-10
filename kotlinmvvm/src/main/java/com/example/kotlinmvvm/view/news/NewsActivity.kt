package com.example.kotlinmvvm.view.news

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NewsBean
import com.example.kotlinmvvm.vm.NewsViewModel
//import org.koin.androidx.viewmodel.ext.android.viewModel
import com.example.kotlinmvvm.databinding.ActivityNewsBinding
import com.example.kotlinmvvm.view.MainActivity
import com.example.kotlinmvvm.view.news.adapter.FragmentManagerdapter
import com.example.kotlinmvvm.view.news.fragment.NewsFragment
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named

@Route(path = "/kotlinmvvm/news")
class NewsActivity : BaseActivity(),XRecyclerView.LoadingListener{
    private var px = 0
    private var pz = 10
    private var tab = ""

    private val fragmentList by lazy {
        mutableListOf<Fragment>()
    }

    override fun onLoadMore() {

    }

    override fun onRefresh() {

    }

    private var binding: ActivityNewsBinding? = null
    val viewModel: NewsViewModel by viewModel(named("news"))
    private val new_tab = mapOf(
        "电视" to "BA10TA81wangning", "电影" to "BD2A9LEIwangning", "明星" to "BD2AB5L9wangning",
        "音乐" to "BD2AC4LMwangning", "体育" to "BA8E6OEOwangning", "财经" to "BA8EE5GMwangning",
        "军事" to "BAI67OGGwangning", "健康" to "BDC4QSV3wangning"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.e("thisActivity",this.toString())
        binding = DataBindingUtil.setContentView(this, R.layout.activity_news)
        ARouter.getInstance().inject(this)

        binding?.tablayout!!.run {
            for (item in new_tab){
                addTab(newTab().setText(item.key).setTag(item.value))
                fragmentList.add(NewsFragment.newInstance(item.value))
            }
            setupWithViewPager(binding?.viewPager)
        }

        binding?.viewPager!!.apply {
            adapter = FragmentManagerdapter(fragmentList,new_tab.keys.toList(),supportFragmentManager)
        }
//
        startActivity(Intent(this,NewsDetailsActivity::class.java))

//        binding?.recyclerNews!!.apply {
//            layoutManager = LinearLayoutManager(this@NewsActivity)
//            setLoadingMoreEnabled(true)
//            setPullRefreshEnabled(true)
//            setLoadingListener(this@NewsActivity)
//        }
//
//        viewModel.newsData.observe(this, Observer<List<NewsBean>> {
//            px += it.size
//
//        })
    }

    override fun setViewModel(): BaseViewModel {
        Log.e("测试1",(viewModel).toString())
        return viewModel
    }

}