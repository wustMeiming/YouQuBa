package com.dream4it.youquba.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Arrays;
import java.util.List;

/**
 * Created by meiming on 17-1-31.
 */

public class ResUtil {
    public static List<String> stringArrayToList(Context context, int arrayId) {
        return Arrays.asList(context.getResources().getStringArray(arrayId));
    }

    public static String resToStr(Context context, int strId) {
        return context.getString(strId);
    }

    public static View inflate(Context context, int viewId, ViewGroup root) {
        return LayoutInflater.from(context).inflate(viewId, root, false);
    }

    public static int resToColor(Context context, int colorId){
        return context.getResources().getColor(colorId);
    }
}
