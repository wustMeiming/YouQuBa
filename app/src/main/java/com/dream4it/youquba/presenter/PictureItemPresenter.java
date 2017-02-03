package com.dream4it.youquba.presenter;

import android.util.Log;

import com.dream4it.youquba.model.IPictureItemModel;
import com.dream4it.youquba.model.impl.PictureItemModelImpl;
import com.dream4it.youquba.rx.RxManager;
import com.dream4it.youquba.rx.RxSubscriber;
import com.dream4it.youquba.ui.view.PictureItemView;
import com.dream4it.youquba.utils.JsoupUtil;

/**
 * Created by meiming on 17-2-1.
 */

public class PictureItemPresenter extends BasePresenter<PictureItemView> {
    private IPictureItemModel mModel;

    public PictureItemPresenter(){
        mModel = new PictureItemModelImpl();
    }

    public void getPictureItemData(String suburl, int page){
        mSubscription = RxManager.getInstance().doSubscribe(mModel.getPictureItemData(suburl, page), new RxSubscriber<String>(false) {
            @Override
            protected void _onNext(String s) {
                mView.onSuccess(JsoupUtil.parsePictures(s));
            }

            @Override
            protected void _onError() {
                Log.e("PictureItemPresenter", "on error");
            }
        });
    }
}
