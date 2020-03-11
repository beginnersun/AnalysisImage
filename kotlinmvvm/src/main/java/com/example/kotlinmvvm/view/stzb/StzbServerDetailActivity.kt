package com.example.kotlinmvvm.view.stzb

import android.animation.Animator
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Bundle
import android.util.Log
import android.util.TimeUtils
import android.view.MotionEvent
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.alibaba.android.arouter.facade.annotation.Route
import com.example.base_module.util.TimeUtil
import com.example.kotlinmvvm.R
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.ServerCityBean
import com.example.kotlinmvvm.databinding.ActivityServerDetailBinding
import com.example.kotlinmvvm.view.stzb.adapter.CityAdapter
import com.example.kotlinmvvm.vm.StzbServerDetailsViewModel
import com.example.kotlinmvvm.widget.AutoLinearLayoutManager
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

    private var serverId = ""
    private val cityBeans:MutableList<ServerCityBean> = mutableListOf()
    private val cityAdapter = CityAdapter(this,cityBeans)
    private var back = false
    private var allCount = 0

    private fun load(){
        viewModel.getServerDetails(serverId, TimeUtil.getCurrentDate())
//        viewModel.getCityInfo(serverId,TimeUtil.getCurrentDate())
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_server_detail)
        serverId = intent.getStringExtra("id")

        clViews.add(binding?.unionRank!!)
        clViews.add(binding?.unionInfo!!)

        initTouchListener()

        val verticalManager = AutoLinearLayoutManager(this)
        binding?.recyclerCity!!.run {
            layoutManager = AutoLinearLayoutManager(this@StzbServerDetailActivity)
            addOnScrollListener(object :RecyclerView.OnScrollListener(){
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    val position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    if (back && position == 0){
                        cityAdapter.setChoosePosition(position)
                        back = false
                        recyclerView.smoothScrollToPosition(cityAdapter.itemCount)
                    }
                }

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val position = (layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
                    Log.e("滚动中","${position+1}  ${cityBeans.size}")
                    if (position + 1 == cityBeans.size){
                        recyclerView.scrollToPosition(0)
                        back = true
                    }else {
                        if (!back) {
                            cityAdapter.setChoosePosition(position + 1)
                        }
                    }
                }
            })
            adapter = cityAdapter
        }

        binding?.ivMap!!.setOnClickListener {
            binding?.recyclerCity!!.smoothScrollToPosition(cityAdapter.itemCount)
        }


        viewModel.serverData.observe(this, Observer {
            binding?.first = it[0]
            binding?.second = it[1]
            binding?.third = it[2]
        })
        viewModel.cityData.observe(this, Observer {
            cityBeans.clear()
            allCount = it.size
            cityBeans.addAll(it)
            Log.e("数据大小1","${cityBeans.size}")
            cityAdapter.notifyDataSetChanged()
            Log.e("数据大小2","${cityAdapter.itemCount}")
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
                    if (currentPosition != 0) {
                        prePage()
                    }
                }else if (dValue <0  && dValue <=-200){
                    if (currentPosition != clViews.size -1 ){
                        nextPage()
                    }
                }
            }
        }
        return true
    }

    private fun infoChanged(){
        currentPosition = targetPosition
        when(currentPosition){
            0 -> {}
            1 -> {
            }
            2 -> {}
            3 -> {}
            else -> {}
        }
    }

    /**
     * 播放下一个页面
     */
    private fun nextPage() {
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
        animatorSet.duration = 1000
        animatorSet.addListener(animatorSetListener)
        animatorSet.start()
    }

    private val animatorSetListener = object :Animator.AnimatorListener{
        override fun onAnimationRepeat(animation: Animator?) {
        }

        override fun onAnimationEnd(animation: Animator?) {
            infoChanged()
        }

        override fun onAnimationCancel(animation: Animator?) {
        }

        override fun onAnimationStart(animation: Animator?) {
        }

    }

    /**
     * 返回上一个页面
     */
    private fun prePage() {
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
            -clViews[currentPosition].bottom.toFloat(),
            0f
        )
        val animatorSet = AnimatorSet()
        animatorSet.play(currentAnimator).with(targetAnimator)
        animatorSet.duration = 1000
        animatorSet.addListener(animatorSetListener)
        animatorSet.start()
    }

    override fun onStop() {
        super.onStop()
        animatorSet.reverse()
        animatorSet.cancel()
    }
}