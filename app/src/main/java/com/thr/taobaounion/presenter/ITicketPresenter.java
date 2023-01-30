package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.IBasePresenter;
import com.thr.taobaounion.view.ITicketPagerCallback;

public interface ITicketPresenter extends IBasePresenter<ITicketPagerCallback> {
    /**
     * 生成淘口令
     * @param title
     * @param url
     * @param cover
     */
    void getTicket(String title, String url, String cover);
}
