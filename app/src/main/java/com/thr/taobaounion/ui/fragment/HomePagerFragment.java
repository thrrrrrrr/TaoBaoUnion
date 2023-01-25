package com.thr.taobaounion.ui.fragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.presenter.impl.CategoryPagerPresenterImpl;
import com.thr.taobaounion.ui.adapter.HomePagerContentAdapter;
import com.thr.taobaounion.utils.Constants;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.view.ICategoryPagerCallback;

import java.util.List;

import butterknife.BindView;

public class HomePagerFragment extends BaseFragment implements ICategoryPagerCallback {

    private ICategoryPagerPresenter categoryPagerPresenter;
    private int materialId;
    private HomePagerContentAdapter mContentListAdapter;

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        //向fragment传数据，传bundle
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @BindView(R.id.home_pager_content_list)
    public RecyclerView mContentList;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View view) {
        //设置布局管理器
        mContentList.setLayoutManager(new LinearLayoutManager(getContext()));
        //创建适配器
        mContentListAdapter = new HomePagerContentAdapter();
        //设置适配器
        mContentList.setAdapter(mContentListAdapter);
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
        materialId = arguments.getInt(Constants.KEY_HOME_PAGER_ID);
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

    }
}
