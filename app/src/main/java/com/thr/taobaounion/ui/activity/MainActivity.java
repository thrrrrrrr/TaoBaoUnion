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
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.ui.fragment.HomeFragment;
import com.thr.taobaounion.ui.fragment.SaleFragment;
import com.thr.taobaounion.ui.fragment.SearchFragment;
import com.thr.taobaounion.utils.LogUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends AppCompatActivity {

    @BindView(R.id.main_navigation_bar)
    public BottomNavigationView navigationView;//底部导航栏
    private HomeFragment homeFragment;
    private SaleFragment saleFragment;
    private SearchFragment searchFragment;//碎片
    private FragmentManager fm;//碎片管理


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initListener();
        initFragment();
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
                LogUtils.d(this.getClass(), "切换到首页");
                switchFragment(homeFragment);
            } else if (item.getItemId() == R.id.sale) {
                LogUtils.d(this.getClass(), "切换到特惠");
                switchFragment(saleFragment);
            } else if (item.getItemId() == R.id.search) {
                LogUtils.d(this.getClass(), "切换到搜索");
                switchFragment(searchFragment);
            }
            return true;
        });
    }

    private void switchFragment(BaseFragment targetFragment) {
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_page_container, targetFragment);
        transaction.commit();
    }
}