package com.dream4it.youquba.ui.fragment;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;

import com.dream4it.youquba.R;
import com.dream4it.youquba.ui.adapter.TypePageAdapter;
import com.dream4it.youquba.utils.ResUtil;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by meiming on 17-2-1.
 */

public class TypeFragment extends BaseFragment{
    private static final String TYPE = "type";

    private String mType;
    private List<BaseMvpFragment> mFragments;
    private List<String> mTitles;

    private TypePageAdapter mTypeAdapter;

    @BindView(R.id.type_tablayout)
    TabLayout mTabLayout;

    @BindView(R.id.type_viewpager)
    ViewPager mViewPager;

    @Override
    protected int initLayoutId() {
        return R.layout.fragment_type_layout;
    }

    @Override
    protected void initView() {
        mTypeAdapter = new TypePageAdapter(getChildFragmentManager());
        mTypeAdapter.setData(mFragments, mTitles);
        mViewPager.setAdapter(mTypeAdapter);
        mViewPager.setOffscreenPageLimit(mTitles.size() - 1);
        mTabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                mViewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    protected void initData() {
        if (getArguments() == null) {
            return;
        }

        mFragments = new ArrayList<>();

        mType = getArguments().getString(TYPE);

        if (ResUtil.resToStr(mActivity, R.string.picture).equals(mType)) {
            mTitles = ResUtil.stringArrayToList(mActivity, R.array.picture);
            for (String title : mTitles) {
                //mFragments.add(PictureItemFragment.newInstance(title));
            }
        } else if (ResUtil.resToStr(mActivity, R.string.movie).equals(mType)) {
            /*mTitles = ResUtil.stringArrayToList(mActivity, R.array.movie);
            List<String> subtypes = ResUtil.stringArrayToList(mActivity, R.array.movie);
            for (String subtype : subtypes) {
                //mFragments.add(MovieItemFragment.newInstance(subtype));
            }
            */
        }

    }


    public static TypeFragment newInstance(String type) {
        TypeFragment fragment = new TypeFragment();
        Bundle arguments = new Bundle();
        arguments.putString(TYPE, type);
        fragment.setArguments(arguments);
        return fragment;
    }
}
