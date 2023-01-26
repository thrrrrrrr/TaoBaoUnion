package com.thr.taobaounion.utils;

import android.widget.Toast;

import com.thr.taobaounion.base.BaseApplication;

public class ToastUtils {

    private static Toast toast = null;

    public static void show(String tips) {
        if (toast == null) {
            toast = Toast.makeText(BaseApplication.getAppContext(), null, Toast.LENGTH_SHORT);
            toast.setText(tips);
        } else {
            toast.setText(tips);
        }
        toast.show();
    }
}
