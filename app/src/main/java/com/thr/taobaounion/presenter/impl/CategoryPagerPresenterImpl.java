package com.thr.taobaounion.presenter.impl;

import com.thr.taobaounion.model.API;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.RetrofitManager;
import com.thr.taobaounion.view.ICategoryPagerCallback;

import java.net.HttpURLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    //统一用在Utils里了
//    private CategoryPagerPresenterImpl() {}
//
//    private static ICategoryPagerPresenter sInstance = new CategoryPagerPresenterImpl();
//
//    public static ICategoryPagerPresenter getInstance() {
//        if (sInstance == null) {
//            sInstance = new CategoryPagerPresenterImpl();
//        }
//        return sInstance;
//    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoading();
            }
        }
        //根据分类id去加载内容
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, DEFAULT_PAGE);
        }
        //网络加载
        Call<HomePagerContent> task = createNetworktask(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(this, "请求内容code:" + code);
                if (response.isSuccessful()) {
                    HomePagerContent pageContent = response.body();
                    LogUtils.d(this, pageContent.toString());
                    //把数据给到ui
                    handleHomePagerContentResult(pageContent, categoryId);
                } else {
                    LogUtils.d(this, "请求内容失败");
                    handleNetworkError(categoryId);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(this, "请求内容失败:" + t.toString());
                handleNetworkError(categoryId);
            }
        });
    }

    private Call<HomePagerContent> createNetworktask(int categoryId, Integer targetPage) {
//        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
//        API api = retrofit.create(API.class);
        Call<HomePagerContent> task = api.getHomePagerContent(categoryId, targetPage);
        return task;
    }

    private void handleNetworkError(int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (categoryId == callback.getCategoryId()) {
                callback.onNetworkError();
            }
        }
    }

    private void handleHomePagerContentResult(HomePagerContent pageContent, int categoryId) {
        //通知ui曾更新数据
        List<HomePagerContent.DataBean> data = pageContent.getData();
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (pageContent == null | pageContent.getData().size() == 0) {
                    callback.onEmpty();
                } else {
                    List<HomePagerContent.DataBean> looperData = data.subList(data.size() - 5, data.size());
                    callback.onLopperListLoaded(looperData);
                    callback.onContentLoaded(data);
                }
            }
        }
    }

    @Override
    public void loaderMore(int categoryId) {
        //加载更多的数据
        //拿到当前页码
        Integer currentPage = pagesInfo.get(categoryId);
        //页码++
        currentPage++;
        pagesInfo.put(categoryId, currentPage);
        //加载数据
        Call<HomePagerContent> task = createNetworktask(categoryId, currentPage);
        //处理结果
        Integer finalCurrentPage = currentPage;
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                //结果
                int code = response.code();
                LogUtils.d(this, "loadmore的返回码： " + code);
                if (code == HttpURLConnection.HTTP_OK) {
                    HomePagerContent result = response.body();
                    LogUtils.d(this, result.toString());
                    handleResoutLoadMore(result, categoryId);
                } else {
                    handleLoadMoreError(categoryId, finalCurrentPage);
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                //加载更多失败
                LogUtils.d(this, "加载更多失败" + t.toString());
                handleLoadMoreError(categoryId, finalCurrentPage);
            }
        });
    }

    private void handleResoutLoadMore(HomePagerContent result, int categoryId) {
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                if (result==null || result.getData().size()==0) {
                    callback.onLoaderMoreEmpty();
                } else {
                    callback.onLoaderMoreLoaded(result.getData());
                }
            }
        }
    }

    private void handleLoadMoreError(int categoryId, int currentPage) {
        //页码减
        currentPage--;
        pagesInfo.put(categoryId, currentPage);
        for (ICategoryPagerCallback callback : callbacks) {
            if (callback.getCategoryId() == categoryId) {
                callback.onLoaderMoreError();
            }
        }
    }

    @Override
    public void reload(int categoryId) {

    }

    private List<ICategoryPagerCallback> callbacks = new ArrayList<>();

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {
        if (!callbacks.contains(callback)) {
            callbacks.add(callback);
        }
    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {
        callbacks.remove(callback);
    }
}
