package com.example.base_module.util

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

    testMap.isEmpty()

    val numbers = listOf("one", "two", "three", "four")
    numbers.associateWith { it.length>3 }
}

/**
 * 与 Java 不同，Kotlin 中的数组是不型变的。这意味着 Kotlin 不允许我们把一个 Array<String> 赋值给一个 Array<Any>
 *
 * 但是对于 Java 方法，这是允许的（通过 Array<(out) String>! 这种形式的平台类型
 */
var StringArray = arrayOf<String>()
var AnyArray = arrayOf<Any>()
fun testArray(){
//    AnyArray = StringArray   这种操作是不被允许的 在Kotlin中 Array是不可型变的    一个Array<Father>  一个Array<Son> son是不能直接赋值给Father的
}
