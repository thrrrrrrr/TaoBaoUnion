package com.thr.taobaounion.base;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.thr.taobaounion.R;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseAvtivity extends AppCompatActivity {

    private Unbinder bind;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getLayoutResId());
        bind = ButterKnife.bind(this);
        initView();
        initEvent();
        initPresenter();
    }


    protected abstract int getLayoutResId();

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (bind != null) {
            bind.unbind();
        }
        this.relese();
    }

    protected void relese() {
        //子类需要就复写
    }

    protected void initEvent() {
        //
    }

    protected abstract void initView();

    protected void initPresenter() {
        //
    }

}
