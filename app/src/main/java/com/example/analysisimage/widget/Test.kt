package com.example.analysisimage.widget

import io.reactivex.Observable
import org.reactivestreams.Subscriber
import org.reactivestreams.Subscription

class Test {

    private fun fasdf() {
        val a = 3 shr 1
        println(a)
        val c = object : Subscriber<String>{
            override fun onComplete() {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onSubscribe(s: Subscription?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onNext(t: String?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

            override fun onError(t: Throwable?) {
                TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
            }

        }
//        Observable.just("123").subscribe(c)
    }
}
