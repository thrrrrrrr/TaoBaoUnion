package com.thr.taobaounion.model;

import com.thr.taobaounion.model.domain.Categories;

import retrofit2.Call;
import retrofit2.http.GET;

public interface API {

    @GET("discovery/categories")
    Call<Categories> getCategories();
}
