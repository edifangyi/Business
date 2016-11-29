package com.fangyi.businessthrough.bean.business;

/**
 * Created by FANGYI on 2016/10/12.
 */

public class OrderMain {
    private String ID;//订单ID
    private String OrderDate;//单据业务日期
    private String DeliveryDate;//交货日期
    private String CustomerSySID;//客户id即cusotmer里的sysid
    private String WareHouseName;//仓库名称
    private String WareHouseSysID;//仓库id
    private String UserSysID;//业务员id kisid
    private String SaveTime;//单据保存日期
    private String IsUpLoad;//是否上传
    private String Message;//备注
    private String DeptID;//部门id
    private String BusinessType;//摘要
    private String FSaleType;//销售方式
    private String FAreaPS;//销售范围
    private String AllMoney;//总价格
    private String FUpdateType;//修改控制
    private String FDelType;//删除控制
    private String PrintNum;//订单类型
    private String fyFSource;//源单

    public String getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public String getFAreaPS() {
        return FAreaPS;
    }

    public void setFAreaPS(String FAreaPS) {
        this.FAreaPS = FAreaPS;
    }

    public String getFyFSource() {
        return fyFSource;
    }

    public void setFyFSource(String fyFSource) {
        this.fyFSource = fyFSource;
    }

    public String getFDelType() {
        return FDelType;
    }

    public void setFDelType(String FDelType) {
        this.FDelType = FDelType;
    }

    public String getFUpdateType() {
        return FUpdateType;
    }

    public void setFUpdateType(String FUpdateType) {
        this.FUpdateType = FUpdateType;
    }

    public String getPrintNum() {
        return PrintNum;
    }

    public void setPrintNum(String printNum) {
        PrintNum = printNum;
    }

    public String getAllMoney() {
        return AllMoney;
    }

    public void setAllMoney(String allMoney) {
        AllMoney = allMoney;
    }

    public String getFSaleType() {
        return FSaleType;
    }

    public void setFSaleType(String FSaleType) {
        this.FSaleType = FSaleType;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public String getCustomerSySID() {
        return CustomerSySID;
    }

    public void setCustomerSySID(String customerSySID) {
        CustomerSySID = customerSySID;
    }

    public String getWareHouseName() {
        return WareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        WareHouseName = wareHouseName;
    }

    public String getWareHouseSysID() {
        return WareHouseSysID;
    }

    public void setWareHouseSysID(String wareHouseSysID) {
        WareHouseSysID = wareHouseSysID;
    }

    public String getUserSysID() {
        return UserSysID;
    }

    public void setUserSysID(String userSysID) {
        UserSysID = userSysID;
    }

    public String getSaveTime() {
        return SaveTime;
    }

    public void setSaveTime(String saveTime) {
        SaveTime = saveTime;
    }

    public String getIsUpLoad() {
        return IsUpLoad;
    }

    public void setIsUpLoad(String isUpLoad) {
        IsUpLoad = isUpLoad;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        Message = message;
    }

    public String getDeptID() {
        return DeptID;
    }

    public void setDeptID(String deptID) {
        DeptID = deptID;
    }

    public String getBusinessType() {
        return BusinessType;
    }

    public void setBusinessType(String businessType) {
        BusinessType = businessType;
    }


    @Override
    public String toString() {
        return "OrderMain{" +
                "ID='" + ID + '\'' +
                ", OrderDate='" + OrderDate + '\'' +
                ", CustomerSySID='" + CustomerSySID + '\'' +
                ", WareHouseName='" + WareHouseName + '\'' +
                ", WareHouseSysID='" + WareHouseSysID + '\'' +
                ", UserSysID='" + UserSysID + '\'' +
                ", SaveTime='" + SaveTime + '\'' +
                ", IsUpLoad='" + IsUpLoad + '\'' +
                ", Message='" + Message + '\'' +
                ", DeptID='" + DeptID + '\'' +
                ", BusinessType='" + BusinessType + '\'' +
                '}';
    }
}
