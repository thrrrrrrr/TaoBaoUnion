package com.thr.taobaounion.ui.adapter;

import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.thr.taobaounion.model.domain.HomePagerContent;

import java.util.ArrayList;
import java.util.List;

public class HomePagerContentAdapter extends RecyclerView.Adapter<HomePagerContentAdapter.InnerHolder> {

    private List<HomePagerContent.DataBean> data = new ArrayList<>();

    @NonNull
    @Override
    public InnerHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull InnerHolder holder, int position) {

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
        public InnerHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
