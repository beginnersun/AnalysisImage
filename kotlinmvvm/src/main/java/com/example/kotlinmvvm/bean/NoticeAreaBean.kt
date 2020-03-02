package com.example.kotlinmvvm.bean

import android.util.Log

data class NoticeAreaBean(
    val attachment: String,
    val author: String,
    val authorid: String,
    val closed: String,
    val dateline: String,
    val dbdateline: String,
    val dblastpost: String,
    val digest: String,
    val displayorder: String,
    val heatlevel: String,
    val lastpost: String,
    val lastposter: String,
    val moderator_reply: String,
    val new: String,
    val post_message: String,
    val price: String,
    val readperm: String,
    val recommend_add: String,
    val replies: String,
    val replycredit: String,
    val rushreply: String,
    val special: String,
    val status: String,
    val subject: String,
    val thread_thumb: List<Any>,
    val tid: String,
    val typeid: String,
    val views: String
){
    val areaInfo:AreaInfo get() {
        val sb = StringBuffer()
        var date = ""
        var time = ""
        var number = ""
        var name = ""
//        《率土之滨》2月29日晚上9点开服公告（1729区：方领矩步）
        for (value in subject.toCharArray()){
            if (value.compareTo('：') != 0 || value.compareTo('）') == 0){
                sb.append(value)
            }
            Log.e("数据变1",sb.toString())
            if (sb.toString().compareTo(("《率土之滨》")) == 0){
                Log.e("数据变2",sb.toString())
                sb.setLength(0)
                Log.e("数据变3",sb.toString())
            }else if(value.compareTo('日') == 0){
                date = sb.toString()
                Log.e("数据",date)
                sb.setLength(0)
                Log.e("数据变4",sb.toString())
            }else if(value.compareTo('点') == 0){
                time = sb.toString()
                Log.e("数据",time)
                sb.setLength(0)
                Log.e("数据变5",sb.toString())
            }else if (value.compareTo('（') == 0){
                Log.e("数据变6",sb.toString())
                sb.setLength(0)
                Log.e("数据变7",sb.toString())
            }else if (value.compareTo('区') == 0){
                number = sb.toString()
                Log.e("数据",number)
                sb.setLength(0)
                Log.e("数据变8",sb.toString())
            }else if (value.compareTo('）') == 0){
                name = sb.deleteCharAt(sb.length-1).toString()
                Log.e("数据",name)
                Log.e("数据变9",sb.toString())
                sb.setLength(0)
            }
        }
        return AreaInfo(date,time,number,name)
    }
}
data class AreaInfo(val date:String,val time:String,val number:String,val name:String)