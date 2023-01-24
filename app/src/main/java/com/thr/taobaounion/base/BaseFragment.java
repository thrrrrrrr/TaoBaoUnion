package com.thr.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thr.taobaounion.R;

public abstract class BaseFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = loadRootView(inflater, container, savedInstanceState);
        initPresenter();
        loadData();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        release();
    }

    protected void release() {
        //释放资源
    }

    protected void initPresenter() {
        //创建presenter
    }

    protected void loadData() {
        //加载数据
    }

    private View loadRootView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        int res = getRootViewResId();
        return inflater.inflate(res, container, false);
    }

    protected abstract int getRootViewResId();

}
