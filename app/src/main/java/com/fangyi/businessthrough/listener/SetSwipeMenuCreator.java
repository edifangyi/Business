package com.fangyi.businessthrough.listener;

import android.graphics.Color;
import android.view.ViewGroup;

import com.fangyi.businessthrough.R;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;

import static com.fangyi.businessthrough.application.FYApplication.getContext;
import static com.fangyi.businessthrough.utils.system.CommonUtils.getResources;


/**
 * Created by FANGYI on 2016/11/8.
 */

public class SetSwipeMenuCreator implements SwipeMenuCreator {
    @Override
    public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
        int width = getResources().getDimensionPixelSize(R.dimen.item_height);

        // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
        int height = ViewGroup.LayoutParams.MATCH_PARENT;


        // 添加右侧的，如果不添加，则右侧不会出现菜单。
        {
            SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                    .setBackgroundDrawable(R.drawable.selector_red)
                    .setImage(R.mipmap.ic_action_delete)
                    .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                    .setTextColor(Color.WHITE)
                    .setWidth(width)
                    .setHeight(height);
            swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

        }
    }
}
