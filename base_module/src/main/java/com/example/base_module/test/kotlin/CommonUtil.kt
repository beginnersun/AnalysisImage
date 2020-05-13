@file:JvmName("TextViewUtils")
@file:JvmMultifileClass
package com.example.base_module.test.kotlin
import android.widget.TextView

fun setText(textView:TextView,message:String){
    textView.text = message
}
fun initMessage(){
    var textView:TextView? = null
    textView?.let { setTextViewText(textView,"fd") }
}