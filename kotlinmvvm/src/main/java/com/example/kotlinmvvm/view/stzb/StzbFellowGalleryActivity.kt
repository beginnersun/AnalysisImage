package com.example.kotlinmvvm.view.stzb

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityStzbNewAreaBinding
import com.example.kotlinmvvm.vm.StzbGalleryViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class StzbFellowGalleryActivity:BaseActivity() {
    override fun setViewModel(): BaseViewModel {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private var binding:ActivityStzbNewAreaBinding? = null
    private val viewModel:StzbGalleryViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_stzb_fellow_gallery)
    }

}