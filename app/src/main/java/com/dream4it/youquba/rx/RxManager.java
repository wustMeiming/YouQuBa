package com.dream4it.youquba.rx;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by meiming on 17-2-1.
 */

public class RxManager {
    private RxManager(){}

    private static class SingletonHolder{
        private static final RxManager INSTANCE = new RxManager();
    }

    public static RxManager getInstance(){
        return SingletonHolder.INSTANCE;
    }

    public <T> Subscription doSubscribe(Observable<T> observable, Subscriber<T> subscriber) {
        return observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(subscriber);
    }
}
