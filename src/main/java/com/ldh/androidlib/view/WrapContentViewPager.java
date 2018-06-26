package com.ldh.androidlib.view;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;

/**
 * desc:切换tab时根据子View高度自适应
 * Created by ldh on 2018/5/28.
 */
public class WrapContentViewPager extends ViewPager {
    private int initPageIndex;

    public WrapContentViewPager(@NonNull Context context) {
        super(context);
    }

    public WrapContentViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {

        ArrayList<Integer> heights = new ArrayList<>();
        for (int i = 0; i < getChildCount(); i++) {
            View child = getChildAt(i);
            child.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
            int h = child.getMeasuredHeight();
            heights.add(h);
        }
        Log.d("WrapContentViewPager", "heights=" + heights.toString());
//
//        if (height != 0) {
//            heightMeasureSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.EXACTLY);
//        }
//
//        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int currentItem = getCurrentItem();
        if (initPageIndex != 0) {
            if (currentItem == initPageIndex) {
                currentItem = 0;
            } else if (currentItem == 0) {
                currentItem = initPageIndex;
            }
        }

        View c = getChildAt(currentItem);
        c.measure(widthMeasureSpec, MeasureSpec.makeMeasureSpec(0, MeasureSpec.UNSPECIFIED));
        int chidH = c.getMeasuredHeight();
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(chidH, MeasureSpec.EXACTLY);

        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    /**
     *
     */
    public void requestLayoutOnSizeChanged() {
        requestLayout();
    }

    /**
     * 当初始化的时候调用setCurrentItem，会导致childView数组中对应的View与索引为0的view交互位置
     *
     * @param initPageIndex
     */
    public void setInitPageIndex(int initPageIndex) {
        this.initPageIndex = initPageIndex;
        setCurrentItem(initPageIndex);
    }
}
