package com.fangyi.businessthrough.bean.business;

/**
 * 商品表
 * Created by FANGYI on 2016/9/12.
 */

public class Goods {


    @Override
    public String toString() {
        return "Goods{" +
                "barcode='" + barcode + '\'' +
                ", goodsSysID='" + goodsSysID + '\'' +
                ", goodsID='" + goodsID + '\'' +
                ", goodsName='" + goodsName + '\'' +
                ", goodsFullName='" + goodsFullName + '\'' +
                ", helpID='" + helpID + '\'' +
                ", retailPrice='" + retailPrice + '\'' +
                ", tradePrice='" + tradePrice + '\'' +
                ", accuracy='" + accuracy + '\'' +
                ", standard='" + standard + '\'' +
                ", unitID='" + unitID + '\'' +
                ", unit='" + unit + '\'' +
                ", unitGoupID='" + unitGoupID + '\'' +
                ", unitGroup='" + unitGroup + '\'' +
                ", conversion='" + conversion + '\'' +
                ", assist='" + assist + '\'' +
                ", category='" + category + '\'' +
                ", warehouse='" + warehouse + '\'' +
                ", warehousePlace='" + warehousePlace + '\'' +
                ", isUsed='" + isUsed + '\'' +
                ", boxCode='" + boxCode + '\'' +
                ", orderNum='" + orderNum + '\'' +
                ", orderPrice='" + orderPrice + '\'' +
                ", orderCounter='" + orderCounter + '\'' +
                ", saledGoodsSysId='" + saledGoodsSysId + '\'' +
                ", saledGoodsName='" + saledGoodsName + '\'' +
                ", saleGoodsNum='" + saleGoodsNum + '\'' +
                ", wareHouseSysId='" + wareHouseSysId + '\'' +
                ", wareHouseName='" + wareHouseName + '\'' +
                ", fName='" + fName + '\'' +
                ", goodsType='" + goodsType + '\'' +
                ", ListGoodsType='" + listGoodsType + '\'' +
                '}';
    }

    public String barcode; //条形码

    public String goodsSysID; //商品内码

    public String goodsID; //商品编码（助记码）

    public String goodsName; //商品名称

    public String goodsFullName; //商品全称

    public String helpID; //帮助编码

    public String retailPrice; //零售价格

    public String tradePrice; //交易价格

    public String accuracy; //精度

    public String standard; //规格

    public String unitID; //单位编码

    public String unit; //单位名称

    public String unitGoupID; //单位组编码

    public String unitGroup; //单位组

    public String conversion;  //转换

    public String assist;  //辅助

    public String category;  //种类

    public String warehouse; //仓储类型

    public String warehousePlace; //仓储位置

    public String isUsed; //是否可用

    public String boxCode;  //箱码

    public String orderNum;  //销售数量

    public String orderPrice;  //销售单价

    public String orderCounter;  //总价格

    public  String saledGoodsSysId;  //贈品對應的銷售商品系統編碼

    public String saledGoodsName; //贈品對應的銷售商品名稱

    public String saleGoodsNum; //赠品对应的销售商品数量

    public String wareHouseSysId;  //仓库系统编码

    public String wareHouseName;  //仓库名称

    public String fName;//促销方案名称

    public String goodsType;//商品类型

    public String listGoodsType;

    public String outNum;

    public String notNum;

}
