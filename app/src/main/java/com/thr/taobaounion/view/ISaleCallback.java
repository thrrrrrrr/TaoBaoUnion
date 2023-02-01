package com.thr.taobaounion.view;

import com.thr.taobaounion.base.IBaseCallback;
import com.thr.taobaounion.model.domain.SaleContent;

public interface ISaleCallback extends IBaseCallback {

    /**
     * Presenter传过来的数据
     * @param saleContent
     */
    void onContentLoadedSuccess(SaleContent saleContent);

    /**
     * 记载更多时Presenter加载过来的数据
     * @param moreResult
     */
    void onMoreLoaded(SaleContent moreResult);

    /**
     * 失败
     */
    void onMoreLoadedError();

    /**
     * 空
     */
    void onMoreLoadedEmpty();

}
