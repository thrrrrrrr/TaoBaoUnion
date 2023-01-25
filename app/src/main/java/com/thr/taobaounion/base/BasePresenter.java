package com.thr.taobaounion.base;

import com.thr.taobaounion.view.IHomeCallback;

public interface BasePresenter<T> {
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
