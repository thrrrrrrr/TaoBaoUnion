package com.thr.taobaounion.view;

import com.thr.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback {
    //数据加载回来
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

    //三种状态 + success
    void onNetworkError(int categoryId);

    void onLoading(int categoryId);

    void onEmpty(int categoryId);

    //下滑加载的动作
    void onLoaderMoreError(int categoryId);

    void onLoaderMoreEmpty(int categoryId);

    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);
    //轮播图
    void onLopperListLoaded(List<HomePagerContent.DataBean> contents);
}
