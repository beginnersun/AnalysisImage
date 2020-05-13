package com.example.base_module.test.java;

import android.widget.TextView;
import com.example.base_module.test.kotlin.Singleton;
import com.example.base_module.test.kotlin.TextBean;
import com.example.base_module.test.kotlin.TextViewUtils;

public class TestJavaUseKotlin {


    private void initInfo(){
        TextView textView = null;
//        TextViewUtils.setTextViewText();
//        TextViewUtils.setTextViewText(textView,"");
        TextBean bean = new TextBean();
        TextBean.message = "";
        Singleton.provider = "fd";
        Singleton.nihao = "C";
        Singleton.nihao = "C";
//        Singleton.nihaoa;
//        Singleton.nihaob;
        Singleton.test();
        TextBean.testJvmStatic();
        TextBean.Companion.testJvmStatic();
    }

    private void setMessage(){

    }

    public static void setTextViewMessage(){

    }

    public static class TextJavaBean{
        private String name;
        public void setMessage(String message){
            this.name = message;
        }
    }
}
