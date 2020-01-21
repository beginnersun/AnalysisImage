package com.example.kotlinmvvm.view.stzb

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.text.Html
import androidx.appcompat.app.AppCompatActivity
import com.example.kotlinmvvm.R
import kotlinx.android.synthetic.main.activity_news_detail.*
import android.os.Handler
import android.os.Message
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.text.Spanned
import android.util.Log
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.*
import java.net.URL


class StzbDetailsActivity : AppCompatActivity() {

    private var htmlInfo = ""

    private var message: Spanned? = null

    private fun Load(){

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_detail)
//        htmlInfo =
//            "<div style=\"padding: 0px; border: 10px solid rgb(0, 0, 0); width: 840px; margin-top: 10px; margin-bottom: 10px; margin-left: 45px; float: left;\">\n" +
//                    "  <div style=\"margin: 2px; padding: 10px; border: 1px solid rgb(0, 0, 0); text-align: center;\">    \n" +
//                    "    <table style=\"width: 810px; height: 60px; border-right-color: rgb(0, 0, 0); border-bottom-color: rgb(0, 0, 0); border-left-color: rgb(0, 0, 0); " +
//                    "border-right-width: 1px; border-bottom-width: 1px; border-left-width: 1px; border-right-style: dashed; border-bottom-style: dashed; border-left-style: " +
//                    "dashed; table-layout: fixed;\" cellspacing=\"0\" cellpadding=\"0\">      <tbody>        <tr>          <td style=\"border-style: dashed; border-color:" +
//                    " white;\" background=\"\"><p style=\"text-align: center;\"><img src=\"https://mgame-f.netease.com/forum/201907/10/142341n9m977usz9emi9bi.jpg\"/><font face" +
//                    "=\"微软雅黑\" color=\"#ff0000\"><b><br></b></font><br></p><p style=\"text-align: center;\"><font face=\"微软雅黑\" size=\"5\"><b>《率土之滨》部分服务器合服维" +
//                    "护预告</b></font></p><p style=\"text-align: left;\"><font face=\"微软雅黑\" size=\"4\"><b>亲爱的主公：</b></font></p><p style=\"text-align: left;\"><br></p><p " +
//                    "style=\"text-align: left;\"><font face=\"微软雅黑\" size=\"3\">　　《率土之滨》</font><font face=\"微软雅黑\" size=\"3\"><font color=\"#ff0000\"><b>S2411,S241" +
//                    "2,S2413,S2414</b></font>，</font><font face=\"微软雅黑\" size=\"3\">将于<b><font color=\"#ff0000\">1月9日10:00</font></b>进行停机维护，并进行赛季切换和服务器合" +
//                    "并，预计维护时间为1小时，期间将无法登录游戏。</font></p><p style=\"text-align: left;\"><font face=\"微软雅黑\" size=\"3\"><br></font></p><p style=\"text-align:" +
//                    " left;\"><font face=\"微软雅黑\" size=\"3\">　　以下是本次合服的方案：</font></p><p style=\"text-align: left;\"><font face=\"微软雅黑\" size=\"3\">　　1329区,13" +
//                    "31区,1332区,1335区,1336区,1337区将进行合并，合并后服务器代号为S3221</font></p><p style=\"text-align: left;\"><font face=\"微软雅黑\" size=\"3\"><br></font></p><" +
//                    "/td>        </tr>      </tbody>    </table>  </div></div>\\n\\n\\n"

//        GlobalScope.launch(Dispatchers.Main) {
//            var spanned = delayText()
//            tv_images.text = spanned!!
//        }



//        if (Build.VERSION.SDK_INT >= 24) {
//            Observable.create<Spanned> {
//                emitter ->
//                emitter.onNext(Html.fromHtml(htmlInfo, FROM_HTML_MODE_LEGACY, imgGetter, null))
//            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//                spanned ->
//                tv_images.text = spanned
//            }
//        }else{
//            Observable.create<Spanned> {
//                    emitter ->
//                emitter.onNext(Html.fromHtml(htmlInfo,imgGetter, null))
//            }.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe {
//                    spanned ->
//                tv_images.text = spanned
//            }
//        }
    }

//    suspend fun delayText(): Spanned =
//        withContext(Dispatchers.IO) {
//            Html.fromHtml(htmlInfo, imgGetter, null)
//        }
//
//
//    //这里面的resource就是fromhtml函数的第一个参数里面的含有的url
//    private val imgGetter: Html.ImageGetter = Html.ImageGetter { source ->
//        var drawable: Drawable?
//        val url: URL
//        try {
//            url = URL(source)
//            drawable = Drawable.createFromStream(url.openStream(), "") // 获取网路图片
//        } catch (e: Exception) {
//            Log.e("出错开始", "1")
//            e.printStackTrace()
//            Log.e("出错结束", "2")
//            return@ImageGetter null
//        }
//        drawable!!.setBounds(
//            0, 0, drawable!!.intrinsicWidth,
//            drawable!!.intrinsicHeight
//        )
//        drawable
//    }


}