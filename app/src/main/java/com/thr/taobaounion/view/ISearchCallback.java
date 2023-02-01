package com.thr.taobaounion.view;

import com.thr.taobaounion.base.IBaseCallback;
import com.thr.taobaounion.model.domain.SearchResult;

import java.util.List;

public interface ISearchCallback extends IBaseCallback {
    //全部都是回调的方法
    void onHistoriesLoaded();

    void onHistoriesDeleted();

    void onSearchSuccess(SearchResult searchResult);

    void onLoadMoreLoaded();

    void onLoadMoreError();

    void onLoadMoreEmpty();

    void onRecommendWordsLoaded(List<String> recommendWords);
}
