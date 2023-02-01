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
        searchPresenter.doSearch("连衣裙");
    }

    @Override
    protected void release() {
        if (searchPresenter != null) {
            searchPresenter.unregisterViewCallback(this);
        }
    }

    @Override
    protected void onRetryClick() {
//        searchPresenter.doSearch("连衣裙");
    }

    @Override
    public void onHistoriesLoaded() {

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

    }

    @Override
    public void onLoadMoreError() {

    }

    @Override
    public void onLoadMoreEmpty() {

    }


}
