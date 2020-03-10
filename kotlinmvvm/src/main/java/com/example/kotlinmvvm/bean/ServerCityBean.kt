package com.example.kotlinmvvm.bean

data class ServerCityBean(
    val alliance_name: String,
    val city_level: Int,
    val city_name: String,
    val dateid: Int,
    val pve_when: String,
    val region_name: String,
    val server: Int,
    val wid: List<Int>
) {
    val level: String
        get() =
            "${city_level}级"
    val areaName: String get() = "【${region_name}】"

    val occupyName:String get() = "$alliance_name  占领"
}