package com.fangyi.businessthrough.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by FANGYI on 2016/11/27.
 */

public class NpScrollSwipeMenuRecyclerView extends SwipeMenuRecyclerView {
    public NpScrollSwipeMenuRecyclerView(Context context) {
        super(context);
    }

    public NpScrollSwipeMenuRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    /**
     * 事件不处理触摸事件，返回false
     */
    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return true;
    }


    /**
     * false：不拦截儿子的触摸事件
     */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return true;
    }
}
