package com.thr.taobaounion.ui.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.View;
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
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.utils.UrlUtils;
import com.thr.taobaounion.view.ITicketCallback;

import butterknife.BindView;
import butterknife.OnClick;

public class TicketActivity extends BaseAvtivity implements ITicketCallback {

    private ITicketPresenter ticketPresenter;

    private boolean hasTaoBAo = false;

    @BindView(R.id.ticket_cover)
    public ImageView mCover;

    @BindView(R.id.ticket_loading)
    public TextView ticketLoading;

    @BindView(R.id.ticket_error)
    public TextView ticketError;

    @BindView(R.id.ticket_code)
    public EditText mTicketCode;

    @BindView(R.id.ticket_copy_button)
    public TextView copyButton;

    @OnClick(R.id.ticket_back)
    public void back() {
        finish();
    }

    @OnClick(R.id.ticket_copy_button)
    public void copyOrEnter() {
        String s = mTicketCode.getText().toString().trim();
        ClipboardManager systemService = (ClipboardManager)getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData data = ClipData.newPlainText("taobao_ticket_code", s);
        systemService.setPrimaryClip(data);
        ToastUtils.show("复制淘口令成功！");
        if (hasTaoBAo) {
            Intent intent = new Intent();
//            intent.setAction("android.intent.action.MAIN");
//            intent.addCategory("android.intent.category.LAUNCHER");
            intent.setComponent(new ComponentName("com.taobao.taobao", "com.taobao.tao.TBMainActivity"));
            LogUtils.d(this, "启动淘宝");
            startActivity(intent);
        }

    }

//    public void retry() {
//        //不写了
//    }

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
        ticketPresenter.registerViewCallback(this);//把自己（ui）传给presenter
        //判断是否安装淘宝
        //act=android.intent.action.MAIN cat=[android.intent.category.LAUNCHER] flg=0x10200000 cmp=com.taobao.taobao/com.taobao.tao.welcome.Welcome
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo("com.taobao.taobao", PackageManager.MATCH_UNINSTALLED_PACKAGES);
            hasTaoBAo = packageInfo!=null;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            hasTaoBAo = false;
        }
        LogUtils.d(this, "有无淘宝 : " + hasTaoBAo);
        copyButton.setText(hasTaoBAo ? "立即领券" : "复制淘口令");
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
        ticketLoading.setVisibility(View.GONE);
        ticketError.setVisibility(View.GONE);
        mCover.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNetworkError() {
        LogUtils.d(this, "失败");
        ticketLoading.setVisibility(View.GONE);
        ticketError.setVisibility(View.VISIBLE);
        mCover.setVisibility(View.GONE);
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
