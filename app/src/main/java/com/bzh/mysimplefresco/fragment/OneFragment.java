package com.bzh.mysimplefresco.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.mysimplefresco.R;
import com.bzh.mysimplefresco.activity.BaseActivity;
import com.bzh.mysimplefresco.lib.MySimpleDraweeView;
import com.bzh.mysimplefresco.lib.Utils;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * ========================================================== <br>
 * <b>版权</b>：　　　别志华 版权所有(c) 2015 <br>
 * <b>作者</b>：　　　别志华 biezhihua@163.com<br>
 * <b>创建日期</b>：　15-9-26 <br>
 * <b>描述</b>：　　　<br>
 * <b>版本</b>：　   V1.0 <br>
 * <b>修订历史</b>：　<br>
 * ========================================================== <br>
 */
public class OneFragment extends BaseFragment {

    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Bind(R.id.collapsing_toolbar_layout)
    CollapsingToolbarLayout collapsingToolbarLayout;

    @Bind(R.id.theme_photo)
    MySimpleDraweeView themePhoto;

    @Bind(R.id.photo_1)
    MySimpleDraweeView photo_1;

    @Bind(R.id.photo_2)
    MySimpleDraweeView photo_2;

    @Bind(R.id.photo_3)
    MySimpleDraweeView photo_3;

    public static OneFragment newInstance() {
        OneFragment fragment = new OneFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    int inflateContentView() {
        return R.layout.bzh_ui_one_fragment;
    }

    @Override
    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceState) {
        super.layoutInit(inflater, savedInstanceState);
        ButterKnife.bind(this, getRootView());

        initToolbar();

        photo_1.setWidthAndHeight(1024, 768);
        photo_1.setAutoPlayAnimations(true);
        photo_1.setGifChartIdentify(true);
        photo_1.setDraweeViewUrl("http://git.oschina.net/biezhihua/MyResource/raw/master/25-173250_974.gif");

        photo_2.setWidthAndHeight(1024, 768);
        photo_2.setLongChartIdentify(true);
        photo_2.setDraweeViewUrl("http://git.oschina.net/biezhihua/MyResource/raw/master/3_1024x768.jpg");

        photo_3.setWidthAndHeight(1024, 768);
        photo_3.setNumberChartIdentify(99);
        photo_3.setDraweeViewUrl("http://git.oschina.net/biezhihua/MyResource/raw/master/3_1024x768.jpg");
    }

    private void initToolbar() {
        if (getActivity() != null) {
            themePhoto.setWidthAndHeight(Utils.getScreenWidth(getActivity()), getActivity().getResources().getDimensionPixelSize(R.dimen.theme_photo_height));
        }
        themePhoto.setDraweeViewUrl("http://git.oschina.net/biezhihua/MyResource/raw/master/3_1024x768.jpg");

        if (toolbar != null && getActivity() != null && getActivity() instanceof BaseActivity) {
            final BaseActivity activity = (BaseActivity) getActivity();
            activity.setSupportActionBar(toolbar);
            activity.getSupportActionBar().setDisplayShowHomeEnabled(false);
            activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            toolbar.setNavigationIcon(R.mipmap.ic_arrow_back_white_24dp);
            toolbar.setNavigationOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.finish();
                }
            });
            collapsingToolbarLayout.setTitle("一句话搞定图片显示");
            collapsingToolbarLayout.setExpandedTitleColor(Color.WHITE);//设置还没收缩时状态下字体颜色
            collapsingToolbarLayout.setCollapsedTitleTextColor(Color.WHITE);//设置收缩后Toolbar上字体的颜色
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        ButterKnife.unbind(getRootView());
    }

}
