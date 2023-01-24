package com.thr.taobaounion.ui.fragment;

import android.view.View;

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
        homePresenter.registerCallback(this);
    }

    @Override
    protected void release() {
        //释放presenter
        if (homePresenter != null) {
            homePresenter.unregisterCallback(this);
        }
    }

    @Override
    protected void loadData() {
        //加载数据
        homePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        //网络请求返回的类在这里
        LogUtils.d(this, "HomeFragment中分类加载...");
        if (homePagerAdapter != null) {
            homePagerAdapter.setCategories(categories);
        }
    }




}
