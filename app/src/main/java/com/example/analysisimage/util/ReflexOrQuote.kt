package com.example.analysisimage.util

import android.app.Person
import com.example.analysisimage.bean.PersonBean
import com.example.analysisimage.bean.PlantBean

/**
 * 反射与类引用
 * 小知识点：
 * ::testReflex 函数实例 相当于(String) -> Unit 这个函数类型的一个值
 */

/**
 * 获取指定对象的类引用
 * ::class     如果该类是java那么还需要加上.java
 */
val b = ""
fun testReflex(name:String){
    println("${PlantBean::class.qualifiedName}")
    val person = PersonBean("ni",22)
    println("${person::class.qualifiedName}")
    println(::b.name)
}

fun html(init: String.() -> Unit): String {
    val html = String()
    html.init()
    return html
}