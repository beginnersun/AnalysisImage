package com.example.analysisimage

import android.content.res.Configuration
import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import android.util.Log
import com.tencent.smtt.export.external.interfaces.*
import com.tencent.smtt.sdk.*
import android.widget.Toast
import android.webkit.JavascriptInterface
import android.widget.Button


class H5VideoPlayerActivity : AppCompatActivity() {

    private var webView: WebView? = null
    private var switchBtn: Button? = null

    private var myVideoView: View? = null
    private var myNormalView: View? = null
    private var callback: IX5WebChromeClient.CustomViewCallback? = null

    private var flage = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.TRANSLUCENT)
        setContentView(R.layout.activity_h5video)
        webView = findViewById(R.id.webView)
        switchBtn = findViewById(R.id.switchBtn)

        switchBtn?.setOnClickListener{
            if (!flage){
                webView!!.loadUrl("http://soft.imtt.qq.com/browser/tes/feedback.html")
            }else{
                webView!!.loadUrl("https://v.qq.com/x/cover/mzc00200h1b5kde/f0966btulm6.html")
            }
            flage = !flage
        }

        webView!!.loadUrl("https://v.qq.com/x/cover/mzc00200h1b5kde/f0966btulm6.html")
        webView!!.webViewClient = object : WebViewClient() {
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                Log.e("标签Url", p1!!)
                return if (p1?.startsWith("http")) {
                    p0?.loadUrl(p1!!)
                    true
                } else {
                    true
                }
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
                enableX5FullscreenFunc()
            }

            override fun onReceivedSslError(p0: WebView?, p1: SslErrorHandler?, p2: SslError?) {
                p1?.proceed()
            }

        }
        initWebViewSettings()
//        webView!!.settings.apply {
//            //            userAgentString = "${userAgentString}my_custom"
//            Log.e("标签log", "执行配置")
////            allowFileAccess = true  //默认就是true  File 域下执行任意 JavaScript 代码
//            javaScriptEnabled = true
//            setSupportZoom(true)  //是否支持缩放
//            domStorageEnabled = true //JS 的 localStorage,sessionStorage 对象才可以使用
////            javaScriptCanOpenWindowsAutomatically = true //JS是否可以打开新窗口
////            saveFormData = true //开启保存表单数据功能
////            savePassword = true //开启保存密码功能
//            cacheMode = WebSettings.LOAD_NO_CACHE
//            loadsImagesAutomatically = true
//            loadWithOverviewMode = true
//            useWideViewPort = true
//            javaScriptCanOpenWindowsAutomatically = true
//
//            setAllowUniversalAccessFromFileURLs(true)
//        }

        window.setFormat(PixelFormat.TRANSLUCENT)
        webView!!.view.overScrollMode = View.OVER_SCROLL_ALWAYS
        webView!!.view.isClickable = true
//        webView?.addJavascriptInterface(object : WebViewJavaScriptFunction {
//
//            override fun onJsFunctionCalled(tag: String) {
//                Log.e("标签Call",tag)
//
//            }
//
//            @JavascriptInterface
//            fun onX5ButtonClicked() {
//                Log.e("标签X5Button","")
//                this@H5VideoPlayerActivity.enableX5FullscreenFunc()
//            }
//
//            @JavascriptInterface
//            fun onCustomButtonClicked() {
//                Log.e("标签onCustomButton","")
//                this@H5VideoPlayerActivity.disableX5FullscreenFunc()
//            }
//
//            @JavascriptInterface
//            fun onLiteWndButtonClicked() {
//                Log.e("标签LiteWndButton","")
//                this@H5VideoPlayerActivity.enableLiteWndFunc()
//            }
//
//            @JavascriptInterface
//            fun onPageVideoClicked() {
//                Log.e("标签PageVideo","")
//                this@H5VideoPlayerActivity.enablePageVideoFunc()
//            }
//        }, "Android")

        /**
         * X5内核首次加载完再调用  虽然只需要第一次加载但是我们不知道用户会先打开哪个 所以必须在每个WebView后都加
         */
//        CookieSyncManager.createInstance(this)
//        CookieSyncManager.getInstance().sync()

    }

    private fun initWebViewSettings() {
        val webSetting = webView?.settings!!
        webSetting.setJavaScriptEnabled(true)
        webSetting.setJavaScriptCanOpenWindowsAutomatically(true)
        webSetting.setAllowFileAccess(true)
        webSetting.setLayoutAlgorithm(WebSettings.LayoutAlgorithm.NARROW_COLUMNS)
        webSetting.setSupportZoom(true)
        webSetting.setBuiltInZoomControls(true)
        webSetting.setUseWideViewPort(true)
        webSetting.setSupportMultipleWindows(true)
        // webSetting.setLoadWithOverviewMode(true);
        webSetting.setAppCacheEnabled(true)
        // webSetting.setDatabaseEnabled(true);
        webSetting.setDomStorageEnabled(true)
        webSetting.setGeolocationEnabled(true)
        webSetting.setAppCacheMaxSize(java.lang.Long.MAX_VALUE)
        // webSetting.setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);
        webSetting.setPluginState(WebSettings.PluginState.ON_DEMAND)
        // webSetting.setRenderPriority(WebSettings.RenderPriority.HIGH);
        webSetting.setCacheMode(WebSettings.LOAD_NO_CACHE)

        // this.getSettingsExtension().setPageCacheCapacity(IX5WebSettings.DEFAULT_CACHE_CAPACITY);//extension
        // settings 的设计
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        // TODO Auto-generated method stub
        try {
            super.onConfigurationChanged(newConfig)
            if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {

            } else if (resources.configuration.orientation == Configuration.ORIENTATION_PORTRAIT) {

            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

    }
    // /////////////////////////////////////////
    // 向webview发出信息
    private fun enableX5FullscreenFunc() {
        Log.e("标签","准备开启全屏")
        if (webView!!.x5WebViewExtension != null) {
            Toast.makeText(this, "开启X5全屏播放模式", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", false)// true表示标准全屏，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView!!.x5WebViewExtension!!.invokeMiscMethod(
                "setVideoParams",
                data
            )

        Log.e("标签","开启全屏")
        }
    }

    private fun disableX5FullscreenFunc() {
        if (webView?.x5WebViewExtension != null) {
            Toast.makeText(this, "恢复webkit初始状态", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", true)// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView?.x5WebViewExtension!!.invokeMiscMethod(
                "setVideoParams",
                data
            )
        }
    }

    private fun enableLiteWndFunc() {
        if (webView?.x5WebViewExtension != null) {
            Toast.makeText(this, "开启小窗模式", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", false)// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", true)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 2)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView?.x5WebViewExtension!!.invokeMiscMethod(
                "setVideoParams",
                data
            )
        }
    }

    private fun enablePageVideoFunc() {
        if (webView?.x5WebViewExtension != null) {
            Toast.makeText(this, "页面内全屏播放模式", Toast.LENGTH_LONG).show()
            val data = Bundle()

            data.putBoolean("standardFullScreen", false)// true表示标准全屏，会调起onShowCustomView()，false表示X5全屏；不设置默认false，

            data.putBoolean("supportLiteWnd", false)// false：关闭小窗；true：开启小窗；不设置默认true，

            data.putInt("DefaultVideoScreen", 1)// 1：以页面内开始播放，2：以全屏开始播放；不设置默认：1

            webView?.x5WebViewExtension!!.invokeMiscMethod(
                "setVideoParams",
                data
            )
        }
    }


    override fun onDestroy() {
        webView?.destroy()

        super.onDestroy()
    }

    override fun onPause() {
        webView?.onPause()
        super.onPause()
    }

    override fun onResume() {
        super.onResume()
        webView?.onResume()
    }

}