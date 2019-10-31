package com.example.analysisimage.util

import android.os.Build
import android.text.TextUtils
import android.util.Base64
import java.io.File
import java.io.FileInputStream

class FileUtil {

    companion object{

        fun FileToBaseEncode(filepath:String):String{
            if (TextUtils.isEmpty(filepath)) return ""
            var result = ""
            var src = File(filepath)
            if (src.exists()) {
                val input = FileInputStream(src)
                var byteArray = ByteArray(input.available())
                input.read(byteArray)
                byteArray = String(byteArray).replaceFirst("data:image/jpeg;base64,","").toByteArray()
                result = Base64.encodeToString(byteArray,Base64.DEFAULT)
            }
            return result
        }

    }

}