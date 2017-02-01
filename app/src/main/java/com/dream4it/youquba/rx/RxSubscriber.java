package com.dream4it.youquba.rx;

import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

import com.dream4it.youquba.R;
import com.dream4it.youquba.YouQuBaApplication;
import com.dream4it.youquba.utils.ToastUtil;

import java.io.IOException;

import rx.Subscriber;

/**
 * Created by meiming on 17-2-1.
 */

public abstract class RxSubscriber<T> extends Subscriber<T> {
    private Context mContext;
    private boolean mIsShowLoading;

    public RxSubscriber(boolean isShowLoading){
        mContext = YouQuBaApplication.getContext();
        mIsShowLoading = isShowLoading;
    }

    @Override
    public void onStart() {
        super.onStart();
        showLoading();
    }

    @Override
    public void onCompleted() {
        cancelLoading();
    }

    @Override
    public void onError(Throwable t) {
        //统一处理请求异常的情况
        if (t instanceof IOException) {
            ToastUtil.showShort(mContext, R.string.net_connect_error);
        } else {
            ToastUtil.showShort(mContext, t.getMessage());
        }

        _onError();

        cancelLoading();
    }

    @Override
    public void onNext(T t) {
        _onNext(t);
    }

    private void showLoading(){
        if (mIsShowLoading){
            //显示加载进度条
        }
    }

    private void cancelLoading(){

    }

    protected abstract void _onNext(T t);

    protected abstract void _onError();
}
