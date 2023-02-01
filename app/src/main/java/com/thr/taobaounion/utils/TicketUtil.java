package com.thr.taobaounion.utils;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;

import com.thr.taobaounion.model.domain.IBaseInfo;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.ui.activity.TicketActivity;

public class TicketUtil {

    public static void toTicketPage(Context context, IBaseInfo item) {
        String title = item.getTitle();
        String url = item.getUrl(); //领券界面
        String cover = item.getCover();
        //拿到ticketPresenter去加载
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);//可能会产生网络请求比活动初始化presenter块的情况，得多写代码
        context.startActivity(new Intent(context, TicketActivity.class));
    }
}
