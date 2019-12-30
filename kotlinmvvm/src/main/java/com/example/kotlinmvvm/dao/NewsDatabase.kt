package com.example.kotlinmvvm.dao

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.kotlinmvvm.bean.NewsBean

@Database(entities = [NewsBean::class],version = 1)
abstract class NewsDatabase:RoomDatabase() {

    abstract fun newsDao():NewsDao

    companion object{
        private val instance by lazy {

        }
    }

}