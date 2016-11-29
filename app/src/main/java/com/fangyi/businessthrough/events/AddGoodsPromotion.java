package com.fangyi.businessthrough.events;

import java.io.Serializable;

/**
 * Created by FANGYI on 2016/10/9.
 */

public class AddGoodsPromotion implements Serializable {

    private String goodsSysID;       //商品ID
    private String goodsName;        //商品名称
    private String goodsPrice;       //商品单价
    private String goodsSumPrice;   //商品总价
    private String goodsUnit;       //商品单位
    private String goodsUnitID;       //商品ID
    private String goodsWareHouseSysId;


    private String goodsNumber;      //商品数量
    private String goodsType;        //商品、促销品标记

    private String fName;           //促销方案名称

    private String promotionID;    //促销品ID
    private String promotionName;    //促销品名称
    private String promotionCount;   //促销品数量
    private String promotionUnit;   //促销品单位
    private String promotionUnitID;   //促销品单位ID
    private String promotionOutNum;
    private String promotionNotNum;
    private String promotionWareHouseSysId;

    private String sumNumber;        //销售商品总数量
    private String sumPrice;         //销售商品总金额
    private String listGoodsType;   //商品列表标记
    private String outNum;

    private String notNum;

    public String getGoodsWareHouseSysId() {
        return goodsWareHouseSysId;
    }

    public void setGoodsWareHouseSysId(String goodsWareHouseSysId) {
        this.goodsWareHouseSysId = goodsWareHouseSysId;
    }

    public String getPromotionWareHouseSysId() {
        return promotionWareHouseSysId;
    }

    public void setPromotionWareHouseSysId(String promotionWareHouseSysId) {
        this.promotionWareHouseSysId = promotionWareHouseSysId;
    }

    public String getPromotionOutNum() {
        return promotionOutNum;
    }

    public void setPromotionOutNum(String promotionOutNum) {
        this.promotionOutNum = promotionOutNum;
    }

    public String getPromotionNotNum() {
        return promotionNotNum;
    }

    public void setPromotionNotNum(String promotionNotNum) {
        this.promotionNotNum = promotionNotNum;
    }

    public String getOutNum() {
        return outNum;
    }

    public void setOutNum(String outNum) {
        this.outNum = outNum;
    }

    public String getNotNum() {
        return notNum;
    }

    public void setNotNum(String notNum) {
        this.notNum = notNum;
    }

    public String getGoodsUnitID() {
        return goodsUnitID;
    }

    public void setGoodsUnitID(String goodsUnitID) {
        this.goodsUnitID = goodsUnitID;
    }

    public String getPromotionUnitID() {
        return promotionUnitID;
    }

    public void setPromotionUnitID(String promotionUnitID) {
        this.promotionUnitID = promotionUnitID;
    }

    public String getListGoodsType() {
        return listGoodsType;
    }

    public void setListGoodsType(String listGoodsType) {
        this.listGoodsType = listGoodsType;
    }

    public String getPromotionUnit() {
        return promotionUnit;
    }

    public void setPromotionUnit(String promotionUnit) {
        this.promotionUnit = promotionUnit;
    }

    public String getPromotionID() {
        return promotionID;
    }

    public void setPromotionID(String promotionID) {
        this.promotionID = promotionID;
    }

    public String getGoodsUnit() {
        return goodsUnit;
    }

    public void setGoodsUnit(String goodsUnit) {
        this.goodsUnit = goodsUnit;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
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

    public String getGoodsPrice() {
        return goodsPrice;
    }

    public void setGoodsPrice(String goodsPrice) {
        this.goodsPrice = goodsPrice;
    }

    public String getGoodsSumPrice() {
        return goodsSumPrice;
    }

    public void setGoodsSumPrice(String goodsSumPrice) {
        this.goodsSumPrice = goodsSumPrice;
    }

    public String getGoodsNumber() {
        return goodsNumber;
    }

    public void setGoodsNumber(String goodsNumber) {
        this.goodsNumber = goodsNumber;
    }

    public String getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(String goodsType) {
        this.goodsType = goodsType;
    }

    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }

    public String getPromotionCount() {
        return promotionCount;
    }

    public void setPromotionCount(String promotionCount) {
        this.promotionCount = promotionCount;
    }

    public String getSumNumber() {
        return sumNumber;
    }

    public void setSumNumber(String sumNumber) {
        this.sumNumber = sumNumber;
    }

    public String getSumPrice() {
        return sumPrice;
    }

    public void setSumPrice(String sumPrice) {
        this.sumPrice = sumPrice;
    }

    @Override
    public String toString() {
        return "AddGoodsPromotion{" +
                "goodsSysID='" + goodsSysID + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsPrice='" + goodsPrice + '\'' +
                ", goodsSumPrice='" + goodsSumPrice + '\'' +
                ", goodsUnit='" + goodsUnit + '\'' +
                ", goodsUnitID='" + goodsUnitID + '\'' +
                ", goodsWareHouseSysId='" + goodsWareHouseSysId + '\'' +
                ", goodsNumber='" + goodsNumber + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", fName='" + fName + '\'' +
                ", promotionID='" + promotionID + '\'' +
                ", promotionName='" + promotionName + '\'' +
                ", promotionCount='" + promotionCount + '\'' +
                ", promotionUnit='" + promotionUnit + '\'' +
                ", promotionUnitID='" + promotionUnitID + '\'' +
                ", promotionOutNum='" + promotionOutNum + '\'' +
                ", promotionNotNum='" + promotionNotNum + '\'' +
                ", promotionWareHouseSysId='" + promotionWareHouseSysId + '\'' +
                ", sumNumber='" + sumNumber + '\'' +
                ", sumPrice='" + sumPrice + '\'' +
                ", listGoodsType='" + listGoodsType + '\'' +
                ", outNum='" + outNum + '\'' +
                ", notNum='" + notNum + '\'' +
                '}';
    }
}
