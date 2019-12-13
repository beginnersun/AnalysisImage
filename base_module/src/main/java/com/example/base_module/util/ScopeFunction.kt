package com.example.base_module.util

import com.example.analysisimage.bean.PersonBean


/**
 * 作用域函数
 * 好处：在作用域中访问对象不需要他的名称
 * let run with apply also
 * 作用域函数的主要区别
 *     上下文对象的引用方式
 *     it or this(this的话可以省略不写)
 *     it   let,also
 *     this run apply with
 *     函数返回对象
 *     apply also 返回对象本身
 *     let run with 返回lambda表达式结果
 *
 *
 * 以下是根据预期目的选择作用域函数的简短指南：
    对一个非空（non-null）对象执行 lambda 表达式：let
    将表达式作为变量引入为局部作用域中：let
    对象配置：apply
    对象配置并且计算结果：run
    在需要表达式的地方运行语句：非扩展的 run
    附加效果：also
    一个对象的一组函数调用：with
 */
fun testScope(){
    val person = PersonBean("test",10)
    person.age = 2
    PersonBean("Bob",22).let {
//        test ->
        it.age = 2
    }
    PersonBean("Seina",20).also {
        it.age = 5
    }
    PersonBean("Jerry",21).run {
        age = 2
        println("$this fda")
    }

    var personHaveJack = PersonBean("Tom",23).apply {
        age = 2
    }.apply {
        name = "TomeOrJack"
    }.let {
        "Jack" in it.name
    }
    with(PersonBean("Tina",20)){
        age = 5
    }
    val numbers = mutableListOf("one", "two", "three", "four", "five")
}

/**
 * takeIf 与 takeUnless 函数
 * 对单个的对象进行判断 takeIf{} 满足返回该对象 不满足返回null    takeUnless函数 满足返回null 不满足返回对象
 * ps 可能返回为空  所以使用返回的对象的时候需要?.
 * 利用?. 与take函数可以减少if else 的使用
 * eg     input.indexOf(sub).takeIf { it >= 0 }?.let {
 *          println("The substring $sub is found in $input.")
 *          println("Its start position is $it.")
 *        }
 */
fun testTakeFunction(){
    var person = PersonBean("Tome",22)
    person.name.takeIf { it.contains("Tom") }?.let {
        println("welcom to $it 's home")
    }
}




//interface Base {
//    fun print()
//}

// 实现此接口的被委托的类
//class BaseImpl(val x: String) : Base {
//    override fun print() {
//        Log.d("haha", x)
//    }
//}
// 通过关键字 by 建立委托类      修改前
//class Derived(b: Base) : Base by b {
//
//    fun doMyOwnPrint() {
//        Log.d("hehe","33")
//        print()
//    }
//}

// 通过关键字 by 建立委托类      修改后
//class Derived(var b: Base) : Base by b {
//
//    override fun print() {
//        Log.d("hehe","33")
//        b.print()
//    }
//}

fun testmain() {
//    val b = BaseImpl("10")
//    Derived(b).doMyOwnPrint()  改前
//    Derived(b).print()   改后
    // 输出 hehe33
    // 输出 haha10
}




//
//import kotlin.reflect.KProperty
//// 定义包含属性委托的类
//class Example {
//    var p: String by Delegate()
//}
//
//// 委托的类
//class Delegate {
//    operator fun getValue(thisRef: Any?, property: KProperty<*>): String {
//        println("调用getValue")
//        if(property.equals("Runoob")){
//            return "改变成功"
//        }
//        return "$thisRef, 这里委托了 ${property.name} 属性"
//    }
//
//    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: String) {
//        println("$thisRef 的 ${property.name} 属性赋值为 $value")
//    }
//}
//fun main(args: Array<String>) {
//    val e = Example()
//    println(e.p)     // 访问该属性，调用 getValue() 函数
//
//    e.p = "Runoob"   // 调用 setValue() 函数
//    println(e.p)
//}