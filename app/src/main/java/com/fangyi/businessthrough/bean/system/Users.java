package com.fangyi.businessthrough.bean.system;

/**
 * Created by FANGYI on 2016/9/21.
 */

public class Users {
    public String id;
    public String userSysID;//登陆用户ID
    public String kISID;//金蝶ID
    public String kISName;//金蝶Name
    public String kISPassWord;
    public String listX;
    public String listY;
    public String listZ;
    public String listTime;

    @Override
    public String toString() {
        return "Users{" +
                "id='" + id + '\'' +
                ", userSysID='" + userSysID + '\'' +
                ", kISID='" + kISID + '\'' +
                ", kISName='" + kISName + '\'' +
                ", kISPassWord='" + kISPassWord + '\'' +
                ", listX='" + listX + '\'' +
                ", listY='" + listY + '\'' +
                ", listZ='" + listZ + '\'' +
                ", listTime='" + listTime + '\'' +
                '}';
    }
}
