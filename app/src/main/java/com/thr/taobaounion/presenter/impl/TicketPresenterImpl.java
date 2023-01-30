package com.thr.taobaounion.presenter.impl;

import com.thr.taobaounion.model.domain.TicketParams;
import com.thr.taobaounion.model.domain.TicketResult;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.UrlUtils;
import com.thr.taobaounion.view.ITicketPagerCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketPagerCallback mCallback;

    @Override
    public void getTicket(String title, String url, String cover) {
//        LogUtils.d(this, " "  + UrlUtils.getCoverPath(url));
        //请求网络 获取淘口令
        TicketParams ticketParams = new TicketParams(UrlUtils.getCoverPath(url), title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(this, "result code:" + code);
                if (response.isSuccessful()) {
                    LogUtils.d(this, response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {

            }
        });
    }

    @Override
    public void registerViewCallback(ITicketPagerCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ITicketPagerCallback callback) {
        mCallback = null;
    }


}
