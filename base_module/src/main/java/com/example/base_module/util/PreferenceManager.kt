package com.example.base_module.util

import android.content.Context
import kotlin.properties.Delegates
import kotlin.reflect.KProperty

/**
 * 因为主构造方法不能有任何代码 所以如果要初始化某些内容
 * 主构造函数如果有修饰符或者注解的话必须有constructor关键字修饰
 * ru  private (...) 这种就不行必须 private constructor(...)
 *
 *
 * 可以写在init里面
 * constructor 可以省略
 *
 * 如果当前类有基类的话需要把基类加载：号后面 如果当前类没有构造方法
 * eg class PreferenceManager:Father{}  那么他的次构造方法必须
 * constructor(...) :super(基类的构造方法的参数)
 *
 * 如果基类没有构造方法那么
 * 什么都不需要操作只需要在类名后面加:+基类名字
 */
class PreferenceManager private constructor(var context: Context, var father: String) : Father(father) {  //主构造函数

    var a1: String = ""

    /**
     * 次构造函数
     */
    constructor(context: Context, father: String, parent: String) : this(
        context,
        father
    ) {   //如果类有一个主构造函数，每个次构造函数需要委托给主构造函数
        a1 = "2"

    }

    var GET: String = ""

    fun set() {
        father = "!"
        a = "2222"

        val data1 = Data1("123", 12, 2)
        var (a, b, c) = data1
    }

    /**
     * 初始化块中的代码实际上会成为主构造函数的一部分。委托给主构造函数会作为次构造函数的第一条语句，因此所有初始化块中的代码都
     * 会在次构造函数体之前执行。即使该类没有主构造函数，这种委托仍会隐式发生，并且仍会执行初始化块：
     */
    init {
        father = "2"
        GET = "111"
    }

    /**
     * 对于override 修饰的方法本来就是可覆写的   如果想禁止覆写 可以加上fianl那么继承PreferenceManager的类便不能覆写sun方法了
     * override var a:String = "22"   也可以覆盖属性 不过如果属性是var 的那么覆盖的时候只能是var 不能是val  如果是val的话var和val都行
     */
    final override fun sun() {  //
        super.sun()
    }

}

/**
 *
 */
data class Data(val name: String, val age: Int) {

}

/**
 * 作为基类 必须有open修饰   open代表允许一个类  子类化（也就是被继承） 或者方法可被覆盖
 */
open class Father(var name: String) {
    open var a: String = ""
    val a22: String? = null
    open fun sun() {
        a = "ss"
        name = "fda"
    }
}

object PreferenceManagerUtil {
    public fun setToken() {
        SingletonDemo.get()

        var bb: A.BBB = A().BBB("")
    }

    public fun getToken() = {}
}

class SingletonDemo private constructor() {
    companion object {
        private var instance: SingletonDemo? = null
            get() {
                if (field == null) {
                    field = SingletonDemo()
                }
                return field
            }

        @Synchronized
        fun get(): SingletonDemo {
            return instance!!
        }
    }

}

interface aa {

    val pro: Int

}

class A : aa {
    override val pro: Int
        get() = 1
    var ccbb: String = "333"

    inner class BBB(var name: String) {
        fun test() {
            ccbb = "3"
        }
    }

}

data class Data1(var name: String, var age: Int, var l: Int)
/**
 * 幕后属性
 */
class Person2(name:String) {
    private var backingName:String = name   //外面直接访问不到  通过name来访问  backingName就是幕后属性
    var name
        get() = backingName
        set(value) {
            if(value.length > 5 || value.length < 2){
                println("你设置的人名不符合要求")
            }else{
                backingName = value  //对幕后字段赋值  backingName为自定义的幕后字段  也就成为了幕后属性
            }
        }
    var number:String = ""
    get() = "$"
    set(value){
        field = "${value}fd"   //field 为幕后字段
    }
}


sealed class Expr
data class Const(val number: Double) : Expr()
data class Sum(val e1: Expr, val e2: Expr) : Expr()
object NotANumber : Expr()
open class BB : Expr() {
}

fun test(expr: Expr): Int = when (expr) {

    is Const -> 2
    else -> 3
}

fun getDelegate(): Delegate {
    return Delegate()
}

class Delegate {
    operator fun getValue(example: Example, property: KProperty<*>): Any {
        return Delegate()
    }
}

class Example {
    val ok by getDelegate()
}

class C {
    // 私有函数，所以其返回类型是匿名对象类型
    private fun foo() = object {
        val x: String = "x"
    }

    // 公有函数，所以其返回类型是 Any
    fun publicFoo() = object {
        val x: String = "x"
    }

    fun bar() {
        val x1 = foo().x        // 没问题
//        val x2 = publicFoo().x  // 错误：未能解析的引用“x”
    }
}

//
val items = listOf(1, 2, 3, 4, 5)
val ccs = listOf("1","2","3")
fun testListMethodsds(){
    items.reduce(cc)
}
/**
 * 高阶函数   高阶函数的参数的每一个函数都是对象 外加一个闭包   这些函数体内的变量等等访问与内存的分配都会引入运行时开销
 */
fun <T, R> Collection<T>.fold(
    initial: R,
    combine: (acc: R, nextElement: T) -> R
): R {
    var accumulator: R = initial
    for (element: T in this) {
        accumulator = combine(accumulator, element)
    }
    return accumulator
}

val product = items.fold(1, Int::times)  //使用Int 的扩展函数

val max: (a: Int, b: Int) -> Int =     // 函数类型为 (Int,Int) -> Int    意思就是类型为传入两个Int返回一个Int  =号后面是函数的实例化
    fun(c, d): Int {
        return c
    }
//    {
//        a,b -> a
//    }
val cc:(Int,Int) -> Int = fun(a,b):Int{   //当然函数类型也可以不写参数名 只写类型
    return a
}

val acv = items.fold(2, cc)   //也可以传递一个匿名函数 fun(x:Int,y:Int) = x + y



class IntTransformer: (Int) -> Int {     //使用函数类型 作为借口 然后实现方法  比如 继承(Int) -> Int 这个函数类型 然后实现
                                         // (Int) -> Int这个方法 必须覆写invoke这个函数（因为(Int)->Int 要在invoke中执行
    override operator fun invoke(x: Int): Int = TODO()

    val cadd:(Int,Int) -> Int = { i,j -> i+1}
}
val a = { i: Int -> i + 1 } // 推断出的类型是 (Int) -> Int

val intPlus: Int.(Int) -> Int = Int::plus   //接收者类型函数   调用不变 intPlus(2.3) 结果是5   plus 在Int中是将传入的值叠加到当前对象
                                            // 意为将传入的value参数增加到当前对象上  Int.(Int) -> Int intPlus具有接收者类型所以讲(Int)里面的Int作为第一个参数传递
                                            //就好比一个扩展函数 2.plus(3)
var repeatFun: String.(Int) -> String = { times -> this.repeat(times) }
val ccFun :(String,Int) -> String = {cc,time -> cc.repeat(time) }   //相同

fun testEquals(){
    repeatFun = ccFun
}
//times 指的是Int这个参数
//repeatFun("#",2)     "sdf".repeatFun(2)  两种调用方式
// 还可以根据repeatFun.invoke(string,int)调用



// 简写的都是只适用于lambda 表达式
val produce = items.fold(1){acc , e -> acc * e}   //如果函数的最后一个参数是函数的话 可以把函数表达式卸载括号外面
//val produce = items.fold(1){_ , e ->   e}   //如果函数的最后一个参数是函数的话 可以把函数表达式卸载括号外面  下划线代表未使用的参数
fun test(){
    run { println("") }   //如果函数的参数就是一个表达式那么可以省略括号直接把表达式写在花括号里面
}

val abcdef:(Int) -> Int = {  //如果函数参数只有一个那么可以用it来代替唯一一个参数     并且可已不用写return 直接把最后一个表达式当做值返回
    it -> it+1
}

val strings = arrayOf("1","2","3","4")
fun testFun(){
    strings.filter { it.length>5 }.sortedBy { it.length }.map { it.toUpperCase() }  //以上方法的测试
    repeatFun("#",2)
    repeatFun.invoke("#",2)
    "sdf".repeatFun(2)
}


class ABC(val p: Int)
fun teprintln(){
    println(ABC::p)
    println(strings[0])
}
val stringPlus: (String, String) -> String = String::plus


fun testVararg(vararg strings:String){

}
fun testReformat(string:String,ac1:Boolean = true,ac2:Boolean = false){

}
fun testReformat1(string:String,ac1:Boolean = true,ac2:Boolean = false,string2:String){

}
fun foo(cc: Int = 0, bb: Int) {
}

fun foo(vararg strings:String){
    var length = strings[0]
}
fun Test(){
    foo(bb = 1)  //bb = 1  命名参数法
    foo("1","2","3")   //foo(*arrayof("1","2","3"))传入可变数量的参数    foo("1","2","3")也可以直接放入值
}
fun testTestMethod(){
    testVararg(strings = * arrayOf("1","2"))
    testVararg("1","2")
    testReformat("")
    testReformat("1",ac1 = false,ac2=  false)
    testReformat("1",ac2=  false)
    testReformat1("1",ac2 = false,string2 = "")
    testReformat1("1",ac2 = false,ac1= false,string2 = "")
//    testReformat1("1",ac2 = false,ac1= false,"")  这种情况不允许  所以 在有默认值的情况下 最好把带默认值的放到最后

}
class TestCCC{
    infix fun testZhongzhui(str:String){  //infix 必须是成员函数或者扩展函数

    }
}



/**
 * 内联函数  降低程序运行时间   (小知识  在kotlin中函数就是对象)
 *     原因：编译器将使用函数的定义体来替代函数调用语句，这种替代行为发生在编译阶段而非程序运行阶段
 *     人话翻译：因为在调用函数时会有寻找函数的时间消耗，创建相关对象的空间消耗 所以为了减少这些消耗这个过程被优化为将调用的函数
 *     体直接放到调用的位置减少消耗。  eg:
 *     fun test(){
 *         执行语句2
 *     }
 *         执行语句1
 *         test()
 *         执行语句3
 *     如果test为inline函数就会变为
 *         执行语句1
 *         执行语句2
 *         执行语句3
 *     PS 1、这些变化是在编译器发生的
 *        2、如果该内联函数有函数参数 那么该函数参数也是内联函数（如果想禁用可以用 noinline 标记代表非内联函数 因为）
 */

//return 在内联函数中的区别
fun returnTest(strings:Array<String>){
    go (strings[0]) {
        if (it.length>2) it.length else return@go 2
    }
    printTest (strings[0]){if (it.length > 6) println("长字符串") else return }   //此时return 可以不加标签
}
inline fun printTest(string:String,f:(String)->Unit){  //内联函数   参数传入lambda表达式 此lambda表达式可以 直接使用return
    f(string)
}
//inline fun printTest(string:String,noinline f:(String)->Unit){  //内联函数   如果加了noinline的话也不可以直接return 了 但是如果仅仅是为了禁止局部返回的话可以使用crossinline标志
//    f(string)
//}
inline fun asss(f:(String)->String):String{
    return f("")
}

fun go(string:String,f:(String) -> Int){   //非内联函数  参数lambda表达式不可直接使用内联函数
    println(f(string))
}

// 将内联函数用在属性的set与get方法上
class InLineClass{
    var name:String = ""
        set(value) { field = "${value}" }   //field 为幕后字段
        get() = String()

    inline var number:String
        get() = String()
        set(value){
            this.name = value+""
        }
//    inline var number:String = ""
//    set(value) {
//        "{$value}的长度为{${value.length}}"
//    }
}

class Address(province:String,city:String){
    var province = province
    var city = city
}
class Person3 {
    var address:String? = null

    val personProvince:Address
        inline get() = Address("河南","")

    var personCity:Address
        get() = Address("","郑州")
        inline set(value) {
            this.address = value.city
        }

    //inline 修饰属性本身，表明读取和设置属性都会内联化
    inline var fullAddress:Address
        get() = Address("广东","广州")
        set(value) {
//            this.address = value.city+value.province
        }

}
/**
 * 委托
 *   1、类委托
 *   2、委托属性
 */
/**
 * 1、类委托   （相当于Java中的代理）
 */
interface Base {
    fun printMessage()
    fun printMessageLine()
}

class BaseImpl(val x: Int) : Base {
    override fun printMessage() { print(x) }
    override fun printMessageLine() { println(x) }
}

class Derived(b: Base) : Base by b {
    override fun printMessage() { print("abc") }
}

fun entrust() {
    val b = BaseImpl(10)
    Derived(b).printMessage()
    Derived(b).printMessageLine()
}
/**
 * 2、委托属性
 *    格式val/var <属性名>: <类型> by <表达式>
 *    1、lazy
 *    2、observable
 *    3、map
 *
 * ps：by背后隐藏的内容     by 后面的表达式所返回的值需要实现getValue与setValue（val不需要）函数
 *                            println(TestTrust().a)使用该委托属性时会调用getValue方法
 *                            println(TestTrust().a = ...)设置值时会调用setValue方法(此时行不通因为我们例子中a为val)
 *     调用函数时返回的值即函数返回的类型 如lazy 函数返回Lazy<T>类型  然后当调用a时就会去执行Lazy的getValue方法
 *  2.1局部委托属性（1.1之后支持）
 */
class TestTrust(val map:Map<String,Any?>){
    val a by lazy(){ //默认传入的参数为SYNCHRONIZED
        print("默认只调用一次")
        "5"       //最后一行 为返回值
    }

    //可观察属性 传入的函数在赋值后执行
    val b by Delegates.observable("没有值那就先给个初始值"){
        property, oldValue, newValue ->
        println("the property is ${property.name}  old = $oldValue   ->     new_tag = $newValue")
    }
    // 可修改  传入的函数用来判断是否修改值
    val b_1:String by Delegates.vetoable("继续给个初始值"){
        property, oldValue, newValue ->
        newValue.length > 5
    }

    val name:String by map
    val age:Int by map
}

fun testTrustMethod(){  //测试委托之映射
    val test = TestTrust(mapOf("name" to "salier","age" to 25))
    println("${test.name} is ${test.age}'s old!")
}



/**
 * 集合
 */
interface Bean{

}
class BeanA:Bean{

}
class BeanB:Bean{

}
var beansList = listOf<Bean>()
var AList = ArrayList<Bean>(3)
val sourceList = mutableListOf(1, 2, 3)
fun testList(){
    AList.add(BeanA())
    AList.add(BeanB())
    sourceList.add(1)
}



