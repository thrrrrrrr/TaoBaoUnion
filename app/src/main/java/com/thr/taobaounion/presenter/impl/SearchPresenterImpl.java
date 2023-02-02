package com.thr.taobaounion.presenter.impl;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.thr.taobaounion.model.domain.Histories;
import com.thr.taobaounion.model.domain.SearchRecommend;
import com.thr.taobaounion.model.domain.SearchResult;
import com.thr.taobaounion.presenter.ISearchPresenter;
import com.thr.taobaounion.utils.JsonCacheUtil;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.view.ISearchCallback;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SearchPresenterImpl implements ISearchPresenter {

    private ISearchCallback mCallback = null;

    private int currentPage = 0;

    private String mKeyWord;

    private final String KEY_HISTORIES = "key_histories";

    private final int MAX_COUNT = 10;

    @Override
    public void getHistories() {
//        ArrayList<String> list = new ArrayList<>();
//        list.add("aaa");
//        list.add("aaa");
//        list.add("aasdafa");
//        list.add("aabbbba");
//        list.add("aabbbba");
//        list.add("aabbbba");
        Histories histories = JsonCacheUtil.getInstance().getValue(KEY_HISTORIES);
        if (histories == null || histories.getList() == null || histories.getList().size() == 0) {
            mCallback.onHistoriesLoaded(null);
            LogUtils.d(this, "历史记录为空");
        }else {
            mCallback.onHistoriesLoaded(histories.getList());
            LogUtils.d(this, "历史记录加载：" + histories.getList());
        }
    }

    @Override
    public void delHistories() {
        JsonCacheUtil.getInstance().delCache(KEY_HISTORIES);
        mCallback.onHistoriesLoaded(null);
    }

    public void saveHistory(String history) {
        Histories histories = JsonCacheUtil.getInstance().getValue(KEY_HISTORIES);
        List<String> list = null;
        if (histories == null) {
            list = new ArrayList<>();
        } else {
            list = histories.getList();
        }
        if (list.contains(history)) {
            list.remove(history);
            list.add(history);
        } else {
            list.add(history);
        }
        if (list.size() > MAX_COUNT) {
            list = list.subList(1, MAX_COUNT+1);
        }
        JsonCacheUtil.getInstance().saveCache(KEY_HISTORIES, new Histories(new ArrayList<>(list)));
        mCallback.onHistoriesLoaded(list);
    }

    @Override
    public void doSearch(String keyWord) {
        mKeyWord = keyWord;
        this.saveHistory(mKeyWord);
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
        currentPage++;
        Call<SearchResult> task = api.getSearchResult(currentPage, mKeyWord);
        task.enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                int code = response.code();
                LogUtils.d(this, "加载更多状态码 : " + code);
                if (response.isSuccessful()) {
                    onLoadedSuccess(response.body());
                } else {
                    onLoadedError();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                mCallback.onLoadMoreError();
            }
        });
    }

    private void onLoadedError() {
        LogUtils.d(this, "加载更多失败");
        currentPage--;
        if (mCallback != null) {
            mCallback.onLoadMoreError();
        }
    }

    private void onLoadedSuccess(SearchResult body) {
        LogUtils.d(this, "加载更多");
        if (mCallback != null) {
            int size = body.getList().size();
            if (size == 0) {
                mCallback.onLoadMoreEmpty();
            } else {
                mCallback.onLoadMoreLoaded(body); //把数据传给ui层
            }
        }
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
