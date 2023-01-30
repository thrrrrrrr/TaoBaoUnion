package com.thr.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.thr.taobaounion.model.domain.HomePagerContent;
import com.thr.taobaounion.utils.UrlUtils;

import java.util.ArrayList;
import java.util.List;

public class LooperPagerAdapter extends PagerAdapter {

    List<HomePagerContent.DataBean> data = new ArrayList<>();
    private OnLooperPageItemClickListener mItemClickListener = null;

    @Override
    public int getCount() {
        return Integer.MAX_VALUE;
    }

    public int getSize() {
        return data.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        //处理下标越界
        int realPosition = position % data.size();
        HomePagerContent.DataBean dataBean = data.get(realPosition);
        String coverPath = UrlUtils.getCoverPath(dataBean.getPict_url());
        ImageView iv = new ImageView(container.getContext());
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams
                (ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        iv.setLayoutParams(layoutParams);
        iv.setScaleType(ImageView.ScaleType.CENTER_CROP);
        Glide.with(container.getContext()).load(coverPath).into(iv);
        container.addView(iv);

        //设置图片点击
        iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mItemClickListener.onLooperItemClick(dataBean);
            }
        });

        return iv;
    }

    public void setOnLooperPageItemClickListener(OnLooperPageItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnLooperPageItemClickListener {
        void onLooperItemClick(HomePagerContent.DataBean item);
    };

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }

    public void setData(List<HomePagerContent.DataBean> contents) {
        data.clear();
        data.addAll(contents);
        notifyDataSetChanged();
    }
}
