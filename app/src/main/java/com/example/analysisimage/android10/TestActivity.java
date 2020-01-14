package com.example.analysisimage.android10;

import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import com.example.analysisimage.R;
import com.example.analysisimage.base.BaseActivity;

import java.io.File;

public class TestActivity extends BaseActivity implements View.OnClickListener{
    TextView first;


    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        first = findViewById(R.id.first);
        testReadOrWriteFile();

        first.setOnClickListener(this);
    }

    public void testReadOrWriteFile(){
//        获取外部媒体文件所在目录
//        File files[] = getExternalMediaDirs();
//        if (files!=null){
//            for (File file:files){
//                Log.e("TestAndroid10————MediaDirs()",file.getAbsolutePath());
//            }
//        }
//
//        获取Obb文件所在目录
//        files = getObbDirs();
//        if (files!=null){
//            for (File file:files){
//                Log.e("TestAndroid10————ObbDirs()",file.getAbsolutePath());
//            }
//        }
//        File obbDir = getObbDir();
//        if (obbDir != null){
//            Log.e("TestAndroid10————ObbDir()",obbDir.getAbsolutePath());
//        }
        /**
         * 对应缓存数据文件  清楚缓存按钮即可清楚
         */
        File files[] = getExternalCacheDirs();
        if (files!=null){
            for (File file:files){
                Log.e("TestAndroid10————CacheDirs()",file.getAbsolutePath());
            }
        }
        File externalCacheDir = getExternalCacheDir();
        if (externalCacheDir != null){
            Log.e("TestAndroid10————cacheDir()",externalCacheDir.getAbsolutePath());
        }
        File cacheDir = getCacheDir();
        if (cacheDir != null){
            Log.e("TestAndroid10————cacheDir()",cacheDir.getAbsolutePath());
        }
        /**
         * 应用私有文件  在AndroidQ中访问不需要权限 随着APP卸载而删除
         */
        files = getExternalFilesDirs(Environment.DIRECTORY_MOVIES);
        if (files!=null){
            for (File file:files){
                Log.e("TestAndroid10————FilesDirs()",file.getAbsolutePath());
            }
        }
        File externalFile = getExternalFilesDir(Environment.MEDIA_MOUNTED_READ_ONLY);
        if (externalFile != null){
            Log.e("TestAndroid10————externalFile()",externalFile.getAbsolutePath());
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.first:
                break;
        }
    }


}
