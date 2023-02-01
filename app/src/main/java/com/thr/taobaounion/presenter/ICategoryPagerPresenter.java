package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.IBasePresenter;
import com.thr.taobaounion.view.ICategoryPageCallback;

public interface ICategoryPagerPresenter extends IBasePresenter<ICategoryPageCallback> {

    /**
     * 根据分类id去获取内容
     *
     * @param categoryId
     */
    void getContentByCategoryId(int categoryId);

    void loaderMore(int categoryId);
}
