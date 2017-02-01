package com.dream4it.youquba.presenter;

import rx.Subscription;

/**
 * Created by meiming on 17-2-1.
 */

public class BasePresenter<V> {
    public V mView;
    protected Subscription mSubscription;

    public void attach(V view) {
        mView = view;
    }

    public void detach() {
        mView = null;
        if (mSubscription != null) {
            mSubscription.unsubscribe();
        }
    }
}
