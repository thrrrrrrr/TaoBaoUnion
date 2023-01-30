package com.thr.taobaounion.ui.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseAvtivity;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.ui.fragment.HomeFragment;
import com.thr.taobaounion.ui.fragment.SaleFragment;
import com.thr.taobaounion.ui.fragment.SearchFragment;
import com.thr.taobaounion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class MainActivity extends BaseAvtivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;//底部导航栏
    private HomeFragment homeFragment;
    private SaleFragment saleFragment;
    private SearchFragment searchFragment;//碎片
    private FragmentManager fm;//碎片管理

    @Override
    protected int getLayoutResId() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView() {
        initFragment();
    }

    @Override
    protected void initEvent() {
        initListener();
    }


    private void initFragment() {
        homeFragment = new HomeFragment();
        saleFragment = new SaleFragment();
        searchFragment = new SearchFragment();
        fm = getSupportFragmentManager();
        switchFragment(homeFragment);
    }

    private void initListener() {
        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                LogUtils.d(this, "切换到首页");
                switchFragment(homeFragment);
            } else if (item.getItemId() == R.id.sale) {
                LogUtils.d(this, "切换到特惠");
                switchFragment(saleFragment);
            } else if (item.getItemId() == R.id.search) {
                LogUtils.d(this, "切换到搜索");
                switchFragment(searchFragment);
            }
            return true;
        });
    }

    private BaseFragment lastOneFragment = null;
    private void switchFragment(BaseFragment targetFragment) {
        //修改成 add和 hide的方式来切换，避免重复加载
        FragmentTransaction transaction = fm.beginTransaction();
        if (!targetFragment.isAdded()) {
            transaction.add(R.id.main_page_container, targetFragment);
        }
        if (lastOneFragment != null) {
            transaction.hide(lastOneFragment);
            transaction.show(targetFragment);
        }
        lastOneFragment = targetFragment;
        transaction.commit();
    }


}