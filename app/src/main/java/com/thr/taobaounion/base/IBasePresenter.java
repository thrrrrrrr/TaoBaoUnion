package com.thr.taobaounion.base;

import com.thr.taobaounion.model.API;
import com.thr.taobaounion.utils.RetrofitManager;

import retrofit2.Retrofit;

public interface IBasePresenter<T> {
    Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
    API api = retrofit.create(API.class);
    /**
     * 获取ui通知接口
     * @param callback
     */
    void registerViewCallback(T callback);

    /**
     * 取消注册ui更新接口
     * @param callback
     */
    void unregisterViewCallback(T callback);
}
