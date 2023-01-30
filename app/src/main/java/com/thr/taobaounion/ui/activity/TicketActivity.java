package com.thr.taobaounion.ui.activity;

import android.text.TextUtils;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseAvtivity;
import com.thr.taobaounion.model.domain.TicketResult;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.utils.UrlUtils;
import com.thr.taobaounion.view.ITicketPagerCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class TicketActivity extends BaseAvtivity implements ITicketPagerCallback {

    private ITicketPresenter ticketPresenter;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_button)
    public TextView copyButton;

    @OnClick(R.id.ticket_back)
    public void back() {
        finish();
    }

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
        String url = UrlUtils.getCoverPath(cover);
        LogUtils.d(this, "成功获取图片： " + url);
        if (!TextUtils.isEmpty(cover) && mCover != null) {
            Glide.with(this).load(url).into(mCover);
        }
        if (result != null && result.get() != null) {
            mTicketCode.setText(result.get());
        }
    }

    @Override
    public void onNetworkError() {
        LogUtils.d(this, "失败");
    }

    @Override
    public void onLoading() {
        LogUtils.d(this, "加载中");
    }

    @Override
    public void onEmpty() {
        LogUtils.d(this, "空");
    }

}
