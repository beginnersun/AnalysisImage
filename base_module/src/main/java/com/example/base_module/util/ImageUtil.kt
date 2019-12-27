package com.example.base_module.util

import android.graphics.ImageFormat
import android.graphics.YuvImage
import android.media.Image
import android.util.Log
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream



object ImageUtil{

    fun isSupportImage(image: Image) =
        when(image.format){
            ImageFormat.YUV_420_888 -> true
            ImageFormat.NV21 -> true
            ImageFormat.YV12 -> true
            else -> false
        }

    /**
     *
     */
    fun getDateFromImage(image: Image):ByteArray{
        if(!isSupportImage(image)) throw IllegalArgumentException("can't support convert Image by format = ${image.format}")
        val crop = image.cropRect
        val format = image.format
        val width = image.width //分辨率
        val height = image.height
        val data = ByteArray(width*height * ImageFormat.getBitsPerPixel(format)/8)  //计算出需要多少字节  然后创建对应的ByteArray数组   像素点个数*每个像素点占用的位数/8 = 字节数

        var channelOffset = 0
        var outputStride = 1
        var pixelStride:Int
        var rowStride:Int
        val planes = image.planes
        val rowData = ByteArray(planes[0].rowStride)
        for ((index,plane) in planes.withIndex()){
            when(index){
                0 -> {
                    channelOffset = 0
                    outputStride = 1
                }
                1 ->{
                    channelOffset = width * height + 1
                    outputStride = 2
                }
                2 ->{
                    channelOffset = width * height
                    outputStride = 2
                }
            }
            val buffer = plane.buffer
            pixelStride = plane.pixelStride
            rowStride = plane.rowStride
            val shift = if (index == 0) 0 else 1
            val w = width shr shift
            val h = height shr  shift
            Log.d("处理位置","$index")
            buffer.position(rowStride * (crop.top shr shift) + pixelStride * (crop.left shr shift))
            for (row in 0 until h){
                var length:Int
                if (pixelStride == 1 && outputStride == 1){
                    length = w
                    buffer.get(data,channelOffset,length)
                    channelOffset += length
                } else{
                    length = (w-1)*pixelStride + 1
                    Log.d("读取大小","当前 position = ${buffer.position()} 读取长度 length = $length  pixelStride 大小为 $pixelStride rowData 的长度为 ${rowData.size}")
                    buffer.get(rowData, 0, length)
                    Log.d("读取后rowData变化","${rowData.size}   w = $w   此时channelOffset为 $channelOffset")
                    for (col in 0 until w){
                        if (col*pixelStride < rowData.size && channelOffset < data.size) {
                            data[channelOffset] = rowData[col * pixelStride]
                            channelOffset += outputStride
                        }
                    }
                }
                if (row < h -1){
                    buffer.position(buffer.position() + rowStride - length)
                }
            }
        }
        return data
    }

    fun getJpegImageForYUVData(fileName:String,image:Image):ByteArray{
        val outputStream = FileOutputStream(fileName)
        val rect = image.cropRect
//        val yuvImage = YuvImage(BitmapUtil.rotateYUV420Degree270(getDateFromImage(image),image.height,image
//            .width),ImageFormat.NV21,rect.width(),rect.height(),null)
        val yuvImage = YuvImage(getDateFromImage(image),ImageFormat.NV21,rect.width(),rect.height(),null)
        val bos = ByteArrayOutputStream()
        yuvImage.compressToJpeg(rect,100,bos)
        val jpegByteArray = bos.toByteArray()
        return  jpegByteArray
    }
}