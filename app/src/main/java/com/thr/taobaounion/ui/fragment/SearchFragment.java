package com.thr.taobaounion.ui.fragment;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.SearchResult;
import com.thr.taobaounion.presenter.ISearchPresenter;
import com.thr.taobaounion.ui.custom.TextFlowLayout;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.PresenterManager;
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.view.ISearchCallback;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SearchFragment extends BaseFragment implements ISearchCallback, TextFlowLayout.OnFlowTextItemClickListener {

    private ISearchPresenter searchPresenter;

    private String mKeyWord;

    @SuppressLint("NonConstantResourceId")
    @BindView(R.id.search_flow_text_layout)
    public TextFlowLayout textFlowLayout;

    @BindView(R.id.search_edit_view)
    public EditText searchEditView;

    @BindView(R.id.search_icon)
    public ImageView searchIcon;

    private void searchClick() {
        String s = searchEditView.getText().toString();
        if (!TextUtils.isEmpty(s)) {
            searchPresenter.doSearch(s);
        }
    }


    @Override
    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_search_layout, container, false);
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void initListener() {
        textFlowLayout.setOnFlowTextItemClickListener(this);
    }

    @Override
    protected void initPresenter() {
        searchPresenter = PresenterManager.getInstance().getSearchPresenter();
        searchPresenter.registerViewCallback(this);
        //获取推荐词
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
        //回显推荐词
        LogUtils.d(this, recommendWords.toString());
        textFlowLayout.setTextList(recommendWords);
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

    @Override
    public void onFlowItemClick(String text) {
        searchPresenter.doSearch(text);
    }
}
