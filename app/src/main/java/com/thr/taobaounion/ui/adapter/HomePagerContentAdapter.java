package com.thr.taobaounion.ui.adapter;

import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.model.GlideUrl;
import com.thr.taobaounion.R;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.UrlUtils;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    private List<HomePagerContent.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_home_pager_content, parent, false);
        return new InnerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        HomePagerContent.DataBean dataBean = data.get(position);
        //设置数据
        holder.setData(dataBean);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.goods_cover)
        public ImageView cover;

        @BindView(R.id.goods_title)
        public TextView title;

        @BindView(R.id.goods_after_off_price)
        public TextView goodsAfterOffPrice;

        @BindView(R.id.goods_off_price)
        public TextView goodsOffPrice;

        @BindView(R.id.goods_original_price)
        public TextView goodsOriginalPrice;

        @BindView(R.id.goods_sell_count)
        public TextView goodsSellCount;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setData(HomePagerContent.DataBean dataBean) {
            //
            title.setText(dataBean.getTitle());
            //
            String url = UrlUtils.getCoverPath(dataBean.getPict_url());
            Glide.with(itemView.getContext()).load(url).into(cover);
            //
            LogUtils.d(this, "优惠" + dataBean.getCoupon_amount());
            goodsOffPrice.setText(String.format(itemView.getContext().getString(R.string.item_off_price), dataBean.getCoupon_amount()));
            //
            double finalPrice = dataBean.getZk_final_price() - dataBean.getCoupon_amount();
            LogUtils.d(this, "折后价" + finalPrice);
            goodsAfterOffPrice.setText(String.format("%.2f", finalPrice));
            //
            LogUtils.d(this, "原价" + dataBean.getZk_final_price());
            goodsOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            goodsOriginalPrice.setText(String.format(itemView.getContext().getString(R.string.item_origin_price), dataBean.getZk_final_price()));
            //
            goodsSellCount.setText(String.format(itemView.getContext().getString(R.string.item_sell_count), dataBean.getVolume()));
        }
    }
}