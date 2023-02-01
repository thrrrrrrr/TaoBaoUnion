package com.thr.taobaounion.presenter.impl;

import com.thr.taobaounion.model.domain.SearchRecommend;
import com.thr.taobaounion.model.domain.SearchResult;
import com.thr.taobaounion.presenter.ISearchPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.view.ISearchCallback;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements ISearchPresenter {

    private ISearchCallback mCallback = null;

    private int currentPage = 0;

    @Override
    public void getHistories() {

    }

    @Override
    public void delHistories() {

    }

    @Override
    public void doSearch(String keyWord) {
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<SearchResult> task = api.getSearchResult(currentPage, keyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                LogUtils.d(this, "搜索关键词返回码： " + response.code());
                if (response.isSuccessful()) {
//                    LogUtils.d(this, response.body().toString());
                    onSuccess(response.body());
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                onError();
            }
        });
    }

    private void onError() {
        LogUtils.d(this, "搜索失败");
        if (mCallback != null) {
            mCallback.onNetworkError();
        }
    }

    private void onSuccess(SearchResult saleContent) {
        LogUtils.d(this, "搜索成功");
        if (mCallback != null) {
            int size = saleContent.getList().size(); //getList出错就返回空列表，判空
            if (size == 0) {
                onEmpty();
            } else {
                mCallback.onSearchSuccess(saleContent); //数据传给ui层
            }
        }
    }

    private void onEmpty() {
        LogUtils.d(this, "搜索为空");
        if (mCallback != null) {
            mCallback.onEmpty();
        }
    }

    @Override
    public void loadMore() {

    }

    @Override
    public void getRecommendWords() {
        Call<SearchRecommend> task = api.getSearchRecommend();
        task.enqueue(new Callback<SearchRecommend>() {
            @Override
            public void onResponse(Call<SearchRecommend> call, Response<SearchRecommend> response) {
                LogUtils.d(this, "推荐词状态码：" + response.code());
                if (response.isSuccessful()) {
                    List<String> data = response.body().getList();
//                    LogUtils.d(this, data.toString());
                    mCallback.onRecommendWordsLoaded(data);
                } else {
                    //
                }
            }

            @Override
            public void onFailure(Call<SearchRecommend> call, Throwable t) {
                //
            }
        });
    }

    @Override
    public void registerViewCallback(ISearchCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISearchCallback callback) {
        mCallback = null;
    }
}
