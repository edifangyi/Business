package com.fangyi.businessthrough.bean.system;

/**
 * Created by FANGYI on 2016/8/31.
 */

public class User {

    public String id;//用户编码
    public String userSysID;//用户内码
    public String userName;//用户名称
    public String password;//用户密码
    public String userType;//用户类型
    public String isUsed;//是否可用
    public String lastCustomer;//下载标号 1990-01-01
    public String lastItemPrice;//下载标号 0
    public String lastGood;//下载标号 0
    public String fEmpGroup;//用户组
    public String lastTime; //上次下载时间戳

    @Override
    public String toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", userSysID='" + userSysID + '\'' +
                ", userName='" + userName + '\'' +
                ", Password='" + password + '\'' +
                ", userType='" + userType + '\'' +
                ", isUsed='" + isUsed + '\'' +
                ", lastCustomer='" + lastCustomer + '\'' +
                ", lastItemPrice='" + lastItemPrice + '\'' +
                ", lastGood='" + lastGood + '\'' +
                ", fEmpGroup='" + fEmpGroup + '\'' +
                ", lastTime='" + lastTime + '\'' +
                '}';
    }
}
