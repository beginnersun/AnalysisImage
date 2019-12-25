package com.example.kotlinmvvm.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityMainBinding
import com.example.kotlinmvvm.view.news.NewsActivity
import com.example.kotlinmvvm.vm.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = "/mvvm/main")
class MainActivity :BaseActivity() {



    private var binding:ActivityMainBinding? = null

    val myviewModel:MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding?.tvContent!!.text = "测试DataBinding"
        binding?.viewModel = viewModel

        binding?.tvContent!!.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewsActivity::class.java))
        }

    }

    override fun setViewModel(): BaseViewModel = myviewModel


}
