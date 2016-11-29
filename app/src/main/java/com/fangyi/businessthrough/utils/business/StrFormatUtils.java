package com.fangyi.businessthrough.utils.business;

import android.text.TextUtils;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class StrFormatUtils {

    /**
     * 格式化 List 转 数组 字符串
     * @param mapValuesList
     * @return
     */
    public static String[] getStringsFormat(List<String> mapValuesList) {
        final String[] arr = new String[mapValuesList.size()];
        mapValuesList.toArray(arr);
        Arrays.sort(arr);
        return arr;
    }


    /**
     * 金额展示格式刷
     *
     * @param sum
     * @return
     */
    public static String AmountShow(double sum) {
        DecimalFormat df1 = new DecimalFormat("#,###,##0.00");
        return df1.format(sum);
    }


    public static double AmountShowBug(String sum) {
        return Double.parseDouble(sum.replace(",", ""));
    }

    public static String AmountShowPolishing(String sum) {


        if (".".equals(sum.subSequence(0, 1))) {
            return "0" + sum;
        }

        return sum;
    }

    /**
     * 整数补齐
     *
     * @param sum
     * @return
     */
    public static String numberInteger(double sum) {
        DecimalFormat df1 = new DecimalFormat("###");
        return df1.format(sum);
    }


    public static String StringFilter(String str) throws PatternSyntaxException {
//        String regEx = "[+-/\\:*?<>|\"\n\t]"; //要过滤掉的字符
        String regEx = "[`~!@#$%^&*()+=|{}':;',\\\\[\\\\]<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]"; //要过滤掉的字符

        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        return m.replaceAll("").trim();
    }

    public static String getInputIsEmpty(String str) {

        if (TextUtils.isEmpty(str)) {
            return "0";
        } else if (".".equals(str)) {
            return "0";
        } else {
            return str;
        }
    }
}
