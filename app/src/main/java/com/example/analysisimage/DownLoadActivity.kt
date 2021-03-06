package com.example.analysisimage

import android.animation.AnimatorInflater
import android.animation.LayoutTransition
import android.animation.TypeEvaluator
import android.animation.ValueAnimator
import android.app.ActivityManager
import android.content.Context
import android.graphics.Point
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.work.*
import com.example.analysisimage.worker.DownLoadWorker
import com.example.base_module.Constants
import com.example.base_module.widget.ArrowView
import com.example.kotlinmvvm.model.Bean
import com.example.kotlinmvvm.widget.PointView
import java.util.concurrent.TimeUnit


class DownLoadActivity:AppCompatActivity() {

    val url = "http://txmov2.a.yximgs.com/upic/2020/03/09/16/BMjAyMDAzMDkxNjA1NDVfMTU0MjIwMDcwXzI0NzAxNjA4Njg3XzBfMw==_b_B802ad12caaec2e43d51e347346bf554e.mp4"

    private var pointView:ArrowView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_load_multidex)

        /**
         * 唯一名称 对应 唯一Work
         * 但是唯一的work肯定是
         */
        val constraints = Constraints.Builder().setRequiredNetworkType(NetworkType.CONNECTED).build()
        val data = workDataOf(Pair("MEME", "url"))
        val workRequest = OneTimeWorkRequestBuilder<DownLoadWorker>() //oneTimeWork 意为执行一次  周期性的使用PeriodicWorkRequest
            .setInputMerger(OverwritingInputMerger::class.java) // 当处于链接工作时  输出的内容怎么处理
            // OverwritingInputMerger表示发生冲突会覆盖
            // ArrayCreateInputMerger 表示合并必要时会创建一个新的数组
            .setConstraints(constraints) //添加约束条件（满足时才回执行）
            .setInitialDelay(5, TimeUnit.MINUTES) //设置初始化延迟事件
            .addTag("down")  //添加标签  多个WorkRequest可以添加同一个标签 这样多个WorkRequest就会分为一个组 然后利用WorkManager的有关tag方法进行统一处理
            .setInputData(data).build() //传入参数给DownLoadWorker的doWork方法 doWork方法里面getInputData获取
        val result = WorkManager.getInstance(this).enqueue(workRequest)  //执行
//        when(result.state){
//            WorkInfo.State.BLOCKED -> print("又尚未完成的前提性工作")
//            WorkInfo.State.ENQUEUED -> print("满足enqueue的条件能够立即执行")
//            WorkInfo.State.RUNNING -> print("已经正在执行")
//        }
//
//
//        /**
//         * 监听状态
//         */
//        WorkManager.getInstance(this).getWorkInfosByTagLiveData("down").observe(this, Observer {
//            for (item in it){
//                print(item.state)
//                val process = item.progress.getInt("process",-1)
//            }
//        })
//
//        /**
//         * 创建链接工作  当workRequest是OneTimeWorkRequest时前一个的输出结果作为后一个的输入参数
//         * begin也可以是一个list 这个list的内容可能会并行
//         * ps:父级状态与从属相关 父级取消则从属取消
//         */
//        WorkManager.getInstance(this).beginWith(workRequest).then(workRequest)
//            .enqueue()
//        WorkManager.getInstance(this).beginWith(listOf(workRequest)).then(workRequest).enqueue()
//
//        /**
//         * 唯一工作链只能是oneTime类型
//         */
//        WorkManager.getInstance(this)
//            .beginUniqueWork("唯一名称",ExistingWorkPolicy.APPEND,workRequest).enqueue() //第二个参数是指冲突策略 当名称重复怎么做
////        WorkManager.getInstance(this).enqueueUniquePeriodicWork()  //唯一名称的重复任务  参数都同上
////        WorkManager.getInstance(this).enqueueUniqueWork()  //唯一名称的单次任务
//
//
//        /**
//         * 根据id与tag可以对任务进行取消和停止
//         */
//        WorkManager.getInstance(this).cancelWorkById(workRequest.id)
//        /**
//         * 取消一个工作链
//         */
//        WorkManager.getInstance(this).cancelUniqueWork("唯一名称的Work")
//
//        val constraintsPeriodic = Constraints.Builder()
//            .setRequiresCharging(true)
//            .build()
//        val downWork = PeriodicWorkRequestBuilder<DownLoadWorker>(1,TimeUnit.MINUTES) //重复间隔1分钟
//            .setConstraints(constraintsPeriodic).build()
//        WorkManager.getInstance(this).enqueue(downWork)

//        var outMetrics = DisplayMetrics()
//        windowManager.defaultDisplay.getMetrics(outMetrics)
//        var widthPixels = outMetrics.widthPixels
//        var heightPixels = outMetrics.heightPixels
//        Constants.SCREEN_WIDTH = widthPixels
//        Constants.SCREEN_HEIGHT = heightPixels
//        pointView = findViewById(R.id.pointView)
//
//        pointView!!.post {
//            pointView!!.start()
//        }
//
//        val activityManager:ActivityManager = getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager

//
//        val downRunnbale = DownRunnable(url)
//        val thread = Thread(downRunnbale)
//        down.setOnClickListener {
//            thread.start()
//        }

        var varags = arrayOf(Point(),Point())
        ValueAnimator.ofObject(object :TypeEvaluator<Point>{
            override fun evaluate(fraction: Float, startValue: Point?, endValue: Point?): Point {
                return Point()
            }
        },Point(),Point()).apply {
            addUpdateListener {
                val point = it.values as Point
             }
        }
        var group = LinearLayout(this)
        LayoutTransition.APPEARING
        val animator = AnimatorInflater.loadStateListAnimator(this,R.animator.design_appbar_state_list_animator)//加载动画
        group.stateListAnimator = animator


    }

//    inner class DownRunnable(val path:String):Runnable{
//
//        override fun run() {
//            val url = URL(path)
//            val con = url.openConnection() as HttpURLConnection
//            con.setReadTimeout(5000)
//            con.setConnectTimeout(5000)
//            con.setRequestProperty("Charset", "UTF-8")
//            con.setRequestMethod("GET")
//
//            var head = con.headerFields
//            for (item in head.entries){
//                Log.e("头部信息","${item.key}    ${item.value}")
//            }
//
//            try {
//                if (con.getResponseCode() === 200) {
//                    val `is` = con.getInputStream()//获取输入流
//                    var fileOutputStream: FileOutputStream? = null//文件输出流
//                    if (`is` != null) {
//                        val file = getExternalFilesDir(Environment.DIRECTORY_MOVIES)
//                        val fileUtils = FileUtils(file!!.absolutePath)
//                        fileOutputStream = FileOutputStream(fileUtils.createFile("test.mp4"))//指定文件保存路径，代码看下一步
//                        val buf = ByteArray(1024)
//                        var ch = `is`.read(buf)
//                        while (ch != -1) {
//                            fileOutputStream!!.write(buf, 0, ch)//将获取到的流写入文件中
//                            ch = `is`.read(buf)
//                        }
//                        Log.e("下载完成","${file.absolutePath}")
////                        Toast.makeText(this@DownLoadActivity,"下载完成${file.absolutePath}",Toast.LENGTH_LONG).show()
//                    }
//                    if (fileOutputStream != null) {
//                        fileOutputStream!!.flush()
//                        fileOutputStream!!.close()
//                    }
//                }
//            }catch (e:Exception){
//                Log.e("下载失败","${e.message}")
//                e.printStackTrace()
//            }
//
//
//        }
//
//    }

}