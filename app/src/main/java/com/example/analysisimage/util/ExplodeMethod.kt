package com.example.analysisimage.util

import android.hardware.camera2.CameraCharacteristics
import androidx.annotation.RequiresApi

/**
 * 筛选 相机硬件支持的情况
 * LEVEL_LEGACY  向后兼容模式（没有额外功能）
 * LEVEL_LIMITED 最基本的的功能和部分高级功能（是FULL的子集）
 * LEVEL_FULL 支持对每一帧数据进行控制 支持高速率图片的拍摄
 * LEVEL_3 支持YUV后处理和Raw格式图片拍摄，支持额外的输出流配置
 * LEVEL_EXTERNAL （外接摄像头 与LIMITED蕾西）
 */
@RequiresApi(21)
fun CameraCharacteristics.isHardwareLevelSupported(requiredLevel: Int): Boolean {
    val sortedLevels = intArrayOf(
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LEGACY,
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_LIMITED,
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_FULL,
        CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL_3
    )

    //在扩展函数中 this代表在点左侧传递的 接收者 参数。(就是调用这个方法的对象  eg: cameraMe.isHardwareLevelSupported   this就代表cameraMe
    val deviceLevel = this[CameraCharacteristics.INFO_SUPPORTED_HARDWARE_LEVEL]
    for (sortedLevel in sortedLevels) {
        if (requiredLevel == sortedLevel) {
            return true
        } else if (deviceLevel == sortedLevel) {
            return false
        }
    }
    return false
}

