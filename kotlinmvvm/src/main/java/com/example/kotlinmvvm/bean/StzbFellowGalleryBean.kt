package com.example.kotlinmvvm.bean

data class StzbFellowGalleryBean(
    val attachment: String,
    val author: String,
    val authorid: String,
    val closed: String,
    val cover: Cover,
    val coverpath: String,
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
    val price: String,
    val readperm: String,
    val recommend_add: String,
    val replies: String,
    val reply: List<Reply>,
    val replycredit: String,
    val rushreply: String,
    val special: String,
    val status: String,
    val subject: String,
    val thread_thumb: List<String>,
    val tid: String,
    val typeid: String,
    val views: String
){
    var scale:Float = 0f
}

data class Cover(
    val h: String,
    val w: String
)
