package com.dream4it.youquba.ui.activity;

import android.content.Intent;
import android.os.Build;
import android.support.design.widget.NavigationView;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.dream4it.youquba.R;
import com.dream4it.youquba.ui.fragment.BaseFragment;
import com.dream4it.youquba.utils.ImageLoaderUtil;
import com.dream4it.youquba.utils.ResUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;

public class MainActivity extends BaseActivity {

    private String mCurrentType;
    private Map<String, BaseFragment> mTypeFragments;

    @BindView(R.id.main_nav_view)
    NavigationView mNavView;
    @BindView(R.id.drawer_layout)
    DrawerLayout mDrawerLayout;
    @BindView(R.id.main_toolbar)
    Toolbar mToolbar;

    @Override
    protected int initLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initStatusBar();

        initDrawer();
        initNavigationView();

        //doReplace(ResUtil.resToStr(mContext, R.string.gank));
    }

    @Override
    protected void initData() {
        mTypeFragments = new HashMap<>();
    }


    private void initDrawer() {
        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawerLayout, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawerLayout.addDrawerListener(toggle);
        //设置左上角显示三道横线
        toggle.syncState();
        mToolbar.setTitle(R.string.app_name);
    }

    private void initNavigationView() {
        ImageView icon = (ImageView) mNavView.getHeaderView(0).findViewById(R.id.nav_head_icon);
        ImageLoaderUtil.load(mContext, R.mipmap.logo, icon);
        TextView name = (TextView) mNavView.getHeaderView(0).findViewById(R.id.nav_head_name);
        name.setText(R.string.app_name);
        mNavView.setCheckedItem(R.id.nav_picture);//设置默认选中
        //设置NavigationView对应menu item的点击事情
        mNavView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.nav_picture:
                        doReplace(ResUtil.resToStr(mContext, R.string.picture));
                        break;
                    case R.id.nav_movie:
                        doReplace(ResUtil.resToStr(mContext, R.string.movie));
                        break;
                    case R.id.nav_cartoon:
                        doReplace(ResUtil.resToStr(mContext, R.string.cartoon));
                        break;
                    case R.id.nav_article:
                        doReplace(ResUtil.resToStr(mContext, R.string.article));
                        break;
                    case R.id.nav_news:
                        doReplace(ResUtil.resToStr(mContext, R.string.news));
                        break;
                    case R.id.nav_setting:
                        openSetting();
                        break;
                    case R.id.nav_about:
                        openAbout();
                        break;
                }
                //隐藏NavigationView
                mDrawerLayout.closeDrawer(GravityCompat.START);
                return true;
            }
        });
    }

    private void doReplace(String type) {
        if (!type.equals(mCurrentType)) {
            //replaceFragment(TypeFragment.newInstance(type), type, mCurrentType);
            mCurrentType = type;
        }
    }

    private void replaceFragment(BaseFragment fragment, String tag, String lastTag) {
        if (mTypeFragments.get(tag) == null) {
            mTypeFragments.put(tag, fragment);

            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.add(R.id.main_fragment_container, fragment, tag)
                    .commit();
        }

        if (mTypeFragments.get(lastTag) != null) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.hide(mTypeFragments.get(lastTag))
                    .show(mTypeFragments.get(tag))
                    .commit();
        }
    }

    private void openSetting() {
        Intent intent = new Intent(mContext, SettingActivity.class);
        startActivity(intent);
    }

    private void openAbout(){
        Intent intent = new Intent(mContext, AboutActivity.class);
        startActivity(intent);
    }

    private void initStatusBar() {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            //将侧边栏顶部延伸至status bar
            mDrawerLayout.setFitsSystemWindows(true);
            //将主页面顶部延伸至status bar
            mDrawerLayout.setClipToPadding(false);
        }
    }
}
