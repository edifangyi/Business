package com.fangyi.businessthrough.bean.business;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class PrintOrderMain implements Serializable {
    public String id;//订单ID
    public String orderDate;//订单日期
    public String deliveryDate;//交货日期
    public String allMoney;//总金额
    public String saveTime;//保存时间
    public String isUpLoad;//是否上传
    public String message;//备注信息
    public String customerSysId;//客户系统编码
    public String customerName;//客户名称
    public String customerAddress;//客户地址
    public String customerTel;//客户电话
    public String customerType;//客户类型
    public String userSysId;//用户系统编码 283
    public String userId;//用户编码 02
    public String deptId;//部门编码
    public String deptName;//部门名称
    public String wareHouseSysId;//销售仓库系统编码
    public String wareHouseName;//销售仓库名称
    public String businessType;//业务类型 0 1 2 3
    public String saleType;//销售方式
    public String areaPS;//销售范围
    public String updateType;//不允许修改单据控制方式
    public String printNum;//打印次数
    public String delType;//控制删除
    public String source;//源单

    public List<Goods> orderGoods = new ArrayList<>();
    public List<Goods> presentGoods = new ArrayList<>();

    @Override
    public String toString() {
        return "PrintOrderMain{" +
                "id='" + id + '\'' +
                ", orderDate='" + orderDate + '\'' +
                ", deliveryDate='" + deliveryDate + '\'' +
                ", allMoney='" + allMoney + '\'' +
                ", saveTime='" + saveTime + '\'' +
                ", isUpLoad='" + isUpLoad + '\'' +
                ", message='" + message + '\'' +
                ", customerSysId='" + customerSysId + '\'' +
                ", customerName='" + customerName + '\'' +
                ", customerAddress='" + customerAddress + '\'' +
                ", customerTel='" + customerTel + '\'' +
                ", customerType='" + customerType + '\'' +
                ", userSysId='" + userSysId + '\'' +
                ", userId='" + userId + '\'' +
                ", deptId='" + deptId + '\'' +
                ", deptName='" + deptName + '\'' +
                ", wareHouseSysId='" + wareHouseSysId + '\'' +
                ", wareHouseName='" + wareHouseName + '\'' +
                ", businessType='" + businessType + '\'' +
                ", saleType='" + saleType + '\'' +
                ", areaPS='" + areaPS + '\'' +
                ", updateType='" + updateType + '\'' +
                ", printNum='" + printNum + '\'' +
                ", delType='" + delType + '\'' +
                ", source='" + source + '\'' +
                ", orderGoods=" + orderGoods +
                ", presentGoods=" + presentGoods +
                '}';
    }
}
