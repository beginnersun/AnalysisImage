package com.example.analysisimage

import android.os.Environment
import java.io.File

class Constants {

    object APPString{
        val APP_DIRECTORY_ABSOLUTEPATH:String = Environment.getExternalStorageDirectory().absolutePath + File.separator
    }

    companion object URL{
        private const val Root = "https://aip.baidubce.com/rest/2.0/"
        const val ANALYSIS_IMAGE_FOR_PLANT = "${Root}image-classify/v1/plant"
        const val ANALYSIS_IMAGE_DETECT = "${Root}image-classify/v1/object_detect"
        const val ANALYSIS_IMAGE_FOR_CURRENCY = "${Root}image-classify/v1/currency"  //货币识别
        const val ANALYSIS_IMAGE_FOR_ANIMAL = "${Root}image-classify/v1/animal"  //动物识别


        const val GET_KUAISHOU_VIDEO = "https://live.kuaishou.com/rest/wd/live/liveStream/myfollow" //快手关注的视频信息
    }

}