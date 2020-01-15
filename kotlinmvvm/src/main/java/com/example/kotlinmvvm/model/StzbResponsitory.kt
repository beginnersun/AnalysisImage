package com.example.kotlinmvvm.model

import com.example.base_module.util.NetWorkState
import com.example.kotlinmvvm.bean.VideoBean
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class StzbResponsitory(val stzbService: StzbService) {

    suspend fun getStzbVideoInfo(page: Int, size: Int): List<VideoBean> =
        withContext(Dispatchers.IO) {
            if (NetWorkState.isConnected()) {
                return@withContext stzbService.getStzbVideoInfo(page, size)
            }
            return@withContext mutableListOf<VideoBean>()
        }!!


}