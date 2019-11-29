package com.example.analysisimage

import android.os.Environment
import java.io.File

class Constants {

    object APPString{
        val APP_DIRECTORY_ABSOLUTEPATH:String = Environment.getExternalStorageDirectory().absolutePath + File.separator
    }

    companion object URL{
        const val ANALYSIS_IMAGE_FOR_PLANT = "https://aip.baidubce.com/rest/2.0/image-classify/v1/plant"
        const val ANALYSIS_IMAGE_FOR_CURRENCY = "https://aip.baidubce.com/rest/2.0/image-classify/v1/currency"  //货币识别
        const val ANALYSIS_IMAGE_FOR_ANIMAL = "https://aip.baidubce.com/rest/2.0/image-classify/v1/animal"  //动物识别
    }

}