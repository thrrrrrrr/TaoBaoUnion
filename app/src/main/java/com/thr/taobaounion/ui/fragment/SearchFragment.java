package com.thr.taobaounion.ui.fragment;

import android.util.Log;
import android.view.View;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.SearchResult;
import com.thr.taobaounion.presenter.ISearchPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.view.ISearchCallback;

import java.util.List;

public class SearchFragment extends BaseFragment implements ISearchCallback {

    private ISearchPresenter searchPresenter;

    private String mKeyWord;

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initPresenter() {
        searchPresenter = PresenterManager.getInstance().getSearchPresenter();
        searchPresenter.registerViewCallback(this);
        searchPresenter.getRecommendWords();
//        searchPresenter.doSearch("连衣裙");
    }

    @Override
    protected void release() {
        if (searchPresenter != null) {
            searchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
        searchPresenter.doSearch(mKeyWord);
    }

    @Override
    public void onHistoriesLoaded(List<String> list) {

    }

    @Override
    public void onHistoriesDeleted() {

    }

    @Override
    public void onRecommendWordsLoaded(List<String> recommendWords) {
        //TODO 回显推荐词
        LogUtils.d(this, recommendWords.toString());
    }

    @Override
    public void onSearchSuccess(SearchResult searchResult) {
        //TODO 回显商品列表
        setUpState(State.SUCCESS);
        LogUtils.d(this, searchResult.toString());
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
    public void onLoadMoreLoaded() {
        ToastUtils.show("加载了" + "xx"+"条数据");
    }

    @Override
    public void onLoadMoreError() {
        ToastUtils.show("加载更多失败");
    }

    @Override
    public void onLoadMoreEmpty() {
        ToastUtils.show("没有更多数据了");
    }


}
