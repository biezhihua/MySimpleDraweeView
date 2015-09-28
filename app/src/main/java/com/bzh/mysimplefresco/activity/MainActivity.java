package com.bzh.mysimplefresco.activity;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bzh.mysimplefresco.R;
import com.bzh.mysimplefresco.fragment.BaseFragment;
import com.bzh.mysimplefresco.fragment.OneFragment;
import com.bzh.mysimplefresco.lib.MySimpleDraweeView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity {


    @Bind(R.id.navigationView)
    NavigationView navigationView;
    @Bind(R.id.drawer)
    DrawerLayout drawer;
    @Bind(R.id.layMainRoot)
    RelativeLayout layMainRoot;
    @Bind(R.id.ivMenuUserProfilePhoto)
    MySimpleDraweeView ivMenuUserProfilePhoto;
    @Bind(R.id.vGlobalMenuHeader)
    LinearLayout vGlobalMenuHeader;
    @Bind(R.id.layContentRoot)
    FrameLayout layContentRoot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        initFragment();

        initMenuHeader();

        initNavigationView();
    }

    private void initFragment() {
        getSupportFragmentManager().beginTransaction()
                .add(R.id.layContentRoot, OneFragment.newInstance(), BaseFragment.FRAGMENT_TAG)
                .commit();
    }

    private void initNavigationView() {
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                Fragment fragment = getSupportFragmentManager().findFragmentByTag(BaseFragment.FRAGMENT_TAG);
                switch (menuItem.getItemId()) {
                    case R.id.menu_1:
                        if (fragment != null && fragment instanceof OneFragment) {
                        } else {
                            getSupportFragmentManager().beginTransaction().replace(R.id.layContentRoot, OneFragment.newInstance(), BaseFragment.FRAGMENT_TAG).commit();
                        }
                        break;
                }
                drawer.closeDrawer(Gravity.LEFT);
                return false;
            }
        });
    }

    private void initMenuHeader() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            vGlobalMenuHeader.setPadding(vGlobalMenuHeader.getPaddingLeft(),
                    vGlobalMenuHeader.getPaddingTop() + this.getResources().getDimensionPixelSize(R.dimen.status_bar_height),
                    vGlobalMenuHeader.getPaddingRight(),
                    vGlobalMenuHeader.getPaddingBottom());
        }
        ivMenuUserProfilePhoto.setRoundDraweeViewUrl("http://git.oschina.net/biezhihua/MyResource/raw/master/biezhihua.png");
    }



    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }
}
