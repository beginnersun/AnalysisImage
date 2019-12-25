package com.example.kotlinmvvm.view.news.fragment


import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.databinding.FragmentListBinding
import com.example.kotlinmvvm.vm.NewsViewModel
import com.jcodecraeer.xrecyclerview.XRecyclerView
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsFragment() : Fragment(),XRecyclerView.LoadingListener {

    override fun onLoadMore() {
        viewModel.getNewsList(type,px,pz)
    }

    override fun onRefresh() {
        px = 0
    }

    private var type:String = ""
    private val viewModel:NewsViewModel by viewModel()
    private var binding:FragmentListBinding? =null

    private var px = 0
    private var pz = 10

    override fun onAttach(context: Context) {
        super.onAttach(context)
        type = arguments!!.getString("tag")
    }

    companion object {
        fun newInstance(tag: String): NewsFragment {
            return NewsFragment().apply {
                arguments = Bundle().apply {putString("tag",tag)}
            }
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_list,container,false)
        binding = DataBindingUtil.bind(view)

        viewModel.getNewsList(type,px,pz)

        binding?.recyclerNews!!.apply {
            layoutManager = LinearLayoutManager(activity)
            setLoadingListener(this@NewsFragment)
            setLoadingMoreEnabled(true)
            setPullRefreshEnabled(true)
        }

        viewModel.getDatas(type).observe(this, Observer {
            px += it.size
        })

        return view
    }

}
