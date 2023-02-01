package com.thr.taobaounion.utils;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.thr.taobaounion.base.BaseApplication;
import com.thr.taobaounion.model.domain.CacheWithDuration;

public class JsonCacheUtil {

    private final String jsonCacheSpName = "json_cache_sp_name";
    private final SharedPreferences sharedPreferences;
    private final Gson gson;

    private JsonCacheUtil() {
        sharedPreferences = BaseApplication.getAppContext().getSharedPreferences(jsonCacheSpName, Context.MODE_PRIVATE);
        gson = new Gson();
    }

    public void saveCache(String key, Object value) {
        this.saveCache(key, value, System.currentTimeMillis() + 1000*20);
    }

    public void saveCache(String key, Object value, long duration) {
        SharedPreferences.Editor edit = sharedPreferences.edit();
        //保存数据，带时间
        CacheWithDuration cacheWithDuration = new CacheWithDuration(value, duration);
        String cache = gson.toJson(cacheWithDuration);
        edit.putString(key, cache);
        edit.apply();
    }

    public void delCache(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    public <T> T getValue(String key, Class<T> clazz) {
        String string = sharedPreferences.getString(key, null);
        if (string == null) {
            return null;
        }
        CacheWithDuration cacheWithDuration = gson.fromJson(string, CacheWithDuration.class);
        if (cacheWithDuration.getDuration() < System.currentTimeMillis()) {
            //过期了
            return null;
        } else {
            //没过期
            Object value = cacheWithDuration.getValue();
            return (T)value;
        }
    }

    private static JsonCacheUtil jsonCacheUtil = null;

    public static JsonCacheUtil getInstance() {
        if (jsonCacheUtil == null) {
            jsonCacheUtil = new JsonCacheUtil();
        }
        return jsonCacheUtil;
    }
}
