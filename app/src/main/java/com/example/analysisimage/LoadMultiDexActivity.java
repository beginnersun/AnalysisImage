package com.example.analysisimage;

import android.graphics.Bitmap;

import android.graphics.Point;
import androidx.collection.LruCache;

import com.example.analysisimage.base.BaseActivity;
import com.example.base_module.util.BitmapUtil;

public class LoadMultiDexActivity extends BaseActivity {

    class TestCC extends Test{

    }

    @Override
    public void initData() {
        int a = TestCC.a;

    }

    @Override
    public void initView() {
        String.class.getClassLoader();
        Point p1 = new Point();
        try {
            p1.getClass().getField("a");
            p1.getClass().getDeclaredField("a");
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
        int max = (int) (Runtime.getRuntime().maxMemory() / 1024);
        int cacheSize = max / 8;
        new LruCache<String, Bitmap>(cacheSize){
            /**
             * 计算一个元素的缓存大小
             * @param key
             * @param value
             * @return
             */
            @Override
            protected int sizeOf(String key, Bitmap value) {
                return BitmapUtil.INSTANCE.getBitmapSize(value);
            }
        };

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_load_multidex;
    }
}
