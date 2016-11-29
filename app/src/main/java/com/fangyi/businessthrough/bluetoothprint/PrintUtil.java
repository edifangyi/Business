package com.fangyi.businessthrough.bluetoothprint;

import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.events.AddGoodsPromotion;

import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class PrintUtil {
    private static int LINE_BYTE_SIZE = 32;

    public static ArrayList<Object> getPrintInfo(String titleType, String companyName, PrintOrderMain orderMain, String businessType, Users loginUsers) {
        ArrayList<Object> infoList = new ArrayList<Object>();

        infoList.add(setFontType((byte) 0));
        infoList.add(setFontSize((byte) 0x11)); //设置字体大小为4
        infoList.add(setMargin((byte) 1)); //设置文字居中
        String title = null;
        if ("1".equals(businessType)) {
            title = "销售单据";
        } else if ("2".equals(businessType)){
            title = "退货单据";
        } else if ("3".equals(businessType)) {
            title = "退货单据";
        } else if ("0".equals(businessType)){
            title = "退货单据";
        }

        if (titleType != null && !titleType.equals(""))
            title = title + "（" + titleType + "）";
        infoList.add(title);
        infoList.add(setR());  //设置回车换行
        infoList.add(setFontSize((byte) 0));
        infoList.add(printSpcLine('='));
        if (companyName != null && !companyName.equals("")) {
            infoList.add(setFontSize((byte) 0)); //设置字体大小为4
            infoList.add(setMargin((byte) 1)); //设置文字居中
            infoList.add(companyName);
            infoList.add(setR());
//			infoList.add(setFontType((byte)0));
//			infoList.add(setFontSize((byte)0));
            infoList.add(printSpcLine('-'));
        }
        infoList.add(setFontSize((byte) 0));
        infoList.add(setMargin((byte) 2));
        infoList.add("客户:" + orderMain.customerName + spaces(24 - orderMain.customerName.length()));
        infoList.add(setR());
        infoList.add("地址:" + orderMain.customerAddress + spaces(25 - orderMain.customerAddress.length()));
        infoList.add(setR());
        infoList.add("电话:" + orderMain.customerTel + spaces(27 - orderMain.customerTel.length()));
        String orderNum;
        orderNum = orderMain.id;
        infoList.add(setR());
        infoList.add("单据号：" + orderNum + spaces(24 - orderNum.length()));
        infoList.add(setR());
//		infoList.add("销售商品：");
//		infoList.add(setR());
        infoList.add(printSpcLine('-'));
        //infoList.add("品名  单位   单价   数量    金额");
        infoList.add("品名 单位  数量    单价     金额");
        infoList.add(printSpcLine('-'));




        List<AddGoodsMessage> goodsInfos = new ArrayList<>();
        List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos = new ArrayList<>();
        Data.ArrangeOrder(orderMain, goodsInfos, goodsPromotionInfos);


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            infoList.add(goodsInfo.goodsName + spaces(27 - goodsInfo.goodsName.length()));
            infoList.add(formatSale(AmountShow(Double.parseDouble(goodsInfo.getGoodsPrice())), goodsInfo.getGoodsNum(), AmountShow(Double.parseDouble(goodsInfo.getGoodsSum())), goodsInfo.getGoodsUom()));
        }



        for (HashMap<String, AddGoodsPromotion> goodsPromotionInfo : goodsPromotionInfos) {
            List<AddGoodsPromotion> list1 = new ArrayList<>();
            for (Map.Entry<String, AddGoodsPromotion> str : goodsPromotionInfo.entrySet()) {

                list1.add(str.getValue());
            }


            for (int i = 0; i < list1.size() + 1; i++) {
                if (i == list1.size()) {

                    infoList.add(list1.get(i - 1).getPromotionName() + spaces(27 - (list1.get(i - 1).getPromotionName().length())));

                    infoList.add(formatSale1("0.00", list1.get(i - 1).getPromotionCount(), "0.00", list1.get(i - 1).getPromotionUnit()));
                } else {


                    infoList.add(list1.get(i).getGoodsName() + spaces(29 - list1.get(i).getGoodsName().length()));

                    infoList.add(formatSale1(AmountShow(Double.parseDouble(list1.get(i).getGoodsPrice())), list1.get(i).getGoodsNumber(), AmountShow(Double.parseDouble(list1.get(i).getGoodsSumPrice())), list1.get(i).getGoodsUnit()));

                }
            }
            infoList.add(setR());
        }
        infoList.add(setR());




//

        infoList.add(printSpcLine('-'));
        infoList.add(formatSaleSum(orderMain.allMoney));


        infoList.add(printSpcLine('='));
        infoList.add("销售人员：" + loginUsers.kISName + "（" + loginUsers.userSysID + "）");
        infoList.add(setR());
        infoList.add("销售日期：" + formatNow() + "  ");
        infoList.add(setR());
        infoList.add(printSpcLine('-'));
        infoList.add("-吉林市金蝶科技开发有限公司");
        infoList.add(setR());
        infoList.add(setPageSkip((byte) 2));
        return infoList;
    }

    public static ArrayList<Object> getPrintInfo_2(String titleType, String companyName, PrintPurchaseOrderMain orderMain, String businessType, Users loginUsers) {
        ArrayList<Object> infoList = new ArrayList<Object>();

        infoList.add(setFontType((byte) 0));
        infoList.add(setFontSize((byte) 0x11)); //设置字体大小为4
        infoList.add(setMargin((byte) 1)); //设置文字居中
        String title = null;
        if ("4".equals(businessType)) {
            title = "采购申请";
        } else if ("5".equals(businessType)) {
            title = "采购入库";
        } else if ("6".equals(businessType)) {
            title = "退货通知";
        } else if ("7".equals(businessType)) {
            title = "采购退货";
        }

        if (titleType != null && !titleType.equals(""))
            title = title + "（" + titleType + "）";
        infoList.add(title);
        infoList.add(setR());  //设置回车换行
        infoList.add(setFontSize((byte) 0));
        infoList.add(printSpcLine('='));
        if (companyName != null && !companyName.equals("")) {
            infoList.add(setFontSize((byte) 0)); //设置字体大小为4
            infoList.add(setMargin((byte) 1)); //设置文字居中
            infoList.add(companyName);
            infoList.add(setR());
            infoList.add(printSpcLine('-'));
        }
        infoList.add(setFontSize((byte) 0));
        infoList.add(setMargin((byte) 2));
        infoList.add("供应商:" + orderMain.fyFSupplier + spaces(24 - orderMain.fyFSupplier.length()));
        String orderNum;
        orderNum = orderMain.id;
        infoList.add(setR());
        infoList.add("单据号：" + orderNum + spaces(24 - orderNum.length()));
        infoList.add(setR());
        infoList.add(printSpcLine('-'));
        infoList.add("品名 单位  数量    单价     金额");
        infoList.add(printSpcLine('-'));

        List<AddGoodsMessage> goodsInfos = new ArrayList<>();


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            infoList.add(goodsInfo.goodsName + spaces(27 - goodsInfo.goodsName.length()));
            infoList.add(formatSale(AmountShow(Double.parseDouble(goodsInfo.getGoodsPrice())), goodsInfo.getGoodsNum(), AmountShow(Double.parseDouble(goodsInfo.getGoodsSum())), goodsInfo.getGoodsUom()));
        }


        infoList.add(printSpcLine('-'));
        infoList.add(formatSaleSum(orderMain.allMoney));

        infoList.add(printSpcLine('='));
        infoList.add("业务员人员：" + loginUsers.kISName + "（" + loginUsers.userSysID + "）");
        infoList.add(setR());
        infoList.add("采购日期：" + formatNow() + "  ");
        infoList.add(setR());
        infoList.add(printSpcLine('-'));
        infoList.add("-吉林市金蝶科技开发有限公司");
        infoList.add(setR());
        infoList.add(setPageSkip((byte) 2));
        return infoList;
    }

    public static ArrayList<Object> getPrintInfo_3(String titleType, String companyName, PrintOrderMain orderMain, String businessType, Users loginUsers) {
        ArrayList<Object> infoList = new ArrayList<Object>();

        infoList.add(setFontType((byte) 0));
        infoList.add(setFontSize((byte) 0x11)); //设置字体大小为4
        infoList.add(setMargin((byte) 1)); //设置文字居中
        String title = null;
        title = "仓库调拨";

        if (titleType != null && !titleType.equals(""))
            title = title + "（" + titleType + "）";
        infoList.add(title);
        infoList.add(setR());  //设置回车换行
        infoList.add(setFontSize((byte) 0));
        infoList.add(printSpcLine('='));
        if (companyName != null && !companyName.equals("")) {
            infoList.add(setFontSize((byte) 0)); //设置字体大小为4
            infoList.add(setMargin((byte) 1)); //设置文字居中
            infoList.add(companyName);
            infoList.add(setR());
//			infoList.add(setFontType((byte)0));
//			infoList.add(setFontSize((byte)0));
            infoList.add(printSpcLine('-'));
        }
        infoList.add(setFontSize((byte) 0));
        infoList.add(setMargin((byte) 2));
        infoList.add("调入:" + orderMain.wareHouseName + spaces(24 - orderMain.wareHouseName.length()));
        infoList.add(setR());
        infoList.add("调出:" + orderMain.deliveryDate + spaces(25 - orderMain.deliveryDate.length()));
        infoList.add(setR());
        infoList.add("验收:" + orderMain.message + spaces(27 - orderMain.message.length()));
        infoList.add(setR());
        infoList.add("保管:" + orderMain.deptId + spaces(27 - orderMain.deptId.length()));
        String orderNum;
        orderNum = orderMain.id;
        infoList.add(setR());
        infoList.add("单据号：" + orderNum + spaces(24 - orderNum.length()));
        infoList.add(setR());
        infoList.add(printSpcLine('-'));
        infoList.add("品名 单位                  数量");
        infoList.add(printSpcLine('-'));


        List<AddGoodsMessage> goodsInfos = new ArrayList<>();
        List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos = new ArrayList<>();
        Data.ArrangeOrder(orderMain, goodsInfos, goodsPromotionInfos);


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            infoList.add(goodsInfo.goodsName + spaces(27 - goodsInfo.goodsName.length()));
            infoList.add(formatSale2(goodsInfo.getGoodsNum(), goodsInfo.getGoodsUom()));
        }

        infoList.add(printSpcLine('-'));

        infoList.add(printSpcLine('='));
        infoList.add("业务员：" + loginUsers.kISName + "（" + loginUsers.userSysID + "）");
        infoList.add(setR());
        infoList.add("调拨日期：" + orderMain.deliveryDate + "  ");
        infoList.add(setR());
        infoList.add(printSpcLine('-'));
        infoList.add("-吉林市金蝶科技开发有限公司");
        infoList.add(setR());
        infoList.add(setPageSkip((byte) 2));
        return infoList;
    }

    /**
     * 设置字体的大小
     *
     * @param size
     * @return
     */
    private static byte[] setFontSize(byte size) {
        return new byte[]{0x1d, 0x21, size};
    }

    /**
     * 设置字符对齐方式
     *
     * @param pos 0-左对齐 1-中间 2-右面
     * @return
     */
    private static byte[] setMargin(byte pos) {
        return new byte[]{0x1b, 0x61, pos};
    }

    /**
     * 回车换行
     *
     * @return
     */
    private static byte[] setR() {
        //return new byte[] {0x0D};//名利达和金丰
        return new byte[]{0x0A};//长春银诺克
    }

    /**
     * 设置走纸行
     *
     * @param n
     * @return
     */
    private static byte[] setPageSkip(byte n) {
        return new byte[]{0x1b, 0x64, n};
    }

    /**
     * 输出分割符
     *
     * @param ch
     * @return
     */
    private static String printSpcLine(char ch) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 32; i++)
            sb.append(ch);
        return sb.toString();

    }

    /**
     * 设置字体加粗
     *
     * @param bold
     * @return
     */
    private static byte[] setBoldFont(boolean bold) {
        if (bold)
            return new byte[]{0x1b, 0x45, 0x01};
        else
            return new byte[]{0x1b, 0x45, 0x00};
    }

    /**
     * 设置字体类型
     *
     * @param n 0-24点阵   1-16点阵
     * @return
     */
    private static byte[] setFontType(byte n) {
        return new byte[]{0x1b, 0x4d, n};
    }

    /**
     * 输出指定数目空格
     *
     * @param n
     * @return
     */
    private static String spaces(int n) {
        String str = "";
        for (int i = 0; i < n; i++)
            str = str + " ";
        return str;
    }


    /**
     * 获取数据长度
     *
     * @param msg
     * @return
     */
    private static int getBytesLength(String msg) {
        return msg.getBytes(Charset.forName("GB2312")).length;
    }

    /**
     * 格式化销售数据
     *
     * @param price
     * @param num
     * @param amount
     * @return
     */
    private static String formatSale(String price, String num, String amount, String unit) {
        //String str=spaces(7-unit.length())+unit+spaces(9-price.length())+price+spaces(6-num.length())+num+spaces(9-amount.length())+amount;
        String str = spaces(4 - unit.length()) + unit + spaces(8 - num.length()) + num + spaces(7 - price.length()) + price + spaces(12 - amount.length()) + amount;
        return str;
    }

    private static String formatSale1(String price, String num, String amount, String unit) {
        //String str=spaces(7-unit.length())+unit+spaces(9-price.length())+price+spaces(6-num.length())+num+spaces(9-amount.length())+amount;
        String str = spaces(3 - unit.length()) + unit + spaces(8 - num.length()) + num + spaces(7 - price.length()) + price + spaces(12 - amount.length()) + amount;
        return str;
    }


    private static String formatSale2(String goodsNum, String goodsUom) {
        String str = spaces(3 - 0) + 0 + spaces(8 - 0) + 0 + spaces(7 - 0) + goodsNum + spaces(12 - goodsUom.length()) + goodsUom;
        return str;
    }

    /**
     * 格式化销售汇总数据
     * @param sum
     * @return
     */
    private static String formatSaleSum(String sum) {
        String str = "合计金额:" + spaces(23 - sum.length()) + sum;
        return str;
    }

    /**
     * 格式化赠品数据
     *
     * @param name
     * @param num
     * @return
     */
    private static String formatPresent(String name, String num) {
        String str = name + spaces(LINE_BYTE_SIZE - getBytesLength(name) - num.length()) + num;
        return str;
    }

    /**
     * 格式化赠品汇总
     *
     * @param count
     * @return
     */
    private static String formatPreSum(String count) {
        String str = "合计:" + spaces(LINE_BYTE_SIZE - 5 - count.length()) + count;
        return str;
    }

    private static String formatNow() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date now = new Date();
        return formatter.format(now);
    }
}
