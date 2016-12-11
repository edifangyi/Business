package com.fangyi.businessthrough.http;

import android.support.v4.app.FragmentActivity;

import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.socks.library.KLog;

import java.util.List;

import static com.fangyi.businessthrough.application.FYApplication.getContext;

/**
 * Created by FANGYI on 2016/11/8.
 */

public class WebUploadService {

    private static String result;

    public static String uploadSaleDateService(final FragmentActivity activity, final String orderId, final String businessType) {

        new Thread() {
            @Override
            public void run() {
                DBBusiness manager = new DBBusiness(activity);
                PrintOrderMain orderMain = manager.getOrderInfo(orderId, businessType);

                String proStr = Data.getUploadProStr(getContext(), orderMain, businessType);


                WebService ws = new WebService(PrefUtils.getString(getContext(), "is_service_address", null));
                String res = ws.uploadData(proStr);

                KLog.e("=====" + res);

                if ("1".equals(res.substring(0, 1))) {
                    KLog.e("======" + orderId + businessType);
                    manager.upDateOrderIsUpload(orderId, businessType);//更改表单中上传状态
                    result = "上传成功";
                } else {
                    result = "上传失败";
                }
            }
        }.start();

        return result;
    }

    public static String uploadPurchaseDataService(final FragmentActivity activity, final String orderId, final String businessType) {

        new Thread() {
            @Override
            public void run() {
                DBBusiness manager = new DBBusiness(activity);
                PrintPurchaseOrderMain orderMain = manager.getPurchaseOrderInfo(orderId, businessType);


                String proStr = Data.getUploadPurchaseProStr(getContext(), orderMain, businessType);


                WebService ws = new WebService(PrefUtils.getString(getContext(), "is_service_address", null));
                String res = ws.uploadData(proStr);


                if ("1".equals(res.substring(0, 1))) {
                    KLog.e("======" + orderId + businessType);
                    manager.upDatePurchaseOrderIsUpload(orderId, businessType);//更改表单中上传状态
                    result = "上传成功";
                } else {
                    result = "上传失败";
                }
            }
        }.start();

        return result;
    }

    public static String uploadStockDateService(final FragmentActivity activity, final String orderId, final String businessType) {

        new Thread() {
            @Override
            public void run() {
                DBBusiness manager = new DBBusiness(activity);
                PrintOrderMain orderMain = manager.getOrderInfo(orderId, businessType);

                String proStr = Data.getUploadStockProStr(getContext(), orderMain, businessType);


                WebService ws = new WebService(PrefUtils.getString(getContext(), "is_service_address", null));
                String res = ws.uploadData(proStr);

                KLog.e("=====" + res);

                if ("1".equals(res.substring(0, 1))) {
                    KLog.e("======" + orderId + businessType);
                    manager.upDateOrderIsUpload(orderId, businessType);//更改表单中上传状态
                    result = "上传成功";
                } else {
                    result = "上传失败";
                }
            }
        }.start();

        return result;
    }


    /**
     * 整单上传
     */
    public static String uploadDateService(final FragmentActivity activity, final String businessType) {

        new Thread() {
            @Override
            public void run() {
                DBBusiness manager = new DBBusiness(activity);
                List<PrintOrderMain> orderMains = manager.getOrderInfo(businessType);


                for (PrintOrderMain orderMain : orderMains) {

                    String proStr = Data.getUploadProStr(getContext(), orderMain, businessType);

                    WebService ws = new WebService(PrefUtils.getString(getContext(), "is_service_address", null));
                    String res = ws.uploadData(proStr);

                    KLog.e("=====" + res);

                    if ("1".equals(res.substring(0, 1))) {
                        manager.upDateOrderIsUpload(orderMain.id, businessType);//更改表单中上传状态
                        result = "上传成功";
                    } else {
                        result = "上传失败";
                    }

                }
            }
        }.start();

        return result;
    }
}
