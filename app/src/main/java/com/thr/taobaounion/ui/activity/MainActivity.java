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
import com.thr.taobaounion.ui.fragment.HomeFragment;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    BottomNavigationView navigationView;//底部导航栏

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initListener();
    }

    private void initListener() {
        navigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.home) {
                Log.d(TAG, "切换到首页");
            } else if (item.getItemId() == R.id.sale) {
                Log.d(TAG, "切换到特惠");
            } else if (item.getItemId() == R.id.search) {
                Log.d(TAG, "切换到搜索");
            }
            return true;
        });
    }

    private void initView() {
        navigationView = findViewById(R.id.main_navigation_bar);


        HomeFragment homeFragment = new HomeFragment();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(R.id.main_page_container, homeFragment);
        transaction.commit();
    }
}