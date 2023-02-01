package com.thr.taobaounion.model;

import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.model.domain.SaleContent;
import com.thr.taobaounion.model.domain.TicketParams;
import com.thr.taobaounion.model.domain.TicketResult;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface API {

    @GET("discovery/categories")
    Call<Categories> getCategories();

    @GET("discovery/{materialId}/{page}")
    Call<HomePagerContent> getHomePagerContent(@Path("materialId")int id, @Path("page")int page);

    @POST("tpwd")
    Call<TicketResult> getTicket(@Body TicketParams ticketParams);

    @GET("onSell/{page}")
    Call<SaleContent> getSaleContent(@Query("page") int page);
}
