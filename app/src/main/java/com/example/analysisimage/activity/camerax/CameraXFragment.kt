package com.example.analysisimage.activity.camerax

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Layout
import android.util.Rational
import android.util.Size
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import androidx.camera.core.CameraX
import androidx.camera.core.Preview
import androidx.camera.core.PreviewConfig
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.analysisimage.R

class CameraXFragment : Fragment(){

    private lateinit var textureView: TextureView
    private var preview: Preview? =null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val view = inflater.inflate(R.layout.fragment_camerax, container, false)
        textureView = view.findViewById(R.id.texture_view)

        val previewConfig = PreviewConfig.Builder()
            .setLensFacing(CameraX.LensFacing.BACK)
            .setTargetAspectRatio(Rational(1, 1))
            .setTargetResolution(Size(textureView.width,textureView.height)).build()

        preview = Preview(previewConfig)
        preview?.setOnPreviewOutputUpdateListener {
            textureView.surfaceTexture = it.surfaceTexture
        }
        if (context?.let { ContextCompat.checkSelfPermission(it, Manifest.permission.CAMERA) } == PackageManager.PERMISSION_GRANTED){
            takePreView()
        }else{
            activity?.let { ActivityCompat.requestPermissions(it, arrayOf(Manifest.permission.CAMERA),1) }
        }
        return view
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (requestCode == 1){
            if (grantResults!![0] == PackageManager.PERMISSION_GRANTED){
                takePreView()
            }
        }
    }

    fun takePreView(){
        CameraX.bindToLifecycle(this,preview)
    }

}