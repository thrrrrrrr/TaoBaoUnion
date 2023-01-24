package com.thr.taobaounion.ui.fragment;

import android.view.View;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;

public class HomePagerFragment extends BaseFragment {
    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
    }
}
