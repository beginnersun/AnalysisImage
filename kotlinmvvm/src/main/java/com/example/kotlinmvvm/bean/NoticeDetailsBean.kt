package com.example.kotlinmvvm.bean

data class NoticeDetailsBean(
    val adminid: String,
    val anonymous: String,
    val attachment: String,
    val author: String,
    val authorid: String,
    val bids: Any,
    val dateline: String,
    val dbdateline: String,
    val first: String,
    val groupiconid: String,
    val groupid: String,
    val memberstatus: String,
    val message: String,
    val number: String,
    val pid: String,
    val position: String,
    val replycredit: String,
    val status: String,
    val subject: String,
    val tid: String,
    val username: String,
    val imageHeadUrl: String = "https://mgame-uc.netease.com/avatar.php?uid=$authorid&size=small"
)