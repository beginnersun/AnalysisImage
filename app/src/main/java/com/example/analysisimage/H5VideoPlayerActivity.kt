package com.example.analysisimage

import android.graphics.PixelFormat
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.tencent.smtt.export.external.interfaces.IX5WebChromeClient
import com.tencent.smtt.export.external.interfaces.JsPromptResult
import com.tencent.smtt.export.external.interfaces.JsResult
import com.tencent.smtt.sdk.*
import kotlinx.android.synthetic.main.activity_h5video.*
import android.util.Log


class H5VideoPlayerActivity:AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.setFormat(PixelFormat.TRANSLUCENT)
        setContentView(R.layout.activity_h5video)
        webView.settings.run {
            userAgentString = "${userAgentString}my_custom"
            allowFileAccess = true  //默认就是true  File 域下执行任意 JavaScript 代码
            setSupportZoom(true)  //是否支持缩放
            domStorageEnabled = true //JS 的 localStorage,sessionStorage 对象才可以使用
            javaScriptCanOpenWindowsAutomatically = true //JS是否可以打开新窗口
            saveFormData = true //开启保存表单数据功能
            savePassword = true //开启保存密码功能
            cacheMode = WebSettings.LOAD_NO_CACHE
            loadsImagesAutomatically = true
            loadWithOverviewMode = true
        }

        webView.webViewClient = object: WebViewClient(){
            override fun shouldOverrideUrlLoading(p0: WebView?, p1: String?): Boolean {
                return super.shouldOverrideUrlLoading(p0, p1)
            }

            override fun onPageFinished(p0: WebView?, p1: String?) {
                super.onPageFinished(p0, p1)
            }

        }

        webView.webChromeClient = object:WebChromeClient(){
            override fun onJsAlert(p0: WebView?, p1: String?, p2: String?, p3: JsResult?): Boolean {
                return super.onJsAlert(p0, p1, p2, p3)
            }

            override fun onJsPrompt(
                p0: WebView?,
                p1: String?,
                p2: String?,
                p3: String?,
                p4: JsPromptResult?
            ): Boolean {
                return super.onJsPrompt(p0, p1, p2, p3, p4)
            }

            override fun onJsConfirm(
                p0: WebView?,
                p1: String?,
                p2: String?,
                p3: JsResult?
            ): Boolean {
                return super.onJsConfirm(p0, p1, p2, p3)
            }

            override fun onShowCustomView(p0: View?, p1: IX5WebChromeClient.CustomViewCallback?) {
                super.onShowCustomView(p0, p1)
                Log.e("标签1","准备全屏")
                if(TbsVideo.canUseTbsPlayer(this@H5VideoPlayerActivity)){  //调用全屏
                    Log.e("标签0","启用全屏")
                    var extraData = Bundle()
                    extraData.putInt("screenMode", 102)
                    TbsVideo.openVideo(this@H5VideoPlayerActivity,"",extraData)
                }
//                val normalView = findViewById<View>(R.id.web_filechooser) as FrameLayout
//                val viewGroup = normalView.parent as ViewGroup
//                viewGroup.removeView(normalView)
//                viewGroup.addView(view)
            }

            override fun onHideCustomView() {
                super.onHideCustomView()
            }
        }
        /**
         * X5内核首次加载完再调用  虽然只需要第一次加载但是我们不知道用户会先打开哪个 所以必须在每个WebView后都加
         */
        CookieSyncManager.createInstance(this)
        CookieSyncManager.getInstance().sync()
    }

}