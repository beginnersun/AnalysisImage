package com.example.kotlinmvvm.view.stzb

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_module.util.TimeUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.databinding.ActivityServerDetailBinding
import com.example.kotlinmvvm.vm.StzbServerDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

@Route(path = "/kotlinmvvm/server_detail")
class StzbServerDetailActivity : BaseActivity(), View.OnTouchListener {

    override fun setViewModel(): BaseViewModel = viewModel

    private val viewModel:StzbServerDetailsViewModel by viewModel()

    private var binding: ActivityServerDetailBinding? = null
    private var currentPosition = 0
    private var targetPosition = 0
    private var touchPosition = 0
    private var oldTouchX = 0f
    private var oldTouchY = 0f
    private var currentX = 0f
    private var currentY = 0f
    private val clViews: MutableList<View> = mutableListOf()
    private var satisfied = false
    private val animatorSet = AnimatorSet()

    private var server_id = ""

    private fun load(){
        viewModel.getServerDetails(server_id, TimeUtil.getCurrentDate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_detail)
        server_id = intent.getStringExtra("id")

        clViews.add(binding?.unionRank!!)
        clViews.add(binding?.unionInfo!!)

        initTouchListener()
        viewModel.serverData.observe(this, Observer {
            binding?.first = it[0]
            binding?.second = it[1]
            binding?.third = it[2]
        })
        load()

    }

    private fun initTouchListener() {
        for ((index,view) in clViews.withIndex()) {
            view.setOnTouchListener(this)
            if (index != 0){
                Log.e("状态","${view.translationY}     ${view.bottom}")
                view.post{
                    view.translationY = view.bottom.toFloat()
                    view.invalidate()
                }
            }
        }
    }





    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        when (event!!.action) {
            MotionEvent.ACTION_DOWN -> {
                touchPosition = clViews.indexOf(v)
                oldTouchX = event.x
                oldTouchY = event.y
            }
            MotionEvent.ACTION_MOVE -> {
                currentX = event.x
                currentY = event.y
            }
            MotionEvent.ACTION_CANCEL -> satisfied = false
            MotionEvent.ACTION_UP -> {
                val dValue = currentY - oldTouchY
                if (dValue > 0 && dValue >= 200) {
                    upInfo()
                }else if (dValue <0  && dValue <=-200){
                    downInfo()
                }
            }
        }
        return true
    }

    /**
     * 播放下一个页面
     */
    private fun upInfo() {
        targetPosition = (currentPosition + 1) % clViews.size
        var currentAnimator = ObjectAnimator.ofFloat(
            clViews[currentPosition],
            "translationY",
            0f,
            -clViews[currentPosition].bottom.toFloat()
        )
        var targetAnimator = ObjectAnimator.ofFloat(
            clViews[targetPosition],
            "translationY",
            clViews[targetPosition].bottom.toFloat(),
            clViews[targetPosition].top.toFloat()
        )
        val animatorSet = AnimatorSet()
        animatorSet.play(currentAnimator).with(targetAnimator)
        animatorSet.duration = 2000
        animatorSet.start()
    }

    /**
     * 返回上一个页面
     */
    private fun downInfo() {
        targetPosition = (currentPosition - 1) % clViews.size
        //当前页面的Y值从0变为view的长度 （下拉到完全看不见）
        var currentAnimator = ObjectAnimator.ofFloat(
            clViews[currentPosition],
            "translationY",
            0f,
            clViews[currentPosition].bottom.toFloat()
        )
        //上一个页面的Y变为0  也就是完全展示出来
        var targetAnimator = ObjectAnimator.ofFloat(
            clViews[targetPosition],
            "translationY",
            clViews[currentPosition].translationY,
            0f
        )
        val animatorSet = AnimatorSet()
        animatorSet.play(currentAnimator).with(targetAnimator)
        animatorSet.duration = 2000
        animatorSet.start()
    }

    override fun onStop() {
        super.onStop()
        animatorSet.reverse()
        animatorSet.cancel()
    }
}