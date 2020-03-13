package com.example.analysisimage

import android.os.Bundle
import android.os.Environment
import android.os.PersistableBundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.analysisimage.util.FileUtils
import kotlinx.android.synthetic.main.activity_down_load.*
import java.io.FileOutputStream
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL


class DownLoadActivity:AppCompatActivity() {

    val url = "http://txmov2.a.yximgs.com/upic/2020/03/09/16/BMjAyMDAzMDkxNjA1NDVfMTU0MjIwMDcwXzI0NzAxNjA4Njg3XzBfMw==_b_B802ad12caaec2e43d51e347346bf554e.mp4"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_down_load)

        val downRunnbale = DownRunnable(url)
        val thread = Thread(downRunnbale)
        down.setOnClickListener {
            thread.start()
        }
    }

    inner class DownRunnable(val path:String):Runnable{

        override fun run() {
            val url = URL(path)
            val con = url.openConnection() as HttpURLConnection
            con.setReadTimeout(5000)
            con.setConnectTimeout(5000)
            con.setRequestProperty("Charset", "UTF-8")
            con.setRequestMethod("GET")

            var head = con.headerFields
            for (item in head.entries){
                Log.e("头部信息","${item.key}    ${item.value}")
            }

            try {
                if (con.getResponseCode() === 200) {
                    val `is` = con.getInputStream()//获取输入流
                    var fileOutputStream: FileOutputStream? = null//文件输出流
                    if (`is` != null) {
                        val file = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
                        val fileUtils = FileUtils(file!!.absolutePath)
                        fileOutputStream = FileOutputStream(fileUtils.createFile("test.mp4"))//指定文件保存路径，代码看下一步
                        val buf = ByteArray(1024)
                        var ch = `is`.read(buf)
                        while (ch != -1) {
                            fileOutputStream!!.write(buf, 0, ch)//将获取到的流写入文件中
                            ch = `is`.read(buf)
                        }
                        Log.e("下载完成","${file.absolutePath}")
//                        Toast.makeText(this@DownLoadActivity,"下载完成${file.absolutePath}",Toast.LENGTH_LONG).show()
                    }
                    if (fileOutputStream != null) {
                        fileOutputStream!!.flush()
                        fileOutputStream!!.close()
                    }
                }
            }catch (e:Exception){
                Log.e("下载失败","${e.message}")
                e.printStackTrace()
            }


        }

    }

}