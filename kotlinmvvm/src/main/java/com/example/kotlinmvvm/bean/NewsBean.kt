package com.example.kotlinmvvm.bean

data class NewsBean(
    val commentCount: Int,
    val digest: String,
    val docid: String,
    val imgextra: List<Imgextra>,
    val imgsrc: String,
    val imgsrc3gtype: String,
    val liveInfo: Any,
    val modelmode: String,
    val photosetID: String,
    val priority: Int,
    val ptime: String,
    val skipType: String,
    val skipURL: String,
    val source: String,
    val stitle: String,
    val title: String,
    val url: String
)

data class Imgextra(
    val imgsrc: String
)