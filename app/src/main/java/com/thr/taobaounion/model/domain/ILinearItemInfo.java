package com.thr.taobaounion.model.domain;

public interface ILinearItemInfo extends IBaseInfo {
    String getTitle();

    String getPict_url();

    int getCoupon_amount();

    double getZk_final_price();

    int getVolume();
}
