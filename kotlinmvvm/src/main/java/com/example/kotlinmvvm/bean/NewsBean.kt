package com.example.kotlinmvvm.bean

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.kotlinmvvm.util.AnyConverters


@Entity(tableName = "news")
@TypeConverters(AnyConverters::class)
data class NewsBean(
    @PrimaryKey val docid: String,
    @ColumnInfo(name = "commentCount") val commentCount: Int,
    @ColumnInfo(name = "digest") val digest: String,
    @ColumnInfo(name = "imgextra") val imgextra: List<Imgextra>,
    @ColumnInfo(name = "imgsrc") val imgsrc: String,
    @ColumnInfo(name = "imgsrc3gtype") val imgsrc3gtype: String,
    @ColumnInfo(name = "") val liveInfo: Any,
    val modelmode: String,
    val photosetID: String,
    val priority: Int,
    val ptime: String,
    val skipType: String,
    val skipURL: String,
    val source: String,
    val stitle: String,
    val title: String,
    val url: String,
    val statusCode: Int,
    val message: String,
    var type:String
)

data class Imgextra(
    val imgsrc: String
)