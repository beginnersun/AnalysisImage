package com.example.kotlinmvvm.bean

import android.view.View

data class NoticeBean(
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
    val highlight: String,
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
    val thread_thumb: List<Any>,
    val tid: String,
    val typeid: String,
    val views: String
) {

    val imageHeadUrl: String
        get() = "https://mgame-uc.netease.com/avatar.php?uid=$authorid&size=small"
    val showNew: Int
        get() = if (new.compareTo("1") == 0) View.VISIBLE else View.GONE
    val showImage: Int
        get() = if (status.compareTo("32") == 0) View.VISIBLE else View.GONE
}

data class Reply(
    val author: String,
    val authorid: String,
    val avatar: String,
    val message: String,
    val pid: String
)