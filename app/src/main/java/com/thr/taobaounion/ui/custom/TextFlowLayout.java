package com.thr.taobaounion.ui.custom;

import android.content.Context;
import android.content.res.TypedArray;
import android.text.Layout;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.LongDef;

import com.thr.taobaounion.R;
import com.thr.taobaounion.utils.LogUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TextFlowLayout extends ViewGroup {

    public static final float DEFAULT_SPACE = 10;

    private float mItemHorizontalSpace = DEFAULT_SPACE;
    private float mItemVerticalSpace = DEFAULT_SPACE;
    private List<String> mTextList = new ArrayList<>();
    private int mSelfWidth;
    private int mItemHeight;
    private OnFlowTextItemClickListener mItemClickListener = null;

    public int getContentSize() {
        return mTextList.size();
    }

    public TextFlowLayout(Context context) {
        this(context, null);
    }

    public TextFlowLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
        mItemHorizontalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_mItemHorizontalSpace, DEFAULT_SPACE);
        mItemVerticalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_mItemVerticalSpace, DEFAULT_SPACE);
        typedArray.recycle();
    }

    public void setTextList(List<String> textList) {
        removeAllViews();
        this.mTextList.clear();
        this.mTextList.addAll(textList);
        //遍历内容
        for (String text : mTextList) {
            //添加子view
            TextView item = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
            item.setText(text);
            item.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mItemClickListener != null) {
                        mItemClickListener.onFlowItemClick(text);
                    }
                }
            });
            addView(item);
        }
    }

    private List<List<View>> lines = new ArrayList<>();

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        if (getChildCount() == 0) {
            return;
        }
        //保证每次测量都是空
        List<View> line = null;
        lines.clear();
        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
        //测量
        int childCount = getChildCount();
        for (int i = 0; i < childCount; i++) {
            View itemView = getChildAt(i);
            if (itemView.getVisibility() != VISIBLE) {
                //不需要进行测量
                continue;
            }
            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);

            if (line == null) {
                line = createNewLine(itemView);
            } else {
                if (canBeAdd(itemView, line)) {
                    line.add(itemView);
                } else {
                    line = createNewLine(itemView);
                }
            }
        }
        mItemHeight = getChildAt(0).getMeasuredHeight();
        int selfHeight = (int) (lines.size() * mItemHeight + mItemVerticalSpace * (lines.size() + 1) + 0.5f);
        setMeasuredDimension(mSelfWidth, selfHeight);
    }

    private List<View> createNewLine(View itemView) {
        List<View> line = new ArrayList<>();
        line.add(itemView);
        lines.add(line);
        return line;
    }

    /**
     * 判断当前行是否可以在继续添加数据
     * @param itemView
     * @param line
     */
    private boolean canBeAdd(View itemView, List<View> line) {
        //所有已近添加的子view宽度相加 + (line.size() + 1) * mItemHorizontalSpace + itemView.getMeasuredWidth()
        //条件：如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        int totalWidth = itemView.getMeasuredWidth();
        for (View view : line) {
            //叠加所有已近添加的宽度
            totalWidth += view.getMeasuredWidth();
        }
        //水平间距的宽度
        totalWidth += mItemHorizontalSpace * (line.size() + 1);
        //如果小于/等于当前控件的宽度，则可以添加，否则不能添加
        return totalWidth <= mSelfWidth;
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        //摆放孩子
        int topOffset = (int) mItemHorizontalSpace;
        for (List<View> views : lines) {
            //views是每一行
            int leftOffset = (int) mItemHorizontalSpace;
            for (View view : views) {
                //view是每一行里的每个item
                view.layout(leftOffset, topOffset, leftOffset+view.getMeasuredWidth(), topOffset+view.getMeasuredHeight());
                leftOffset += view.getMeasuredWidth() + mItemHorizontalSpace;
            }
            topOffset += mItemHeight + mItemHorizontalSpace;
        }
    }

    public float getItemHorizontalSpace() {
        return mItemHorizontalSpace;
    }

    public void setItemHorizontalSpace(float itemHorizontalSpace) {
        mItemHorizontalSpace = itemHorizontalSpace;
    }

    public float getItemVerticalSpace() {
        return mItemVerticalSpace;
    }

    public void setItemVerticalSpace(float itemVerticalSpace) {
        mItemVerticalSpace = itemVerticalSpace;
    }

    public void setOnFlowTextItemClickListener(OnFlowTextItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    public interface OnFlowTextItemClickListener {
        void onFlowItemClick(String text);
    }

}

//    public static final float DEFAULT_SPACE = 10;
//
//    private float mItemHorizontalSpace = DEFAULT_SPACE;
//    private float mItemVerticalSpace = DEFAULT_SPACE;
//    private int mSelfWidth;
//
//    public float getmItemHorizontalSpace() {
//        return mItemHorizontalSpace;
//    }
//
//    public void setmItemHorizontalSpace(float mItemHorizontalSpace) {
//        this.mItemHorizontalSpace = mItemHorizontalSpace;
//    }
//
//    public float getmItemVerticalSpace() {
//        return mItemVerticalSpace;
//    }
//
//    public void setmItemVerticalSpace(float mItemVerticalSpace) {
//        this.mItemVerticalSpace = mItemVerticalSpace;
//    }
//
//    private List<String> mTextList = new ArrayList<>();
//
//    public TextFlowLayout(Context context) {
//        this(context, null);
//    }
//
//    public TextFlowLayout(Context context, AttributeSet attrs) {
//        this(context, attrs, 0);
//    }
//
//    public TextFlowLayout(Context context, AttributeSet attrs, int defStyleAttr) {
//        super(context, attrs, defStyleAttr);
//        //去拿相关属性(xml属性)
////        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TextFlowLayout);
////        mItemHorizontalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_mItemHorizontalSpace, DEFAULT_SPACE);
////        mItemVerticalSpace = typedArray.getDimension(R.styleable.TextFlowLayout_mItemVerticalSpace, DEFAULT_SPACE);
////        typedArray.recycle();
////        LogUtils.d(this, mItemHorizontalSpace + "  " + mItemVerticalSpace);
//    }
//
//    public void setTextList(List<String> textList) {
//        this.mTextList = textList;
//        for (String text : mTextList) {
//            TextView textView = new TextView(getContext());
//            //添加子View
//            TextView view = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.flow_text_view, this, false);
//            view.setText(text);
//            addView(view);
//        }
//    }
//
//    private List<View> line = null;
//    private List<List<View>> lines = new ArrayList<>();
//
//    @Override
//    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
//        LogUtils.d(this, "调用测量也添加数据");
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
//        mSelfWidth = MeasureSpec.getSize(widthMeasureSpec) - getPaddingLeft() - getPaddingRight();
////        LogUtils.d(this, "mySeleWidth : " + mSelfWidth); //1080
//        //测量孩子
//        int childCount = getChildCount();
//        for (int i = 0; i < childCount; i++) {
////            lines.clear();
//            View itemView = getChildAt(i);
////            LogUtils.d(this, "测量前" + itemView.getMeasuredHeight());
//            measureChild(itemView, widthMeasureSpec, heightMeasureSpec);
////            LogUtils.d(this, "测量后" + itemView.getMeasuredHeight());
//            if (line == null) {
//                //说明当前行为空，可以添加
//                line = new ArrayList<>();
//                line.add(itemView);
//                lines.add(line);
//            } else {
//                //判断是否可以添加进来
//                if (canBeAdd(itemView, line)) {
//                    //添加进此行
//                    line.add(itemView);
//                } else {
//                    //新建一行
//                    line = new ArrayList<>();
//                    line.add(itemView);
//                    lines.add(line);
//                }
//            }
//        }
//        int mSelfHeight = (int) (lines.size()*getChildAt(0).getMeasuredHeight() + mItemVerticalSpace * (line.size() + 1) + 0.5f);
//        //测量自己
//        setMeasuredDimension(mSelfWidth, mSelfHeight);
//    }
//
//    private boolean canBeAdd(View itemView, List<View> line) {
//        //所有已添加的子view + (line.size()+1)*mItemHorizontalSpace + itemView.getMeasureWidth();
//        //小于等于当前控件的宽度，否则不能添加
//        int totalWidth = itemView.getMeasuredWidth();
//        for (View view : line) {
//            totalWidth += view.getMeasuredWidth();
//        }
//        totalWidth += (line.size() + 1) *  mItemHorizontalSpace;
//        if (totalWidth <= mSelfWidth) {
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//    @Override
//    protected void onLayout(boolean changed, int l, int t, int r, int b) {
//        //摆放孩子
//        LogUtils.d(this, "调用绘制,孩子数 : " + getChildCount());
//        int top = (int)mItemVerticalSpace;
//        for (List<View> views : lines) {
//            int left = (int)mItemHorizontalSpace;
//            for (View view : views) {
//                view.layout(left, top, left + view.getMeasuredWidth(), top + view.getMeasuredHeight());
//                left += view.getMeasuredWidth() + mItemHorizontalSpace;
//            }
//            top += views.get(0).getMeasuredHeight() + mItemVerticalSpace;
//        }
//        LogUtils.d(this, "hang" + lines.size() + "  lie:" + lines.get(0).size());
//
//    }
