package com.example.analysisimage.util

import android.content.Context

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
class PreferenceManager constructor(var context: Context,var father:String):Father(father){  //主构造函数

    var a1 :String =""
    /**
     * 次构造函数
     */
    constructor(context: Context,father: String,parent:String):this(context,father){   //如果类有一个主构造函数，每个次构造函数需要委托给主构造函数
        a1 = "2"

    }

    var GET:String = ""

    fun set(){
        father = "!"
         a = "2222"
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
data class Data  (val name:String,val age:Int){

}

/**
 * 作为基类 必须有open修饰   open代表允许一个类  子类化（也就是被继承） 或者方法可被覆盖
 */
open class Father(var name:String){
    open var a:String = ""
    val a22:String?= null
    open fun sun(){
        a = "ss"
        name = "fda"
    }
}

object PreferenceManagerUtil{
    public fun setToken(){
        SingletonDemo.get()

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
        fun get(): SingletonDemo{
            return instance!!
        }
    }

}
interface aa{

    val pro:Int

}

class A :aa{
    override val pro: Int
        get() = 1
}