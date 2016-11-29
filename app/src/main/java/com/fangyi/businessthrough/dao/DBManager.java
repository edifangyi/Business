package com.fangyi.businessthrough.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fangyi.businessthrough.bean.system.DeviceBill;
import com.fangyi.businessthrough.bean.system.DeviceUI;
import com.fangyi.businessthrough.bean.system.FEntry_22_Set;
import com.fangyi.businessthrough.bean.system.FEntry_23_Set;
import com.fangyi.businessthrough.bean.system.FEntry_24_Set;
import com.fangyi.businessthrough.bean.system.FEntry_25_Set;
import com.fangyi.businessthrough.bean.system.FEntry_26_Set;
import com.fangyi.businessthrough.bean.system.FEntry_27_Set;
import com.fangyi.businessthrough.bean.system.FEntry_28_Set;
import com.fangyi.businessthrough.bean.system.FEntry_29_Set;
import com.fangyi.businessthrough.bean.system.FEntry_30_Set;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.db.DBHelper;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeNYR;

/**
 * 查询用户系统设置类
 * Created by FANGYI on 2016/9/4.
 */

public class DBManager {
    private DBHelper helper;
    private SQLiteDatabase db;

    public DBManager(Context context) {

        helper = new DBHelper(context);

        db = helper.openDatabase();
    }


    /**
     * 添加登陆用户信息
     */
    public void setUserInfo(String UserSysID, String Password, String UserType, String time) {
        ContentValues values = new ContentValues();
        values.put("UserSysID", UserSysID);
        values.put("Password", Password);
        values.put("UserType", UserType);
        values.put("LastCustomer", "1999-01-01");
        values.put("LastGood", "0");
        values.put("LastItemPrice", "0");
        values.put("LastTime", time);
        db.insert("User", null, values);
    }

    /**
     * 在登陆用户信息User表中添加时间戳
     */
    public void setLastTime(String time) {
        ContentValues values = new ContentValues();
        values.put("LastTime", time);
        db.insert("User", null, values);
    }

    /**
     * 获取登陆用户信息
     */
    public User getUserInfo(String[] strings) {
        /**
         * User表登录的时候已经下载完成了;
         */
        String[] columns = new String[]{"id", "userSysID", "userName", "password", "userType", "isUsed", "lastCustomer", "lastItemPrice", "lastGood", "fEmpGroup", "lastTime"};
        //String 类型 数组colums;
        Cursor cursor = db.query("User", columns, "UserSysID = ?", strings, null, null, null);
        //查询Users表;"Users"为查询的表名;columns为查询的项;
        User user = null;
        if (cursor != null && cursor.getCount() > 0) {//判断cursor;
            user = new User();//实例化用户类;
            while (cursor.moveToNext()) {
                user.id = cursor.getString(0);
                user.userSysID = cursor.getString(1);
                user.userName = cursor.getString(2);
                user.password = cursor.getString(3);
                user.userType = cursor.getString(4);
                user.isUsed = cursor.getString(5);
                user.lastCustomer = cursor.getString(6);
                user.lastItemPrice = cursor.getString(7);
                user.lastGood = cursor.getString(8);
                user.fEmpGroup = cursor.getString(9);
                user.lastTime = cursor.getString(10);
            }//user赋值;
        }
//        String sql = "SELECT max(lastTime) from goods";//查询的Sql语句;
//        cursor = db.rawQuery(sql, null);//执行查询语句;
//        if (user != null && cursor.getCount() > 0) {
//            while (cursor.moveToNext()) {
//                user.goodsTimestamp = cursor.getString(0);//商品时间赋值;
//            }
//        }
        return user;
    }

    public Users getUsersInfo(String[] strings) {
        String[] columns = new String[]{"id", "userSysID", "kISID", "kISName","kISPassWord","listX", "listY", "listZ", "listTime"};
        Cursor cursor=db.query("Users", columns, "UserSysID = ?", strings, null, null, null);
        Users users=null;
        if (cursor != null && cursor.getCount() > 0) {
            users=new Users();
            while(cursor.moveToNext())
            {
                users.id=cursor.getString(0);
                users.userSysID=cursor.getString(1);
                users.kISID=cursor.getString(2);
                users.kISName=cursor.getString(3);
                users.kISPassWord=cursor.getString(4);
                users.listX=cursor.getString(5);
                users.listY=cursor.getString(6);
                users.listZ=cursor.getString(7);
                users.listTime=cursor.getString(8);
            }
        }

        return users;
    }

    /**
     * 获取登陆用户设置信息
     *
     * @param strings
     */
    public DeviceBill getDeviceBill(String[] strings) {

        String[] columns = new String[]{"id", "userSysID", "fCompanyName", "fIsWifi", "fAutoLanding", "fServerIP", "fnotice", "fImei", "fCodeID", "fTitleSize", "fEntrySize", "fHeaderSize", "fFooterSize"};
        Cursor cursor = db.query("DeviceBill", columns, "UserSysID = ?", strings, null, null, null);
        DeviceBill deviceBill = null;
        if (cursor != null && cursor.getCount() > 0) {
            deviceBill = new DeviceBill();
            while (cursor.moveToNext()) {
                deviceBill.id = cursor.getString(0);
                deviceBill.userSysID = cursor.getString(1);
                deviceBill.fCompanyName = cursor.getString(2);
                deviceBill.fIsWifi = cursor.getString(3);
                deviceBill.fAutoLanding = cursor.getString(4);
                deviceBill.fServerIP = cursor.getString(5);
                deviceBill.fnotice = cursor.getString(6);
                deviceBill.fImei = cursor.getString(7);
                deviceBill.fCodeID = cursor.getString(8);
                deviceBill.fTitleSize = cursor.getString(9);
                deviceBill.fEntrySize = cursor.getString(10);
                deviceBill.fHeaderSize = cursor.getString(11);
                deviceBill.fFooterSize = cursor.getString(12);
            }
        }

        return deviceBill;
    }


    /**
     * 获取登陆用户 UI 权限信息
     *
     * @param userid
     * @return
     */
    public DeviceUI getDeviceUI(String[] userid) {
        String[] columns = new String[]{"id", "userSysID", "userName", "fEntry22", "fEntry23", "fEntry24", "fEntry25", "fBilCgFilter", "fBillCgSum", "fEntry26", "fEntry27", "fEntry28", "fEntry29", "fBillXsFilter", "fBillXsSum", "fBillXsOrder", "fEntry30", "fBillDbFilter", "fBillKcFilter"};
        //String 类型 数组colums;
        Cursor cursor = db.query("DeviceUI", columns, "UserSysID = ?", userid, null, null, null);
        //查询Users表;"Users"为查询的表名;columns为查询的项;
        DeviceUI deviceUI = null;
        if (cursor != null && cursor.getCount() > 0) {//判断cursor;
            deviceUI = new DeviceUI();//实例化用户类;
            while (cursor.moveToNext()) {
                deviceUI.id = cursor.getString(0);
                deviceUI.userSysID = cursor.getString(1);
                deviceUI.userName = cursor.getString(2);
                deviceUI.fEntry22 = cursor.getString(3);
                deviceUI.fEntry23 = cursor.getString(4);
                deviceUI.fEntry24 = cursor.getString(5);
                deviceUI.fEntry25 = cursor.getString(6);
                deviceUI.fBilCgFilter = cursor.getString(7);
                deviceUI.fBillCgSum = cursor.getString(8);
                deviceUI.fEntry26 = cursor.getString(9);
                deviceUI.fEntry27 = cursor.getString(10);
                deviceUI.fEntry28 = cursor.getString(11);
                deviceUI.fEntry29 = cursor.getString(12);
                deviceUI.fBillXsFilter = cursor.getString(13);
                deviceUI.fBillXsSum = cursor.getString(14);
                deviceUI.fBillXsOrder = cursor.getString(15);
                deviceUI.fEntry30 = cursor.getString(16);
                deviceUI.fBillDbFilter = cursor.getString(17);
                deviceUI.fBillKcFilter = cursor.getString(18);
            }//user赋值;
        }
        return deviceUI;
    }


    /**
     * 获取登陆用户 采购申请单 权限信息
     *
     * @return
     */
    public FEntry_22_Set getFEntry_22_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_22_Set fEntry22Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry22Set = new FEntry_22_Set();
            while (cursor.moveToNext()) {
                fEntry22Set.id = cursor.getString(0);
                fEntry22Set.userSysID = cursor.getString(1);
                fEntry22Set.userX = cursor.getString(2);
                fEntry22Set.userName = cursor.getString(3);
                fEntry22Set.tableName = cursor.getString(4);

                fEntry22Set.fRequesterV = cursor.getString(11);
                fEntry22Set.fRequesterD = cursor.getString(12);

                fEntry22Set.fBizTypeV = cursor.getString(17);
                fEntry22Set.fBizTypeD = cursor.getString(18);
                fEntry22Set.fDeptV = cursor.getString(19);
                fEntry22Set.fDeptD = cursor.getString(20);

                fEntry22Set.fPlanBeginDay = cursor.getString(28);
                fEntry22Set.fPlanFetchDay = cursor.getString(29);

                fEntry22Set.fChooseDate = cursor.getString(40);
                fEntry22Set.fCheckType = cursor.getString(41);
                fEntry22Set.fOnType = cursor.getString(42);
                fEntry22Set.fUpdateType = cursor.getString(43);
                fEntry22Set.fDelType = cursor.getString(44);
                fEntry22Set.fOfFLineUpDate = cursor.getString(45);
                fEntry22Set.fOfFLineOnDate = cursor.getString(46);
                fEntry22Set.fSaFePrint = cursor.getString(47);
                fEntry22Set.fPrintCountV = cursor.getString(48);
                fEntry22Set.fPrintSame = cursor.getString(49);
                fEntry22Set.fTitle1 = cursor.getString(50);
                fEntry22Set.fTitle2 = cursor.getString(51);
                fEntry22Set.fHeader1 = cursor.getString(52);
                fEntry22Set.fHeader2 = cursor.getString(53);
                fEntry22Set.fHeader3 = cursor.getString(54);
                fEntry22Set.fHeader4 = cursor.getString(55);
                fEntry22Set.fHeader5 = cursor.getString(56);
                fEntry22Set.fHeader6 = cursor.getString(57);
                fEntry22Set.fFooter1 = cursor.getString(58);
                fEntry22Set.fFooter2 = cursor.getString(59);
                fEntry22Set.fFooter3 = cursor.getString(60);
                fEntry22Set.fFooter4 = cursor.getString(61);
                fEntry22Set.fFooter5 = cursor.getString(62);
            }//user赋值;
        }

        return fEntry22Set;
    }

    /**
     * 获取登陆用户 采购入库单 权限信息
     *
     * @return
     */
    public FEntry_23_Set getFEntry_23_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_23_Set fEntry23Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry23Set = new FEntry_23_Set();
            while (cursor.moveToNext()) {
                fEntry23Set.id = cursor.getString(0);
                fEntry23Set.userSysID = cursor.getString(1);
                fEntry23Set.userX = cursor.getString(2);
                fEntry23Set.userName = cursor.getString(3);
                fEntry23Set.tableName = cursor.getString(4);

                fEntry23Set.fPOStyleV = cursor.getString(7);
                fEntry23Set.fPOStyleD = cursor.getString(8);

                fEntry23Set.fDeptV = cursor.getString(19);
                fEntry23Set.fDeptD = cursor.getString(20);
                fEntry23Set.fFManagerV = cursor.getString(21);
                fEntry23Set.fFManagerD = cursor.getString(22);
                fEntry23Set.fSManagerV = cursor.getString(23);
                fEntry23Set.fSManagerD = cursor.getString(24);
                fEntry23Set.fDCStockV = cursor.getString(25);
                fEntry23Set.fDCStockD = cursor.getString(26);
                fEntry23Set.fStockPosition = cursor.getString(27);

                fEntry23Set.fBillInterV = cursor.getString(30);
                fEntry23Set.fBillInterM = cursor.getString(31);
                fEntry23Set.fBillInterC = cursor.getString(32);
                fEntry23Set.fBatchNo = cursor.getString(33);

                fEntry23Set.fAmountV = cursor.getString(38);
                fEntry23Set.fChooseAmount = cursor.getString(39);
                fEntry23Set.fChooseDate = cursor.getString(40);
                fEntry23Set.fCheckType = cursor.getString(41);
                fEntry23Set.fOnType = cursor.getString(42);
                fEntry23Set.fUpdateType = cursor.getString(43);
                fEntry23Set.fDelType = cursor.getString(44);
                fEntry23Set.fOfFLineUpDate = cursor.getString(45);
                fEntry23Set.fOfFLineOnDate = cursor.getString(46);
                fEntry23Set.fSaFePrint = cursor.getString(47);
                fEntry23Set.fPrintCountV = cursor.getString(48);
                fEntry23Set.fPrintSame = cursor.getString(49);
                fEntry23Set.fTitle1 = cursor.getString(50);
                fEntry23Set.fTitle2 = cursor.getString(51);
                fEntry23Set.fHeader1 = cursor.getString(52);
                fEntry23Set.fHeader2 = cursor.getString(53);
                fEntry23Set.fHeader3 = cursor.getString(54);
                fEntry23Set.fHeader4 = cursor.getString(55);
                fEntry23Set.fHeader5 = cursor.getString(56);
                fEntry23Set.fHeader6 = cursor.getString(57);
                fEntry23Set.fFooter1 = cursor.getString(58);
                fEntry23Set.fFooter2 = cursor.getString(59);
                fEntry23Set.fFooter3 = cursor.getString(60);
                fEntry23Set.fFooter4 = cursor.getString(61);
                fEntry23Set.fFooter5 = cursor.getString(62);
            }//user赋值;
        }

        return fEntry23Set;
    }

    /**
     * 获取登陆用户 采购退货通知单 权限信息
     *
     * @return
     */
    public FEntry_24_Set getFEntry_24_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_24_Set fEntry24Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry24Set = new FEntry_24_Set();
            while (cursor.moveToNext()) {
                fEntry24Set.id = cursor.getString(0);
                fEntry24Set.userSysID = cursor.getString(1);
                fEntry24Set.userX = cursor.getString(2);
                fEntry24Set.userName = cursor.getString(3);
                fEntry24Set.tableName = cursor.getString(4);
                fEntry24Set.fPOStyleV = cursor.getString(5);
                fEntry24Set.fPOStyleD = cursor.getString(6);
                fEntry24Set.fAreaPSV = cursor.getString(7);
                fEntry24Set.fAreaPSD = cursor.getString(8);

                fEntry24Set.fBizTypeV = cursor.getString(17);
                fEntry24Set.fBizTypeD = cursor.getString(18);
                fEntry24Set.fDeptV = cursor.getString(19);
                fEntry24Set.fDeptD = cursor.getString(20);
                fEntry24Set.fFManagerV = cursor.getString(21);
                fEntry24Set.fFManagerD = cursor.getString(22);
                fEntry24Set.fSManagerV = cursor.getString(23);
                fEntry24Set.fSManagerD = cursor.getString(24);
                fEntry24Set.fDCStockV = cursor.getString(25);
                fEntry24Set.fDCStockD = cursor.getString(26);
                fEntry24Set.fStockPosition = cursor.getString(27);

                fEntry24Set.fBatchNo = cursor.getString(33);

                fEntry24Set.fAmountV = cursor.getString(38);
                fEntry24Set.fChooseAmount = cursor.getString(39);
                fEntry24Set.fChooseDate = cursor.getString(40);
                fEntry24Set.fCheckType = cursor.getString(41);
                fEntry24Set.fOnType = cursor.getString(42);
                fEntry24Set.fUpdateType = cursor.getString(43);
                fEntry24Set.fDelType = cursor.getString(44);
                fEntry24Set.fOfFLineUpDate = cursor.getString(45);
                fEntry24Set.fOfFLineOnDate = cursor.getString(46);
                fEntry24Set.fSaFePrint = cursor.getString(47);
                fEntry24Set.fPrintCountV = cursor.getString(48);
                fEntry24Set.fPrintSame = cursor.getString(49);
                fEntry24Set.fTitle1 = cursor.getString(50);
                fEntry24Set.fTitle2 = cursor.getString(51);
                fEntry24Set.fHeader1 = cursor.getString(52);
                fEntry24Set.fHeader2 = cursor.getString(53);
                fEntry24Set.fHeader3 = cursor.getString(54);
                fEntry24Set.fHeader4 = cursor.getString(55);
                fEntry24Set.fHeader5 = cursor.getString(56);
                fEntry24Set.fHeader6 = cursor.getString(57);
                fEntry24Set.fFooter1 = cursor.getString(58);
                fEntry24Set.fFooter2 = cursor.getString(59);
                fEntry24Set.fFooter3 = cursor.getString(60);
                fEntry24Set.fFooter4 = cursor.getString(61);
                fEntry24Set.fFooter5 = cursor.getString(62);
            }//user赋值;
        }

        return fEntry24Set;
    }
    /**
     * 获取登陆用户 采购退货 权限信息
     *
     * @return
     */
    public FEntry_25_Set getFEntry_25_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_25_Set fEntry25Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry25Set = new FEntry_25_Set();
            while (cursor.moveToNext()) {
                fEntry25Set.id = cursor.getString(0);
                fEntry25Set.userSysID = cursor.getString(1);
                fEntry25Set.userX = cursor.getString(2);
                fEntry25Set.userName = cursor.getString(3);
                fEntry25Set.tableName = cursor.getString(4);
                fEntry25Set.fPOStyleV = cursor.getString(5);
                fEntry25Set.fPOStyleD = cursor.getString(6);

                fEntry25Set.fDeptV = cursor.getString(19);
                fEntry25Set.fDeptD = cursor.getString(20);
                fEntry25Set.fFManagerV = cursor.getString(21);
                fEntry25Set.fFManagerD = cursor.getString(22);
                fEntry25Set.fSManagerV = cursor.getString(23);
                fEntry25Set.fSManagerD = cursor.getString(24);
                fEntry25Set.fDCStockV = cursor.getString(25);
                fEntry25Set.fDCStockD = cursor.getString(26);
                fEntry25Set.fStockPosition = cursor.getString(27);

                fEntry25Set.fBillInterV = cursor.getString(30);
                fEntry25Set.fBillInterM = cursor.getString(31);
                fEntry25Set.fBillInterC = cursor.getString(32);
                fEntry25Set.fBatchNo = cursor.getString(33);

                fEntry25Set.fAmountV = cursor.getString(38);
                fEntry25Set.fChooseAmount = cursor.getString(39);
                fEntry25Set.fChooseDate = cursor.getString(40);
                fEntry25Set.fCheckType = cursor.getString(41);
                fEntry25Set.fOnType = cursor.getString(42);
                fEntry25Set.fUpdateType = cursor.getString(43);
                fEntry25Set.fDelType = cursor.getString(44);
                fEntry25Set.fOfFLineUpDate = cursor.getString(45);
                fEntry25Set.fOfFLineOnDate = cursor.getString(46);
                fEntry25Set.fSaFePrint = cursor.getString(47);
                fEntry25Set.fPrintCountV = cursor.getString(48);
                fEntry25Set.fPrintSame = cursor.getString(49);
                fEntry25Set.fTitle1 = cursor.getString(50);
                fEntry25Set.fTitle2 = cursor.getString(51);
                fEntry25Set.fHeader1 = cursor.getString(52);
                fEntry25Set.fHeader2 = cursor.getString(53);
                fEntry25Set.fHeader3 = cursor.getString(54);
                fEntry25Set.fHeader4 = cursor.getString(55);
                fEntry25Set.fHeader5 = cursor.getString(56);
                fEntry25Set.fHeader6 = cursor.getString(57);
                fEntry25Set.fFooter1 = cursor.getString(58);
                fEntry25Set.fFooter2 = cursor.getString(59);
                fEntry25Set.fFooter3 = cursor.getString(60);
                fEntry25Set.fFooter4 = cursor.getString(61);
                fEntry25Set.fFooter5 = cursor.getString(62);
            }//user赋值;
        }

        return fEntry25Set;
    }



    /**
     * 获取登陆用户 销售订货单 权限信息
     *
     * @return
     */
    public FEntry_26_Set getFEntry_26_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_26_Set fEntry26Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry26Set = new FEntry_26_Set();
            while (cursor.moveToNext()) {
                fEntry26Set.id = cursor.getString(0);
                fEntry26Set.userSysID = cursor.getString(1);
                fEntry26Set.userX = cursor.getString(2);
                fEntry26Set.userName = cursor.getString(3);
                fEntry26Set.tableName = cursor.getString(4);

                fEntry26Set.fAreaPSV = cursor.getString(9);
                fEntry26Set.fAreaPSD = cursor.getString(10);

                fEntry26Set.fCustAddV = cursor.getString(13);
                fEntry26Set.fCustPhoV = cursor.getString(14);
                fEntry26Set.fEmpV = cursor.getString(15);
                fEntry26Set.fEmpD = cursor.getString(16);
                fEntry26Set.FSaleStyleV = cursor.getString(17);
                fEntry26Set.FSaleStyleD = cursor.getString(18);
                fEntry26Set.fDeptV = cursor.getString(19);
                fEntry26Set.fDeptD = cursor.getString(20);

                fEntry26Set.fDCStockV = cursor.getString(25);
                fEntry26Set.fDCStockD = cursor.getString(26);
                fEntry26Set.fStockPosition = cursor.getString(27);
                fEntry26Set.FFetchDayV = cursor.getString(28);
                fEntry26Set.FPlanFetchDay = cursor.getString(29);

                fEntry26Set.fWholePro = cursor.getString(35);
                fEntry26Set.fPlanPro = cursor.getString(36);
                fEntry26Set.fProAmountV = cursor.getString(37);

                fEntry26Set.fChooseAmount = cursor.getString(39);
                fEntry26Set.fChooseDate = cursor.getString(40);
                fEntry26Set.fCheckType = cursor.getString(41);
                fEntry26Set.fOnType = cursor.getString(42);
                fEntry26Set.fUpdateType = cursor.getString(43);
                fEntry26Set.fDelType = cursor.getString(44);
                fEntry26Set.fOfFLineUpDate = cursor.getString(45);
                fEntry26Set.fOfFLineOnDate = cursor.getString(46);
                fEntry26Set.fSaFePrint = cursor.getString(47);
                fEntry26Set.fPrintCountV = cursor.getString(48);
                fEntry26Set.fPrintSame = cursor.getString(49);
                fEntry26Set.fTitle1 = cursor.getString(50);
                fEntry26Set.fTitle2 = cursor.getString(51);
                fEntry26Set.fHeader1 = cursor.getString(52);
                fEntry26Set.fHeader2 = cursor.getString(53);
                fEntry26Set.fHeader3 = cursor.getString(54);
                fEntry26Set.fHeader4 = cursor.getString(55);
                fEntry26Set.fHeader5 = cursor.getString(56);
                fEntry26Set.fHeader6 = cursor.getString(57);
                fEntry26Set.fFooter1 = cursor.getString(58);
                fEntry26Set.fFooter2 = cursor.getString(59);
                fEntry26Set.fFooter3 = cursor.getString(60);
                fEntry26Set.fFooter4 = cursor.getString(61);
                fEntry26Set.fFooter5 = cursor.getString(62);
            }//user赋值;
        }

        return fEntry26Set;
    }

    /**
     * 获取登陆用户 销售出库单 权限信息
     *
     * @return
     */
    public FEntry_27_Set getFEntry_27_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_27_Set fEntry27Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry27Set = new FEntry_27_Set();
            while (cursor.moveToNext()) {
                fEntry27Set.id = cursor.getString(0);
                fEntry27Set.userSysID = cursor.getString(1);
                fEntry27Set.userX = cursor.getString(2);
                fEntry27Set.userName = cursor.getString(3);
                fEntry27Set.tableName = cursor.getString(4);

                fEntry27Set.fSaleStyleV = cursor.getString(5);
                fEntry27Set.fSaleStyleD = cursor.getString(6);

                fEntry27Set.fCustAddV = cursor.getString(13);
                fEntry27Set.fCustPhoV = cursor.getString(14);
                fEntry27Set.fEmpV = cursor.getString(15);
                fEntry27Set.fEmpD = cursor.getString(16);
                fEntry27Set.fBizTypeV = cursor.getString(17);
                fEntry27Set.fBizTypeD = cursor.getString(18);
                fEntry27Set.fDeptV = cursor.getString(19);
                fEntry27Set.fDeptD = cursor.getString(20);

                fEntry27Set.fDCStockV = cursor.getString(25);
                fEntry27Set.fDCStockD = cursor.getString(26);
                fEntry27Set.fStockPosition = cursor.getString(27);

                fEntry27Set.fBillInterV = cursor.getString(30);
                fEntry27Set.fBillInterM = cursor.getString(31);
                fEntry27Set.fBillInterC = cursor.getString(32);
                fEntry27Set.fBatchNo = cursor.getString(33);
                fEntry27Set.fBillAuto = cursor.getString(34);
                fEntry27Set.fWholePro = cursor.getString(35);
                fEntry27Set.fPlanPro = cursor.getString(36);
                fEntry27Set.fProAmountV = cursor.getString(37);

                fEntry27Set.fChooseAmount = cursor.getString(39);
                fEntry27Set.fChooseDate = cursor.getString(40);
                fEntry27Set.fCheckType = cursor.getString(41);
                fEntry27Set.fOnType = cursor.getString(42);
                fEntry27Set.fUpdateType = cursor.getString(43);
                fEntry27Set.fDelType = cursor.getString(44);
                fEntry27Set.fOfFLineUpDate = cursor.getString(45);
                fEntry27Set.fOfFLineOnDate = cursor.getString(46);
                fEntry27Set.fSaFePrint = cursor.getString(47);
                fEntry27Set.fPrintCountV = cursor.getString(48);
                fEntry27Set.fPrintSame = cursor.getString(49);
                fEntry27Set.fTitle1 = cursor.getString(50);
                fEntry27Set.fTitle2 = cursor.getString(51);
                fEntry27Set.fHeader1 = cursor.getString(52);
                fEntry27Set.fHeader2 = cursor.getString(53);
                fEntry27Set.fHeader3 = cursor.getString(54);
                fEntry27Set.fHeader4 = cursor.getString(55);
                fEntry27Set.fHeader5 = cursor.getString(56);
                fEntry27Set.fHeader6 = cursor.getString(57);
                fEntry27Set.fFooter1 = cursor.getString(58);
                fEntry27Set.fFooter2 = cursor.getString(59);
                fEntry27Set.fFooter3 = cursor.getString(60);
                fEntry27Set.fFooter4 = cursor.getString(61);
                fEntry27Set.fFooter5 = cursor.getString(62);
            }
        }

        return fEntry27Set;
    }

    /**
     * 获取登陆用户 销售退货通知单 权限信息
     *
     * @return
     */
    public FEntry_28_Set getFEntry_28_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_28_Set fEntry28Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry28Set = new FEntry_28_Set();
            while (cursor.moveToNext()) {

                fEntry28Set.id = cursor.getString(0);
                fEntry28Set.userSysID = cursor.getString(1);
                fEntry28Set.userX = cursor.getString(2);
                fEntry28Set.userName = cursor.getString(3);
                fEntry28Set.tableName = cursor.getString(4);
                fEntry28Set.fSaleStyleV = cursor.getString(5);
                fEntry28Set.fSaleStyleD = cursor.getString(6);

                fEntry28Set.fAreaPSV = cursor.getString(9);
                fEntry28Set.fAreaPSD = cursor.getString(10);

                fEntry28Set.fCustAddV = cursor.getString(13);
                fEntry28Set.fCustPhoV = cursor.getString(14);
                fEntry28Set.fEmpV = cursor.getString(15);
                fEntry28Set.fEmpD = cursor.getString(16);

                fEntry28Set.fDeptV = cursor.getString(19);
                fEntry28Set.fDeptD = cursor.getString(20);

                fEntry28Set.fDCStockV = cursor.getString(25);
                fEntry28Set.fDCStockD = cursor.getString(26);
                fEntry28Set.fStockPosition = cursor.getString(27);

                fEntry28Set.fBatchNo = cursor.getString(33);

                fEntry28Set.fWholePro = cursor.getString(35);
                fEntry28Set.fPlanPro = cursor.getString(36);
                fEntry28Set.fProAmountV = cursor.getString(37);

                fEntry28Set.fChooseAmount = cursor.getString(39);
                fEntry28Set.fChooseDate = cursor.getString(40);
                fEntry28Set.fCheckType = cursor.getString(41);
                fEntry28Set.fOnType = cursor.getString(42);
                fEntry28Set.fUpdateType = cursor.getString(43);
                fEntry28Set.fDelType = cursor.getString(44);
                fEntry28Set.fOfFLineUpDate = cursor.getString(45);
                fEntry28Set.fOfFLineOnDate = cursor.getString(46);
                fEntry28Set.fSaFePrint = cursor.getString(47);
                fEntry28Set.fPrintCountV = cursor.getString(48);
                fEntry28Set.fPrintSame = cursor.getString(49);
                fEntry28Set.fTitle1 = cursor.getString(50);
                fEntry28Set.fTitle2 = cursor.getString(51);
                fEntry28Set.fHeader1 = cursor.getString(52);
                fEntry28Set.fHeader2 = cursor.getString(53);
                fEntry28Set.fHeader3 = cursor.getString(54);
                fEntry28Set.fHeader4 = cursor.getString(55);
                fEntry28Set.fHeader5 = cursor.getString(56);
                fEntry28Set.fHeader6 = cursor.getString(57);
                fEntry28Set.fFooter1 = cursor.getString(58);
                fEntry28Set.fFooter2 = cursor.getString(59);
                fEntry28Set.fFooter3 = cursor.getString(60);
                fEntry28Set.fFooter4 = cursor.getString(61);
                fEntry28Set.fFooter5 = cursor.getString(62);
            }
        }

        return fEntry28Set;
    }


    /**
     * 获取登陆用户 销售退货单 权限信息
     *
     * @return
     */
    public FEntry_29_Set getFEntry_29_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_29_Set fEntry29Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry29Set = new FEntry_29_Set();
            while (cursor.moveToNext()) {
                fEntry29Set.id = cursor.getString(0);
                fEntry29Set.userSysID = cursor.getString(1);
                fEntry29Set.userX = cursor.getString(2);
                fEntry29Set.userName = cursor.getString(3);
                fEntry29Set.tableName = cursor.getString(4);
                fEntry29Set.fSaleStyleV = cursor.getString(5);
                fEntry29Set.fSaleStyleD = cursor.getString(6);

                fEntry29Set.fCustAddV = cursor.getString(13);
                fEntry29Set.fCustPhoV = cursor.getString(14);
                fEntry29Set.fEmpV = cursor.getString(15);
                fEntry29Set.fEmpD = cursor.getString(16);
                fEntry29Set.fBizTypeV = cursor.getString(17);
                fEntry29Set.fBizTypeD = cursor.getString(18);
                fEntry29Set.fDeptV = cursor.getString(19);
                fEntry29Set.fDeptD = cursor.getString(20);

                fEntry29Set.fDCStockV = cursor.getString(25);
                fEntry29Set.fDCStockD = cursor.getString(26);
                fEntry29Set.fStockPosition = cursor.getString(27);

                fEntry29Set.fBillInterV = cursor.getString(30);
                fEntry29Set.fBillInterM = cursor.getString(31);
                fEntry29Set.fBillInterC = cursor.getString(32);
                fEntry29Set.fBatchNo = cursor.getString(33);

                fEntry29Set.fWholePro = cursor.getString(35);
                fEntry29Set.fPlanPro = cursor.getString(36);
                fEntry29Set.fProAmountV = cursor.getString(37);

                fEntry29Set.fChooseAmount = cursor.getString(39);
                fEntry29Set.fChooseDate = cursor.getString(40);
                fEntry29Set.fCheckType = cursor.getString(41);
                fEntry29Set.fOnType = cursor.getString(42);
                fEntry29Set.fUpdateType = cursor.getString(43);
                fEntry29Set.fDelType = cursor.getString(44);
                fEntry29Set.fOfFLineUpDate = cursor.getString(45);
                fEntry29Set.fOfFLineOnDate = cursor.getString(46);
                fEntry29Set.fSaFePrint = cursor.getString(47);
                fEntry29Set.fPrintCountV = cursor.getString(47);
                fEntry29Set.fPrintSame = cursor.getString(48);
                fEntry29Set.fTitle1 = cursor.getString(49);
                fEntry29Set.fTitle2 = cursor.getString(50);
                fEntry29Set.fHeader1 = cursor.getString(51);
                fEntry29Set.fHeader2 = cursor.getString(52);
                fEntry29Set.fHeader3 = cursor.getString(53);
                fEntry29Set.fHeader4 = cursor.getString(54);
                fEntry29Set.fHeader5 = cursor.getString(55);
                fEntry29Set.fHeader6 = cursor.getString(56);
                fEntry29Set.fFooter1 = cursor.getString(57);
                fEntry29Set.fFooter2 = cursor.getString(58);
                fEntry29Set.fFooter3 = cursor.getString(59);
                fEntry29Set.fFooter4 = cursor.getString(60);
                fEntry29Set.fFooter5 = cursor.getString(61);
            }
        }

        return fEntry29Set;
    }

    /**
     * 获取登陆用户 调拨单 权限信息
     *
     * @return
     */
    public FEntry_30_Set getFEntry_30_Set(String[] userid) {
        String[] columns = new String[]{"ID", "UserSysID", "UserX", "UserName", "TableName", "FAV", "FAD", "FBV", "FBD", "FCV", "FCD", "FRequesterV", "FRequesterD", "FCustAddV", "FCustPhoV", "FEmpV", "FEmpD", "FDV", "FDD", "FDeptV", "FDeptD", "FFManagerV", "FFManagerD", "FSManagerV", "FSManagerD", "FDCStockV", "FDCStockD", "FStockPosition", "FEADay", "FEBDay", "FBillInterV", "FBillInterM", "FBillInterC", "FBatchNo", "FBillAuto", "FWholePro", "FPlanPro", "FProAmountV", "FAmountV", "FChooseAmount", "FChooseDate", "FCheckType", "FOnType", "FUpdateType", "FDelType", "FOffLineUpDate", "FOffLineOnDate", "FSafePrint", "FPrintCountV", "FPrintSame", "FTitle1", "FTitle2", "FHeader1", "FHeader2", "FHeader3", "FHeader4", "FHeader5", "FHeader6", "FFooter1", "FFooter2", "FFooter3", "FFooter4", "FFooter5", "FSCStockV", "FSCStockD", "FSStockPosition"};
        Cursor cursor = db.query("DeviceBillSet", columns, "UserSysID = ? and TableName = ?", userid, null, null, null);
        FEntry_30_Set fEntry30Set = null;
        if (cursor != null && cursor.getCount() > 0) {
            fEntry30Set = new FEntry_30_Set();
            while (cursor.moveToNext()) {

                fEntry30Set.id = cursor.getString(0);
                fEntry30Set.userSysID = cursor.getString(1);
                fEntry30Set.userX = cursor.getString(2);
                fEntry30Set.userName = cursor.getString(3);
                fEntry30Set.tableName = cursor.getString(4);

                fEntry30Set.fEmpV = cursor.getString(15);
                fEntry30Set.fEmpD = cursor.getString(16);

                fEntry30Set.fDeptV = cursor.getString(19);
                fEntry30Set.fDeptD = cursor.getString(20);
                fEntry30Set.fFManagerV = cursor.getString(21);
                fEntry30Set.fFManagerD = cursor.getString(22);
                fEntry30Set.fSManagerV = cursor.getString(23);
                fEntry30Set.fSManagerD = cursor.getString(24);
                fEntry30Set.fDCStockV = cursor.getString(25);
                fEntry30Set.fDCStockD = cursor.getString(26);
                fEntry30Set.fDStockPosition = cursor.getString(27);

                fEntry30Set.fBatchNo = cursor.getString(33);

                fEntry30Set.fAmountV = cursor.getString(38);
                fEntry30Set.fChooseAmount = cursor.getString(39);
                fEntry30Set.fChooseDate = cursor.getString(40);
                fEntry30Set.fCheckType = cursor.getString(41);
                fEntry30Set.fOnType = cursor.getString(42);
                fEntry30Set.fUpdateType = cursor.getString(43);
                fEntry30Set.fDelType = cursor.getString(44);
                fEntry30Set.fOfFLineUpDate = cursor.getString(45);
                fEntry30Set.fOfFLineOnDate = cursor.getString(46);
                fEntry30Set.fSaFePrint = cursor.getString(47);
                fEntry30Set.fPrintCountV = cursor.getString(48);
                fEntry30Set.fPrintSame = cursor.getString(49);
                fEntry30Set.fTitle1 = cursor.getString(50);
                fEntry30Set.fTitle2 = cursor.getString(51);
                fEntry30Set.fHeader1 = cursor.getString(52);
                fEntry30Set.fHeader2 = cursor.getString(53);
                fEntry30Set.fHeader3 = cursor.getString(54);
                fEntry30Set.fHeader4 = cursor.getString(55);
                fEntry30Set.fHeader5 = cursor.getString(56);
                fEntry30Set.fHeader6 = cursor.getString(57);
                fEntry30Set.fFooter1 = cursor.getString(58);
                fEntry30Set.fFooter2 = cursor.getString(59);
                fEntry30Set.fFooter3 = cursor.getString(60);
                fEntry30Set.fFooter4 = cursor.getString(61);
                fEntry30Set.fFooter5 = cursor.getString(62);
            }
        }

        return fEntry30Set;
    }

    /**
     * 清空下载数据
     *
     * @param tableName
     */
    public void deleteDownloadData(String tableName) {
        db.beginTransaction();//数据库操作开启事务;
        if ("Customer".equals(tableName)) {
            db.delete("Customer", null, null);
        } else if ("Goods".equals(tableName)) {
            db.delete("Goods", null, null);
        } else if ("ItemPrice".equals(tableName)) {
            db.delete("ItemPrice", null, null);
        } else if ("Phone".equals(tableName)) {
            db.delete("Phone", null, null);
        } else if ("UserCustomer".equals(tableName)) {
            db.delete("UserCustomer", null, null);
        } else if ("Parameter".equals(tableName)) {
            db.delete("Parameter", null, null);
        } else if ("WareHouse".equals(tableName)) {
            db.delete("WareHouse", null, null);
        } else if ("FreeBill".equals(tableName)) {
            db.delete("FreeBill", null, null);
        } else if ("FreeBillDetail".equals(tableName)) {
            db.delete("FreeBillDetail", null, null);
        } else if ("UnitGroup".equals(tableName)) {
            db.delete("UnitGroup", null, null);
        }else if ("Seobill".equals(tableName)) {
            db.delete("SEOrderMain", null, null);
        }else if ("Seobillentry".equals(tableName)) {
            db.delete("SEOrderDetail", null, null);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 	清空下载数据
     */
    public void deleteDownloadData(Map<String,String> whereMap)
    {
        db.beginTransaction();//数据库操作开启事务;
        db.delete("Customer", null, null);
        db.delete("Goods", null, null);
        db.delete("ItemPrice", null, null);

        db.delete("Phone", null, null);
        db.delete("UserCustomer", null, null);
        db.delete("Parameter", null, null);
        db.delete("WareHouse", null, null);
        db.delete("FreeBill", null, null);
        db.delete("FreeBillDetail", null, null);
        db.delete("UnitGroup", null, null);
        db.delete("SEOrderMain", null, null);
        db.delete("SEOrderDetail", null, null);
//    	db.delete("CustomerTask", null, null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    /**
     * 将下载的数据库插入数据库
     *
     * @param insertSqlList 将执行的插入语句
     * @return 插入行数
     */
    public int insertDownloadData(List<String> insertSqlList) {
        db.beginTransaction();//启动事务
        for (String sql : insertSqlList) {
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
//        updateUserPassword();
//        insertDefaultDept();
        return insertSqlList.size();
    }

    /**
     * 将下载的数据库插入数据库
     * @param insertSqlList 将执行的插入语句
     * @return 插入行数
     */
    public int insertDownloadData(List<String> insertSqlList,Map<String,String> whereMap)
    {
        deleteDownloadData(whereMap);//调用清空下载数据的方法;
        db.beginTransaction();//启动事务；
        for(String sql:insertSqlList)
        {
            db.execSQL(sql);
        }
        db.setTransactionSuccessful();
        db.endTransaction();
        return insertSqlList.size();
    }


    /**
     * 登录验证
     *
     * @param userSysID
     * @return
     */
    public String queryLogin(String userSysID) {
        Cursor cursor = db.rawQuery("SELECT Password FROM User WHERE UserSysID='" + userSysID + "'", null);
        String password = null;
        while (cursor.moveToNext()) {
            password = cursor.getString(0);
        }
        return password;
    }

    /**
     * 修改登陆密码
     *
     * @param password
     * @param userSysID
     */
    public void updataPassword(String userSysID, String password) {
        db.execSQL("UPDATE Users SET Password='" + password + "'" + " WHERE UserSysID='" + userSysID + "'");
    }

    /**
     * 根据用户ID，删除UI表
     */
    public void deleteDevice(String tableName, String userSysID) {
        db.execSQL("delete from " + tableName + " WHERE UserSysID='" + userSysID + "'");
    }


    public void selectAPI(String tableName, String set[], String requirements, String where[]) {
        Cursor cursor = db.query(tableName, set, "name" + " = ?", where, null, null, null, null);
        while (cursor.moveToNext()) {
            String name = cursor.getString(0);
            String money = cursor.getString(1);
            System.out.println(name + ";" + money);
        }
    }

    /**
     * 存通知到数据库
     *
     * @param date
     * @param string
     */
    public void insertPush(Date date, String string) {
        String str[] = new String[]{getTimeNYR(date), string};
        db.execSQL("INSERT INTO PushNotification(date,content) VALUES (?,?);", str);
    }

    /**
     * 删除通知
     *
     * @param string
     */
    public void deletePush(String string) {
        String str[] = new String[]{string};
        db.execSQL("DELETE FROM PushNotification where content =?", str);
    }


    /**
     * 通过商品ID 查找 单位率
     * @return
     */
    public List<Map<String, String>> selectPush() {

        List<Map<String, String>> list = new ArrayList<>();

        String sql = "SELECT date,content FROM PushNotification WHERE 1 order by id desc";
        Cursor custor = db.rawQuery(sql, null);

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
     * 关闭数据库
     */
    public void closeDB() {
        db.close(); // 释放数据库资源
    }

    /**
     * 清空下载数据
     */
    public void emptyData(String str) {
        db.beginTransaction();//数据库操作开启事务;
        db.delete(str, null, null);
        db.setTransactionSuccessful();
        db.endTransaction();
    }

    public void deleteEmptyOrderData(String tableName) {
        db.beginTransaction();//数据库操作开启事务;
        String sql = "SELECT ID FROM " + tableName + " WHERE IsUpLoad = '0'";
        List<String> list = new ArrayList<>();
        Cursor custor = db.rawQuery(sql, null);
        while (custor.moveToNext()) {
            list.add(custor.getString(0));
        }
        String tableNameVice = null;
        if ("OrderMain".equals(tableName)) {
            tableNameVice = "OrderDetail";
        } else if ("PurchaseOrderMain".equals(tableName)) {
            tableNameVice = "PurchaseOrderDetail";
        }

        db.execSQL("DELETE FROM " + tableName + " where IsUpLoad = '0'");
        for (String s : list) {
            db.execSQL("DELETE FROM " + tableNameVice + " WHERE id = '" + s + "'");
        }


        db.setTransactionSuccessful();
        db.endTransaction();
    }
}
