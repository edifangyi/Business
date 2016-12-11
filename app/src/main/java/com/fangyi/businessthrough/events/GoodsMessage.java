package com.fangyi.businessthrough.events;

/**
 * Created by FANGYI on 2016/9/23.
 */

public class GoodsMessage {
    public String goodsSysID;
    public String goodsName;
    public String barcode;
    public String standard;
    public String conversion;
    public String uom;
    public String unitID;
    public String unitGroupID;
    public String price;

    public String getConversion() {
        return conversion;
    }

    public void setConversion(String conversion) {
        this.conversion = conversion;
    }

    public String getUom() {
        return uom;
    }

    public void setUom(String uom) {
        this.uom = uom;
    }

    public String getUnitGroupID() {
        return unitGroupID;
    }

    public void setUnitGroupID(String unitGroupID) {
        this.unitGroupID = unitGroupID;
    }

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
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

    public String getBarcode() {
        return barcode;
    }

    public void setBarcode(String barcode) {
        this.barcode = barcode;
    }

    public String getStandard() {
        return standard;
    }

    public void setStandard(String standard) {
        this.standard = standard;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "GoodsMessage{" +
                "goodsSysID='" + goodsSysID + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", barcode='" + barcode + '\'' +
                ", standard='" + standard + '\'' +
                ", price=" + price +
                '}';
    }
}
