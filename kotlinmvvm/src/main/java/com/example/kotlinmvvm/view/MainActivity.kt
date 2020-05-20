package com.example.kotlinmvvm.view

import android.content.Intent
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.*
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import com.example.base_module.rxbinding.RxView
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityMainKotlinBinding
import com.example.kotlinmvvm.view.news.NewsActivity
import com.example.kotlinmvvm.vm.MainViewModel
import com.example.kotlinmvvm.vm.StzbViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.qualifier.named
import kotlinx.coroutines.*

@Route(path = "/kotlinmvvm/main")
class MainActivity :BaseActivity() {



    private var binding:ActivityMainKotlinBinding? = null

    val myviewModel:MainViewModel by viewModel((named("main")))

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_kotlin)
        viewModelStore


        ARouter.getInstance().inject(this)

        binding?.tvContent!!.text = "测试DataBinding"
        binding?.viewModel = myviewModel

        RxView.bindClick(binding!!.tvInfo).subscribe{
            myviewModel.test()
        }




        Thread{
            this.binding.toString()
            print("")
        }.start()
//        launch{
//
//        }

//        binding?.tvContent!!.setOnClickListener {
//                startActivity(Intent(this@MainActivity, NewsActivity::class.java))
//        }

    }

    override fun setViewModel(): BaseViewModel = myviewModel


}
