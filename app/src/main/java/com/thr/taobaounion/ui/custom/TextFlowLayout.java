package com.thr.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.thr.taobaounion.R;
import com.thr.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10;

    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;

    public float getmItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setmItemHorizontalSpace(float mItemHorizontalSpace) {
        this.mItemHorizontalSpace = mItemHorizontalSpace;
    }

    public float getmItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setmItemVerticalSpace(float mItemVerticalSpace) {
        this.mItemVerticalSpace = mItemVerticalSpace;
    }

    private List<String> mTextList = new ArrayList<>();

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //去拿相关属性
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mItemHorizontalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_mItemHorizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_mItemVerticalSpace, DEFAULT_SPACE);
        typedArray.recycle();
        LogUtils.d(this, mItemHorizontalSpace + "  " + mItemVerticalSpace);
    }

    public void setTextList(List<String> textList) {
        this.mTextList = textList;
        for (String text : mTextList) {
            TextView textView = new TextView(getContext());
            //TODO 添加子View
            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            view.setText(text);
            addView(view);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //测量
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
    }
}
