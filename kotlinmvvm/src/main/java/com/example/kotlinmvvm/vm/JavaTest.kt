package com.example.kotlinmvvm.vm

import io.reactivex.Observable
import io.reactivex.ObservableSource

import java.lang.reflect.Array
import java.util.concurrent.Callable

object JavaTest {

    fun main(args: String) {
        val observable = Observable.defer { Observable.just(5) }
    }

}
