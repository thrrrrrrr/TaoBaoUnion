package com.thr.taobaounion.utils;

public class UrlUtils {
    public static String getCoverPath(String pic_url, int size) {
        return "https:" + pic_url + "_" + size + "x" + size + ".jpg";
    }

    public static String getCoverPath(String pic_url) {
        return "https:" + pic_url;
    }
}
