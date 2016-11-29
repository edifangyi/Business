package com.fangyi.businessthrough.view;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

/**
 * Created by FANGYI on 2016/10/11.
 */

public class FYLayoutManager extends LinearLayoutManager {
    SwipeMenuRecyclerView ryList1;
    int size;

    public FYLayoutManager(Context context, SwipeMenuRecyclerView ryList1, int size) {
        super(context);
        this.ryList1 = ryList1;
        this.size = size;
    }

    @Override
    public void onMeasure(RecyclerView.Recycler recycler, RecyclerView.State state, int widthSpec, int heightSpec) {
        final int width = RecyclerView.LayoutManager.chooseSize(widthSpec,
                getPaddingLeft() + getPaddingRight(),
                ViewCompat.getMinimumWidth(ryList1));
        final int height = RecyclerView.LayoutManager.chooseSize(heightSpec,
                getPaddingTop() + getPaddingBottom(),
                ViewCompat.getMinimumHeight(ryList1));
        setMeasuredDimension(width, height * size);
    }
}