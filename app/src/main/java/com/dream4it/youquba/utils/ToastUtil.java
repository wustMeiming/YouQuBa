package com.dream4it.youquba.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by meiming on 17-2-1.
 */

public class ToastUtil {
    public static void showShort(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }

    public static void showShort(Context context, int res){
        Toast.makeText(context, res, Toast.LENGTH_SHORT).show();
    }

    public static void showLong(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }

    public static void showLong(Context context, int res){
        Toast.makeText(context, res, Toast.LENGTH_LONG).show();
    }
}
