package com.fangyi.businessthrough.utils.system;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.telephony.TelephonyManager;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.fangyi.businessthrough.activity.system.ClickButtonActivity;
import com.fangyi.businessthrough.activity.system.ClickPurchaseItemActivity;
import com.fangyi.businessthrough.activity.system.ClickSaleItemActivity;
import com.fangyi.businessthrough.activity.system.ClickStockItemActivity;
import com.fangyi.businessthrough.application.FYApplication;

import java.text.DecimalFormat;
import java.util.Date;

import static com.fangyi.businessthrough.application.FYApplication.getContext;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeYYYY_MM_DD_HH;

/**
 * Created by FANGYI on 2016/8/17.
 */

public class CommonUtils {
    /**
     * dip转化成px
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转化成dip
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * px转化成sp
     */
    public static int px2sp(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * sp转化成px
     */
    public static int sp2px(Context context, float spValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (spValue * scale + 0.5f);
    }

    public static void setListViewHeightBasedOnChildren(ListView listView) {

        //获取listview的适配器
        ListAdapter listAdapter = listView.getAdapter();
        int itemHeight = 64;
        if (listAdapter == null) {
            return;
        }


        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) {
            totalHeight += CommonUtils.dip2px(FYApplication.getContext(),itemHeight)+listView.getDividerHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight;

        listView.setLayoutParams(params);
    }

//    public static void setRecyclerViewHeightBasedOnChildren(SwipeMenuRecyclerView listView) {
//
//        //获取listview的适配器
//        SwipeMenuRecyclerView listAdapter = SwipeMenuRecyclerView.();
//        int itemHeight = 64;
//        if (listAdapter == null) {
//            return;
//        }
//
//
//        int totalHeight = 0;
//
//        for (int i = 0; i < listAdapter.getCount(); i++) {
//            totalHeight += CommonUtils.dip2px(getContext(),itemHeight)+listView.getDividerHeight();
//        }
//
//        ViewGroup.LayoutParams params = listView.getLayoutParams();
//        params.height = totalHeight;
//
//        listView.setLayoutParams(params);
//    }

    /**
     * 在主线程执行任务
     */
    public static void runOnUIThread(Runnable r) {
        FYApplication.getHandler().post(r);
    }

    /**
     * 获取Resource对象
     */
    public static Resources getResources() {
        return getContext().getResources();
    }

    /**
     * 获取Drawable资源
     */
    public static Drawable getDrawable(int resId) {
        return getResources().getDrawable(resId);
    }

    /**
     * 获取字符串资源
     */
    public static String getString(int resId) {
        return getResources().getString(resId);
    }

    /**
     * 获取color资源
     */
    public static int getColor(int resId) {
        return getResources().getColor(resId);
    }

    /**
     * 获取dimens资源
     */
    public static float getDimens(int resId) {
        return getResources().getDimension(resId);
    }

    /**
     * 获取字符串数组资源
     */
    public static String[] getStringArray(int resId) {
        return getResources().getStringArray(resId);
    }

    /**
     * 将自己从父容器中移除
     */
    public static void removeSelfFromParent(View child) {
        // 获取父view
        if (child != null) {
            ViewParent parent = child.getParent();
            if (parent instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) parent;
                // 将自己移除
                viewGroup.removeView(child);
            }
        }
    }

    /**
     * 获取 手机IMEI码
     * @param context
     * @return
     */
    public static  String getIMEI(Context context)
    {
        TelephonyManager telephonyManager= (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
        String imei=telephonyManager.getDeviceId();
        return imei;
    }

    /**
     * 相机使用
     * @return
     */
    public static boolean isCameraCanUse(){
        boolean bool = false;

        PackageManager pm = getContext().getPackageManager();
        bool = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) || pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT);

        return bool;
    }


    /**
     * 设置标题
     * @param getActivity
     * @param title
     */
    public static void setTitle(Activity getActivity, String title){
        String str = "<b>" + title + "</b>";
        ClickButtonActivity activity = (ClickButtonActivity) getActivity;
        activity.tvTitle.setText(Html.fromHtml(str));
    }


    public static void setPurchaseTitle(Activity getActivity, String title) {
        String str = "<b>" + title + "</b>";
        ClickPurchaseItemActivity activity = (ClickPurchaseItemActivity) getActivity;
        activity.tvTitle.setText(Html.fromHtml(str));
    }

    public static void setSaleTitle(Activity getActivity, String title) {
        String str = "<b>" + title + "</b>";
        ClickSaleItemActivity activity = (ClickSaleItemActivity) getActivity;
        activity.tvTitle.setText(Html.fromHtml(str));
    }

    public static void setStockTitle(Activity getActivity, String title) {
        String str = "<b>" + title + "</b>";
        ClickStockItemActivity activity = (ClickStockItemActivity) getActivity;
        activity.tvTitle.setText(Html.fromHtml(str));
    }


    /**
     * 获取订单号
     *
     * @param s
     * @return
     */
    public static String setOrderId(String s) {
        DecimalFormat f = new DecimalFormat("0000");
        int i = Integer.parseInt(s);
        String str = f.format(i + 1);
        return "S" + getTimeYYYY_MM_DD_HH(new Date()) + str;
    }

    public static String setPurchaseOrderId(String s) {
        DecimalFormat f = new DecimalFormat("0000");
        int i = Integer.parseInt(s);
        String str = f.format(i + 1);
        return "B" + getTimeYYYY_MM_DD_HH(new Date()) + str;
    }

    public static String setStockOrderId(String s) {
        DecimalFormat f = new DecimalFormat("0000");
        int i = Integer.parseInt(s);
        String str = f.format(i + 1);
        return "C" + getTimeYYYY_MM_DD_HH(new Date()) + str;
    }



}