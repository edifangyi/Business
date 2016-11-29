package com.fangyi.businessthrough.bean.business;

/**
 * Created by FANGYI on 2016/11/15.
 */

public class Summarizing {
    private String customerName;
    private String goodsName;
    private String standard;
    private String AllMoney;

    public Summarizing() {
        this.customerName = "";
        this.goodsName = "";
        this.standard = "";
        this.AllMoney = "";
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(String allMoney) {
        AllMoney = allMoney;
    }


    @Override
    public String toString() {
        return "Summarizing{" +
                "customerName='" + customerName + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", standard='" + standard + '\'' +
                ", AllMoney='" + AllMoney + '\'' +
                '}';
    }
}
