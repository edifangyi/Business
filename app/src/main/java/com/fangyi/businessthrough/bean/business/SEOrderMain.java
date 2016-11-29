package com.fangyi.businessthrough.bean.business;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FANGYI on 2016/11/11.
 */

public class SEOrderMain{
    private String UserSysID;
    private String CustomerName;
    private String CustomerSySID;
    private String OrderDate;
    private String ID;
    private String BusinessType;
    private String FSaleStyleID;
    private String FSaleStyleName;
    private String DeptID;
    private String KISID;
    private String Message;
    private String KINGID;
    private String AllMoney;


    private List<Goods> orderGoods = new ArrayList<>(); //订购商品
    private List<Goods> presentGoods = new ArrayList<>(); //订购赠品


    public List<Goods> getOrderGoods() {
        return orderGoods;
    }

    public void setOrderGoods(List<Goods> orderGoods) {
        this.orderGoods = orderGoods;
    }

    public List<Goods> getPresentGoods() {
        return presentGoods;
    }

    public void setPresentGoods(List<Goods> presentGoods) {
        this.presentGoods = presentGoods;
    }

    public String getUserSysID() {
        return UserSysID;
    }

    public void setUserSysID(String userSysID) {
        UserSysID = userSysID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getCustomerSySID() {
        return CustomerSySID;
    }

    public void setCustomerSySID(String customerSySID) {
        CustomerSySID = customerSySID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getFSaleStyleID() {
        return FSaleStyleID;
    }

    public void setFSaleStyleID(String FSaleStyleID) {
        this.FSaleStyleID = FSaleStyleID;
    }

    public String getFSaleStyleName() {
        return FSaleStyleName;
    }

    public void setFSaleStyleName(String FSaleStyleName) {
        this.FSaleStyleName = FSaleStyleName;
    }

    public String getDeptID() {
        return DeptID;
    }

    public void setDeptID(String deptID) {
        DeptID = deptID;
    }

    public String getKISID() {
        return KISID;
    }

    public void setKISID(String KISID) {
        this.KISID = KISID;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getKINGID() {
        return KINGID;
    }

    public void setKINGID(String KINGID) {
        this.KINGID = KINGID;
    }

    public String getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(String allMoney) {
        AllMoney = allMoney;
    }



    @Override
    public String toString() {
        return "SEOrderMain{" +
                "UserSysID='" + UserSysID + '\'' +
                ", CustomerName='" + CustomerName + '\'' +
                ", CustomerSySID='" + CustomerSySID + '\'' +
                ", OrderDate='" + OrderDate + '\'' +
                ", ID='" + ID + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", FSaleStyleID='" + FSaleStyleID + '\'' +
                ", FSaleStyleName='" + FSaleStyleName + '\'' +
                ", DeptID='" + DeptID + '\'' +
                ", KISID='" + KISID + '\'' +
                ", Message='" + Message + '\'' +
                ", KINGID='" + KINGID + '\'' +
                ", AllMoney='" + AllMoney + '\'' +
                ", orderGoods=" + orderGoods +
                ", presentGoods=" + presentGoods +
                '}';
    }

}
