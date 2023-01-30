package com.thr.taobaounion.ui.fragment;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.presenter.ITicketPresenter;
import com.thr.taobaounion.ui.activity.TicketActivity;
import com.thr.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.thr.taobaounion.ui.adapter.LooperPagerAdapter;
import com.thr.taobaounion.ui.custom.AutoLooperViewPager;
import com.thr.taobaounion.ui.custom.TbNestedSerollView;
import com.thr.taobaounion.utils.Constants;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.utils.SizeUtils;
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePageFragment extends BaseFragment implements ICategoryPagerCallback, HomePagerContentAdapter.OnListItemClickListener, LooperPagerAdapter.OnLooperPageItemClickListener {

    private ICategoryPagerPresenter categoryPagerPresenter;
    private int materialId;
    private HomePagerContentAdapter mContentListAdapter;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public AutoLooperViewPager looperPgaer;
    private LooperPagerAdapter mLooperPagerAdapter;

    @BindView(R.id.home_pager_title)
    public TextView currentPageTitle;

    //五个点的容器
    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;

    //smartRefresh容器
    @BindView(R.id.home_pager_refresh)
    public SmartRefreshLayout homePagerRefresh;

    @BindView(R.id.home_pager_parent)
    public LinearLayout homePagerParent;

    //TbNestedSerollView 给他设置高度,解决NestedSerollView与RecyclerView的冲突
    @BindView(R.id.home_pager_nested_scroll)
    public TbNestedSerollView homePagerNestedScroll;

    @BindView(R.id.home_header_container)
    public LinearLayout homeHeaderContainer;


    public static HomePageFragment newInstance(Categories.DataBean category) {
        HomePageFragment homePagerFragment = new HomePageFragment();
        Bundle bundle = new Bundle();
        //向fragment传数据，传bundle
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        //其调用自动轮播
        looperPgaer.startLoop();
    }

    @Override
    public void onPause() {
        super.onPause();
        //关闭自动轮播
        looperPgaer.stopLoop();
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View view) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        mContentList.addItemDecoration(new RecyclerView.ItemDecoration() {
            @Override
            public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
                outRect.top = 8;
                outRect.bottom = 8;
            }
        });
        //创建适配器
        mContentListAdapter = new HomePagerContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentListAdapter);
        //创轮播图适配器
        mLooperPagerAdapter = new LooperPagerAdapter();
        //设置轮播图适配器
        looperPgaer.setAdapter(mLooperPagerAdapter);
        //设置Refresh相关属性
        homePagerRefresh.setEnableRefresh(false);
        homePagerRefresh.setEnableLoadMore(true);
        homePagerRefresh.setEnableAutoLoadMore(true);
        homePagerRefresh.setEnableOverScrollDrag(true);
    }

    @Override
    protected void initPresenter() { //初始化Presenter
        categoryPagerPresenter = PresenterManager.getInstance().getCategoryPagerPresenter();
        categoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        //设置点击商品的
        mContentListAdapter.setOnListItemClickListener(this);
        mLooperPagerAdapter.setOnLooperPageItemClickListener(this);
        //设置RecyclerView的高 动态，看不太懂不做了擦
        homePagerParent.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                if (homeHeaderContainer != null && homePagerParent != null && homePagerNestedScroll !=null){
                    //设置同步下滑的阈值，把上面轮播图和文字的高度给到TbNestedscrollView里面的阈值
                    int height = homeHeaderContainer.getMeasuredHeight();
                    LogUtils.d(this, "阈值是 : " + height);
                    homePagerNestedScroll.setHeaderHeight(height);

                    int measuredHeight = homePagerParent.getMeasuredHeight();
                    LogUtils.d(this, "measuredHeight: " + measuredHeight);
                    ViewGroup.LayoutParams layoutParams = mContentList.getLayoutParams();
                    layoutParams.height = measuredHeight;
                    mContentList.setLayoutParams(layoutParams);
                    if (measuredHeight != 0) {
                        homePagerParent.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                    }
                }
            }
        });

        looperPgaer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                if (mLooperPagerAdapter.getSize() == 0) {
                    return;
                }
                int targetPosition = position % mLooperPagerAdapter.getSize();
                //切换指示器
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });

        homePagerRefresh.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                LogUtils.d(this, "触发loadmore....");
                homePagerRefresh.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        //用Presenter去加载更多
                        if (categoryPagerPresenter != null) {
                            categoryPagerPresenter.loaderMore(materialId);
                        }
                    }
                }, 0);
            }
        });
    }

    private void updateLooperIndicator(int targetPosition) {
        ///切换指示器
        if (looperPointContainer != null) {//报空指针
            for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
                View point = looperPointContainer.getChildAt(i);
                if (i == targetPosition) {//选择点，只有一个点是
                    point.setBackgroundResource(R.drawable.shapr_looper_point_selected);
                } else {
                    point.setBackgroundResource(R.drawable.shapr_looper_point_normal);
                }
            }
        }
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        materialId = arguments.getInt(Constants.KEY_HOME_PAGER_ID);
        LogUtils.d(this, "title: " + title + ", " + "materialId: " + materialId);
        //加载数据
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.getContentByCategoryId(materialId);
        }
        if (currentPageTitle != null) {
            currentPageTitle.setText(title);
        }
    }

    @Override //释放presenter
    protected void release() {
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    public int getCategoryId() {
        return materialId;
    }

    //均是CallBack接口中的
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {
        //数据列表加载
        mContentListAdapter.setData(contents);
        setUpState(State.SUCCESS);
    }

    @Override
    public void onNetworkError() {
        //网络错误
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

    //loadmore
    @Override
    public void onLoaderMoreError() {
        ToastUtils.show("网络异常请稍后重试");
        homePagerRefresh.finishLoadMore();
    }

    @Override
    public void onLoaderMoreEmpty() {
        ToastUtils.show("没有更多的商品了");
        homePagerRefresh.finishLoadMore();
    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {
        //添加到适配器的底部
        mContentListAdapter.addData(contents);
        homePagerRefresh.finishLoadMore();
        ToastUtils.show("加载了" + contents.size() + "条记录");
    }

    //轮播图
    @Override
    public void onLopperListLoaded(List<HomePagerContent.DataBean> contents) {
        LogUtils.d(this, "looper: size :" + contents.size());
        mLooperPagerAdapter.setData(contents);
        //设置到中间点
        looperPgaer.setCurrentItem((Integer.MAX_VALUE/2)/contents.size()*contents.size());
        //动态添加点
        looperPointContainer.removeAllViews();
        //设置两种不同颜色的点
        for (int i = 0; i < contents.size(); i++) {
            View point = new View(getContext());
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams
                    (SizeUtils.dip2px(getContext(), 6), SizeUtils.dip2px(getContext(), 6));
            layoutParams.leftMargin = SizeUtils.dip2px(getContext(), 5);
            layoutParams.rightMargin = SizeUtils.dip2px(getContext(), 5);
            point.setLayoutParams(layoutParams);
            if (i == 0) {//选择点，只有一个点是
                point.setBackgroundResource(R.drawable.shapr_looper_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shapr_looper_point_normal);
            }
            looperPointContainer.addView(point);
        }
    }

    @Override
    public void onItemClick(HomePagerContent.DataBean item) {
        //列表被点击
//        LogUtils.d(this, item.getTitle());
        handleItemClick(item);
    }

    @Override
    public void onLooperItemClick(HomePagerContent.DataBean item) {
//        LogUtils.d(this, item.getTitle());
        handleItemClick(item);
    }

    private void handleItemClick(HomePagerContent.DataBean item) {
        String title = item.getTitle();
        String url = item.getClick_url();
        String cover = item.getPict_url();
        //拿到ticketPresenter去加载
        ITicketPresenter ticketPresenter = PresenterManager.getInstance().getTicketPresenter();
        ticketPresenter.getTicket(title, url, cover);//可能会产生网络请求比活动初始化presenter块的情况，得多写代码
        startActivity(new Intent(getContext(), TicketActivity.class));
    }
}
