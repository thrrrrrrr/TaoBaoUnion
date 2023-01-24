package com.thr.taobaounion.ui.fragment;

import android.view.View;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;

public class SearchFragment extends BaseFragment {
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_search;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
    }
}
