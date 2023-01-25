package com.thr.taobaounion.ui.fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.presenter.IHomePresenter;
import com.thr.taobaounion.presenter.impl.HomePresenterImpl;
import com.thr.taobaounion.ui.adapter.HomePagerAdapter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.view.IHomeCallback;

import butterknife.BindView;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    @BindView(R.id.home_indicator)
    public TabLayout mTabLayout;

    @BindView(R.id.home_pager)
    public ViewPager mHomePager;

    private IHomePresenter homePresenter;
    private HomePagerAdapter homePagerAdapter;

    @Override
    protected int getRootViewResId() {
        //子类返回布局xml
        return R.layout.fragment_home;
    }

    @Override
    protected void initView(View view) {
        //TabLayout和ViewPager绑定
        mTabLayout.setupWithViewPager(mHomePager);
        //创建适配器
        homePagerAdapter = new HomePagerAdapter(getChildFragmentManager());
        //适配器和ViewPager绑定
        mHomePager.setAdapter(homePagerAdapter);
    }

    @Override
    protected void initPresenter() {
        //加载presenter
        homePresenter = new HomePresenterImpl();
        homePresenter.registerViewCallback(this);
    }

    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_home_fragment_layout, container, false);
    }

    @Override
    protected void release() {
        //释放presenter
        if (homePresenter != null) {
            homePresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void loadData() {
        //加载数据
        homePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        //网络请求返回的数据调用此方法
        setUpState(State.SUCCESS);
        LogUtils.d(this, "HomeFragment中分类加载...");
        if (homePagerAdapter != null) {
            homePagerAdapter.setCategories(categories);
        }
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
    protected void onRetryClick() {
        //重写网络错误充实
        if (homePresenter != null) {
            homePresenter.getCategories();
        }
    }
}
