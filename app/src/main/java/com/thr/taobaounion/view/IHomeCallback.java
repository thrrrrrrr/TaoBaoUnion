package com.thr.taobaounion.view;

import com.thr.taobaounion.model.domain.Categories;

public interface IHomeCallback {

    void onCategoriesLoaded(Categories categories);

    void onNetworkError();

    void onLoading();

    void onEmpty();
}
