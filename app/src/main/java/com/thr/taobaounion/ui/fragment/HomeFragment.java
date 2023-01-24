package com.thr.taobaounion.ui.fragment;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.presenter.IHomePresenter;
import com.thr.taobaounion.presenter.impl.HomePresenterImpl;
import com.thr.taobaounion.view.IHomeCallback;

public class HomeFragment extends BaseFragment implements IHomeCallback {

    private IHomePresenter homePresenter;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home;
    }

    @Override
    protected void initPresenter() {
        //加载presenter
        homePresenter = new HomePresenterImpl();
        homePresenter.registerCallback(this);
    }

    @Override
    protected void loadData() {
        //加载数据
        homePresenter.getCategories();
    }

    @Override
    public void onCategoriesLoaded(Categories categories) {
        //加载是数据会从这里回来
    }

    @Override
    protected void release() {
        if (homePresenter != null) {
            homePresenter.unregisterCallback(this);
        }
    }
}
