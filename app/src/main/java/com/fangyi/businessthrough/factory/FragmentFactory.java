package com.fangyi.businessthrough.factory;

import android.support.v4.app.Fragment;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.fragment.main.MainFragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.PurchaseFragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.FBillCgSumFragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.FEntry22Fragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.FEntry23Fragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.FEntry24Fragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.FEntry25Fragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.filter.FBillCgFilterFragment;
import com.fangyi.businessthrough.fragment.main.imp.purchase.imp.filter.imp.Filter22Fragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.SaleFragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.FBillXsOrderFragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.FBillXsSumFragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.FEntry26Fragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.FEntry27Fragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.FEntry28Fragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.FEntry29Fragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.filter.FBillXsFilterFragment;
import com.fangyi.businessthrough.fragment.main.imp.sale.imp.filter.imp.Filter26Fragment;
import com.fangyi.businessthrough.fragment.main.imp.stock.StockFragment;
import com.fangyi.businessthrough.fragment.main.imp.stock.imp.FBillDbFilterFragment;
import com.fangyi.businessthrough.fragment.main.imp.stock.imp.FBillKcFilterFragment;
import com.fangyi.businessthrough.fragment.main.imp.stock.imp.FEntry30Fragment;
import com.fangyi.businessthrough.fragment.me.MeFragment;
import com.fangyi.businessthrough.fragment.me.data.DeleteFragment;
import com.fangyi.businessthrough.fragment.me.data.DownloadDataFragment;
import com.fangyi.businessthrough.fragment.me.data.DownloadOrderFragment;
import com.fangyi.businessthrough.fragment.me.data.UploadDataFragment;
import com.fangyi.businessthrough.fragment.set.SetFragment;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by FANGYI on 2016/8/17.
 */

public class FragmentFactory {

    /**
     * main
     *
     * @param position
     * @return
     */
    public static Fragment createForMain(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0:// 首页
                fragment = new MainFragment();
                break;
            case 1:// 我的
                fragment = new MeFragment();
                break;
            case 2:// 设置
                fragment = new SetFragment();
                break;
        }
        return fragment;
    }

    /**
     * 车票预订、订单查询、我的12306
     *
     * @param resId
     * @return
     */
    public static Fragment createById(int resId) {
        Fragment fragment = null;
        switch (resId) {

            case R.id.me_operation_center:// 上传数据
                fragment = new UploadDataFragment();
                break;
            case R.id.me_consume_center://下载
                fragment = new DownloadDataFragment();
                break;
            case R.id.me_manager_center://订单下载
                fragment = new DownloadOrderFragment();
                break;
            case R.id.me_data_center://订单下载
                fragment = new DeleteFragment();
                break;

        }
        return fragment;
    }


    public static Fragment createForMainTab(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0://销售
                fragment = new SaleFragment();
                break;
            case 1://采购
                fragment = new PurchaseFragment();
                break;
            case 2://库存
                fragment = new StockFragment();
                break;
        }
        return fragment;
    }


    /**
     * 销售订单查询
     *
     * @param position
     * @return
     */
    public static Fragment createForFBillXsFilterTab(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0://销售订货单
                fragment = Filter26Fragment.getInstance("1");
                break;
            case 1://销售出库单
                fragment = Filter26Fragment.getInstance("2");
                break;
            case 2://销售退货通知
                fragment = Filter26Fragment.getInstance("3");
                break;
            case 3://销售退货单
                fragment = Filter26Fragment.getInstance("0");
                break;
        }
        return fragment;
    }

    /**
     * 销售订单查询
     *
     * @param position
     * @return
     */
    public static Fragment createForFBillCgFilterTab(int position) {
        Fragment fragment = null;
        switch (position) {
            case 0://销售订货单
                fragment = Filter22Fragment.getInstance("4");
                break;
            case 1://销售出库单
                fragment = Filter22Fragment.getInstance("5");
                break;
            case 2://销售退货通知
                fragment = Filter22Fragment.getInstance("6");
                break;
            case 3://销售退货单
                fragment = Filter22Fragment.getInstance("7");
                break;
        }
        return fragment;
    }

    public static Fragment createBySaleItem(int resId, String[] item) {
        List<Fragment> list = new ArrayList<>();
        list.add(FEntry26Fragment.getInstance("1"));
        list.add(FEntry27Fragment.getInstance("2"));
        list.add(FEntry28Fragment.getInstance("3"));
        list.add(FEntry29Fragment.getInstance("0"));
        list.add(new FBillXsFilterFragment());
        list.add(new FBillXsSumFragment());
        list.add(new FBillXsOrderFragment());

        List<Fragment> list1 = new ArrayList<>();
        return getFragment(resId, item, list, list1);
    }

    public static Fragment createByPurchaseItem(int resId, String[] item) {
        List<Fragment> list = new ArrayList<>();
//        list.add(new FEntry22Fragment("4"));
        list.add(FEntry22Fragment.getInstance("4"));
        list.add(FEntry23Fragment.getInstance("5"));
        list.add(FEntry24Fragment.getInstance("6"));
        list.add(FEntry25Fragment.getInstance("7"));
        list.add(new FBillCgFilterFragment());
        list.add(new FBillCgSumFragment());
        List<Fragment> list1 = new ArrayList<>();
        return getFragment(resId, item, list, list1);
    }

    public static Fragment createByStockItem(int resId, String[] item) {
        List<Fragment> list = new ArrayList<>();
        list.add(FEntry30Fragment.getInstance("8"));
        list.add(new FBillDbFilterFragment());
        list.add(new FBillKcFilterFragment());

        List<Fragment> list1 = new ArrayList<>();
        return getFragment(resId, item, list, list1);
    }

    private static Fragment getFragment(int resId, String[] item, List<Fragment> list, List<Fragment> list1) {
        for (int i = 0; i < item.length; i++) {
            if ("1".equals(item[i])) {
                list1.add(list.get(i));
            }
        }
        return list1.get(resId);
    }


}
