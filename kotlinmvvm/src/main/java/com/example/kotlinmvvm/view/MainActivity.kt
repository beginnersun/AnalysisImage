package com.example.kotlinmvvm.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityMainBinding
import com.example.kotlinmvvm.view.news.NewsActivity
import com.example.kotlinmvvm.vm.MainViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = "/kotlinmvvm/main")
class MainActivity :BaseActivity() {



    private var binding:ActivityMainBinding? = null

    val myviewModel:MainViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        ARouter.getInstance().inject(this)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        binding?.tvContent!!.text = "测试DataBinding"
        binding?.viewModel = myviewModel

        binding?.tvContent!!.setOnClickListener {
            startActivity(Intent(this@MainActivity, NewsActivity::class.java))
        }

    }

    override fun setViewModel(): BaseViewModel = myviewModel


}
