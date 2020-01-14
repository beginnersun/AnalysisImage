package com.example.base_module.util

import android.content.Context
import android.os.Environment
import android.text.TextUtils
import android.util.Base64
import java.io.*

class FileUtil {

    companion object{

        fun FileToBaseEncode(filepath:String):String{
            if (TextUtils.isEmpty(filepath)) return ""
            var result = ""
            var src = File(filepath)
            if (src.exists()) {
                val bos = ByteArrayOutputStream(src.length().toInt())  //用byteArrayOutputStream 不需要创建任何一个东西去保存数据  他会有自己的输入流
                val bin = BufferedInputStream(FileInputStream(src))
                val buffer = ByteArray(1024)
                var len = bin.read(buffer,0,1024)
                while (len!=-1){
                    bos.write(buffer,0,len)
                    len = bin.read(buffer,0,1024)
                }
                val bye = bos.toByteArray()
                result = String(Base64.encode(bye,Base64.DEFAULT))

            }
            return result
        }

        fun createCameraFile(context: Context, path:String):File{
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)  //获取本地私有目录  不需要权限并且可以通过File直接访问
            if (!dir!!.exists()){
                dir.mkdir()
            }
            val file = File(dir,path)
            if (!file.exists()){
                file.createNewFile()
            }
            return file
        }

        fun createTempFile(context: Context, name:String,formate:String):File{
            val dir = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)  //获取本地私有目录  不需要权限并且可以通过File直接访问
            if (!dir!!.exists()){
                dir.mkdir()
            }
            val file = File(dir, name + formate)
            if(!file.exists()){
                file.createNewFile()
            }
            return file
        }

    }

}