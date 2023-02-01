package com.thr.taobaounion.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.thr.taobaounion.R;
import com.thr.taobaounion.utils.LogUtils;

import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment {
    private static State currentState = State.NONE;

    public static boolean isLoading() {
        return currentState == State.LOADING;
    }

    private View loadingView;
    private View successView;
    private View errorView;
    private View emptyView;

    public enum State {
        NONE, LOADING, SUCCESS, ERROR, EMPTY
    }

    private Unbinder bind;
    private FrameLayout baseContainer;

    @OnClick(R.id.network_error_tips)
    public void retry() {
        //点击了重新加载
        LogUtils.d(this, "重新加载");
        onRetryClick();
    }

    protected void onRetryClick() {
        //子类实现重新加载的逻辑
    }

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        //采用帧布局挖个坑，填四种状态的碎片
        View rootView = loadRootView(inflater, container);
        baseContainer = rootView.findViewById(R.id.base_container);
        loadStateView(inflater, container); //首页、特惠、搜索中多种状态的碎片的叠加
        bind = ButterKnife.bind(this, rootView); //初始化黄油刀
        initView(rootView); // 子类重写,子类都启动
        initPresenter();// 子类重写
        initListener();//子类重写
        loadData();// 子类重写
        return rootView;
    }


    protected View loadRootView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.base_fragment_layout, container, false);
    }

    /**
     * 加载各种状态的视图
     *
     * @param inflater
     * @param container
     */
    private void loadStateView(LayoutInflater inflater, ViewGroup container) {
        //成功的View
        successView = loadSuccessView(inflater, container);
        baseContainer.addView(successView);
        //Loading的View
        loadingView = loadLoadingView(inflater, container);
        baseContainer.addView(loadingView);
        //错误页面
        errorView = loadErrorView(inflater, container);
        baseContainer.addView(errorView);
        //空界面
        emptyView = loadEmptyView(inflater, container);
        baseContainer.addView(emptyView);
        setUpState(State.NONE);
    }

    /**
     * 子类通过调用切换不同的状态
     *
     * @param state
     */
    public void setUpState(State state) {
        this.currentState = state;
        //是否成功
        successView.setVisibility(currentState == State.SUCCESS ? View.VISIBLE : View.GONE);
        //是否加载中
        loadingView.setVisibility(currentState == State.LOADING ? View.VISIBLE : View.GONE);
        //error yes or no
        errorView.setVisibility(currentState == State.ERROR ? View.VISIBLE : View.GONE);
        //空
        emptyView.setVisibility(currentState == State.EMPTY ? View.VISIBLE : View.GONE);

    }

    protected View loadSuccessView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container) {
        int res = getRootViewResId();
        return inflater.inflate(res, container, false);
    }

    protected View loadLoadingView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_loading, container, false);
    }

    protected View loadErrorView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_error, container, false);
    }

    protected View loadEmptyView(LayoutInflater inflater, ViewGroup container) {
        return inflater.inflate(R.layout.fragment_empty, container, false);
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

    protected void initListener() {
        //创建监听器
    }

    protected void loadData() {
        //加载数据
    }

    protected abstract int getRootViewResId();

}
