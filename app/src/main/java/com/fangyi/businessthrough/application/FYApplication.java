package com.fangyi.businessthrough.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;

import cn.jpush.android.api.JPushInterface;

public class FYApplication extends Application {
    private static Context mContext;
    private static Handler mHandler;


    /**
     * 跳转执行代码
     */
    public static final int ADD_CUSTOMER_REQ_CODE = 0;//搜索添加客户名称
    public static final int ADD_GOODS_REQ_CODE = 1;//搜索添加商品
    public static final int ADD_DONATION_REQ_CODE = 2;//搜索添加赠品
    public static final int ADD_PROMOTION_REQ_CODE = 3;//搜索添加促销

    public static final int ADD_HISTORY_CUSTOMER_REQ_CODE = 4;//搜索添加历史订单客户
    public static final int ADD_HISTORY_ORDER = 5;//搜索添加历史订单
    public static final int ADD_HISTORY_GOODS_REQ_CODE = 6;//搜索添加历史订单商品
    public static final int ADD_SUPPLIER_REQ_CODE = 7;//搜索添加历史订单商品
    public static final int THE_CALLBACK_TO_REFRESH = 10;//回调刷新

    public String tempDate;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
        mHandler = new Handler();

        JPushInterface.setDebugMode(true); 	// 设置开启日志,发布时请关闭日志
        JPushInterface.init(this);     		// 初始化 JPush
    }


    /**
     * 获取全局的context
     */
    public static Context getContext() {
        return mContext;
    }

    /**
     * 获取全局的主线程的handler
     */
    public static Handler getHandler() {
        return mHandler;
    }

}
