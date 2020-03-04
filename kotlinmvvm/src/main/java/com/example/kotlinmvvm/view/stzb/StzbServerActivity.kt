package com.example.kotlinmvvm.view.stzb

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityServerInfoBinding
import com.example.kotlinmvvm.util.ContactDecoration
import com.example.kotlinmvvm.vm.StzbServerInfoViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StzbServerActivity:BaseActivity() {
    override fun setViewModel(): BaseViewModel = viewModel

    private val viewModel:StzbServerInfoViewModel by viewModel()
    private var binding:ActivityServerInfoBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_info)


    }
}