package com.fangyi.businessthrough.utils.business;

import android.text.TextUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class DateUtil {

    /**
     * 日期格式刷
     *
     * @param date
     * @return
     */
    public static String getTimeNYR(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return format.format(date);
    }




    /**
     * 日期格式刷
     *
     * @param date
     * @return
     */
    public static String getTimeYYYY_MM_dd(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        return format.format(date);
    }

    /**
     * 日期格式化
     * * @return
     */
    public static String getTimeYYYY_MM_DD_HH(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmmSS");
        return format.format(date);
    }


    /**
     * 日期格式化
     * * @return
     */
    public static String getTimeYYYY_MM_DD_HH_MM_SS(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH-mm-SS");
        return format.format(date);
    }

    /**
     * 字符串转换成日期
     *
     * @param str
     * @return date
     */
    public static String getStrToDate(String str) {
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = null;
        try {
            date = format2.format(format1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    /**
     * 日期格式化
     * * @return
     */
    public static String getTime(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return format.format(date);
    }


    public static String getTime(String date) {
        return date.substring(0, 11);
    }

    /**
     * 日期格式化
     *
     * @param day
     * @return
     */
    public static String getTimeNYR(int day) {
        Calendar cal = Calendar.getInstance();//使用默认时区和语言环境获得一个日历。
        cal.add(Calendar.DAY_OF_MONTH, +day);
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日HH时mm分ss秒");
        return format.format(cal.getTime());
    }

    /**
     * 超时判断
     * * @return
     *
     * @param isOvertimeDownload
     */
    public static boolean judgeOverTime(String isOvertimeDownload) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Date endDate = null;
        try {
            if (TextUtils.isEmpty(isOvertimeDownload)) {
                return false;
            } else {
                endDate = format.parse(isOvertimeDownload);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long day = (date.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000);
        if (CalculationUtils.compare(day, 10) > -1) {
            return false;
        } else {
            return true;
        }
    }


    public static String calculateDays(String isOvertimeDownload) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd");
        Date date = new Date();
        Date endDate = null;
        try {
            if (TextUtils.isEmpty(isOvertimeDownload)) {
                return "0";
            } else {
                endDate = format.parse(isOvertimeDownload);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        long day = (date.getTime() - endDate.getTime()) / (24 * 60 * 60 * 1000);
        return String.valueOf(day);
    }


}
