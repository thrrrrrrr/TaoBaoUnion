package com.thr.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.SaleContent;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.presenter.impl.SalePresenterImpl;
import com.thr.taobaounion.ui.activity.TicketActivity;
import com.thr.taobaounion.ui.adapter.SalePageContentAdapter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.utils.SizeUtils;
import com.thr.taobaounion.utils.TicketUtil;
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.view.ISaleCallback;

import butterknife.BindView;

public class SaleFragment extends BaseFragment implements ISaleCallback, SalePageContentAdapter.SalePageItemClickListener {

    private SalePresenterImpl salePresenter;

    @BindView(R.id.sale_content_list)
    public RecyclerView saleContentList;

    private SalePageContentAdapter salePageContentAdapter;

    @BindView(R.id.sale_refresh_layout)
    public SmartRefreshLayout saleRefreshLayout;

    @Override
    protected void initPresenter() {
        salePresenter = PresenterManager.getInstance().getSalePresenter();
        salePresenter.registerViewCallback(this);
        salePresenter.getOnSaleContent();
    }

    @Override
    protected void release() {
        if (salePresenter != null) {
            salePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        if (salePresenter != null) {
            salePresenter.getOnSaleContent();
        }
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_sale;
    }

    @Override
    protected void initView(View view) {
        //设置布局管理器
        saleContentList.setLayoutManager(new GridLayoutManager(getContext(), 2));
        //创建适配器
        salePageContentAdapter = new SalePageContentAdapter();
        //设置适配器
        saleContentList.setAdapter(salePageContentAdapter);
        saleContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = SizeUtils.dip2px(getContext(), 3);
                outRect.bottom = SizeUtils.dip2px(getContext(), 3);
                outRect.left = SizeUtils.dip2px(getContext(), 4);
                outRect.right = SizeUtils.dip2px(getContext(), 4);
            }
        });

        //loadmore
        saleRefreshLayout.setEnableRefresh(false);
        saleRefreshLayout.setEnableLoadMore(true);
        saleRefreshLayout.setEnableAutoLoadMore(true);
        saleRefreshLayout.setEnableOverScrollDrag(true);
    }

    @Override
    protected void initListener() {
        saleRefreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                if (salePresenter != null) {
                    salePresenter.loaderMore();
                }
            }
        });
        salePageContentAdapter.setItemClickListener(this);
    }

    //处理状态
    @Override
    public void onContentLoadedSuccess(SaleContent result) {
        //数据回来了
        LogUtils.d(this, result.getList().toString());
        setUpState(State.SUCCESS);

        salePageContentAdapter.setData(result);
    }

    @Override
    public void onNetworkError() {
        setUpState(State.ERROR);
    }

    @Override
    public void onLoading() {
        setUpState(State.LOADING);
    }

    @Override
    public void onEmpty() {
        setUpState(State.EMPTY);
    }

    @Override
    public void onMoreLoaded(SaleContent moreResult) {
        salePageContentAdapter.addData(moreResult);
        saleRefreshLayout.finishLoadMore();
        ToastUtils.show("加载了" + moreResult.getList().size() + "条记录");
    }

    @Override
    public void onMoreLoadedError() {
        saleRefreshLayout.finishLoadMore();
        ToastUtils.show("加载更多失败");
    }

    @Override
    public void onMoreLoadedEmpty() {
        saleRefreshLayout.finishLoadMore();
        ToastUtils.show("没有更多了");
    }

    @Override
    public void onItemClick(SaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean item) {
//        LogUtils.d(this, data.getTitle());
//        String title = item.getTitle();
//        String url = item.getCoupon_click_url(); //领券界面
//        if (TextUtils.isEmpty(url)) {
//            url = item.getClick_url(); //详情界面
//        }
//        String cover = item.getPict_url();
//        //拿到ticketPresenter去加载
//        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
//        ticketPresenter.getTicket(title, url, cover);//可能会产生网络请求比活动初始化presenter块的情况，得多写代码
//        startActivity(new Intent(getContext(), TicketActivity.class));
        TicketUtil.toTicketPage(getContext(), item);
    }
}
