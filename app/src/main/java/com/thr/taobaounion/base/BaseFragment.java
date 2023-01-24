package com.thr.taobaounion.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thr.taobaounion.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {

    private Unbinder bind;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = loadRootView(inflater, container, savedInstanceState);
        bind = ButterKnife.bind(this, view);
        initView(view);
        initPresenter();
        loadData();
        return view;
    }

    protected void initView(View view) {
        //初始化控件，子类覆盖
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (bind != null) {
            bind.unbind();
        }
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
