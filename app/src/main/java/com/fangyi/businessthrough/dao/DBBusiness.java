package com.fangyi.businessthrough.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.fangyi.businessthrough.bean.business.Goods;
import com.fangyi.businessthrough.bean.business.OrderDetail;
import com.fangyi.businessthrough.bean.business.OrderMain;
import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.bean.business.PurchaseOrderMain;
import com.fangyi.businessthrough.bean.business.SEOrderMain;
import com.fangyi.businessthrough.bean.business.Summarizing;
import com.fangyi.businessthrough.bean.system.SearchCondition;
import com.fangyi.businessthrough.db.DBHelper;
import com.socks.library.KLog;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.utils.business.CalculationUtils.mulss;

/**
 * 查询业务数据类
 * Created by FANGYI on 2016/9/12.
 */

public class DBBusiness {

    private DBHelper helper;
    private SQLiteDatabase db;
    private int orderMainPrintNum;

    public DBBusiness(Context context) {

        helper = new DBHelper(context);

        db = helper.openDatabase();
    }


    /**
     * 查找对应客户
     *
     * @return
     */
    public Map<String, String> getCustomers(String[] userSysId) {
        Map<String, String> clientMap = new HashMap<>();
//        String sql = "select c.customerSysId || ',' || c.typeId,c.customerName from Customer c,Users u,UserCustomer cu  where  cu.userSysId=? and c.customerSysId=cu.customerSysId ";
        String sql = "select c.customerSysId ,c.customerName from Customer c,Users u,UserCustomer cu  where  cu.userSysId=? and c.customerSysId=cu.customerSysId ";

        Cursor custor = db.rawQuery(sql, userSysId);

        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                clientMap.put(custor.getString(0), custor.getString(1));
            }
        }
        //select c.customerSysId || ',' || c.typeId,c.customerName from Customer c,Users u,UserCustomer cu where  u.userSysId=cu.userSysId and c.customerSysId=cu.customerSysId
        return clientMap;
    }


    /**
     * 得到参数
     *
     * @return
     */
    public Map<String, String> getParameter(String str) {
        Map<String, String> clientMap = new HashMap<>();
        String[] parameter = new String[]{str};
        String sql = "SELECT ParameterID,ParameterName FROM Parameter WHERE Parametertype = ?";

        Cursor custor = db.rawQuery(sql, parameter);

        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                clientMap.put(custor.getString(0), custor.getString(1));
            }
        }
        return clientMap;
    }


    /**
     * 得到供应商
     * @param str
     * @return
     */
    public Map<String, String> getSupplier(String str) {
        Map<String, String> clientMap = new HashMap<>();
        String[] parameter = new String[]{str};
        String sql = "SELECT ParameterID,ParameterName FROM Parameter WHERE Parametertype = ?";

        Cursor custor = db.rawQuery(sql, parameter);

        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                clientMap.put(custor.getString(0), custor.getString(1));
            }
        }
        return clientMap;
    }

    /**
     * 得到仓库名称     *
     * @return
     */
    public Map<String, String> getWareHouse(String str) {
        Map<String, String> clientMap = new HashMap<>();
        String sql = "SELECT WareHouseSysID,WareHouseName FROM WareHouse WHERE UserSysID = ?";
        String[] parameter = new String[]{str};
        Cursor custor = db.rawQuery(sql, parameter);

        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                clientMap.put(custor.getString(0), custor.getString(1));
            }
        }
        return clientMap;
    }

    /**
     * 获取全部商品的信息（系统内码，商品名称）
     *
     * @return
     */
    public Map<String, String> getGoods() {
        Map<String, String> goodsMap = new HashMap<String, String>();
        String sql = "SELECT goodsSysId,GoodsName FROM Goods";
        Cursor custor = db.rawQuery(sql, null);
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                goodsMap.put(custor.getString(0), custor.getString(1));
            }
        }

        return goodsMap;
    }

    /**
     * 根据GoodsSysID获取当个商品信息
     */
    public Map<String, String> getAloneGoodsMessage(String[] strings) {
        Map<String, String> goodsMap = new HashMap<>();

        String sql = "SELECT g.GoodsSysID, g.GoodsName,  g.Barcode, g.UnitID, g.UnitGroupID, g.Standard, i.Price FROM Goods g, ItemPrice i WHERE i.ItemID = g.GoodsSysID AND i.Uom = g.Unit AND g.GoodsSysID = ?";
        Cursor custor = db.rawQuery(sql, strings);
        int cols_len = custor.getColumnCount();
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                for (int i = 0; i < cols_len; i++) {
                    String cols_name = custor.getColumnName(i);
                    String cols_values = custor.getString(custor.getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    goodsMap.put(cols_name, cols_values);
                }

            }
        }
        return goodsMap;
    }


    /**
     * 根据GoodsSysID获取当个商品信息
     */
    public List<Map<String, String>> getBaseGoodsMessage(String goodsSysID) {
        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT g.GoodsSysID, g.GoodsName, g.Barcode, g.Standard, u.Conversion,\n" +
                "u.Uom, u.UnitID, u.UnitGroupID, u.Ubase FROM Goods g, UnitGroup u\n" +
                "WHERE g.UnitGroupID = u.UnitGroupID\n" +
                "AND g.GoodsSysID = '" + goodsSysID + "'";

        Cursor custor = db.rawQuery(sql, null);
        int cols_len = custor.getColumnCount();
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                Map<String, String> goodsMap = new HashMap<>();
                for (int i = 0; i < cols_len; i++) {

                    String cols_name = custor.getColumnName(i);
                    String cols_values = custor.getString(custor.getColumnIndex(cols_name));
                    if (cols_values == null) {
                        cols_values = "";
                    }
                    goodsMap.put(cols_name, cols_values);
                }
                list.add(goodsMap);
            }
        }
        return list;
    }


    public String getBaseGoodsMessagePrice(String changeNum, String fyFCustNameID, String goodsSysID, String kISID, String unitID, String conversion) {

//        KLog.e("====" + changeNum + "====" + fyFCustNameID + "====" + goodsSysID + "====" + kISID + "====" + unitID + "====" + conversion);

        String price = null;
        db.beginTransaction();
        Cursor cursor = db.rawQuery("SELECT\n" +
                "\tPrice\n" +
                "FROM\n" +
                "\tItemPrice\n" +
                "WHERE\n" +
                "\t(\n" +
                "\t\tItemID = '" + fyFCustNameID + "'\n" +
                "\t\tOR ItemID = '" + goodsSysID + "'\n" +
                "\t\tOR ItemID = '" + kISID + "'\n" +
                "\t)\n" +
                "AND Uom = '" + unitID + "'\n" +
                "AND BeginDate <= datetime('now', 'localtime')\n" +
                "AND EndDate >= datetime('now', 'localtime')\n" +
                "AND CAST (BeginSales AS double) <= '" + changeNum + "'\n" +
                "AND CAST (EndSales AS double) >= '" + changeNum + "'\n" +
                "ORDER BY\n" +
                "\tPriority DESC", null);

        while (cursor.moveToNext()) {
            price = cursor.getString(0);
        }

        if (TextUtils.isEmpty(price)) {

            Cursor c = db.rawQuery("SELECT g.TradePrice FROM Goods g WHERE g.GoodsSysID='" + goodsSysID + "'", null);
            while (c.moveToNext()) {
                price = String.valueOf(mulss(conversion, c.getString(0)));
            }
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return price;
    }

    /**
     * 通过商品ID 查找 单位率
     *
     * @param strings
     * @return
     */
    public List<Map<String, String>> getUnitGroup(String[] strings) {

        List<Map<String, String>> list = new ArrayList<>();

        String sql = "SELECT Uom, Conversion FROM UnitGroup WHERE UnitGroupID = ( SELECT u.UnitGroupID FROM UnitGroup u, Goods g WHERE u.UnitID = g.UnitID AND g.GoodsSysID = ? )";
        Cursor custor = db.rawQuery(sql, strings);

        int cols_len = custor.getColumnCount();
        while (custor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = custor.getColumnName(i);
                String cols_values = custor.getString(custor.getColumnIndex(cols_name));
                if (cols_values == null) {
                    cols_values = "";
                }
                map.put(cols_name, cols_values);
            }
            list.add(map);
        }

        return list;
    }

    /**
     * 根据GoodsSysID获取商品促销方案
     */
    public List<Map<String, String>> getGoodsPromotion(String[] strings) {

        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT * FROM FreeBillDetail WHERE FPRoItem = ? AND FFlag = 1 AND UserSysID = ?";
        Cursor custor = db.rawQuery(sql, strings);

        int cols_len = custor.getColumnCount();
        while (custor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = custor.getColumnName(i);
                String cols_values = custor.getString(custor.getColumnIndex(cols_name));
                if (cols_values == null) {
                    cols_values = "";
                }
                map.put(cols_name, cols_values);
            }
            list.add(map);
        }

        return list;
    }

    /**
     * 通过方案编号查找所有方案子类
     * @param strings
     * @return
     */
    public List<Map<String, String>> getGoodsPromotionChild(String[] strings) {

        List<Map<String, String>> list = new ArrayList<>();
        String sql = "SELECT fbd.FPRoItem, fbd.FIndex, fbd.FproUnit, fbd.FProQty, fbd.FProAmount, fbd.FFlag, fb.FName, fb.FSaleType, fb.FSumQty, fb.FSumAmount, fb.FDate, fb.FStarDate, fb.FEndDate, fb.FSuit, fb.FMultiple FROM FreeBillDetail fbd, FreeBill fb WHERE fb.FIndex = fbd.FIndex AND fb.UserSysID = fbd.UserSysID AND fbd.FIndex = ? AND fbd.UserSysID = ?";
        Cursor custor = db.rawQuery(sql, strings);

        int cols_len = custor.getColumnCount();
        while (custor.moveToNext()) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < cols_len; i++) {
                String cols_name = custor.getColumnName(i);
                String cols_values = custor.getString(custor.getColumnIndex(cols_name));
                if (cols_values == null) {
                    cols_values = "";
                }
                map.put(cols_name, cols_values);
            }
            list.add(map);
        }

        return list;
    }

    /**
     * 通过商品id 查询商品名称
     */
    public String selectSQLGoodsName(String goodsSysID) {
        Cursor cursor = db.rawQuery("SELECT GoodsName FROM Goods WHERE GoodsSysID='" + goodsSysID + "'", null);
        String id = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
        }
        return id;
    }

    /**
     * 通过商品id 查询 商品单位
     * @param unitID
     * @return
     */
    public String selectSQLUom(String unitID) {
        Cursor cursor = db.rawQuery("SELECT Uom FROM UnitGroup WHERE UnitID='" + unitID + "'", null);
        String id = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
        }
        return id;
    }

    /**
     * 通过商品id 查询 商品单价
     * @param itemID
     * @return
     */
    public String selectSQLMinPrice(String itemID) {
        Cursor cursor = db.rawQuery("SELECT MIN(Price) FROM ItemPrice WHERE ItemID='" + itemID + "'", null);
        String id = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
        }
        return id;
    }

    /**
     * 通过客户id 查询 客户名称
     *
     * @param itemID
     * @return
     */
    public Map<String, String> selectSQLCustomerAddressTel(String itemID) {

        Map<String, String> goodsMap = new HashMap<>();

        Cursor cursor = db.rawQuery("SELECT Address,Tel FROM Customer WHERE CustomerSysID='" + itemID + "'", null);

        if (cursor != null && cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                goodsMap.put(cursor.getString(0), cursor.getString(1));
            }
        }
        return goodsMap;
    }


    /**
     * 通过KISID id 查询 登陆ID
     *
     * @param itemID
     * @return
     */
    public String selectSQLUserUserSysID(String itemID) {
        Cursor cursor = db.rawQuery("SELECT UserSysID FROM Users WHERE KISID='" + itemID + "'", null);
        String id = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
        }
        return id;
    }

    /**
     * 通过部门id 查询 部门名称
     *
     * @param itemID
     * @return
     */
    public String selectSQLParameterName(String itemID) {
        Cursor cursor = db.rawQuery("SELECT ParameterName FROM Parameter WHERE ParameterID='" + itemID + "'", null);
        String id = null;
        while (cursor.moveToNext()) {
            id = cursor.getString(0);
        }
        return id;
    }


    /**
     * 关闭数据库
     */
    public void closeDB() {
        db.close(); // 释放数据库资源
    }


    /**
     * ****************************************************************************************************************************
     */


    /**
     * 读取单据
     *
     * @param orderId
     * @return
     */
    public PrintOrderMain getOrderInfo(String orderId, String businessType) {
        db.beginTransaction();

        PrintOrderMain orderMain = null;
        String sql = "SELECT o.ID, o.OrderDate, o.AllMoney, o.SaveTime, o.IsUpLoad, o.Message, o.CustomerSySID, c.CustomerName, c.address, c.Tel, o.UserSysID, us.UserSysID, o.DeptID, p.ParameterName, o.WareHouseSysID, o.WareHouseName, o.BusinessType, o.FSaleType, o.FAreaPS, o.DeliveryDate, o.FSource\n" +
                "FROM OrderMain o, Customer c, Users us, Parameter p\n" +
                "WHERE o.ID = '" + orderId + "'\n" +
                "AND us.KISID = o.UserSysID\n" +
                "AND p.ParameterID = o.DeptID\n" +
                "AND c.CustomerSySID = o.CustomerSySID\n" +
                "AND BusinessType = '" + businessType + "'\n";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            orderMain = new PrintOrderMain();
            while (cursor.moveToNext()) {
                orderMain.id = cursor.getString(0);
                orderMain.orderDate = cursor.getString(1);
                orderMain.allMoney = cursor.getString(2);
                orderMain.saveTime = cursor.getString(3);
                orderMain.isUpLoad = cursor.getString(4);
                orderMain.message = cursor.getString(5);

                orderMain.customerSysId = cursor.getString(6);
                orderMain.customerName = cursor.getString(7);
                orderMain.customerAddress = cursor.getString(8);
                orderMain.customerTel = cursor.getString(9);

                orderMain.userSysId = cursor.getString(10);
                orderMain.userId = cursor.getString(11);

                orderMain.deptId = cursor.getString(12);
                orderMain.deptName = cursor.getString(13);

                orderMain.wareHouseSysId = cursor.getString(14);
                orderMain.wareHouseName = cursor.getString(15);

                orderMain.businessType = cursor.getString(16);
                orderMain.saleType = cursor.getString(17);
                orderMain.areaPS = cursor.getString(18);
                orderMain.deliveryDate = cursor.getString(19);
                orderMain.source = cursor.getString(20);
            }
        }


        List<Goods> orderGoods = new ArrayList<>();
        List<Goods> presentGoods = new ArrayList<>();
        String sqls = "SELECT g.goodsSysId,goodsId,goodsName,barCode,helpId,o.standard,conversion,orderNum,orderPrice,OrderType,OrderMoney,orderByGoodId," +
                "(select goodsName from goods sg where sg.goodsSysId=o.orderByGoodId) as  saledGoodsName ," +
                "(select orderNum from orderDetail od where od.goodsSysId=o.orderByGoodId) as  saledGoodsNum,wareHouseSysId,(select wareHouseName from wareHouse wh where wh.wareHouseSysId=o.wareHouseSysId) as wareHouseName,g.unit,o.orderFree,o.ListGoodsType,o.unitID  " +
                "FROM Goods g,orderDetail o " +
                "where g.goodsSysId=o.goodsSysId  and id='" + orderId + "' and " +
                "businessType = '" + businessType + "'";

        Cursor cursors = db.rawQuery(sqls, null);

        if (cursors != null && cursors.getCount() > 0) {
            while (cursors.moveToNext()) {
                Goods goods = new Goods();
                goods.goodsSysID = cursors.getString(0);
                goods.goodsID = cursors.getString(1);
                goods.goodsName = cursors.getString(2);
                goods.barcode = cursors.getString(3);
                goods.helpID = cursors.getString(4);
                goods.unit = cursors.getString(5);
                goods.conversion = cursors.getString(6);
                goods.orderNum = cursors.getString(7);
                goods.orderPrice = cursors.getString(8);
                goods.goodsType = cursors.getString(9);
                goods.orderCounter = cursors.getString(10);
                goods.saledGoodsSysId = cursors.getString(11);
                goods.saledGoodsName = cursors.getString(12);
                goods.saleGoodsNum = cursors.getString(13);
                goods.wareHouseSysId = cursors.getString(14);
                goods.wareHouseName = cursors.getString(15);
                goods.unit = cursors.getString(16);
                goods.fName = cursors.getString(17);
                goods.listGoodsType = cursors.getString(18);
                goods.unitID = cursors.getString(19);

                if ("1".equals(cursors.getString(9))) {
                    orderGoods.add(goods);
                } else
                    presentGoods.add(goods);
            }
        }


        KLog.e("======$$$$$$$=======" + orderGoods.toString());
        KLog.e("======%%%%%%%=======" + presentGoods.toString());

        if (!orderGoods.equals(null)) {
            orderMain.orderGoods = orderGoods;
        }

        if (!presentGoods.equals(null)) {
            orderMain.presentGoods = presentGoods;
        }


        db.setTransactionSuccessful();
        db.endTransaction();

        return orderMain;
    }

    /**
     * 保存数据
     *
     * @param orderMain
     * @param orderDetailArrayList
     */
    public void saveOrder(String orderId, OrderMain orderMain, ArrayList<OrderDetail> orderDetailArrayList) {

        db.execSQL("delete FROM OrderMain where id = '" + orderId + "' and businessType = '" + orderMain.getBusinessType() + "'");
        db.execSQL("delete FROM OrderDetail where id = '" + orderId + "' and businessType = '" + orderMain.getBusinessType() + "'");

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put("ID", orderMain.getID());
        values.put("OrderDate", orderMain.getOrderDate());
        values.put("DeliveryDate", orderMain.getDeliveryDate());
        values.put("CustomerSySID", orderMain.getCustomerSySID());
        values.put("WareHouseName", orderMain.getWareHouseName());
        values.put("WareHouseSysID", orderMain.getWareHouseSysID());
        values.put("UserSysID", orderMain.getUserSysID());
        values.put("SaveTime", orderMain.getSaveTime());
        values.put("IsUpload", orderMain.getIsUpLoad());
        values.put("Message", orderMain.getMessage());
        values.put("DeptID", orderMain.getDeptID());
        values.put("BusinessType", orderMain.getBusinessType());
        values.put("FSaleType", orderMain.getFSaleType());
        values.put("FAreaPS", orderMain.getFAreaPS());
        values.put("AllMoney", orderMain.getAllMoney());
        values.put("FUpdateType", orderMain.getFUpdateType());
        values.put("FDelType", orderMain.getFDelType());
        values.put("PrintNum", orderMain.getPrintNum());
        values.put("FSource", orderMain.getFyFSource());
        db.insert("orderMain", null, values);

        int i = 0;
        for (OrderDetail orderDetail : orderDetailArrayList) {

//            KLog.e("=== " + i++ + " =====" + orderDetail.toString());

            String sql = "insert into OrderDetail(ID,GoodsSysID,unitID,Standard,OrderNum,OrderPrice,OrderMoney,OrderType,OrderFree,OrderbyGoodID,Node,WareHouseSysId,businessType,ListGoodsType) ";
            sql = sql + "values('" + orderDetail.getID() + "','" + orderDetail.getGoodsSysID() + "','" + orderDetail.getUnitID() + "','" + orderDetail.getStandard() + "','" + orderDetail.getOrderNum() + "','" + orderDetail.getOrderPrice() + "','" + orderDetail.getOrderMoney() + "','" + orderDetail.getOrderType() + "','" + orderDetail.getOrderFree() + "','" + orderDetail.getOrderbyGoodID() + "','" + orderDetail.getNode() + "','" + orderDetail.getWareHouseSysId() + "','" + orderDetail.getBusinessType() + "','" + orderDetail.getListGoodsType() + "')";
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    //获取订单ID
    private String getNewOrderId() {
        DateFormat format = new SimpleDateFormat("yyyyMMdd");
        DateFormat format2 = new SimpleDateFormat("yyyy-MM-dd");
        String sql_maxId = "SELECT max(id) FROM orderMain where saveTime='" + format2.format(new Date()) + "'";
        Cursor custor = db.rawQuery(sql_maxId, null);

        if (custor != null && custor.getCount() > 0) {
            String maxId = "";
            while (custor.moveToNext()) {
                maxId = custor.getString(0);
                if (maxId != null && maxId.length() > 0)
                    break;
            }
            if (maxId != null && maxId.length() > 0)
                return String.valueOf((Long.parseLong(maxId) + 1));
        }
        return format.format(new Date()) + "0001";
    }

    /**
     * 删除指定订单号的订单
     *
     * @param orderId
     */
    public void delOrderList(String orderId) {
        db.beginTransaction();
        db.delete("orderMain", "id=?", new String[]{orderId});
        db.delete("orderDetail", "id=?", new String[]{orderId});
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 删除指定订单号的订单
     *
     * @param orderIds 订单号列表
     */
    public void delOrderList(List<String> orderIds) {
        db.beginTransaction();
        for (String orderId : orderIds) {
            db.delete("orderMain", "id=?", new String[]{orderId});
            db.delete("orderDetail", "id=?", new String[]{orderId});
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * 查询订单表数量
     */
    public String selectOrderMainCount(String kISID, String BusinessType) {
        Cursor cursor = db.rawQuery("SELECT count(ID) FROM OrderMain WHERE UserSysID='" + kISID + "' AND BusinessType='" + BusinessType + "'", null);
        String count = null;
        while (cursor.moveToNext()) {
            count = cursor.getString(0);
        }
        return count;
    }


    /**
     * 更改订单上传状态
     *
     * @param orderId
     */
    public void upDateOrderIsUpload(String orderId, String businessType) {
        KLog.e("======" + orderId + businessType);
        db.execSQL("UPDATE OrderMain SET IsUpLoad = '1' WHERE ID = ? AND businessType = ?", new String[]{orderId, businessType});
    }

    /**
     * 修改打印数量
     *
     * @param orderMainPrintNum
     * @param orderId
     * @param businessType
     */
    public void upDateOrderMainPrintNum(int orderMainPrintNum, String orderId, String businessType) {
        db.execSQL("UPDATE OrderMain SET PrintNum ='" + orderMainPrintNum + "'  WHERE id ='" + orderId + "' AND businessType ='" + businessType + "'");
    }


    /**
     * 获取所有订单信息
     *
     * @return
     */
    public List<PrintOrderMain> getOrderList(String businessType, SearchCondition condition) {
        List<PrintOrderMain> orderList = new ArrayList<>();
        String sql = "select id,orderDate,customerName,allMoney,isUpload,businessType,address,tel,WareHouseName,c.CustomerSysID,FUpdateType,FDelType,PrintNum,FSource " +
                "from orderMain m,customer c " +
                "where m.customerSysId=c.customerSysId  and businessType='" + businessType + "' ";
        if (condition != null) {
            if (condition.IsUpload != null)
                sql = sql + "and isUpload='" + condition.IsUpload.toString() + "' ";
            if (condition.StartDate != null && !condition.StartDate.equals(""))
                sql = sql + "and orderDate>='" + condition.StartDate + "' ";
            if (condition.EndDate != null && !condition.EndDate.equals(""))
                sql = sql + "and orderDate<='" + condition.EndDate + "' ";
            if (condition.CustomerSysId != null)
                sql = sql + "and m.customerSysId='" + condition.CustomerSysId + "' ";
            if (condition.wareHouseName != null)
                sql = sql + "and wareHouseName='" + condition.wareHouseName + "' ";

        }

        sql = sql + " order by orderDate desc ";

        Cursor custor = db.rawQuery(sql, null);
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                PrintOrderMain order = new PrintOrderMain();
                order.id = custor.getString(0);
                order.orderDate = custor.getString(1);
                order.customerName = custor.getString(2);
                order.allMoney = custor.getString(3);
                order.isUpLoad = custor.getString(4);
                order.businessType = custor.getString(5);
                order.customerAddress = custor.getString(6);
                order.customerTel = custor.getString(7);
                order.wareHouseName = custor.getString(8);
                order.customerSysId = custor.getString(9);
                order.updateType = custor.getString(10);
                order.delType = custor.getString(11);
                order.printNum = custor.getString(12);
                order.source = custor.getString(13);
                orderList.add(order);
            }
        }

        return orderList;
    }

    /**
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     */


    /**
     * 获取已销售的客户的信息（系统内码、客户名称）
     *
     * @return key-系统内码  value-客户名称
     */
    public Map<String, String> getOrderedCustomers(String businessType) {
        Map<String, String> clientMap = new HashMap<String, String>();
        String sql = "SELECT DISTINCT (c.customerSysId), c.customerName FROM customer c, orderMain o WHERE c.customerSysId = o.customerSysId AND o.businessType = '" + businessType + "'";
        Cursor custor = db.rawQuery(sql, null);
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                clientMap.put(custor.getString(0), custor.getString(1));
            }
        }

        return clientMap;
    }

    public List<String> getOrderedWareHouseName(String businessType) {
        List<String> wareHouseNameList = new ArrayList<>();
        String sql = "SELECT DISTINCT (WareHouseName) FROM orderMain WHERE businessType = '" + businessType + "'";
        Cursor custor = db.rawQuery(sql, null);
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                wareHouseNameList.add(custor.getString(0));
            }
        }
        return wareHouseNameList;
    }


    /**
     * 读取单据列表
     *
     * @return
     */
    public List<SEOrderMain> getSEOrderList(String userSysID, String custNameID, String businessType) {
        db.beginTransaction();

        String businessTypes = null;
        if ("1".equals(businessType)) {
            businessTypes = "81";
        }

        List<SEOrderMain> orderList = new ArrayList<>();


        String sql = "SELECT * FROM SEOrderMain \n" +
                "WHERE CustomerSySID = '" + custNameID + "'\n" +
                "AND UserSysID ='" + userSysID + "'\n" +
                "AND BusinessType ='" + businessTypes + "'\n" +
                "order by orderDate desc";


        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                SEOrderMain order = new SEOrderMain();
                order.setUserSysID(cursor.getString(0));
                order.setCustomerName(cursor.getString(1));
                order.setCustomerSySID(cursor.getString(2));
                order.setOrderDate(cursor.getString(3));
                order.setID(cursor.getString(4));

                String sqls = "SELECT GoodsSysID, GoodsName, Standard, UnitID, Uom, OrderNum, OrderPrice, OrderMoney, OrderFree, ListGoodsType, OrderType, OutNum, NotNum FROM SEOrderDetail \n" +
                        "WHERE \n" +
                        "id = '" + cursor.getString(4) + "'\n" +
                        "AND UserSysID ='" + userSysID + "'";

                List<Goods> orderGoods = new ArrayList<>();
                List<Goods> presentGoods = new ArrayList<>();

                Cursor cursors = db.rawQuery(sqls, null);

                if (cursors != null && cursors.getCount() > 0) {
                    while (cursors.moveToNext()) {
                        Goods goods = new Goods();
                        goods.goodsSysID = cursors.getString(0);
                        goods.goodsName = cursors.getString(1);
                        goods.standard = cursors.getString(2);
                        goods.unitID = cursors.getString(3);
                        goods.unit = cursors.getString(4);
                        goods.orderNum = cursors.getString(5);
                        goods.orderPrice = cursors.getString(6);
                        goods.orderCounter = cursors.getString(7);
                        goods.fName = cursors.getString(8);
                        goods.listGoodsType = cursors.getString(9);
                        goods.goodsType = cursors.getString(10);
                        goods.outNum = cursors.getString(11);
                        goods.notNum = cursors.getString(12);

                        if ("1".equals(cursors.getString(9))) {
                            orderGoods.add(goods);
                        } else
                            presentGoods.add(goods);

//                        KLog.e("=====" + goods.toString());
                    }
                }

                if (!orderGoods.equals(null)) {
                    order.setOrderGoods(orderGoods);
                }

                if (!presentGoods.equals(null)) {
                    order.setPresentGoods(presentGoods);
                }

                order.setBusinessType(cursor.getString(5));
                order.setFSaleStyleID(cursor.getString(6));
                order.setFSaleStyleName(cursor.getString(7));
                order.setDeptID(cursor.getString(8));
                order.setKISID(cursor.getString(9));
                order.setMessage(cursor.getString(10));
                order.setKINGID(cursor.getString(11));
                order.setAllMoney(cursor.getString(12));

                orderList.add(order);
            }
        }


        db.setTransactionSuccessful();
        db.endTransaction();

        return orderList;
    }


    /**
     * 读取单据列表
     *
     * @return
     */
    public SEOrderMain getSEOrder(String userSysID, String orderId, String businessType) {
        db.beginTransaction();
        String businessTypes = null;
        if ("2".equals(businessType)) {
            businessTypes = "81";
        }


        String sql = "SELECT * FROM SEOrderMain \n" +
                "WHERE ID = '"+orderId+ "'\n" +
                "AND BusinessType ='" + businessTypes + "'\n" +
                "ORDER BY orderDate DESC";

        SEOrderMain order = new SEOrderMain();

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {

            while (cursor.moveToNext()) {
                order.setUserSysID(cursor.getString(0));
                order.setCustomerName(cursor.getString(1));
                order.setCustomerSySID(cursor.getString(2));
                order.setOrderDate(cursor.getString(3));
                order.setID(cursor.getString(4));
                order.setBusinessType(cursor.getString(5));
                order.setFSaleStyleID(cursor.getString(6));
                order.setFSaleStyleName(cursor.getString(7));
                order.setDeptID(cursor.getString(8));
                order.setKISID(cursor.getString(9));
                order.setMessage(cursor.getString(10));
                order.setKINGID(cursor.getString(11));
                order.setAllMoney(cursor.getString(12));

            }
        }


        String sqls = "SELECT GoodsSysID, GoodsName, Standard, UnitID, Uom, OrderNum, OrderPrice, OrderMoney, OrderFree, ListGoodsType, OrderType, OutNum, NotNum FROM SEOrderDetail \n" +
                "WHERE \n" +
                "id = '" + orderId + "'\n" +
                "AND UserSysID ='" + userSysID + "'";

        List<Goods> orderGoods = new ArrayList<>();
        List<Goods> presentGoods = new ArrayList<>();

        Cursor cursors = db.rawQuery(sqls, null);

        if (cursors != null && cursors.getCount() > 0) {
            while (cursors.moveToNext()) {
                Goods goods = new Goods();
                goods.goodsSysID = cursors.getString(0);
                goods.goodsName = cursors.getString(1);
                goods.standard = cursors.getString(2);
                goods.unitID = cursors.getString(3);
                goods.unit = cursors.getString(4);
                goods.orderNum = cursors.getString(5);
                goods.orderPrice = cursors.getString(6);
                goods.orderCounter = cursors.getString(7);
                goods.fName = cursors.getString(8);
                goods.listGoodsType = cursors.getString(9);
                goods.goodsType = cursors.getString(10);
                goods.outNum = cursors.getString(11);
                goods.notNum = cursors.getString(12);

                if ("1".equals(cursors.getString(10))) {
                    orderGoods.add(goods);
                } else {
                    presentGoods.add(goods);
                }

            }
        }

        if (!orderGoods.equals(null)) {
            order.setOrderGoods(orderGoods);
        }

        if (!presentGoods.equals(null)) {
            order.setPresentGoods(presentGoods);
        }


        db.setTransactionSuccessful();
        db.endTransaction();

        return order;
    }


    /**
     * 更改源单选择标识状态
     *
     * @param orderId
     */
    public void upDateSEOrderSELECTID(String orderId, String businessType) {
        db.execSQL("UPDATE SEOrderMain SET SELECTID = '1' WHERE ID = ? AND businessType = ?", new String[]{orderId, businessType});
    }


    /**
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     */



    /**
     * 未打印数
     *
     * @return
     */
    public HashMap<String, String> notPrintNumber() {
        Cursor PrintNum = db.rawQuery("SELECT count(*) FROM OrderMain WHERE PrintNum = '0'", null);
        Cursor IsUpLoad = db.rawQuery("SELECT count(*) FROM OrderMain WHERE IsUpLoad = '0'", null);

        HashMap<String, String> hashMap = new HashMap<>();

        String printNum = "0";
        while (PrintNum.moveToNext()) {
            printNum = PrintNum.getString(0);
        }
        hashMap.put("printNum", printNum);

        String isUpLoad = "0";
        while (IsUpLoad.moveToNext()) {
            isUpLoad = IsUpLoad.getString(0);
        }
        hashMap.put("isUpLoad", isUpLoad);

        return hashMap;
    }


    /**
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     */



    /**
     * 销售总汇
     *
     * @param sqlStrType
     * @param sqlDocumentType
     * @param strStartDate
     * @param strEndDate
     * @param strDept
     * @param strCustomerSysId
     * @param strGoodsSysId    @return
     */
    public List<Summarizing> getSumShow(String sqlStrType, String sqlDocumentType, String strStartDate, String strEndDate, String strDept, String strCustomerSysId, String strGoodsSysId) {
        db.beginTransaction();
        List<Summarizing> summarizings = new ArrayList<>();

        db.execSQL("CREATE TABLE Data (\n" +
                "\tDateReceiptNumber TEXT,\n" +
                "\tDataDate TEXT,\n" +
                "\tDataWarehouse TEXT,\n" +
                "\tDataDocumentType TEXT,\n" +
                "\tDataCustomerSysID TEXT,\n" +
                "\tDataCustomerName TEXT,\n" +
                "\tDataGoodsSysID TEXT,\n" +
                "\tDataGoodsName TEXT,\n" +
                "\tDataStandard TEXT,\n" +
                "\tDataAllMoney BLOB,\n" +
                "\tDataFOrder INTEGER,\n" +
                "\tDataFSum INTEGER\n" +
                ");\n");

        db.execSQL("INSERT INTO Data (\n" +
                "\tDateReceiptNumber,\n" +
                "\tDataDate,\n" +
                "\tDataWarehouse,\n" +
                "\tDataDocumentType,\n" +
                "\tDataCustomerSysID,\n" +
                "\tDataCustomerName,\n" +
                "\tDataGoodsSysID,\n" +
                "\tDataGoodsName,\n" +
                "\tDataStandard,\n" +
                "\tDataAllMoney,\n" +
                "\tDataFSum\n" +
                ") SELECT\n" +
                "\t单据表.id DateReceiptNumber,\n" +
                "\t单据表.orderDate DataDate,\n" +
                "\t单据表.WareHouseName DataWarehouse,\n" +
                "\t单据表.businessType DataDocumentType,\n" +
                "\t客户表.CustomerSysID DataCustomerSysID,\n" +
                "\t客户表.CustomerName DataCustomerName,\n" +
                "\t单据分录表.goodsSysID DataGoodsSysID,\n" +
                "\t商品表.goodsname DataGoodsName,\n" +
                "\t商品表.standard DataStandard,\n" +
                "\t单据分录表.orderMoney DataAllMoney,\n" +
                "\t0 DataFSum\n" +
                "FROM\n" +
                "\torderMain 单据表\n" +
                "INNER JOIN orderdetail 单据分录表 ON 单据表.id = 单据分录表.id\n" +
                "INNER JOIN customer 客户表 ON 单据表.customerSysId = 客户表.customerSysId\n" +
                "INNER JOIN goods 商品表 ON 单据分录表.GoodsSysID = 商品表.GoodsSysID\n" +
                "WHERE\n" +
                "\t单据表.businessType ='1';");


        if ("0".equals(sqlStrType)) {

            db.execSQL("--汇总依据（商品）\n" +
                    "Insert Into Data (DataGoodsSysID,DataGoodsName,DataStandard,DataAllMoney,DataFOrder,DataFSum)\n" +
                    "Select DataGoodsSysID,DataGoodsName,DataStandard,Sum(DataAllMoney),1 DataFOrder,1 DataFSum\n" +
                    "From Data Where DataFSum=0\n" +
                    "Group By DataGoodsSysID,DataGoodsName,DataStandard;");

            db.execSQL("Insert Into Data (DataGoodsSysID,DataGoodsName,DataAllMoney,DataFOrder,DataFSum)\n" +
                    "Select '合计','合计',Sum(DataAllMoney),9 DataFOrder,1 DataFSum\n" +
                    "From Data Where DataFSum=0;");

            String sql = "Select DataGoodsName 商品,DataStandard 规格,DataAllMoney\n" +
                    "From Data Where DataFSum>0 \n" +
                    "Order By DataFOrder,DataGoodsSysID,DataGoodsName;";

            KLog.e("====1111111111111======" + sql);

            Cursor custor = db.rawQuery(sql, null);
            if (custor != null && custor.getCount() > 0) {
                while (custor.moveToNext()) {
                    Summarizing summarizing = new Summarizing();
                    summarizing.setGoodsName(custor.getString(0));
                    summarizing.setStandard(custor.getString(1));
                    summarizing.setAllMoney(custor.getString(2));
                    summarizings.add(summarizing);


                }
            }

        } else if ("1".equals(sqlStrType)) {
            db.execSQL("--汇总依据（客户）\n" +
                    "Insert Into Data (DataCustomerSysID,DataCustomerName,DataAllMoney,DataFOrder,DataFSum)\n" +
                    "Select DataCustomerSysID,DataCustomerName,Sum(DataAllMoney),1 DataFOrder,1 DataFSum\n" +
                    "From Data Where DataFSum=0\n" +
                    "Group By DataCustomerSysID,DataCustomerName ;");

            db.execSQL("Insert Into Data (DataCustomerSysID,DataCustomerName,DataAllMoney,DataFOrder,DataFSum)\n" +
                    "Select '合计','合计',Sum(DataAllMoney),9 DataFOrder,1 DataFSum\n" +
                    "From Data Where DataFSum=0 ;");

            String sql = "Select DataCustomerName 客户,DataAllMoney\n" +
                    "From Data Where DataFSum>0 \n" +
                    "Order By DataFOrder,DataCustomerSysID,DataCustomerName ;";

            KLog.e("====1111111111111======" + sql);

            Cursor custor = db.rawQuery(sql, null);
            if (custor != null && custor.getCount() > 0) {
                while (custor.moveToNext()) {
                    Summarizing summarizing = new Summarizing();
                    summarizing.setCustomerName(custor.getString(0));
                    summarizing.setAllMoney(custor.getString(1));
                    summarizings.add(summarizing);

                    KLog.e("=======00000=======" + summarizing.toString());
                }
            }
        } else if ("2".equals(sqlStrType)) {
            db.execSQL("--汇总依据（客户+商品）\n" +
                    "INSERT INTO Data (\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName,\n" +
                    "\tDataGoodsSysID,\n" +
                    "\tDataGoodsName,\n" +
                    "\tDataStandard,\n" +
                    "\tDataAllMoney,\n" +
                    "\tDataFOrder,\n" +
                    "\tDataFSum\n" +
                    ") SELECT\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName,\n" +
                    "\tDataGoodsSysID,\n" +
                    "\tDataGoodsName,\n" +
                    "\tDataStandard,\n" +
                    "\tSum(DataAllMoney),\n" +
                    "\t1 DataFOrder,\n" +
                    "\t1 DataFSum\n" +
                    "FROM\n" +
                    "\tData\n" +
                    "WHERE\n" +
                    "\tDataFSum = 0\n" +
                    "GROUP BY\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName,\n" +
                    "\tDataGoodsSysID,\n" +
                    "\tDataGoodsName,\n" +
                    "\tDataStandard;");

            db.execSQL("INSERT INTO Data (\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName,\n" +
                    "\tDataGoodsSysID,\n" +
                    "\tDataGoodsName,\n" +
                    "\tDataStandard,\n" +
                    "\tDataAllMoney,\n" +
                    "\tDataFOrder,\n" +
                    "\tDataFSum\n" +
                    ") SELECT\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName || '小计',\n" +
                    "\tDataGoodsSysID,\n" +
                    "\tDataGoodsName,\n" +
                    "\tDataStandard,\n" +
                    "\tSum(DataAllMoney),\n" +
                    "\t1 DataFOrder,\n" +
                    "\t1 DataFSum\n" +
                    "FROM\n" +
                    "\tData\n" +
                    "WHERE\n" +
                    "\tDataFSum = 0\n" +
                    "GROUP BY\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName;");

            db.execSQL("INSERT INTO Data (\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName,\n" +
                    "\tDataAllMoney,\n" +
                    "\tDataFOrder,\n" +
                    "\tDataFSum\n" +
                    ") SELECT\n" +
                    "\t'合计',\n" +
                    "\t'合计',\n" +
                    "\tSum(DataAllMoney),\n" +
                    "\t9 DataFOrder,\n" +
                    "\t1 DataFSum\n" +
                    "FROM\n" +
                    "\tData\n" +
                    "WHERE\n" +
                    "\tDataFSum = 0;");

            String sql = "SELECT\n" +
                    "\tDataCustomerName 客户,\n" +
                    "\tDataGoodsName 商品,\n" +
                    "\tDataStandard 规格,\n" +
                    "\tDataAllMoney\n" +
                    "FROM\n" +
                    "\tData\n" +
                    "WHERE\n" +
                    "\tDataFSum > 0\n" +
                    "ORDER BY\n" +
                    "\tDataFOrder,\n" +
                    "\tDataCustomerSysID,\n" +
                    "\tDataCustomerName,\n" +
                    "\tDataGoodsSysID;";

            KLog.e("====1111111111111======" + sql);

            Cursor custor = db.rawQuery(sql, null);
            if (custor != null && custor.getCount() > 0) {
                while (custor.moveToNext()) {
                    Summarizing summarizing = new Summarizing();
                    summarizing.setCustomerName(custor.getString(0) + "");
                    summarizing.setGoodsName(custor.getString(1) + "");
                    summarizing.setStandard(custor.getString(2) + "");
                    summarizing.setAllMoney(custor.getString(3) + "");
                    summarizings.add(summarizing);

                    KLog.e("=======00000=======" + summarizing.toString());
                }
            }
        }

        db.execSQL("DROP TABLE Data");

        db.setTransactionSuccessful();
        db.endTransaction();

        return summarizings;
    }



    /**
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     */


    /**
     * 保存采购数据
     *
     * @param ID
     * @param orderDetailArrayList
     */
    public void savePurchaseOrder(String ID, PurchaseOrderMain orderMain, ArrayList<OrderDetail> orderDetailArrayList) {

        db.execSQL("delete FROM PurchaseOrderMain where id = '" + ID + "' and businessType = '" + orderMain.strBusinessType + "'");
        db.execSQL("delete FROM PurchaseOrderDetail where id = '" + ID + "' and businessType = '" + orderMain.strBusinessType + "'");

        db.beginTransaction();

        ContentValues values = new ContentValues();
        values.put("ID", orderMain.ID);
        values.put("fyFFetchDayV", orderMain.strFyFFetchDayV);
        values.put("fyFPlanFetchDay1", orderMain.strFyFPlanFetchDay1);
        values.put("fyFPlanFetchDay2", orderMain.strFyFPlanFetchDay2);
        values.put("fyFPOStyle", orderMain.strFyFPOStyle);
        values.put("fyFAreaPS", orderMain.strFyFAreaPS);
        values.put("fyFBizType", orderMain.strFyFBizType);
        values.put("fyFSource", orderMain.strFyFSource);
        values.put("fyFSupplier", orderMain.strFyFSupplier);
        values.put("fyFRequester", orderMain.strFyFRequester);
        values.put("fyFDept", orderMain.strFyFDept);
        values.put("fyFFManager", orderMain.strFyFFManager);
        values.put("fyFSManager", orderMain.strFyFSManager);
        values.put("fyFDCStock", orderMain.strFyFDCStock);
        values.put("fyFDigest", orderMain.strFyFDigest);
        values.put("BusinessType", orderMain.strBusinessType);
        values.put("AllMoney", orderMain.strAllMoney);
        values.put("IsUpLoad", orderMain.strIsUpLoad);
        values.put("FUpdateType", orderMain.strFUpdateType);
        values.put("FDelType", orderMain.strFDelType);
        values.put("PrintNum", orderMain.strPrintNum);
        db.insert("PurchaseOrderMain", null, values);


        for (OrderDetail orderDetail : orderDetailArrayList) {

            String sql = "insert into PurchaseOrderDetail(ID,GoodsSysID,unitID,Standard,OrderNum,OrderPrice,OrderMoney,OrderType,OrderFree,OrderbyGoodID,Node,WareHouseSysId,businessType,ListGoodsType) ";
            sql = sql + "values('" + orderDetail.getID() + "','" + orderDetail.getGoodsSysID() + "','" + orderDetail.getUnitID() + "','" + orderDetail.getStandard() + "','" + orderDetail.getOrderNum() + "','" + orderDetail.getOrderPrice() + "','" + orderDetail.getOrderMoney() + "','" + orderDetail.getOrderType() + "','" + orderDetail.getOrderFree() + "','" + orderDetail.getOrderbyGoodID() + "','" + orderDetail.getNode() + "','" + orderDetail.getWareHouseSysId() + "','" + orderDetail.getBusinessType() + "','" + orderDetail.getListGoodsType() + "')";
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }


    /**
     * 修改打印数量
     *
     * @param orderMainPrintNum
     * @param orderId
     * @param businessType
     */
    public void upDatePurchaseOrderMainPrintNum(int orderMainPrintNum, String orderId, String businessType) {
        db.execSQL("UPDATE PurchaseOrderMain SET PrintNum ='" + orderMainPrintNum + "'  WHERE id ='" + orderId + "' AND businessType ='" + businessType + "'");
    }


    /**
     * 采购  获取单据
     *
     * @param orderId
     * @param businessType
     * @return
     */
    public PrintPurchaseOrderMain getPurchaseOrderInfo(String orderId, String businessType) {
        db.beginTransaction();
        PrintPurchaseOrderMain orderMain = null;
        String sql = "SELECT\n" +
                "\t*\n" +
                "FROM\n" +
                "\tPurchaseOrderMain\n" +
                "WHERE\n" +
                "\tid = '" + orderId + "'\n" +
                "AND businessType = '" + businessType + "'";

        Cursor cursor = db.rawQuery(sql, null);
        if (cursor != null && cursor.getCount() > 0) {
            orderMain = new PrintPurchaseOrderMain();
            while (cursor.moveToNext()) {
                orderMain.id = cursor.getString(0);
                orderMain.fyFFetchDayV = cursor.getString(1);
                orderMain.fyFPlanFetchDay1 = cursor.getString(2);
                orderMain.fyFPlanFetchDay2 = cursor.getString(3);
                orderMain.fyFPOStyle = cursor.getString(4);
                orderMain.fyFAreaPS = cursor.getString(5);
                orderMain.fyFBizType = cursor.getString(6);


                if ("4".equals(businessType)) {
                    orderMain.fyFSupplier = "";
                    orderMain.fyFSource = "";
                } else {
                    orderMain.fyFSource = cursor.getString(7);
                    orderMain.fyFSupplier = cursor.getString(8);
                }

                orderMain.fyFRequester = cursor.getString(9);
                orderMain.fyFDept = cursor.getString(10);
                orderMain.fyFFManager = cursor.getString(11);
                orderMain.fyFSManager = cursor.getString(12);
                orderMain.fyFDCStock = cursor.getString(13);
                orderMain.fyFDigest = cursor.getString(14);
                orderMain.businessType = cursor.getString(15);
                orderMain.allMoney = cursor.getString(16);
                orderMain.isUpLoad = cursor.getString(17);
                orderMain.fUpdateType = cursor.getString(18);
                orderMain.fDelType = cursor.getString(19);
                orderMain.printNum = cursor.getString(20);
            }
        }


        List<Goods> orderGoods = new ArrayList<>();

        String sqls = "SELECT\n" +
                "\tg.goodsSysId,goodsId,goodsName,barCode,helpId,o.standard,conversion,orderNum,orderPrice,OrderType,OrderMoney,orderByGoodId,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\tgoodsName\n" +
                "\t\tFROM\n" +
                "\t\t\tgoods sg\n" +
                "\t\tWHERE\n" +
                "\t\t\tsg.goodsSysId = o.orderByGoodId\n" +
                "\t) AS saledGoodsName,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\torderNum\n" +
                "\t\tFROM\n" +
                "\t\t\torderDetail od\n" +
                "\t\tWHERE\n" +
                "\t\t\tod.goodsSysId = o.orderByGoodId\n" +
                "\t) AS saledGoodsNum,\n" +
                "\twareHouseSysId,\n" +
                "\t(\n" +
                "\t\tSELECT\n" +
                "\t\t\twareHouseName\n" +
                "\t\tFROM\n" +
                "\t\t\twareHouse wh\n" +
                "\t\tWHERE\n" +
                "\t\t\twh.wareHouseSysId = o.wareHouseSysId\n" +
                "\t) AS wareHouseName,\n" +
                "\tg.unit,o.orderFree,o.ListGoodsType,o.unitID\n" +
                "FROM\n" +
                "\tGoods g,\n" +
                "\tPurchaseOrderDetail o\n" +
                "WHERE\n" +
                "\tg.goodsSysId = o.goodsSysId\n" +
                "AND id = '" + orderId + "'\n" +
                "AND businessType = '" + businessType + "'";

        Cursor cursors = db.rawQuery(sqls, null);

        if (cursors != null && cursors.getCount() > 0) {
            while (cursors.moveToNext()) {
                Goods goods = new Goods();
                goods.goodsSysID = cursors.getString(0);
                goods.goodsID = cursors.getString(1);
                goods.goodsName = cursors.getString(2);
                goods.barcode = cursors.getString(3);
                goods.helpID = cursors.getString(4);
                goods.unit = cursors.getString(5);
                goods.conversion = cursors.getString(6);
                goods.orderNum = cursors.getString(7);

                if ("4".equals(businessType)) {
                    goods.orderPrice = "";
                    goods.goodsType = "";
                } else {
                    goods.orderPrice = cursors.getString(8);
                    goods.goodsType = cursors.getString(9);
                }

                goods.orderCounter = cursors.getString(10);
                goods.saledGoodsSysId = cursors.getString(11);
                goods.saledGoodsName = cursors.getString(12);
                goods.saleGoodsNum = cursors.getString(13);
                goods.wareHouseSysId = cursors.getString(14);
                goods.wareHouseName = cursors.getString(15);
                goods.unit = cursors.getString(16);
                goods.fName = cursors.getString(17);
                goods.listGoodsType = cursors.getString(18);
                goods.unitID = cursors.getString(19);

                orderGoods.add(goods);
            }
        }

        orderMain.orderGoods = orderGoods;


        db.setTransactionSuccessful();
        db.endTransaction();

        return orderMain;

    }


    /**
     * 查询订单表数量
     */
    public String selectPurchaseOrderMainCount(String kISID, String BusinessType) {
        Cursor cursor = db.rawQuery("SELECT count(ID) FROM PurchaseOrderMain WHERE fyFRequester='" + kISID + "' AND BusinessType='" + BusinessType + "'", null);
        String count = null;
        while (cursor.moveToNext()) {
            count = cursor.getString(0);
        }
        return count;
    }


    /**
     * 更改订单上传状态
     *
     * @param orderId
     */
    public void upDatePurchaseOrderIsUpload(String orderId, String businessType) {
        KLog.e("======" + orderId + businessType);
        db.execSQL("UPDATE PurchaseOrderMain SET IsUpLoad = '1' WHERE ID = ? AND businessType = ?", new String[]{orderId, businessType});
    }



    /**
     * 获取所有订单信息
     *
     * @return
     */
    public List<PrintPurchaseOrderMain> getPurchaseOrderOrderList(String businessType, SearchCondition condition) {
        List<PrintPurchaseOrderMain> orderList = new ArrayList<>();
        String sql = "SELECT * FROM PurchaseOrderMain WHERE BusinessType = '" + businessType + "' ";
        if (condition != null) {
            if (condition.IsUpload != null)
                sql = sql + "and isUpload='" + condition.IsUpload.toString() + "' ";
            if (condition.StartDate != null && !condition.StartDate.equals(""))
                sql = sql + "and orderDate>='" + condition.StartDate + "' ";
            if (condition.EndDate != null && !condition.EndDate.equals(""))
                sql = sql + "and orderDate<='" + condition.EndDate + "' ";
            if (condition.CustomerSysId != null)
                sql = sql + "and m.customerSysId='" + condition.CustomerSysId + "' ";
            if (condition.wareHouseName != null)
                sql = sql + "and wareHouseName='" + condition.wareHouseName + "' ";

        }

        sql = sql + " order by fyFFetchDayV desc ";

        Cursor custor = db.rawQuery(sql, null);
        if (custor != null && custor.getCount() > 0) {
            while (custor.moveToNext()) {
                PrintPurchaseOrderMain orderMain = new PrintPurchaseOrderMain();
                orderMain.id = custor.getString(0);
                orderMain.fyFFetchDayV = custor.getString(1);
                orderMain.fyFPlanFetchDay1 = custor.getString(2);
                orderMain.fyFPlanFetchDay2 = custor.getString(3);
                orderMain.fyFPOStyle = custor.getString(4);
                orderMain.fyFAreaPS = custor.getString(5);
                orderMain.fyFBizType = custor.getString(6);
                orderMain.fyFSource = custor.getString(7);
                orderMain.fyFSupplier = custor.getString(8);
                orderMain.fyFRequester = custor.getString(9);
                orderMain.fyFDept = custor.getString(10);
                orderMain.fyFFManager = custor.getString(11);
                orderMain.fyFSManager = custor.getString(12);
                orderMain.fyFDCStock = custor.getString(13);
                orderMain.fyFDigest = custor.getString(14);
                orderMain.businessType = custor.getString(15);
                orderMain.allMoney = custor.getString(16);
                orderMain.isUpLoad = custor.getString(17);
                orderMain.fUpdateType = custor.getString(18);
                orderMain.fDelType = custor.getString(19);
                orderMain.printNum = custor.getString(20);
                orderList.add(orderMain);
            }
        }

        return orderList;
    }


    /**
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     * *****************************************************************************************************************************
     */




}
