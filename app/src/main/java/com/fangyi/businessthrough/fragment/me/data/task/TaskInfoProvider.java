package com.fangyi.businessthrough.fragment.me.data.task;


import com.fangyi.businessthrough.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by FANGYI on 2016/9/15.
 */

public class TaskInfoProvider {

    public static List<TaskInfo> getAllTaskInfos() {

        List<TaskInfo> taskInfos = new ArrayList<>();
        String[] showName = {"客户表", "商品表", "价格表",
                "联系人", "业务表", "参数表",
                "仓库表", "赠品方案", "赠品详细"};
        String[] tableName = {"Customer", "Goods", "ItemPrice",
                "Phone", "UserCustomer", "Parameter",
                "WareHouse", "Freebill", "Freebilldetail"};
        String userSysID = "02";
        String[] param = {"1999-01-01", "0", "0",
                "", "", "",
                "", "", ""};
        int ids[] = {R.mipmap.customer, R.mipmap.goods, R.mipmap.iterprice,
                R.mipmap.phone, R.mipmap.usercustomer, R.mipmap.parameter,
                R.mipmap.warehouse, R.mipmap.freebill, R.mipmap.freebilldetail};

        for (int i = 0; i < tableName.length; i++) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setShowName(showName[i]);
            taskInfo.setTableName(tableName[i]);
            taskInfo.setUserSysID(userSysID);
            taskInfo.setParam(param[i]);
            taskInfo.setIcon(ids[i]);

            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }

}
