package com.dream4it.youquba.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.dream4it.youquba.presenter.BasePresenter;

/**
 * Created by meiming on 17-2-1.
 */

public abstract class BaseMvpFragment<V, P extends BasePresenter<V>> extends BaseFragment {
    protected static final String SUB_TYPE = "subtype";

    protected P mPresenter;

    protected abstract P initPresenter();

    protected abstract void fetchData();

    protected boolean mIsViewInitiated;
    protected boolean mIsVisibleToUser;
    protected boolean mIsDataInitiated;

    protected void initFetchData(){
        if (mIsVisibleToUser && mIsViewInitiated && !mIsDataInitiated){
            fetchData();
            mIsDataInitiated = true;
        }
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        mIsVisibleToUser = isVisibleToUser;
        initFetchData();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mPresenter = initPresenter();
        mPresenter.attach((V)this);

        mIsViewInitiated = true;

        initFetchData();
    }

    @Override
    public void onDestroy() {
        mPresenter.detach();
        super.onDestroy();
    }
}
