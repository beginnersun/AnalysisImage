package com.example.kotlinmvvm.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.kotlinmvvm.bean.NewsBean
import io.reactivex.Flowable

@Dao
interface NewsDao {

    @Query("select * from news")
    fun getAll(): Flowable<List<NewsBean>>

    @Query("select * from news where type = (:type) Limit (:step) Offset (:start) ")
    fun getNewsList(type:String,start:Int,step:Int):List<NewsBean>

    @Insert
    fun insertAll(newsBeans: List<NewsBean>)

    @Insert
    fun insertVarg(vararg newsBeans: NewsBean)

}