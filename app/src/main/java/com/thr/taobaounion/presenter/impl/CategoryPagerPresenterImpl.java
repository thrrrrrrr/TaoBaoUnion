package com.thr.taobaounion.presenter.impl;

import android.util.Log;

import com.thr.taobaounion.model.API;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.presenter.ICategoryPagerPresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.RetrofitManager;
import com.thr.taobaounion.view.ICategoryPagerCallback;

import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class CategoryPagerPresenterImpl implements ICategoryPagerPresenter {

    private Map<Integer, Integer> pagesInfo = new HashMap<>();

    public static final int DEFAULT_PAGE = 1;

    private CategoryPagerPresenterImpl() {}

    private static ICategoryPagerPresenter sInstance = null;

    public static ICategoryPagerPresenter getInstance() {
        if (sInstance == null) {
            sInstance = new CategoryPagerPresenterImpl();
        }
        return sInstance;
    }

    @Override
    public void getContentByCategoryId(int categoryId) {
        //根据分类id去加载内容
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Integer targetPage = pagesInfo.get(categoryId);
        if (targetPage == null) {
            targetPage = DEFAULT_PAGE;
            pagesInfo.put(categoryId, DEFAULT_PAGE);
        }
        Call<HomePagerContent> task = api.getHomePagerContent(categoryId, targetPage);
        task.enqueue(new Callback<HomePagerContent>() {
            @Override
            public void onResponse(Call<HomePagerContent> call, Response<HomePagerContent> response) {
                int code = response.code();
                LogUtils.d(this, "请求内容code:" + code);
                if (response.isSuccessful()) {
                    HomePagerContent pagerContent = response.body();
                    LogUtils.d(this, pagerContent.toString());
                } else {
                    LogUtils.d(this, "请求内容失败");
                }
            }

            @Override
            public void onFailure(Call<HomePagerContent> call, Throwable t) {
                LogUtils.d(this, "请求内容失败:" + t.toString());
            }
        });
    }

    @Override
    public void loaderMore(int categoryId) {

    }

    @Override
    public void reload(int categoryId) {

    }

    @Override
    public void registerViewCallback(ICategoryPagerCallback callback) {

    }

    @Override
    public void unregisterViewCallback(ICategoryPagerCallback callback) {

    }
}
