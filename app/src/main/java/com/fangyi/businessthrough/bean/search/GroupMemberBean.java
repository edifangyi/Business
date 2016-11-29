package com.fangyi.businessthrough.bean.search;

/**
 * Created by FANGYI on 2016/9/20.
 */

public class GroupMemberBean {
    private String sysId;  //系统编码
    private String name;   //显示的数据
    private String sortLetters;  //显示数据拼音的首字母


    public String getSysId() {
        return sysId;
    }
    public void setSysId(String sysId) {
        this.sysId = sysId;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getSortLetters() {
        return sortLetters;
    }
    public void setSortLetters(String sortLetters) {
        this.sortLetters = sortLetters;
    }
}
