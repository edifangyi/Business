package com.fangyi.businessthrough.factory;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;

import com.fangyi.businessthrough.activity.system.ClickButtonActivity;
import com.fangyi.businessthrough.activity.system.ClickPurchaseItemActivity;
import com.fangyi.businessthrough.activity.system.ClickSaleItemActivity;
import com.fangyi.businessthrough.activity.system.ClickStockItemActivity;
import com.fangyi.businessthrough.application.FYApplication;


/**
 * Created by zhaoshuo on 2016/3/17.
 */
public class StartUtils {
    public static void startActivityById(Context context, int resId) {
        Intent intent = new Intent(FYApplication.getContext(), ClickButtonActivity.class);
        intent.putExtra("resId", resId);
        context.startActivity(intent);
    }

    public static void startActivityByIdForResult(Fragment activity, int resId, int requestCode) {
        Intent intent = new Intent(FYApplication.getContext(), ClickButtonActivity.class);
        intent.putExtra("resId", resId);
        activity.startActivityForResult(intent, requestCode);
    }

    public static void startActivityBySaleItem(Context context, int resId, String[] item) {
        Intent intent = new Intent(FYApplication.getContext(), ClickSaleItemActivity.class);
        intent.putExtra("resId", resId);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    public static void startActivityByStockItem(Context context, int resId, String[] item) {
        Intent intent = new Intent(FYApplication.getContext(), ClickStockItemActivity.class);
        intent.putExtra("resId", resId);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }

    public static void startActivityByPurchaseItem(Context context, int resId, String[] item) {
        Intent intent = new Intent(FYApplication.getContext(), ClickPurchaseItemActivity.class);
        intent.putExtra("resId", resId);
        intent.putExtra("item", item);
        context.startActivity(intent);
    }
}
