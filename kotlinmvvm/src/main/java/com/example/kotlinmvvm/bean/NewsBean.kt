package com.example.kotlinmvvm.bean

import android.view.View
import androidx.room.*
import com.example.kotlinmvvm.util.AnyConverters
import java.util.List

@Fts4
@Entity(tableName = "news")
@TypeConverters(AnyConverters::class)
class NewsBean(
    @PrimaryKey(autoGenerate = true) @ColumnInfo(name = "rowid") val id: Int,  //为了支持全文搜索 必须有rowid且是Int类型 作为主键  autoGenerate 让id自增
    @ColumnInfo(name = "docid") val docid: String,
    @ColumnInfo(name = "commentCount") val commentCount: Int,
    @ColumnInfo(name = "digest") val digest: String,
    @ColumnInfo(name = "imgextra") val imgextra: ArrayList<Imgextra>,
    @ColumnInfo(name = "imgsrc") val imgsrc: String,
    @ColumnInfo(name = "imgsrc3gtype") val imgsrc3gtype: String,
    @Ignore val liveInfo: String,
    @ColumnInfo(name = "modelmode") val modelmode: String,
    @ColumnInfo(name = "photosetID") val photosetID: String,
    @ColumnInfo(name = "priority") val priority: Int,
    @ColumnInfo(name = "ptime") val ptime: String,
    @ColumnInfo(name = "skipType") val skipType: String,
    @ColumnInfo(name = "skipURL") val skipURL: String,
    @ColumnInfo(name = "source") val source: String,
    @ColumnInfo(name = "stitle") val stitle: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "url") val url: String,
    @Ignore val statusCode: Int,
    @Ignore val message: String,
    @ColumnInfo(name = "type") var type: String
) {

    val showOne:Int
    get() = if (imgsrc3gtype.compareTo("1") == 0) View.VISIBLE else View.GONE

    val showThree:Int
        get() = if (imgsrc3gtype.compareTo("2") == 0) View.VISIBLE else View.GONE

}

data class Imgextra(
    val imgsrc: String
)