package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.BasePresenter;
import com.thr.taobaounion.view.ICategoryPagerCallback;

public interface ICategoryPagerPresenter extends BasePresenter<ICategoryPagerCallback> {

    /**
     * 根据分类id去获取内容
     *
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loaderMore(int categoryId);

    void reload(int categoryId);
}
