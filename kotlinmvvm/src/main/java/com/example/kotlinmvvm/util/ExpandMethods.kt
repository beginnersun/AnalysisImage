package com.example.kotlinmvvm.util

import android.text.TextUtils
import android.widget.EditText
import android.widget.TextView
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import org.json.JSONObject
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.asyncTask(delay: Long = 0): Observable<T> =
    this.subscribeOn(Schedulers.io()).delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())


fun <T> Single<T>.asyncTask(delay: Long = 0): Single<T> =
    this.subscribeOn(Schedulers.io()).delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())

fun  JSONObject.optStringNotNull(key:String):String{
    var value = this.optString(key)
    return if (TextUtils.isEmpty(value) || TextUtils.equals(value,"null")) "" else{ value }
}
fun TextView.isEmpty():Boolean{
    return TextUtils.isEmpty(this.text) || TextUtils.equals(this.text.trim(),"")
}
fun TextView.setNoTrimText(text:String){
    if (TextUtils.isEmpty(this.text) || TextUtils.equals(this.text.trim(),"null")){
        this.text = ""
    }else{
        this.text = text
    }
}

