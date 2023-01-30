package com.thr.taobaounion.ui.adapter;

import android.app.role.RoleManager;
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
    private OnListItemClickListener mItemClickListener = null;

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
        LogUtils.d(this, "position: " + position);
        holder.setData(dataBean);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    //设置监听器??????
    public void setOnListItemClickListener(OnListItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnListItemClickListener {
        void onItemClick(HomePagerContent.DataBean item);
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

    public void addData(List<HomePagerContent.DataBean> contents) {
        int oldSize = data.size();
        data.addAll(contents);
        //通知更新
        notifyItemRangeChanged(oldSize, contents.size());
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
            ViewGroup.LayoutParams layoutParams = cover.getLayoutParams();
            int size = layoutParams.width;
            size = size/100*100 + 100;
            String url = UrlUtils.getCoverPath(dataBean.getPict_url(), size);
            Glide.with(itemView.getContext()).load(url).into(cover);
            //
            //LogUtils.d(this, "优惠" + dataBean.getCoupon_amount());
            goodsOffPrice.setText(String.format(itemView.getContext().getString(R.string.item_off_price), dataBean.getCoupon_amount()));
            //
            double finalPrice = dataBean.getZk_final_price() - dataBean.getCoupon_amount();
            //LogUtils.d(this, "折后价" + finalPrice);
            goodsAfterOffPrice.setText(String.format("%.2f", finalPrice));
            //
            //LogUtils.d(this, "原价" + dataBean.getZk_final_price());
            goodsOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            goodsOriginalPrice.setText(String.format(itemView.getContext().getString(R.string.item_origin_price), dataBean.getZk_final_price()));
            //
            goodsSellCount.setText(String.format(itemView.getContext().getString(R.string.item_sell_count), dataBean.getVolume()));
        }
    }

}
