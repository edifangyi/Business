package com.fangyi.businessthrough.utils.business;

import java.math.BigDecimal;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class CalculationUtils {

    /**
     * 提供精确乘法运算的mul方法
     * @param value1
     * @param value2
     * @return
     */
    public static double mul(double value1, double value2) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        return b1.multiply(b2).doubleValue();
    }

    public static double mul(String value1, String value2) {
        double v1 = Double.parseDouble(value1);
        double v2 = Double.parseDouble(value2);

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }



    public static double mulss(String value1, String value2) {
        double v1 = Double.parseDouble(value1);
        double v2 = Double.parseDouble(value2);

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }

    public static double mulsss(String value1, String value2, String value3) {
        double v1 = Double.parseDouble(value1);
        double v2 = Double.parseDouble(value2);
        double v3 = Double.parseDouble(value3);

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.multiply(b2).multiply(b3).doubleValue();
    }

    public static double mulddd(double value1, double value2, double value3) {
        BigDecimal b1 = new BigDecimal(Double.toString(value1));
        BigDecimal b2 = new BigDecimal(Double.toString(value2));
        BigDecimal b3 = new BigDecimal(Double.toString(value3));
        return b1.multiply(b2).multiply(b3).doubleValue();
    }

    /**
     * 提供精确的加法运算
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }

    public static double add2(double v1, double v2, double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.add(b2).add(b3).doubleValue();
    }


    /**
     * 提供精确的减法运算。
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }

    public static double sub2(double v1, double v2, double v3) {
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        BigDecimal b3 = new BigDecimal(Double.toString(v3));
        return b1.subtract(b2).subtract(b3).doubleValue();
    }

    /**
     * 两个double类型比较
     *
     * @param v1
     * @param v2
     * @return
     */
    public static int compare(double v1, double v2) {
        BigDecimal val1 = new BigDecimal(Double.toString(v1));
        BigDecimal val2 = new BigDecimal(Double.toString(v2));
        int b = 0;
        if (val1.compareTo(val2) < 0) {
            b = -1;//第二个大
        }
        if (val1.compareTo(val2) == 0) {
            b = 0;//相等
        }
        if (val1.compareTo(val2) > 0) {
            b = 1;//第一个大
        }
        return b;
    }

    /**
     * 两个String类型比较
     *
     * @param v1
     * @param v2
     * @return
     */
    public static int compare(String v1, String v2) {
        BigDecimal val1 = new BigDecimal(v1);
        BigDecimal val2 = new BigDecimal(v2);
        int b = 0;
        if (val1.compareTo(val2) < 0) {
            b = -1;//第二个大
        }
        if (val1.compareTo(val2) == 0) {
            b = 0;//相等
        }
        if (val1.compareTo(val2) > 0) {
            b = 1;//第一个大
        }
        return b;
    }





    /**
     * 取余运算
     *
     * @param v1
     * @return
     */
    public static double remainder(double v1, double v2) {

        BigDecimal val1 = new BigDecimal(Double.toString(v1 % v2));
        double val = val1.setScale(2, BigDecimal.ROUND_HALF_UP).doubleValue();
        return val;
    }


    /**
     * （相对）精确的除法运算,当发生除不尽的情况时，
     * 由scale参数指定精度，以后的数字四舍五入。
     * @param d1 被除数
     * @param d2 除数
     * @param scale 需要精确到小数点以后几位
     * @return 两个参数的商
     */
    public static double div(double d1, double d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }


    public static double div(String d1, String d2, int scale) {
        if (scale < 0) {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }
        double v1 = Double.parseDouble(d1);
        double v2 = Double.parseDouble(d2);

        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }
    /**
     * （相对）精确的除法运算,当发生除不尽的情况时， 促销品方案用的
     * 由scale参数指定精度，以后的数字四舍五入。
     * @param d1 被除数
     * @param d2 除数
     * @return 两个参数的商
     */
    public static int div2(double d1, double d2) {
        BigDecimal b1 = new BigDecimal(Double.toString(d1));
        BigDecimal b2 = new BigDecimal(Double.toString(d2));
        return b1.divide(b2, 0, BigDecimal.ROUND_DOWN).intValue();
    }

    /**
     * 添加商品方案中用的
     *
     * @param value1
     * @param value2
     * @return
     */
    public static double divAddGoods(String value1, String value2) {
        if ("0.".equals(value2)) {
            value2 = "1";
        }

        double v1 = Double.parseDouble(value1);
        double v2 = Double.parseDouble(value2);
        BigDecimal b1 = new BigDecimal(v1);
        BigDecimal b2 = new BigDecimal((v2));
        return b1.divide(b2, 2, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     * 取小数后面x位
     *
     * @param strings
     * @return
     */
    public static double getBigDecimal_6(String strings) {
        double price = Double.parseDouble(strings);
        BigDecimal bg = new BigDecimal(price).setScale(6, BigDecimal.ROUND_UP);
        return bg.doubleValue();
    }

    /**
     * 取小数后面0位
     *
     * @param strings
     * @return
     */
    public static double getBigDecimal_0(String strings) {
        double price = Double.parseDouble(strings);
        BigDecimal bg = new BigDecimal(price).setScale(0, BigDecimal.ROUND_UP);
        return bg.doubleValue();
    }

}
