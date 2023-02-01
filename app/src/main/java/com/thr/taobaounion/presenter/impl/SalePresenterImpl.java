package com.thr.taobaounion.presenter.impl;

import com.thr.taobaounion.model.domain.SaleContent;
import com.thr.taobaounion.presenter.ISalePresenter;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.ToastUtils;
import com.thr.taobaounion.view.ISaleCallback;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SalePresenterImpl implements ISalePresenter {

    ISaleCallback mCallback = null;

    private int currentPage = 1;

    @Override
    public void getOnSaleContent() {
        if (isLoading) return;

        isLoading = true;
        if (mCallback != null) {
            mCallback.onLoading();
        }
        Call<SaleContent> task = api.getSaleContent(currentPage);
        task.enqueue(new Callback<SaleContent>() {
            @Override
            public void onResponse(Call<SaleContent> call, Response<SaleContent> response) {
                isLoading = false;
                int code = response.code();
                if (response.isSuccessful()) {
                    LogUtils.d(this, response.body().getList().toString());
                    LogUtils.d(this, response.body().getList().get(0).toString());
                    SaleContent saleContent = response.body();
                    onSuccess(saleContent);
                } else {
                    onError();
                }
            }

            @Override
            public void onFailure(Call<SaleContent> call, Throwable t) {
                isLoading = false;
                onError();
            }
        });
    }

    private void onError() {
        if (mCallback != null) {
            mCallback.onNetworkError();
        }
    }

    private void onSuccess(SaleContent saleContent) {
        if (mCallback != null) {
            int size = saleContent.getList().size();
            if (size == 0) {
                onEmpty();
            } else {
                mCallback.onContentLoadedSuccess(saleContent); //数据传给ui层
            }
        }
    }

    private void onEmpty() {
        if (mCallback != null) {
            mCallback.onEmpty();
        }
    }

    @Override
    public void retry() {
        this.getOnSaleContent();
    }

    /**
     * 当前是否在加载更多中
     */
    private boolean isLoading = false;

    @Override
    public void loaderMore() {
        if (isLoading) return;
        //加载更多
        isLoading = true;
        currentPage++;
        Call<SaleContent> task = api.getSaleContent(currentPage);
        task.enqueue(new Callback<SaleContent>() {
            @Override
            public void onResponse(Call<SaleContent> call, Response<SaleContent> response) {
                isLoading = false;
                int code = response.code();
                if (response.isSuccessful()) {
                    LogUtils.d(this, response.body().getList().toString());
                    LogUtils.d(this, response.body().getList().get(0).toString());
                    SaleContent saleContent = response.body();
                    onLoadSuccess(saleContent);
                } else {
                    onLoadError();
                }
            }

            @Override
            public void onFailure(Call<SaleContent> call, Throwable t) {
                isLoading = false;
                onLoadError();
            }
        });

    }

    private void onLoadError() {
        currentPage--;
        mCallback.onMoreLoadedError();
    }

    private void onLoadSuccess(SaleContent saleContent) {
        if (mCallback != null) {
            int size = saleContent.getList().size();
            if (size == 0) {
                mCallback.onMoreLoadedEmpty();
            } else {
                mCallback.onMoreLoaded(saleContent); //数据传给ui层
            }
        }
    }



    @Override
    public void registerViewCallback(ISaleCallback callback) {
        mCallback = callback;
    }

    @Override
    public void unregisterViewCallback(ISaleCallback callback) {
        mCallback = null;
    }
}
