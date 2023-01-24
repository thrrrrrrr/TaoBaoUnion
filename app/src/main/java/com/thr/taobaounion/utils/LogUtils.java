package com.thr.taobaounion.utils;

import android.util.Log;

public class LogUtils {
    public static int current = 4;
    public static final int debugLev = 4;
    public static final int infoLev = 3;
    public static final int warningLev = 2;
    public static final int errorLev = 1;

    public static void d(Object object, String log) {
        if (current >= debugLev) Log.d(object.getClass().getSimpleName(), log);
    }

    public static void i(Object object, String log) {
        if (current >= infoLev) Log.i(object.getClass().getSimpleName(), log);
    }

    public static void w(Object object, String log) {
        if (current >= warningLev) Log.w(object.getClass().getSimpleName(), log);
    }

    public static void e(Object object, String log) {
        if (current >= errorLev) Log.e(object.getClass().getSimpleName(), log);
    }
}
