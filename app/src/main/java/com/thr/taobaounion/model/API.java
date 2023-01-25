package com.thr.taobaounion.model;

import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.model.domain.HomePagerContent;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface API {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET("discovery/{materialId}/{page}")
    Call<HomePagerContent> getHomePagerContent(@Path("materialId")int id, @Path("page")int page);
}
