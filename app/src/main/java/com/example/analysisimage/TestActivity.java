package com.example.analysisimage;

import android.widget.ImageView;
import com.bumptech.glide.Glide;
import com.example.analysisimage.base.BaseActivity;
import com.example.analysisimage.util.TextureMeteringPointFactoryKt;

//import static com.example.analysisimage.util.TextureMeteringPointFactoryKt.testAAVV;

public class TestActivity extends BaseActivity {
    @Override
    public void initData() {

    }

    @Override
    public void initView() {

        int a = getResources().getDisplayMetrics().widthPixels << 4;
//        ImageView(this)

    }

    public static void fdfd(int a /* canshu a */,int b /* 参数b */){
        System.out.println(a + b);
        TextureMeteringPointFactoryKt.testAAVV();

    }

    @Override
    public int getLayoutId() {
        return R.layout.activity_test;
    }
}
