package com.example.analysisimage.android10;

import android.content.ContentValues;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.RequiresApi;
import androidx.exifinterface.media.ExifInterface;

import java.io.*;

/**
 * 兼容Android10的工具软件
 */
public class Android10FileUtil {

    public enum FileType {
        Image(Environment.DIRECTORY_PICTURES, 1),
        Video(Environment.DIRECTORY_MOVIES, 2),
        Audio(Environment.DIRECTORY_MUSIC, 3),
        Other(Environment.DIRECTORY_DCIM, 4);
        private String path;
        private int id;

        FileType(String path, int id) {
            this.path = path;
            this.id = id;
        }

        public String getPath() {
            return path;
        }

        public int getId() {
            return id;
        }
    }

    /**
     * 将图片保存到本地
     * @param context
     * @param bitmap
     * @param fileName
     * @param type
     * @return  返回的是Uri的path  因为对于公共目录在AndroidQ以后不允许通过File直接访问
     */
    public static String saveBitmap(Context context, Bitmap bitmap, String fileName, FileType type) throws IOException {
        String path = "";
        ContentValues values = new ContentValues();
        values.put(MediaStore.Audio.Media.DISPLAY_NAME, fileName);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
//            values.put(MediaStore.Images.Media.RELATIVE_PATH, type.getPath() + File.separator + fileName);
        } else {
            values.put(MediaStore.Images.Media.DATA, type.getPath() + File.separator + fileName);
        }
        values.put(MediaStore.Images.Media.MIME_TYPE, "image/JPEG");
        Uri uri = context.getContentResolver().insert(MediaStore.Images.Media.INTERNAL_CONTENT_URI,values);
        if (uri != null){
            OutputStream outputStream = context.getContentResolver().openOutputStream(uri);
            if (outputStream!=null){
                bitmap.compress(Bitmap.CompressFormat.JPEG,90,outputStream);
                outputStream.flush();
                outputStream.close();
                path = uri.getPath();
            }
        }
        return path;
    }

    public static String saveVideo(){
        String path = "";

        return path;
    }


    /**
     * 获取图片中的位置信息
     * @param context 上下文
     * @param uri 图片对应的Uri
     * @return
     */
    @RequiresApi(api = 29)
    public static double[] getExifInterFaceInUri(Context context, Uri uri){
        uri = MediaStore.setRequireOriginal(uri);
        InputStream stream;
        try {
            stream = context.getContentResolver().openInputStream(uri);
            if (stream!=null) {
                ExifInterface exifInterface = new ExifInterface(stream);
                double LatLong[] = exifInterface.getLatLong();
                stream.close();
                return LatLong;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new double[2];
    }

}
