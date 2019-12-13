package com.example.kotlinmvvm.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
//import com.example.base_module.util.BitmapUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.databinding.ActivityMainBinding

class MainActivity :AppCompatActivity() {

    private var binding:ActivityMainBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this,R.layout.activity_main)

        binding?.tvContent!!.text = "测试DataBinding"

    }

}