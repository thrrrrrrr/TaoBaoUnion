package com.thr.taobaounion.utils;

public class UrlUtils {
    public static String getCoverPath(String pic_url, int size) {
        if(pic_url.startsWith("http")) {
            return pic_url + "_" + size + "x" + size + ".jpg";
        } else {
            return "http:" + pic_url + "_" + size + "x" + size + ".jpg";
        }
    }

    public static String getCoverPath(String pic_url) {
        if(pic_url.startsWith("http")) {
            return pic_url;
        } else {
            return "http:" + pic_url;
        }
    }
}
