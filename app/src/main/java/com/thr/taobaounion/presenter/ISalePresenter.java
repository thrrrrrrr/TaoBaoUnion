package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.IBasePresenter;
import com.thr.taobaounion.view.ISaleCallback;

public interface ISalePresenter extends IBasePresenter<ISaleCallback> {

    /**
     * 加载特惠内容
     */
    void getOnSaleContent();

    /**
     * 加载更多
     */
    void loaderMore();

}
