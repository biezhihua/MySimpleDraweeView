package com.bzh.mysimplefresco.fragment;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.bzh.mysimplefresco.activity.BaseActivity;

import java.nio.charset.IllegalCharsetNameException;

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
public abstract class BaseFragment extends Fragment {

    public static final String FRAGMENT_TAG = "FRAGMENT_TAG";

    private ViewGroup rootView;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        if (context instanceof Activity && (Activity) context instanceof BaseActivity) {
            ((BaseActivity) context).addFragment(toString(), this);
        }

    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (getActivity() != null && getActivity() instanceof BaseActivity) {
            ((BaseActivity) getActivity()).removeFragment(this.toString());
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        if (inflateContentView() > 0) {
            rootView = (ViewGroup) inflater.inflate(inflateContentView(), null);
            rootView.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            _layoutInit(inflater, savedInstanceState);

            layoutInit(inflater, savedInstanceState);

            return rootView;
        }
        return super.onCreateView(inflater, container, savedInstanceState);
    }


    protected void _layoutInit(LayoutInflater inflater, Bundle savedInstanceState) {
    }

    protected void layoutInit(LayoutInflater inflater, Bundle savedInstanceState) {
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState == null) {
            requestData();
        }
    }

    void requestData() {


    }

    abstract int inflateContentView();

    public ViewGroup getRootView() {
        return rootView;
    }
}
