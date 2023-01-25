package com.thr.taobaounion.view;

import com.thr.taobaounion.base.IBaseCallback;
import com.thr.taobaounion.model.domain.HomePagerContent;

import java.util.List;

public interface ICategoryPagerCallback extends IBaseCallback {

    int getCategoryId();

    //数据加载回来
    void onContentLoaded(List<HomePagerContent.DataBean> contents);

//    void onNetworkError();
//
//    void onLoading();
//
//    void onEmpty();

    //下滑加载的动作
    void onLoaderMoreError();

    void onLoaderMoreEmpty();

    void onLoaderMoreLoaded(List<HomePagerContent.DataBean> contents);

    //轮播图
    void onLopperListLoaded(List<HomePagerContent.DataBean> contents);
}
