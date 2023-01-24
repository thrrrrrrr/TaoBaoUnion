package com.thr.taobaounion.utils;

import android.util.Log;

public class LogUtils {
    public static int current = 4;
    public static final int debugLev = 4;
    public static final int infoLev = 3;
    public static final int warningLev = 2;
    public static final int errorLev = 1;

    public static void d(Class clazz, String log) {
        if (current >= debugLev) Log.d(clazz.getSimpleName(), log);
    }

    public static void i(Class clazz, String log) {
        if (current >= infoLev) Log.i(clazz.getSimpleName(), log);
    }

    public static void w(Class clazz, String log) {
        if (current >= warningLev) Log.w(clazz.getSimpleName(), log);
    }

    public static void e(Class clazz, String log) {
        if (current >= errorLev) Log.e(clazz.getSimpleName(), log);
    }
}
