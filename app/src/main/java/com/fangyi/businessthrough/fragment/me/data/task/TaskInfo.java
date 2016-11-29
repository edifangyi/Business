package com.fangyi.businessthrough.fragment.me.data.task;

/**
 * Created by FANGYI on 2016/9/15.
 */

public class TaskInfo {
    private int icon;
    private String showName;
    private String tableName;
    private String userSysID;
    private String param;
    private int progress;
    private int finish;

    public int getFinish() {
        return finish;
    }

    public void setFinish(int finish) {
        this.finish = finish;
    }

    public String getShowName() {
        return showName;
    }

    public void setShowName(String showName) {
        this.showName = showName;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getUserSysID() {
        return userSysID;
    }

    public void setUserSysID(String userSysID) {
        this.userSysID = userSysID;
    }

    public String getParam() {
        return param;
    }

    public void setParam(String param) {
        this.param = param;
    }

    @Override
    public String toString() {
        return "TaskInfo{" +
                "icon=" + icon +
                ", showName='" + showName + '\'' +
                ", tableName='" + tableName + '\'' +
                ", userSysID='" + userSysID + '\'' +
                ", param='" + param + '\'' +
                ", progress=" + progress +
                '}';
    }
}
