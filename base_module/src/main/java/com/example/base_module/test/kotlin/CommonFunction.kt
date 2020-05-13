@file:JvmName("TextViewUtils")
@file:JvmMultifileClass
package com.example.base_module.test.kotlin
import android.widget.TextView

fun setTextViewText(textView:TextView,message:String){
    textView.text = message
}
fun initInfo(){
    var textView:TextView? = null
    textView?.let { setTextViewText(textView,"fd") }
}

/**
 *
 */
class TextBean{
    var name:String =""

    fun setMessage(message:String){
        this.name = message
    }

    companion object{
        @JvmField var message:String = "static"
        @JvmStatic fun testJvmStatic(){
            println("mfasd")
        }
        fun testNoJvmStatic(){

        }

    }
}
object Singleton {
    var normalMesage = ""
    lateinit var provider: String
    @JvmField var nihao:String = ""
    @JvmField val nihaoa:String = ""
    const val nihaob:String = ""

    @JvmStatic
    fun test(){}

}