package com.fangyi.businessthrough.dao;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FANGYI on 2016/9/4.
 */

public class ProtocolUtil {
    private static String userSql = "insert into User(UserSysID,UserName,Password,UserType) values(#fieldNames#)";
    private static String usersSql = "insert into Users(UserSysID,KISID,KISName,KISPassWord,ListX,ListY,ListZ,ListTime) values(#fieldNames#)";

    private static String deviceuiSql = "insert into DeviceUI(UserSysID,UserName,FEntry22,FEntry23,FEntry24,FEntry25,FBilCgFilter,FBillCgSum,FEntry26,FEntry27,FEntry28,FEntry29,FBillXsFilter,FBillXsSum,FBillXsOrder,FEntry30,FBillDbFilter,FBillKcFilter) values(#fieldNames#)";
    private static String devicebillSql = "insert into DeviceBill(UserSysID,FCompanyName,FIsWifi,FAutoLanding,FServerIP,Fnotice,FImei,FCodeID,FX,FTitleSize,FEntrySize,FHeaderSize,FFooterSize) values(#fieldNames#)";
    private static String devicebillsetSql = "insert into DeviceBillSet(UserSysID,UserX,UserName,TableName,FAV,FAD,FBV,FBD,FCV,FCD,FRequesterV,FRequesterD,FCustAddV,FCustPhoV,FEmpV,FEmpD,FDV,FDD,FDeptV,FDeptD,FFManagerV,FFManagerD,FSManagerV,FSManagerD,FDCStockV,FDCStockD,FStockPosition,FEADay,FEBDay,FBillInterV,FBillInterM,FBillInterC,FBatchNo,FBillAuto,FWholePro,FPlanPro,FProAmountV,FAmountV,FChooseAmount,FChooseDate,FCheckType,FOnType,FUpdateType,FDelType,FOffLineUpDate,FOffLineOnDate,FSafePrint,FPrintCountV,FPrintSame,FTitle1,FTitle2,FHeader1,FHeader2,FHeader3,FHeader4,FHeader5,FHeader6,FFooter1,FFooter2,FFooter3,FFooter4,FFooter5,FSCStockV,FSCStockD,FSStockPosition) values(#fieldNames#)";

    private static String customerSql = "insert into Customer(CustomerSysID,CustomerNum,CustomerName,DeptID,DeptName,TypeID,TypeName,IsUsed,Address,Contact,Tel,Mobile,Employee,Etel,Emobile,LastTime) values(#fieldNames#)";
    private static String goodSql = "insert into Goods(Barcode,GoodsSysID,GoodsID,GoodsName,GoodsFullName,HelpID,RetailPrice,TradePrice,Accuracy,Standard,UnitID,Unit,UnitGroupID,UnitGroup,Conversion,Assist,Category,Warehouse,WarehousePlace,IsUsed,BoxCode,LastTime,OldBarcode,IsDel) values(#fieldNames#)";
    private static String itemPriceSql = "insert into ItemPrice(ID,RelatedID,Priority,ItemID,Uom,BeginSales,EndSales,Currency,PriceType,Price,BeginDate,EndDate,MinPrice,IsMinPriceCtrl,IsChecked,IsUser,PlyType,LastTime,PriceX) values(#fieldNames#)";

    private static String phoneSql = "insert into Phone(UserSysId,UsuerName,Phone) values(#fieldNames#)";
    private static String usercustomerSql = "insert into UserCustomer(UserSysID,CustomerSysID) values(#fieldNames#)";
    private static String parameterSql = "insert into Parameter(Parametertype,ParameterID,ParameterName) values(#fieldNames#)";
    private static String warehouseSql = "insert into WareHouse(UserSysID,WareHouseSysID,WareHouseNum,WareHouseName,IsCar) values(#fieldNames#)";

    private static String freebillSql = "insert into FreeBill(UserSysID,FIndex,Fx,Fy,FIndex2,FName,FSaleType,FSumQty,FSumAmount,FDate,FStarDate,FEndDate,FSuit,FMultiple) values(#fieldNames#)";
    private static String freebilldetailSql = "insert into FreeBillDetail(UserSysID,FIndex,Fx,FPRoItem,FproUnit,FProQty,FProAmount,FFlag) values(#fieldNames#)";

    private static String unitgroupSql = "insert into UnitGroup(UnitID,UnitGroupID,Uom,Conversion,Ubase) values(#fieldNames#)";

    private static String seordermainSql = "insert into SEOrderMain(UserSysID,CustomerName,CustomerSySID,OrderDate,ID,BusinessType,FSaleStyleID,FSaleStyleName,DeptID,KISID,Message,KINGID,AllMoney) values(#fieldNames#)";

    private static String seorderdetailSql = "insert into SEOrderDetail(UserSysID,ID,GoodsSysID,GoodsName,Standard,UnitID,EntryID,OrderNum,OrderPrice,OrderMoney,OrderFree,ListGoodsType,OrderType,OutNum,NotNum,Uom) values(#fieldNames#)";

    public static List<String> getInsertSQL(String tableName, String protStr) {
        if (tableName == null || tableName.equals(""))
            return null;
        Integer isUsed_index = null;
        String insertSql = null;
        if (tableName.equals("User")) {
            insertSql = userSql;
            isUsed_index = 4;
        } else if (tableName.equals("Users")) {
            insertSql = usersSql;
        } else if (tableName.equals("DeviceUI")) {
            insertSql = deviceuiSql;
        } else if (tableName.equals("DeviceBill")) {
            insertSql = devicebillSql;
        } else if (tableName.equals("DeviceBillSet")) {
            insertSql = devicebillsetSql;
        } else if (tableName.equals("Customer")) {
            insertSql = customerSql;
        } else if (tableName.equals("Goods")) {
            insertSql = goodSql;
        } else if (tableName.equals("ItemPrice")) {
            insertSql = itemPriceSql;
        } else if (tableName.equals("Phone")) {
            insertSql = phoneSql;
        } else if (tableName.equals("UserCustomer")) {
            insertSql = usercustomerSql;
        } else if (tableName.equals("Parameter")) {
            insertSql = parameterSql;
        } else if (tableName.equals("WareHouse")) {
            insertSql = warehouseSql;
        } else if (tableName.equals("FreeBill")) {
            insertSql = freebillSql;
        } else if (tableName.equals("FreeBillDetail")) {
            insertSql = freebilldetailSql;
        } else if (tableName.equals("UnitGroup")) {
            insertSql = unitgroupSql;
        } else if (tableName.equals("Seobill")) {
            insertSql = seordermainSql;
        } else if (tableName.equals("Seobillentry")) {
            insertSql = seorderdetailSql;
        } else {
            return null;
        }


        List<String> sqlList = new ArrayList<>();

        protStr = protStr.replace("<1>", "<2>");
        String[] lineStrs = protStr.split("<2>");//将一个字符串分割为子字符串，然后将结果作为字符串数组返回。

        if (lineStrs != null && lineStrs.length > 0)

        {
            for (int i = 1; i < lineStrs.length; i++) {
                String lineStr = "";
                if (lineStrs[i].endsWith("<3>")) {
                    lineStrs[i] = lineStrs[i] + " ";
                }
                String[] itemStrs = lineStrs[i].split("<3>");


                if (isUsed_index != null
                        && itemStrs[isUsed_index] != null
                        && (!tableName.equals("ItemPrice")
                        && itemStrs[isUsed_index].equals("1") || tableName
                        .equals("ItemPrice")
                        && itemStrs[isUsed_index].equals("0")))
                    continue;

                for (String str : itemStrs) {
                    lineStr = lineStr + "'" + str + "'" + ",";
                }
                lineStr = lineStr.substring(0, lineStr.length() - 1);
                sqlList.add(insertSql.replace("#fieldNames#", lineStr));
            }
        }

        return sqlList;
    }


}
