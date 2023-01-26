package com.thr.taobaounion.ui.custom;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

import com.thr.taobaounion.utils.LogUtils;

public class TbNestedSerollView extends NestedScrollView {

    private int currentScroll = 0;
    private int headerHeight = 460;

    public TbNestedSerollView(@NonNull Context context) {
        super(context);
    }

    public TbNestedSerollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public TbNestedSerollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setHeaderHeight(int i) {
        headerHeight = i;
    }

    @Override
    public void onNestedPreScroll(@NonNull View target, int dx, int dy, @NonNull int[] consumed, int type) {
        super.onNestedPreScroll(target, dx, dy, consumed, type);
        if (currentScroll < headerHeight) {
            scrollBy(dx, dy);
            consumed[0] = dx;
            consumed[1] = dy;
        }
    }

    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        currentScroll = t;
        super.onScrollChanged(l, t, oldl, oldt);
    }
}
