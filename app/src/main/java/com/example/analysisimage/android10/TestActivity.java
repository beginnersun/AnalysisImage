package com.example.analysisimage.android10;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.analysisimage.R;
import com.example.analysisimage.base.BaseActivity;

import java.io.*;

/**
 * 公开目录    外部存储(类似SD的位置) 与 内部存储(手机本身存储位置)
 * 私有目录    外部存储 与 内部存储
 */
public class TestActivity extends BaseActivity implements View.OnClickListener{
    TextView first;
    Button second;
    ImageView imageView;

    @Override
    public void initData() {

    }

    @Override
    public void initView() {
        first = findViewById(R.id.first);
        second = findViewById(R.id.second);
        imageView = findViewById(R.id.imageView);
        testReadOrWriteFile();

        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},1);

        try {
            String path = Android10FileUtil.saveBitmap(this,BitmapFactory.decodeResource(getResources(),R.mipmap.server_bg),"ccbb.png", Android10FileUtil.FileType.Image);
            Log.e("图片保存位置",path);
        } catch (IOException e) {
            e.printStackTrace();
        }


        first.setOnClickListener(this);
        second.setOnClickListener(this);
        findViewById(R.id.third).setOnClickListener(this);
    }

    public void testReadOrWriteFile(){
//      (外部与内部媒体文件私有目录）
        File files[] = getExternalMediaDirs();
        if (files!=null){
            for (File file:files){
                Log.e("TestAndroid10————ExternalMediaDirs()",file.getAbsolutePath());
            }
        }

        File Infile = getFilesDir();
        if (Infile!=null){
            Log.e("TestAndroid10 ----InFile",Infile.getAbsolutePath());
        }
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
        File filesCache[] = getExternalCacheDirs();
        if (filesCache!=null){
            for (File file:filesCache){
                Log.e("TestAndroid10————ExternalCacheDirs()",file.getAbsolutePath());
            }
        }
        File externalCacheDir = getExternalCacheDir();
        if (externalCacheDir != null){
            Log.e("TestAndroid10————ExternalCacheDir()",externalCacheDir.getAbsolutePath());
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
                Log.e("TestAndroid10————ExternalFilesDirs()",file.getAbsolutePath());
            }
        }
        File externalFile = getExternalFilesDir(Environment.MEDIA_MOUNTED_READ_ONLY);
        if (externalFile != null){
            Log.e("TestAndroid10————ExternalFile()",externalFile.getAbsolutePath());
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
                GETCONTENT();
                break;
            case R.id.second:
                OpenDocument();
                break;
            case R.id.third:
                OpenDocumentAndDelete();
                break;
        }
    }

    private static int REQUEST_IMAGE_GET = 3;
    private static int REQUEST_IMAGE_OPEN = 2;
    private static int REQUEST_IMAGE_OPEN_DELETE = 4;
    private static int REQUEST_DOCUMENT = 1;

    public void OpenDocument(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN);
    }

    public void CreateDocument(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_DOCUMENT);
    }

    public void GETCONTENT(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    public void OpenDocumentAndDelete(){
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // Only the system receives the ACTION_OPEN_DOCUMENT, so no need to test.
        startActivityForResult(intent, REQUEST_IMAGE_OPEN_DELETE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_GET && resultCode == RESULT_OK) {
//            Bitmap thumbnail = data.getParcelable("data");
            Uri fullPhotoUri = data.getData();
            Log.e("uri地址1",fullPhotoUri.toString());
            try {
                imageView.setImageBitmap(getBitmapFromUri(fullPhotoUri));
            } catch (IOException e) {
                Log.e("uri地址1","失败");
                e.printStackTrace();
            }
            // Do work with photo saved at fullPhotoUri
        }
        if (requestCode == REQUEST_IMAGE_OPEN && resultCode == RESULT_OK){

            Uri fullPhotoUri = data.getData();
            Log.e("uri地址2",fullPhotoUri.toString());
        }
        if (requestCode == REQUEST_DOCUMENT && resultCode == RESULT_OK){
            alterDocument(data.getData());
        }
        if (requestCode == 33 && resultCode == RESULT_OK){
            alterDocument(data.getData());
        }
        if (requestCode == REQUEST_IMAGE_OPEN_DELETE && resultCode == RESULT_OK){
            deleteFile(data.getData());
        }
    }
    private Bitmap getBitmapFromUri(Uri uri) throws IOException {
        ParcelFileDescriptor parcelFileDescriptor =
                getContentResolver().openFileDescriptor(uri, "r");
        FileDescriptor fileDescriptor = parcelFileDescriptor.getFileDescriptor();
        Bitmap image = BitmapFactory.decodeFileDescriptor(fileDescriptor);
        parcelFileDescriptor.close();
        return image;
    }

    private void readTextFromUri(Uri uri){
        StringBuilder builder = new StringBuilder();
        InputStream is = null;
        try {
            is = getContentResolver().openInputStream(uri);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line = null;
            while((line = reader.readLine())!= null){
                builder.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        Log.e("读取内容是",builder.toString());
//        Environment.getExternalStorageDirectory()
    }

    public void createFile() {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        // 文件类型
        intent.setType("text/plain");
        // 文件名称
        intent.putExtra(Intent.EXTRA_TITLE, "226655" + ".txt");
        startActivityForResult(intent, 33);
    }

    public void deleteFile(Uri uri){
        try {
            DocumentsContract.deleteDocument(getContentResolver(),uri);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void alterDocument(Uri uri) {
        try {
            ParcelFileDescriptor pfd = getContentResolver().
                    openFileDescriptor(uri, "w");
            FileOutputStream fileOutputStream =
                    new FileOutputStream(pfd.getFileDescriptor());
            fileOutputStream.write(("Overwritten by MyCloud at " +
                    System.currentTimeMillis() + "\n").getBytes());
            // Let the document provider know you're done by closing the stream.
            fileOutputStream.close();
            pfd.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
