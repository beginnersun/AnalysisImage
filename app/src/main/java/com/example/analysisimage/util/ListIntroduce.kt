package com.example.analysisimage.util

/**
 * 集合概述
 */
/**
 * 声明一个列表的方法
 *   不可变 listOf(T,T) / listOf<T>()  第一种根据已有数据创建一个对应数据类型的List   第二种根据传入泛型参数创建对应的List
 *   可变   mutableListOf(T,T) / mutableListOf<T>()
 * 声明一个空列表
 *   不可变 emptyList<T>()
 * 同理Map与Set也是如此创建
 * ps Map使用第一种创建方式时  传入的参数为Pair（通常可以使用中缀函数创建 key to value ）
 */
val StringDefaultList = listOf<String>()
val StringList = listOf("1","2")
val StringMutableDefaultList = mutableListOf<String>()
val StringMutableList = mutableListOf("1","2")
val testMap = mapOf<String,String>()
val testMutableMap = mutableMapOf<String,String>()

fun testListCollection(){
    StringList[2]
    testMap["2"] to "2"
    testMutableMap["2"] = "2"

    val numbers = listOf("one", "two", "three", "four")
    numbers.associateWith { it.length>3 }
}
