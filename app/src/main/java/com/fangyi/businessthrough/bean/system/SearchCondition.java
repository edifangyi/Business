package com.fangyi.businessthrough.bean.system;

/**
 * Created by FANGYI on 2016/10/29.
 */

public class SearchCondition {
    public String IsUpload;  //是否上传

    public String StartDate; //起始日期

    public String EndDate;  //结束日期

    public String CustomerSysId;  //客户系统ID

    public String wareHouseName;//仓库


    @Override
    public String toString() {
        return "SearchCondition{" +
                "IsUpload='" + IsUpload + '\'' +
                ", StartDate='" + StartDate + '\'' +
                ", EndDate='" + EndDate + '\'' +
                ", CustomerSysId='" + CustomerSysId + '\'' +
                ", wareHouseName='" + wareHouseName + '\'' +
                '}';
    }

    public String getIsUpload() {
        return IsUpload;
    }

    public void setIsUpload(String isUpload) {
        IsUpload = isUpload;
    }

    public String getStartDate() {
        return StartDate;
    }

    public void setStartDate(String startDate) {
        StartDate = startDate;
    }

    public String getEndDate() {
        return EndDate;
    }

    public void setEndDate(String endDate) {
        EndDate = endDate;
    }

    public String getCustomerSysId() {
        return CustomerSysId;
    }

    public void setCustomerSysId(String customerSysId) {
        CustomerSysId = customerSysId;
    }

    public String getWareHouseName() {
        return wareHouseName;
    }

    public void setWareHouseName(String wareHouseName) {
        this.wareHouseName = wareHouseName;
    }
}
