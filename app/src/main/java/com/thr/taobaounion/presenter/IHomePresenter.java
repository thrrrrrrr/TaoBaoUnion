package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.BasePresenter;
import com.thr.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends BasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();


}
