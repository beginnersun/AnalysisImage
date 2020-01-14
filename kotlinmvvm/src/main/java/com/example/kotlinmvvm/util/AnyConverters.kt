package com.example.kotlinmvvm.util

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import org.json.JSONObject

class AnyConverters {

    @TypeConverter
    fun stringToList(value:String):List<Any>{
        val listType = object :TypeToken<Any>(){}.type
        return Gson().fromJson(value,listType)
    }

    @TypeConverter
    fun listToString(list:List<Any>):String{
        return Gson().toJson(list)
    }

    @TypeConverter
    fun jsonToString(json:JSONObject):String = json.toString()

    @TypeConverter
    fun stringToJson(value:String):JSONObject = JSONObject(value)

}