package com.example.base_module.util

import android.annotation.SuppressLint
import java.text.SimpleDateFormat
import java.util.*

object TimeUtil {

    /**
     * 此处的格式为
     * 2019-12-27 10:25:59
     */
    fun getTimeFromString6(timeString:String):Date{
        val simpleFormate = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")
        return simpleFormate.parse(timeString)
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 22:22:22"）
     *
     * @param time
     * @return
     */
    fun getTimeFromStamp6(time: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = java.lang.Long.parseLong(time) * 1000
        @SuppressLint("SimpleDateFormat")
        val sf = SimpleDateFormat("yyyy-MM-dd hh:mm:ss")//这里的格式可换"yyyy年-MM月dd日-HH时mm分ss秒"等等格式
        return sf.format(calendar.time)
    }

    /**
     * 调用此方法输入所要转换的时间戳输入例如（1402733340）输出（"2014-06-14 22:22:22"）
     *
     * @param time
     * @return
     */
    fun getTimeFromStamp3(time: String): String {
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = java.lang.Long.parseLong(time) * 1000
        @SuppressLint("SimpleDateFormat")
        val sf = SimpleDateFormat("yyyy-MM-dd")//这里的格式可换"yyyy年-MM月dd日-HH时mm分ss秒"等等格式
        return sf.format(calendar.time)
    }

    fun getTimeDifferenceWithCurrent(time:String):String{
        val currentTime = System.currentTimeMillis()
        val srcTime =  getTimeFromString6(time).time
        val difference = (currentTime - srcTime) / 1000   //得到秒
        val minute = difference / 60
        val hour = minute / 60
        val day = hour / 24
        val currentCalendar = Calendar.getInstance()
        val oldCalendar = Calendar.getInstance().apply { this.time = getTimeFromString6(time) }
        if (day>=28){
            val month = currentCalendar.get(Calendar.MONTH) -oldCalendar.get(Calendar.MONTH)
            val year = currentCalendar.get(Calendar.YEAR) - oldCalendar.get(Calendar.YEAR)
            if (month > 1){
                if (year > 1){
                    return getTimeFromStamp3(time)   //相差时间过长直接显示年月日
                }else if (year == 0){
                    return "${month}个月前"
                }
            }else if (month < 0){
                if (year > 1){
                    return getTimeFromStamp3(time)   //相差时间过长直接显示年月日
                }
            }else if (month == 0){
                if (year == 0){
                    return "${day}天前"
                }else {
                    return getTimeFromStamp3(time)
                }
            }
        }else if (day > 0){
            return "${day}天前"
        }else if (hour > 0){
            return "${hour}小时前"
        }else if (minute > 0) {
            return "${minute}小时前"
        }
        return getTimeFromStamp6(time)
    }

}