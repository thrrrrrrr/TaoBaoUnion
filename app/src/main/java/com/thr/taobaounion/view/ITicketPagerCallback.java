package com.thr.taobaounion.view;

import com.thr.taobaounion.base.IBaseCallback;
import com.thr.taobaounion.model.domain.TicketResult;

public interface ITicketPagerCallback extends IBaseCallback {

    /**
     * 淘口令加载结果
     * @param cover
     * @param result
     */
    void onTicketLoaded(String cover, TicketResult result);

}
