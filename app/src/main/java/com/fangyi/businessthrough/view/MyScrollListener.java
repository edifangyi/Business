package com.fangyi.businessthrough.view;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ExpandableListView;

/**
 * Created by FANGYI on 2016/11/7.
 */

public class MyScrollListener extends ExpandableListView implements AbsListView.OnScrollListener {



    Activity context;
    public MyScrollListener(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = (Activity) context;
    }


    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        if (SCROLL_STATE_TOUCH_SCROLL == scrollState) {
            View currentFocus = context.getCurrentFocus();
            if (currentFocus != null) {
                currentFocus.clearFocus();
            }
        }

    }

    @Override
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

    }
}
