package com.example.kotlinmvvm.view.news.fragment

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
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseFragment
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.FragmentListBinding
import com.example.kotlinmvvm.databinding.PageFragmentListBinding
import com.example.kotlinmvvm.view.news.adapter.NewsAdapter
import com.example.kotlinmvvm.vm.NewsPageViewModel
import com.example.kotlinmvvm.vm.NewsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import org.koin.androidx.viewmodel.ext.android.viewModel

class NewsPageFragment: BaseActivity() {
    override fun setViewModel() = viewModel

    private val viewModel:NewsPageViewModel by viewModel()

    private var type:String = ""
    private var binding: PageFragmentListBinding? =null

    private var newsAdapter: NewsAdapter? = null


}