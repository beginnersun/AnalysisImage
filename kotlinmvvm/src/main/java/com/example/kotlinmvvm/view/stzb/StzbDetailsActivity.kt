package com.example.kotlinmvvm.view.stzb
import android.content.pm.ActivityInfo
import android.graphics.Bitmap
import android.os.Bundle
import com.example.kotlinmvvm.R
import android.os.Message
import android.util.Log
import android.webkit.WebSettings
import android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.base_module.widget.VideoPlayer
import com.example.kotlinmvvm.base.BaseActivity
import com.example.kotlinmvvm.base.BaseViewModel
import com.example.kotlinmvvm.bean.NoticeDetailsBean
import com.example.kotlinmvvm.databinding.ActivityStzbDetailBinding
import com.example.kotlinmvvm.view.stzb.adapter.CommentAdapter
import com.example.kotlinmvvm.vm.StzbDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.os.Build
import android.webkit.WebChromeClient


class StzbDetailsActivity : BaseActivity(), VideoPlayer.VideoListenerCallBack {
    override fun setViewModel(): BaseViewModel = viewModel

    override fun stationChanged(station: Int, message: Message?) {

    }

    override fun fullScreen(full: Boolean) {
        if (full) {
            oldHeight = binding!!.videoPlayer.layoutParams.height
            binding!!.videoPlayer.layoutParams.height = ConstraintLayout.LayoutParams.MATCH_PARENT
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        } else {
            binding!!.videoPlayer.layoutParams.height = oldHeight
            requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        }
    }

    override fun share() {

    }

    private var oldHeight = 0
    private var tid: String = ""
    private val details = mutableListOf<NoticeDetailsBean>()
    private var noticeDetails: NoticeDetailsBean? = null
    private var commentAdapter: CommentAdapter = CommentAdapter(this, details)

    private fun load() {
        viewModel.getDetail(tid)
    }

    private fun initInfo(info: MutableList<NoticeDetailsBean>) {
        showLoading()
        for (value in info) {
            if (value.first.compareTo("1") == 0) {
                noticeDetails = value
            } else if (value.first.compareTo("0") == 0) {
                details.add(value)
            }
        }
//
        binding?.data = noticeDetails
//        binding?.webView!!.loadUrl("https://v.163.com/paike/V8H1BIE6U/VAG52A1KT.html")
        binding?.webView!!.loadDataWithBaseURL(
            null,
            "<style>img{ max-width:100%;height:auto}</style> \r\n ${noticeDetails!!.message}",
            "application/x-shockwave-flash",
            "UTF-8",
            null
        )
        Log.e("播放模式","application/x-shockwave-flash")
        Log.e("data数据", binding?.data.toString())
        commentAdapter.notifyDataSetChanged()

    }

    private var binding: ActivityStzbDetailBinding? = null
    private val viewModel: StzbDetailsViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_stzb_detail)

        binding?.model = viewModel

        tid = intent.getStringExtra("tid")
        binding?.commentRecyclerView!!.run {
            layoutManager = LinearLayoutManager(this@StzbDetailsActivity)
            adapter = commentAdapter
        }
        binding?.webView!!.settings.run {
            javaScriptEnabled = true
            useWideViewPort = true
            loadWithOverviewMode = true
            allowFileAccess = true
            setSupportZoom(true)
            javaScriptCanOpenWindowsAutomatically = true
            domStorageEnabled = true
            cacheMode = WebSettings.LOAD_NO_CACHE
            textZoom = 200
            pluginState = WebSettings.PluginState.ON
            mixedContentMode = MIXED_CONTENT_ALWAYS_ALLOW
        }
        try {
            if (Build.VERSION.SDK_INT >= 16) {
                val clazz = binding?.webView!!.getSettings()::class.java
                val method = clazz.getMethod("setAllowUniversalAccessFromFileURLs", Boolean::class.javaPrimitiveType)
                if (method != null) {
                    method!!.invoke(binding?.webView!!.getSettings(), true)
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        binding?.webView!!.webChromeClient = object :WebChromeClient(){
            override fun getDefaultVideoPoster(): Bitmap? {
                return Bitmap.createBitmap(1, 1, Bitmap.Config.ARGB_8888)
            }
        }
        binding?.webView!!.webViewClient = object :WebViewClient(){
            override fun onPageFinished(view: WebView?, url: String?) {
                endLoading()
                super.onPageFinished(view, url)
            }
        }
        binding?.webView!!.isHorizontalScrollBarEnabled = false
        binding?.webView!!.isVerticalScrollBarEnabled = false
        load()

        viewModel.detailData.observe(this, Observer {
            details.clear()
            initInfo(it.toMutableList())
        })
    }

    override fun onDestroy() {
        binding?.webView?.run {
            loadDataWithBaseURL(null, "", "application/x-shockwave-flash", "utf-8", null)
            clearHistory()
            destroy()
        }
        super.onDestroy()
    }

}