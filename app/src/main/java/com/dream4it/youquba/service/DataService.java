package com.dream4it.youquba.service;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Parcelable;

import com.dream4it.youquba.data.PictureItemData;
import com.dream4it.youquba.utils.ImageLoaderUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by meiming on 17-2-3.
 */

public class DataService extends IntentService {
    public DataService() {
        super("");
    }

    public static void startService(Context context, List<PictureItemData> datas, String subtype) {
        Intent intent = new Intent(context, DataService.class);
        intent.putParcelableArrayListExtra("data", (ArrayList<? extends Parcelable>) datas);
        intent.putExtra("subtype", subtype);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        List<PictureItemData> datas = intent.getParcelableArrayListExtra("data");
        String subtype = intent.getStringExtra("subtype");
        handlePictureItemData(datas, subtype);
    }

    private void handlePictureItemData(List<PictureItemData> datas, String subtype) {
        if (datas.size() == 0) {
            System.out.println("size=0");
            EventBus.getDefault().post("finish");
            return;
        }
        for (PictureItemData data : datas) {
            ImageLoaderUtil.load(this, data.getImage());
            data.setSubtype(subtype);
        }
        EventBus.getDefault().post(datas);
    }
}

