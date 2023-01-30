package com.thr.taobaounion.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import com.thr.taobaounion.utils.LogUtils;

public class AutoLooperViewPager extends ViewPager {


    public AutoLooperViewPager(@NonNull Context context) {
        super(context);
    }

    public AutoLooperViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    private boolean isLoop = true;

    public void startLoop() {
        //先拿到当前的位置
        isLoop = true;
        post(new Runnable() {
            @Override
            public void run() {
                int currentItem = getCurrentItem();
                currentItem++;
                setCurrentItem(currentItem);
                if (isLoop) {
                    postDelayed(this, 3000);
                }
            }
        });
    }

    public void stopLoop() {
        isLoop = false;
    }
}
