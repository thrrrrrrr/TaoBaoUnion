package com.thr.taobaounion.ui.activity;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseAvtivity;
import com.thr.taobaounion.model.domain.TicketResult;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.view.ITicketPagerCallback;

public class TicketActivity extends BaseAvtivity implements ITicketPagerCallback {

    private ITicketPresenter ticketPresenter;

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_ticket;
    }

    @Override
    protected void initView() {

    }

    @Override
    protected void initEvent() {

    }

    @Override
    protected void initPresenter() {
        ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.registerViewCallback(this);
    }

    @Override
    protected void relese() {
        if (ticketPresenter != null) {
            ticketPresenter.unregisterViewCallback(this);
        }
    }

    //四种请求状态
    @Override
    public void onTicketLoaded(String cover, TicketResult result) {

    }

    @Override
    public void onNetworkError() {

    }

    @Override
    public void onLoading() {

    }

    @Override
    public void onEmpty() {

    }

}
