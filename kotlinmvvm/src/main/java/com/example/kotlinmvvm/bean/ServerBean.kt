package com.example.kotlinmvvm.bean

data class ServerBean(
    val cfg_db_id: Int,
    val map_width: Int,
    val name: String,
    val server_id: Int
) {
    val serverName: String
        get() =
            when (name.toCharArray()[0]) {
                'S' -> " "
                'X' -> " "
                else -> name.split("_")[1]
            }

    val serverNum: String
        get() = when (name.toCharArray()[0]) {
            'S' -> name
            'X' -> name
            else -> name.split("_")[0]
        }
}