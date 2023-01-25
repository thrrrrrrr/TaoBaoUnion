package com.thr.taobaounion.view;

import com.thr.taobaounion.base.IBaseCallback;
import com.thr.taobaounion.model.domain.Categories;

public interface IHomeCallback extends IBaseCallback {

    void onCategoriesLoaded(Categories categories);

}
