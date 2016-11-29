package com.fangyi.businessthrough.bean.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class PrintPurchaseOrderMain {

    public String id;               //订单
    public String fyFFetchDayV;  //申请日期、申请日期、通知日期、退货日期
    public String fyFPlanFetchDay1;  //采购日期
    public String fyFPlanFetchDay2;  //到货日期

    public String fyFPOStyle;    //采购方式
    public String fyFAreaPS;     //采购范围
    public String fyFBizType;    //业务类型

    public String fyFSource;     //源单号
    public String fyFSupplier;   //供应商

    public String fyFRequester;  //业务员
    public String fyFDept;       //部门
    public String fyFFManager;   //验收
    public String fyFSManager;   //保管
    public String fyFDCStock;    //仓库
    public String fyFDigest;     //摘要

    public String businessType;  //单据类型 4-5-6-7

    public String allMoney;      //单据总金额
    public String isUpLoad;      //是否上传
    public String fUpdateType;   //不允许修改单据控制方式
    public String fDelType;      //控制删除
    public String printNum;      //打印次数


    public List<Goods> orderGoods = new ArrayList<>(); //订购商品

    @Override
    public String toString() {
        return "PrintPurchaseOrderMain{" +
                "id='" + id + '\'' +
                ", fyFFetchDayV='" + fyFFetchDayV + '\'' +
                ", fyFPlanFetchDay1='" + fyFPlanFetchDay1 + '\'' +
                ", fyFPlanFetchDay2='" + fyFPlanFetchDay2 + '\'' +
                ", fyFPOStyle='" + fyFPOStyle + '\'' +
                ", fyFAreaPS='" + fyFAreaPS + '\'' +
                ", fyFBizType='" + fyFBizType + '\'' +
                ", fyFSource='" + fyFSource + '\'' +
                ", fyFSupplier='" + fyFSupplier + '\'' +
                ", fyFRequester='" + fyFRequester + '\'' +
                ", fyFDept='" + fyFDept + '\'' +
                ", fyFFManager='" + fyFFManager + '\'' +
                ", fyFSManager='" + fyFSManager + '\'' +
                ", fyFDCStock='" + fyFDCStock + '\'' +
                ", fyFDigest='" + fyFDigest + '\'' +
                ", businessType='" + businessType + '\'' +
                ", allMoney='" + allMoney + '\'' +
                ", isUpLoad='" + isUpLoad + '\'' +
                ", fUpdateType='" + fUpdateType + '\'' +
                ", fDelType='" + fDelType + '\'' +
                ", printNum='" + printNum + '\'' +
                ", orderGoods=" + orderGoods +
                '}';
    }
}
