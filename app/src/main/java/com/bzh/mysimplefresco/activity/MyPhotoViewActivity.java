package com.bzh.mysimplefresco.activity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import com.bzh.mysimplefresco.MyPhotoView;
import com.bzh.mysimplefresco.R;
import com.facebook.imagepipeline.common.ResizeOptions;

import java.io.IOException;
import java.io.InputStream;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ========================================================== <br>
 * <b>版权</b>：　　　音悦台 版权所有(c) 2015 <br>
 * <b>作者</b>：　　　别志华 biezhihua@163.com<br>
 * <b>创建日期</b>：　2015/11/16 17:39 <br>
 * <b>描述</b>：　　　<br>
 * <b>版本</b>：　   V1.0 <br>
 * <b>修订历史</b>：　<br>
 * ========================================================== <br>
 */
public class MyPhotoViewActivity extends BaseActivity {

    @Bind(R.id.myPhotoView)
    MyPhotoView myPhotoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.photoview_activity);
        ButterKnife.bind(this);

        final int widthPixels = getResources().getDisplayMetrics().widthPixels;
        final int heightPixels = getResources().getDisplayMetrics().heightPixels;

        try {
            InputStream open = getAssets().open("aa.jpg");
            Bitmap bitmap = BitmapFactory.decodeStream(open);
//            myPhotoView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
            myPhotoView.setImageBitmap(bitmap);
            myPhotoView.setImageUri("http://img5.duitang.com/uploads/item/201511/04/20151104214718_FfnST.jpeg"
                    , null);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
}
