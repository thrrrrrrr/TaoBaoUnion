package com.thr.taobaounion.presenter;

import com.thr.taobaounion.base.IBasePresenter;
import com.thr.taobaounion.view.ISearchCallback;

public interface ISearchPresenter extends IBasePresenter<ISearchCallback> {


    /**
     * 保存历史记录
     */
    void saveHistory(String history);

    /**
     * 获取搜索历史
     */
    void getHistories();

    /**
     * 删除搜索历史
     */
    void delHistories();

    /**
     * 搜索关键词
     * @param keyWord
     */
    void doSearch(String keyWord);

    /**
     * 加载更多
     */
    void loadMore();

    /**
     * 获取推荐词
     */
    void getRecommendWords();
}
