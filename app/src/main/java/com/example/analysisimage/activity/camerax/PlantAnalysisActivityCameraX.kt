package com.example.analysisimage.activity.camerax

import android.os.Bundle
import android.os.PersistableBundle
import android.view.TextureView
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import com.example.analysisimage.R

class PlantAnalysisActivityCameraX:AppCompatActivity() {

    private lateinit var textureView:TextureView
    private var preview:Preview? =null

    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        setContentView(R.layout.activity_camerax)
        textureView = findViewById(R.id.texture_view)

        preview = getDefaultPreViewConfig()
        preview?.setOnPreviewOutputUpdateListener { output: Preview.PreviewOutput? ->

        }

    }


    fun getDefaultPreViewConfig():Preview{
        val previewConfigBuilder = PreviewConfig.Builder()
        return Preview(previewConfigBuilder.build())
    }

}