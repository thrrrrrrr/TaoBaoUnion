package com.thr.taobaounion.presenter;

import com.thr.taobaounion.view.IHomeCallback;

public interface IHomePresenter {
    /**
     * 获取商品分类
     */
    void getCategories();

    /**
     * 获取ui通知接口
     * @param callback
     */
    void registerCallback(IHomeCallback callback);

    /**
     * 取消注册ui更新接口
     * @param callback
     */
    void unregisterCallback(IHomeCallback callback);
}
