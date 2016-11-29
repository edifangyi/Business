package com.fangyi.businessthrough.events;

import java.io.Serializable;

/**
 * Created by FANGYI on 2016/9/24.
 */

public class AddGoodsMessage implements Serializable {

    public String goodsSysID;
    public String goodsName;
    public String goodsNum;
    public String goodsPrice;
    public String goodsSum;
    public String unitID;
    public String goodsUom;
    public String goodsType;
    public String wareHouseSysId;
    public String listGoodsType;
    public String outNum;
    public String notNum;


    public String getNotNum() {
        return notNum;
    }

    public void setNotNum(String notNum) {
        this.notNum = notNum;
    }

    public String getOutNum() {
        return outNum;
    }

    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    public String getListGoodsType() {
        return listGoodsType;
    }

    public void setListGoodsType(String listGoodsType) {
        this.listGoodsType = listGoodsType;
    }

    public String getWareHouseSysId() {
        return wareHouseSysId;
    }

    public void setWareHouseSysId(String wareHouseSysId) {
        this.wareHouseSysId = wareHouseSysId;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getGoodsUom() {
        return goodsUom;
    }

    public void setGoodsUom(String goodsUom) {
        this.goodsUom = goodsUom;
    }

    public String getGoodsSysID() {
        return goodsSysID;
    }

    public void setGoodsSysID(String goodsSysID) {
        this.goodsSysID = goodsSysID;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsNum() {
        return goodsNum;
    }

    public void setGoodsNum(String goodsNum) {
        this.goodsNum = goodsNum;
    }

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsSum() {
        return goodsSum;
    }

    public void setGoodsSum(String goodsSum) {
        this.goodsSum = goodsSum;
    }

    @Override
    public String toString() {
        return "AddGoodsMessage{" +
                "goodsSysID='" + goodsSysID + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsNum='" + goodsNum + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsSum='" + goodsSum + '\'' +
                ", goodsUom='" + goodsUom + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", wareHouseSysId='" + wareHouseSysId + '\'' +
                '}';
    }
}
