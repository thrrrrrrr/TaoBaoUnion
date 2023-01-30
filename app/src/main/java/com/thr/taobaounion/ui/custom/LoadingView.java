package com.thr.taobaounion.ui.custom;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.thr.taobaounion.R;
import com.thr.taobaounion.base.BaseFragment;
import com.thr.taobaounion.ui.fragment.HomeFragment;
import com.thr.taobaounion.utils.LogUtils;

public class LoadingView extends androidx.appcompat.widget.AppCompatImageView {

    private float mDegrees = 10;

    private boolean flag = true;

    public LoadingView(Context context) {
        this(context, null);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LoadingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setImageResource(R.mipmap.loading);
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();
        flag = true;
        startRotate();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        stopRotate();
    }

    private void startRotate() {
        post(new Runnable() {
            @Override
            public void run() {
                //判断是否继续线程
                mDegrees = (mDegrees + 10) % 360;
                invalidate();
                LogUtils.d(this, "加载中..." + BaseFragment.isLoading());
                if (BaseFragment.isLoading() && flag) {
                    postDelayed(this, 20);
                }
            }
        });
    }

    private void stopRotate() {
        flag = false;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.rotate(mDegrees, getWidth()/2, getHeight()/2);
        super.onDraw(canvas);
    }
}
