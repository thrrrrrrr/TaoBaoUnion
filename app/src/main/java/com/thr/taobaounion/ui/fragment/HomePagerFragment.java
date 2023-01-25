package com.thr.taobaounion.ui.fragment;

import android.graphics.Rect;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.thr.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.thr.taobaounion.ui.adapter.LooperPagerAdapter;
import com.thr.taobaounion.utils.Constants;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.SizeUtils;
import com.thr.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter categoryPagerPresenter;
    private int materialId;
    private HomePagerContentAdapter mContentListAdapter;

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @BindView(R.id.looper_pager)
    public ViewPager looperPgaer;
    private LooperPagerAdapter mLooperPagerAdapter;

    @BindView(R.id.home_pager_title)
    public TextView currentPageTitle;

    @BindView(R.id.looper_point_container)
    public LinearLayout looperPointContainer;


    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        //向fragment传数据，传bundle
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
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
    }

    @Override
    protected void initPresenter() { //初始化Presenter
        categoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        categoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void initListener() {
        looperPgaer.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {}

            @Override
            public void onPageSelected(int position) {
                int targetPosition = position % mLooperPagerAdapter.getSize();
                //切换指示器
                updateLooperIndicator(targetPosition);
            }

            @Override
            public void onPageScrollStateChanged(int state) {}
        });
    }

    private void updateLooperIndicator(int targetPosition) {
        ///切换指示器
        for (int i = 0; i < looperPointContainer.getChildCount(); i++) {
            View point = looperPointContainer.getChildAt(i);
            if (i == targetPosition) {//选择点，只有一个点是
                point.setBackgroundResource(R.drawable.shapr_looper_point_selected);
            } else {
                point.setBackgroundResource(R.drawable.shapr_looper_point_normal);
            }
        }
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        materialId = arguments.getInt(Constants.KEY_HOME_PAGER_ID);
        LogUtils.d(this, "title: " + title + ", " + "materialId: " + materialId);
        //TODO：加载数据
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

    @Override
    public void onLoaderMoreError() {

    }

    @Override
    public void onLoaderMoreEmpty() {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

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
}
