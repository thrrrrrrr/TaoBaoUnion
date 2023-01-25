package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.IBasePresenter;
import com.thr.taobaounion.view.IHomeCallback;

public interface IHomePresenter extends IBasePresenter<IHomeCallback> {
    /**
     * 获取商品分类
     */
    void getCategories();


}
