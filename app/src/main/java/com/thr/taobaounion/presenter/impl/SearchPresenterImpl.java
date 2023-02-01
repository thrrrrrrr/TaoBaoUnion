package com.thr.taobaounion.presenter.impl;

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

    @Override
    public void saveHistory(String data) {
//        //如果已经存在，就干掉，再更新
//        JsonCacheUtil jsonCacheUtil = JsonCacheUtil.getInstance();
//        Histories data = jsonCacheUtil.getValue(KEY_HISTORIES, Histories.class);
//        if (data != null && data.getHistories() != null) {
//            List<String> historiesList = data.getHistories();
//            if (historiesList.contains(history)) {
//                historiesList.remove(history);
//            }
//        }//去重完成
//
//        //对个数限制
    }

    @Override
    public void getHistories() {
        List<String> temp = new ArrayList<>();
        temp.add("aa");
        temp.add("bb");
        temp.add("cc");
        mCallback.onHistoriesLoaded(temp);
    }

    @Override
    public void delHistories() {
        //缓存中清除历史记录缓存
    }

    @Override
    public void doSearch(String keyWord) {
        mKeyWord = keyWord;
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
                mCallback.onEmpty();
            } else {
                mCallback.onSearchSuccess(body); //把数据传给ui层
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
