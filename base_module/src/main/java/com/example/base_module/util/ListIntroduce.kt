package com.example.base_module.util

import com.example.base_module.bean.PersonBean
import com.example.base_module.bean.UserBean
import java.util.*
import java.util.stream.Collectors
import kotlin.collections.ArrayList


fun testListKotlinOrJava() {


}


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
val StringList = listOf("1", "2")
val StringMutableDefaultList = mutableListOf<String>()
val StringMutableList = mutableListOf("1", "2")
val testMap = mapOf<String, String>()
val testMutableMap = mutableMapOf<String, String>()

fun testListCollection() {
    testListKotlinOrJava()
    StringList[2]
    testMap["2"] to "2"
    testMutableMap["2"] = "2"

    testMap.isEmpty()

    val numbers = listOf("one", "two", "three", "four")
    numbers.associateWith { it.length > 3 }


    StringMutableDefaultList.filter { it.length >= 2 }.map { it.length }

    val containers = listOf(
        listOf("one", "two", "three"),
        listOf("four", "five", "six"),
        listOf("seven", "eight")
    )
    containers.flatMap { it.map { a -> a.length } }[0]


    StringMutableDefaultList.stream().collect(Collectors.groupingBy(String::first))

    sequenceOf(arrayOf(1, 2, 3)).distinct().forEach { }

    StringMutableDefaultList.asSequence()

    sequence {
        yield(1)
        yieldAll(listOf(1, 2, 3))
    }.take(15)

    val persons = mutableListOf<PersonBean>()
    val normalList = mutableListOf<UserBean>()
    persons.map { it.age }[5]

    persons.asSequence().map { }
    val containerUserBean = listOf(
        listOf(PersonBean("22", 2), PersonBean("22", 3), PersonBean("22", 4)),
        listOf(PersonBean("22", 5), PersonBean("22", 6), PersonBean("22", 7)),
        listOf(PersonBean("22", 10), PersonBean("22", 8), PersonBean("22", 9))
    )

    containerUserBean.flatMap { it.filter { bean -> bean.age>16 } }.forEach { icc ->
        run {
            icc.age = icc.age * 2
            icc.name = "12"
        }
    }
    "fsda".last()



//    persons.zip(normalList)[0].first.age
    persons.distinct()
    persons.take(5).drop(5).chunked(5){it.map {bean -> bean.age }}.windowed(5)

    persons.reverse()
    persons.groupBy { it.age }[5];

    val sss = (persons.groupBy(keySelector = {it.age},valueTransform = {it.name})[25] ?: error(""))
    persons.groupingBy { it.age }.reduce{key,bean1,bean2 -> bean1}
    persons.groupingBy { it.age }.fold(5){ age,bean2 ->
        age+bean2.age
    }

    //分支合并
    println("测试分支合并")

}

fun String.first() = this[0]

/**
 * 与 Java 不同，Kotlin 中的数组是不型变的。这意味着 Kotlin 不允许我们把一个 Array<String> 赋值给一个 Array<Any>
 *
 * 但是对于 Java 方法，这是允许的（通过 Array<(out) String>! 这种形式的平台类型
 */
var StringArray = arrayOf<String>()
var AnyArray = arrayOf<Any>()
fun testArray() {
//    AnyArray = StringArray   这种操作是不被允许的 在Kotlin中 Array是不可型变的    一个Array<Father>  一个Array<Son> son是不能直接赋值给Father的
}


