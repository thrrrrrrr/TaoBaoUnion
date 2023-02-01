package com.thr.taobaounion.ui.adapter;

import android.graphics.Paint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.thr.taobaounion.R;
import com.thr.taobaounion.model.domain.SaleContent;
import com.thr.taobaounion.utils.LogUtils;
import com.thr.taobaounion.utils.UrlUtils;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class SalePageContentAdapter extends RecyclerView.Adapter<SalePageContentAdapter.InnerHolder> {

    private List<SaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> dataList = new ArrayList<>();
    private SalePageItemClickListener mItemClickListener;

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sale_content, parent, false);
        return new InnerHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
        //绑定数据
        SaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean = dataList.get(position);
        holder.setUI(dataBean);
        //绑定监听器
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mItemClickListener != null) {
                    mItemClickListener.onItemClick(dataBean);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public void setData(SaleContent result) {
        dataList.clear();
        dataList.addAll(result.getList());
        notifyDataSetChanged();
    }

    public void addData(SaleContent moreResult) {
        int oldSize = dataList.size();
        List<SaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean> moreData = moreResult.getList();
        dataList.addAll(moreData);
        notifyItemRangeChanged(oldSize, dataList.size());
    }

    public class InnerHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.sale_title)
        public TextView saleTitle;

        @BindView(R.id.sale_cover)
        public ImageView saleCover;

        @BindView(R.id.sale_original_price)
        public TextView saleOriginalPrice;

        @BindView(R.id.sale_after_off_price)
        public TextView saleAfterOffPrice;

        public InnerHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setUI(SaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean data) {
            saleTitle.setText(data.getTitle());
            ViewGroup.LayoutParams layoutParams = saleCover.getLayoutParams();
            int size = Math.max(layoutParams.width, layoutParams.height);
            size = (size-1)/100*100 + 100;
            String url = UrlUtils.getCoverPath(data.getPict_url(), size);
            Glide.with(saleCover.getContext()).load(url).into(saleCover);
            double finalPrice = data.getZk_final_price() - data.getCoupon_amount();
            //LogUtils.d(this, "折后价" + finalPrice);
            saleAfterOffPrice.setText(String.format(" 券后:%.2f", finalPrice));
            //
            //LogUtils.d(this, "原价" + dataBean.getZk_final_price());
            saleOriginalPrice.setPaintFlags(Paint.STRIKE_THRU_TEXT_FLAG);
            saleOriginalPrice.setText(String.format(itemView.getContext().getString(R.string.item_origin_price), data.getZk_final_price()));
            //
        }
    }

    public interface SalePageItemClickListener {
        void onItemClick(SaleContent.DataBean.TbkDgOptimusMaterialResponseBean.ResultListBean.MapDataBean dataBean);
    }

    public void setItemClickListener(SalePageItemClickListener m) {
        mItemClickListener = m;
    }
}
