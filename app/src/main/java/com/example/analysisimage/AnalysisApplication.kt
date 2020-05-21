package com.example.analysisimage

import android.app.ActivityManager
import android.content.Context
import android.content.Intent
import android.util.Log
import com.example.base_module.BaseApplication
import com.example.kotlinmvvm.localModule
import com.example.kotlinmvvm.mainViewModel
import com.example.kotlinmvvm.newsViewModel
import com.example.kotlinmvvm.remoteModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.android.logger.AndroidLogger
import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import android.os.Process
import com.tencent.smtt.sdk.QbSdk
import org.koin.core.context.GlobalContext
import java.io.File
import java.util.regex.Pattern

class AnalysisApplication : BaseApplication() {

    private val cc: (KoinApplication) -> Unit = { it ->
        Log.e("测试1","初始化Module")
        it.androidLogger()
        it.androidContext(this@AnalysisApplication)
        it.modules(listOf(remoteModule, mainViewModel,newsViewModel, localModule))
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        val isMainProcess = isMainProcess(base!!)

        Log.e("当前进程名attach","${getProcessName(this)}")
        Log.e("当前线程名","${Thread.currentThread().name}")
        if (isMainProcess && !isVMMultidexCapable(System.getProperty("java.vm.version")!!)){
//            loadMultiDex(base!!)
        }
    }

    override fun onCreate() {
        Log.e("测试","开始")
        super.onCreate()
        initWebX5()
        Log.e("当前进程名onCreate","${getProcessName(this)}")
        Log.e("当前线程名","${Thread.currentThread().name}")
        Log.e("测试","加载Module")
        startKoin(GlobalContext(),cc)
    }

    private fun initWebX5(){
        QbSdk.setDownloadWithoutWifi(true)
//        val map = HashMap<String, Any>()
//        map[TbsCoreSettings.TBS_SETTINGS_USE_SPEEDY_CLASSLOADER] = true
//        QbSdk.initTbsSettings(map)

        QbSdk.initX5Environment(applicationContext,object: QbSdk.PreInitCallback{
            override fun onCoreInitFinished() {
                Log.e("标签cc","初始化完毕准备加载X5")
            }
            override fun onViewInitFinished(p0: Boolean) {
                Log.e("标签ss","加载内核是否成功:$p0")

            }
        })
    }

    private fun isMainProcess(context:Context):Boolean{
        return context.packageName.equals(getProcessName(context))
    }

    private fun loadMultiDex(context: Context){
        newTempFile(context)

        context.startActivity(Intent(context,LoadMultiDexActivity::class.java).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK))

    }

    /**
     * 预加载一些Activity
     * Java 中在创建一个对象时会先判断对应的class是否已加载如果没有那么jvm会先查找到这个class 然后载入 而如果已经加载过那么会直接实例化（减少创建时间）
     */
    private fun preNewActivity(){
        val startTime = System.currentTimeMillis()
        val mainActivity = MainActivity()
        val downLoadActivity = DownLoadActivity()
        Log.d("加载MainActivity耗时","${System.currentTimeMillis() - startTime}")
    }

    /**
     * 创建一个临时文件  在MultiDex执行install成功后会删除
     */
    private fun newTempFile(context: Context){
        File(context.cacheDir.absolutePath,"load_dex.tmp").run {
            if (!exists()){
                createNewFile()
            }
        }
    }



    private fun getProcessName(context: Context):String{
        val activityManager = (context.getSystemService(Context.ACTIVITY_SERVICE) as ActivityManager)
        val appProcesses = activityManager.runningAppProcesses
        var myPid = Process.myPid()
        if (appProcesses == null || appProcesses.size == 0){
            return ""
        }
        for (appProcess in appProcesses){
            if (appProcess.processName.compareTo(context.packageName) == 0){
                if (appProcess.pid == myPid){
                    return appProcess.processName;
                }
            }
        }
        return ""
    }

    private fun isVMMultidexCapable(version:String):Boolean{
        Log.e("验证Version","$version")
        var isMultidexCapable = false
        val matcher = Pattern.compile("(\\d+)\\.(\\d+)(\\.\\d+)?").matcher(version)
        if (matcher.matches()){
            var major = Integer.parseInt(matcher.group(1))
            var minor = Integer.parseInt(matcher.group(2))
            isMultidexCapable = major > 2 || major == 2 && minor >= 1
        }
        return isMultidexCapable
    }

//
//    val koin:KoinApplication.() -> Unit = {
//        androidContext(this@MvvmApplication)
//        logger(AndroidLogger())
//        androidFileProperties("koin.properties")
//        modules(listOf(remoteModule, mainViewModel))
//    }

}