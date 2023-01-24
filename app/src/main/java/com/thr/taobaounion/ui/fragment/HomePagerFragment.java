package com.thr.taobaounion.ui.fragment;

import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.model.domain.Categories;
import com.thr.taobaounion.utils.Constants;
import com.thr.taobaounion.utils.LogUtils;

public class HomePagerFragment extends BaseFragment {

    public static HomePagerFragment newInstance(Categories.DataBean category) {
        HomePagerFragment homePagerFragment = new HomePagerFragment();
        Bundle bundle = new Bundle();
        //向fragment传数据，传bundle
        bundle.putString(Constants.KEY_HOME_PAGER_TITLE, category.getTitle());
        bundle.putInt(Constants.KEY_HOME_PAGER_ID, category.getId());
        homePagerFragment.setArguments(bundle);
        return homePagerFragment;
    }

    @Override
    protected int getRootViewResId() {
        return R.layout.fragment_home_pager;
    }

    @Override
    protected void initView(View view) {
        setUpState(State.SUCCESS);
    }

    @Override
    protected void loadData() {
        Bundle arguments = getArguments();
        String title = arguments.getString(Constants.KEY_HOME_PAGER_TITLE);
        int materialId = arguments.getInt(Constants.KEY_HOME_PAGER_ID);
        //TODO：加载数据
        LogUtils.d(this, "title: " + title);
        LogUtils.d(this, "materialId: " + materialId);
    }
}
