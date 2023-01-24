package com.thr.taobaounion.presenter.impl;

import androidx.annotation.RestrictTo;

import com.thr.taobaounion.model.API;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.presenter.IHomePresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.RetrofitManager;
import com.thr.taobaounion.view.IHomeCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

public class HomePresenterImpl implements IHomePresenter {
    private IHomeCallback mcallback = null;

    @Override
    public void getCategories() {
        Retrofit retrofit = RetrofitManager.getInstance().getRetrofit();
        API api = retrofit.create(API.class);
        Call<Categories> task = api.getCategories();
        task.enqueue(new Callback<Categories>() {
            @Override
            public void onResponse(Call<Categories> call, Response<Categories> response) {
                //成功
                int code = response.code();
                LogUtils.d(this, "http请求返回码：" + code);
                if (response.isSuccessful()) {
                    //成功
                    Categories categories = response.body();
                    //LogUtils.d(this, categories.toString());
                    if (mcallback != null) {
                        mcallback.onCategoriesLoaded(categories);
                    }
                } else {
                    //返回结果错误
                    LogUtils.e(this, "请求错误...");
                }
            }

            @Override
            public void onFailure(Call<Categories> call, Throwable t) {
                //失败
                LogUtils.e(this, "请求错误...");
            }
        });
    }

    @Override
    public void registerCallback(IHomeCallback callback) {
        this.mcallback = callback;
    }

    @Override
    public void unregisterCallback(IHomeCallback callback) {
        this.mcallback = null;
    }
}
