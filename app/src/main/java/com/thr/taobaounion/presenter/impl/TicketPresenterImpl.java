package com.thr.taobaounion.presenter.impl;

import com.thr.taobaounion.model.domain.TicketParams;
import com.thr.taobaounion.model.domain.TicketResult;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.view.ITicketCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TicketPresenterImpl implements ITicketPresenter {

    private ITicketCallback mCallback;

    private State currentState = State.NONE;

    enum State {
        LOADING, ERROR, EMPTY, NONE, SUCCESS;
    }

    private String cover;
    private TicketResult result;

    @Override
    public void getTicket(String title, String url, String cover) {
        LogUtils.d(this, "淘口令生成： "  + url);
        onTicktLoadind();
        //请求网络 获取淘口令
        this.cover = cover;
        TicketParams ticketParams = new TicketParams(url, title);
        Call<TicketResult> task = api.getTicket(ticketParams);
        task.enqueue(new Callback<TicketResult>() {
            @Override
            public void onResponse(Call<TicketResult> call, Response<TicketResult> response) {
                int code = response.code();
                LogUtils.d(this, "result code:" + code);
                if (response.isSuccessful()) {
                    //成功
                    LogUtils.d(this, response.body().toString());
                    result = response.body();
                    onTicktLoadedSuccess();
                } else {
                    //失败
                    onTicktLoadedError();
                }
            }

            @Override
            public void onFailure(Call<TicketResult> call, Throwable t) {
                //失败
                onTicktLoadedError();
            }
        });
    }

    private void onTicktLoadind() {
        if (mCallback != null) {
            mCallback.onLoading();
        } else {
            currentState = State.LOADING;
        }
    }

    private void onTicktLoadedError() {
        if (mCallback != null) {
            mCallback.onNetworkError();
        } else {
            currentState = State.ERROR;
        }
    }

    private void onTicktLoadedSuccess() {
        if (mCallback != null) {
            mCallback.onTicketLoaded(cover, result);
        } else {
            currentState = State.SUCCESS;
        }
    }

    @Override
    public void registerViewCallback(ITicketCallback callback) {
        if (currentState != State.NONE) {
            //说明确实，网络请求在活动初始化之前完成了，此时回头望一下状态记录
            if (currentState == State.SUCCESS) {
                onTicktLoadedSuccess();
            } else if (currentState == State.ERROR){
                onTicktLoadedError();
            } else if (currentState == State.LOADING) {
                onTicktLoadind();
            }
        }
        mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ITicketCallback callback) {
        mCallback = null;
    }


}
