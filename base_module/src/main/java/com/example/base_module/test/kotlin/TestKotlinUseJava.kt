package com.example.base_module.test.kotlin

import com.example.base_module.test.java.TestJavaUseKotlin

class TestKotlinUseJava {
    fun testFun(){
        val bean = TestJavaUseKotlin.TextJavaBean()
        bean.setMessage("")
    }

}