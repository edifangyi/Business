package com.fangyi.businessthrough.bean.business;

/**
 * Created by FANGYI on 2016/10/12.
 */

public class OrderDetail {
    private String ID;
    private String GoodsSysID;//商品id  goods 里sysid
    private String unitID;//单位id
    private String Standard;//规格
    private String OrderNum;//数量
    private String OrderPrice;//价格
    private String OrderMoney;//总价
    private String OrderType;//赠品标识
    private String OrderFree;//方案标识
    private String OrderbyGoodID;// id  主键   //在30中是 第二个仓库id
    private String Node;//备注
    private String WareHouseSysId;//仓库id
    private String BusinessType;//订单类型
    private String ListGoodsType;//商品列表标记

    public String getUnitID() {
        return unitID;
    }

    public void setUnitID(String unitID) {
        this.unitID = unitID;
    }

    @Override
    public String toString() {
        return "OrderDetail{" +
                "ID='" + ID + '\'' +
                ", GoodsSysID='" + GoodsSysID + '\'' +
                ", Standard='" + Standard + '\'' +
                ", OrderNum='" + OrderNum + '\'' +
                ", OrderPrice='" + OrderPrice + '\'' +
                ", OrderType='" + OrderType + '\'' +
                ", OrderFree='" + OrderFree + '\'' +
                ", OrderbyGoodID='" + OrderbyGoodID + '\'' +
                ", Node='" + Node + '\'' +
                ", WareHouseSysId='" + WareHouseSysId + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                ", ListGoodsType='" + ListGoodsType + '\'' +
                '}';
    }

    public String getOrderMoney() {
        return OrderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        OrderMoney = orderMoney;
    }
    public String getListGoodsType() {
        return ListGoodsType;
    }

    public void setListGoodsType(String listGoodsType) {
        ListGoodsType = listGoodsType;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getGoodsSysID() {
        return GoodsSysID;
    }

    public void setGoodsSysID(String goodsSysID) {
        GoodsSysID = goodsSysID;
    }

    public String getStandard() {
        return Standard;
    }

    public void setStandard(String standard) {
        Standard = standard;
    }

    public String getOrderNum() {
        return OrderNum;
    }

    public void setOrderNum(String orderNum) {
        OrderNum = orderNum;
    }

    public String getOrderPrice() {
        return OrderPrice;
    }

    public void setOrderPrice(String orderPrice) {
        OrderPrice = orderPrice;
    }

    public String getOrderType() {
        return OrderType;
    }

    public void setOrderType(String orderType) {
        OrderType = orderType;
    }

    public String getOrderFree() {
        return OrderFree;
    }

    public void setOrderFree(String orderFree) {
        OrderFree = orderFree;
    }

    public String getOrderbyGoodID() {
        return OrderbyGoodID;
    }

    public void setOrderbyGoodID(String orderbyGoodID) {
        OrderbyGoodID = orderbyGoodID;
    }

    public String getNode() {
        return Node;
    }

    public void setNode(String node) {
        Node = node;
    }

    public String getWareHouseSysId() {
        return WareHouseSysId;
    }

    public void setWareHouseSysId(String wareHouseSysId) {
        WareHouseSysId = wareHouseSysId;
    }

}
