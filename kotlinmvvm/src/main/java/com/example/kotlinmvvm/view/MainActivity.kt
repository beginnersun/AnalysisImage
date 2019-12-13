package com.example.kotlinmvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.databinding.ActivityMainBinding
import com.example.kotlinmvvm.vm.MainViewModel

class MainActivity :AppCompatActivity() {

    private var binding:ActivityMainBinding? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)
        val viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)

        binding?.tvContent!!.text = "测试DataBinding"
        binding?.viewModel = viewModel

    }

}