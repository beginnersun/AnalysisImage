package com.example.kotlinmvvm.util

import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.asyncTask(delay: Long = 0): Observable<T> =
    this.subscribeOn(Schedulers.io()).delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())


fun <T> Single<T>.asyncTask(delay: Long = 0): Single<T> =
    this.subscribeOn(Schedulers.io()).delay(delay, TimeUnit.MILLISECONDS).observeOn(AndroidSchedulers.mainThread())