package com.thr.taobaounion.view;

import com.thr.taobaounion.base.BaseCallback;
import com.thr.taobaounion.model.domain.Categories;

public interface IHomeCallback extends BaseCallback {

    void onCategoriesLoaded(Categories categories);

}
