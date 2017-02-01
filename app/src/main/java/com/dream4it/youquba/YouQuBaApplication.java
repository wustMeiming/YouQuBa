package com.dream4it.youquba;

import android.app.Application;
import android.content.Context;

/**
 * Created by meiming on 17-2-1.
 */

public class YouQuBaApplication extends Application{
    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();

        mContext = getApplicationContext();
    }

    public static Context getContext(){
        return mContext;
    }
}
