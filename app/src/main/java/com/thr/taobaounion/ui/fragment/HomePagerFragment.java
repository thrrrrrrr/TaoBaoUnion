package com.thr.taobaounion.ui.fragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.thr.taobaounion.utils.Constants;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter categoryPagerPresenter;

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
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initPresenter() { //初始化Presenter
        categoryPagerPresenter = CategoryPagerPresenterImpl.getInstance();
        categoryPagerPresenter.registerViewCallback(this);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_ID);
        LogUtils.d(this, "title: " + title + ", " + "materialId: " + materialId);
        //TODO：加载数据
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.getContentByCategoryId(materialId);
        }
    }

    @Override //释放presenter
    protected void release() {
        if (categoryPagerPresenter != null) {
            categoryPagerPresenter.unregisterViewCallback(this);
        }
    }

    //均是CallBack接口中的
    @Override
    public void onContentLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onNetworkError(int categoryId) {

    }

    @Override
    public void onLoading(int categoryId) {

    }

    @Override
    public void onEmpty(int categoryId) {

    }

    @Override
    public void onLoaderMoreError(int categoryId) {

    }

    @Override
    public void onLoaderMoreEmpty(int categoryId) {

    }

    @Override
    public void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents) {

    }

    @Override
    public void onLopperListLoaded(List<HomePagerContent.DataBean> contents) {

    }
}
