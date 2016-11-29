package com.fangyi.businessthrough.parameter;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by FANGYI on 2016/9/19.
 */

public class SystemFieldValues {

    public static Map<String, String> getSystemValues() {
        Map<String, String> map = new LinkedHashMap<>();
        map.put("2", "二号");
        map.put("3", "三号");
        map.put("4", "四号");
        map.put("5", "五号");
        map.put("6", "六号");
        map.put("7", "七号");

        map.put("12510", "采购入库");
        map.put("12511", "订单委外");

        map.put("251", "现购");
        map.put("252", "赊购");
        map.put("20301", "受托入库");
        map.put("20302", "购销");
        map.put("20303", "调拔");

        map.put("12530", "销售出库类型");
        map.put("12531", "受托出库类型");

        map.put("100", "现销");
        map.put("101", "赊销");
        map.put("102", "分期收款销售");
        map.put("103", "委托代销");
        map.put("20297", "受托代销");

        map.put("0" + "wh", "单据界面");
        map.put("1" + "wh", "商品界面");

        map.put("0" + "mdm", "滑动显示");
        map.put("1" + "mdm", "下拉显示");

        map.put("0" + "dum", "不控制");
        map.put("1" + "dum", "单张上传");
        map.put("2" + "dum", "批量上传");

        map.put("0" + "dc", "保存后");
        map.put("1" + "dc", "打印后");
        map.put("2" + "dc", "上传后");

        map.put("0" + "ddc", "保存后");
        map.put("1" + "ddc", "打印后");
        map.put("2" + "ddc", "上传后");

        map.put("0" + "rcn", "一联");
        map.put("1" + "rcn", "二联");
        map.put("2" + "rcn", "三联");

        map.put("02","283");

        map.put("1" + "fst", "数量");
        map.put("2" + "fst", "金额");
        map.put("3" + "fst", "数量或金额");
        map.put("4" + "fst", "数量且金额");

        map.put("0" + "ff", "赠品");//赠品
        map.put("1" + "ff", "商品");//商品
        map.put("2" + "ff", "赠品");//促销品

        return map;
    }
}
