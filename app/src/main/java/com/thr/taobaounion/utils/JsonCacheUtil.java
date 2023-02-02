package com.thr.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.thr.taobaounion.base.BaseApplication;
import com.thr.taobaounion.model.domain.Histories;

public class JsonCacheUtil {

    public static final String JSON_CACHE_SP_NAME = "json_cache_sp_name";

    private final SharedPreferences mSp;
    private final Gson mGson;

    private JsonCacheUtil() {
        mSp = BaseApplication.getAppContext().getSharedPreferences(JSON_CACHE_SP_NAME, Context.MODE_PRIVATE);
        mGson = new Gson();
    }

    private static JsonCacheUtil sJsonCacheUtil = null;

    public static JsonCacheUtil getInstance() {
        if (sJsonCacheUtil == null) {
            sJsonCacheUtil = new JsonCacheUtil();
        }
        return sJsonCacheUtil;
    }


    public void saveCache(String key, Object value) {
        String valueStr = mGson.toJson(value);

        SharedPreferences.Editor editor = mSp.edit();
        editor.putString(key, valueStr);
        editor.apply();
    }

    public void delCache(String key) {
        mSp.edit().remove(key).apply();
    }

    public Histories getValue(String key) {
        String value = mSp.getString(key, null);
        if (value == null) {
            return null;
        }
        Histories histories = mGson.fromJson(value, Histories.class);
        return histories;
    }
}
