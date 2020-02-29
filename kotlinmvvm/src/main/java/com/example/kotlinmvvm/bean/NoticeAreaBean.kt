package com.example.kotlinmvvm.bean

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
            if (sb.toString().compareTo(("《率土之滨》")) == 0){
                sb.reverse()
            }else if(value.compareTo('日') == 0){
                date = sb.toString()
                sb.reverse()
            }else if(value.compareTo('点') == 0){
                time = sb.toString()
                sb.reverse()
            }else if (value.compareTo('（') == 0){
                sb.reverse()
            }else if (value.compareTo('区') == 0){
                number = sb.toString()
                sb.reverse()
            }else if (value.compareTo('）') == 0){
                name = sb.toString()
                sb.reverse()
            }
        }
        return AreaInfo(date,time,number,name)
    }
}
data class AreaInfo(val date:String,val time:String,val number:String,val name:String)