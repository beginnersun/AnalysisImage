package com.example.base_module.util

import com.example.base_module.bean.PersonBean
import kotlinx.coroutines.*
import kotlin.system.measureTimeMillis

/**
 * 协程  类似Java中的Thread(轻量级线程)
 * 关键 launch（协程构建器）  runBlocking（协程构建器）  delay
 *    launch  GlobalScope.launch{ 异步代码 }   启动一个协程
 *    runBlocking()   调用了 runBlocking 的线程会一直 阻塞 直到 runBlocking 内部的协程执行完毕。
 *    delay(long 毫秒) 延迟多少毫秒执行  因为是suspend函数，所以只能在协程中使用
 * GlobalScope.launch 启动的是一个顶层线程（生命周期是全局的 新协程的生命周期只受整个程序的生命周期限制）
 * 为了防止频繁使用协程导内存消耗过多 可以使用结构化并发的模式处理
 * 在runBlocking{ }中使用launch方法创建协程  （在runBlocking中GlobalScope实例已经默认有了）
 * 因为runBlocking的特性（内部的程序不执行完不会挂掉 我们还可以省略delay或者join(等待，直到子线程结束，省去开发人员判断
 *   到底需要延迟多长时间)来防止JVM提前结束）在代码中其创建的协程属于所处线程
 *
 * GlobalScope.launch()这种协程的执行不会影响所处线程
 * 但是runBlocking会影响到
 *
 * launch()会返回一个Job对象用来控制整个协程  有默认参数 CoroutineContext = CoroutineContext（此为默认值）
 *         CoroutineStart = Dispatchers.DEFAULT (默认选择协程调度器，对应的线程为公告线程池  用来指明选择协程是属于哪个线程的)
 * join()  //等待作业执行结束（挂起协程直到结束）（避免因为主线程执行完毕jvm退出而导致崩溃）
 * cancel  //立即取消协程执行
 * cancelAndJoin()  //取消这个任务然后挂起协程直到任务执行完毕
 *
 * 协程挂起 (eg 被delay()就是被挂起
 * 恢复   （阻塞协程的内容执行完毕 继续执行协程）
 * 调度
 *
 *
 */

fun testThread() {

    val job = GlobalScope.launch {
        println(coroutineContext[Job])
        delay(10000)
        launch {

        }
    }
    PersonBean("", 1)
    val c = runBlocking {
        launch {
        }
        job.cancel()
        job.join()
        job.cancelAndJoin()  //取消线程并且挂起
        delay(1000)
    }

    measureTimeMillis {

    }

//    job.en

//    runBlocking {
//
//    }
}

class HTML {
    fun body() {
        println("55")
    }
}

fun html(cc: HTML.(Int) -> Unit): HTML {
    val html = HTML()  // 创建接收者对象
    html.cc(5)        // 将该接收者对象传给该 lambda
    cc.invoke(html,5)
    return html
}

val ccH:HTML.(Int) -> Unit = {
    a -> println(a+5)
    body()
}

fun testJieShouZhe(){
    html {
        body()
    }
    GlobalScope.launch {
    coroutineScope {
        delay(1000L)

    }
    }
}

fun main() = runBlocking { // this: CoroutineScope

    val jobInfo = launch(Dispatchers.Default){

    }

    GlobalScope.launch {
        val token = async { "fdaadsf" }.await()
        val post = async { "create" }.await()
    }

    var job = launch { // 在 runBlocking 作用域中启动一个新协程
        delay(1000L)
        println("World!")
        while (isActive){

        }
    }

    job.cancel()
    job.join()
    job.cancelAndJoin()


    println("Hello,")

    GlobalScope.async {  }
//    testFun{
//        this.and(5)
//    }

}



