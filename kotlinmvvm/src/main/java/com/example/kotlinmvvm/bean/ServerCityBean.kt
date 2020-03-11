package com.example.kotlinmvvm.bean

import android.text.TextUtils

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
            if (city_level != 0) "${city_level}级" else ""
    val areaName: String get() = if (!TextUtils.isEmpty(region_name)) {"【${region_name}】"} else ""

    val occupyName:String get() = if (!TextUtils.isEmpty(alliance_name)){ "被  $alliance_name  占领"} else ""
}