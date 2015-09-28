package com.bzh.mysimplefresco.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.bzh.mysimplefresco.fragment.BaseFragment;

import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Map;

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
public class BaseActivity extends AppCompatActivity {
    private Map<String, WeakReference<BaseFragment>> fragmentRefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        fragmentRefs = new HashMap<String, WeakReference<BaseFragment>>();
    }


    public void addFragment(String tag, BaseFragment fragment) {
        fragmentRefs.put(tag, new WeakReference<BaseFragment>(fragment));
    }

    public void removeFragment(String tag) {
        fragmentRefs.remove(tag);
    }

}
