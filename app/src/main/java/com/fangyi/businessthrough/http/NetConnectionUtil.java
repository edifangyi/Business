package com.fangyi.businessthrough.http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.IOException;

/**
 * Created by FANGYI on 2016/8/31.
 */

public class NetConnectionUtil {
    /**
     * 这个类里设置网络判断等;
     * @param context
     * @return
     */
    //判断网络是否可用
    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if (cm == null) {
        } else {
            NetworkInfo[] info = cm.getAllNetworkInfo();
            if (info != null) {
                for (int i = 0; i < info.length; i++) {
                    if (info[i].getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
    //判断wifi是否打开
    public static boolean isWifiEnabled(Context context) {
        ConnectivityManager mgrConn = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        TelephonyManager mgrTel = (TelephonyManager) context
                .getSystemService(Context.TELEPHONY_SERVICE);
        return ((mgrConn.getActiveNetworkInfo() != null && mgrConn
                .getActiveNetworkInfo().getState() == NetworkInfo.State.CONNECTED) || mgrTel
                .getNetworkType() == TelephonyManager.NETWORK_TYPE_UMTS);
    }
    //判断是否是手机网络
    public static boolean is3rd(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_MOBILE) {
            return true;
        }
        return false;
    }
    //判断是wifi网络还是手机网络
    public static boolean isWifi(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkINfo = cm.getActiveNetworkInfo();
        if (networkINfo != null
                && networkINfo.getType() == ConnectivityManager.TYPE_WIFI) {
            return true;
        }
        return false;
    }
    /**
     * 测试服务器地址是否通
     * @param ipAddress 服务器IP地址
     * @return
     */
    public static boolean pingService(String ipAddress)
    {
        Log.e("", "-----------------------IOException e=进入ping");
        Runtime run = Runtime.getRuntime();
        Process proc = null;
        String pingStr="ping -c 1 -i 0.2 -W 1 "+ ipAddress;
        try {
            proc = run.exec(pingStr);
            int result = proc.waitFor();
            return result==0 || result==2;
        } catch (IOException e) {
            Log.e("", "-----------------------IOException e="+e.getMessage());
            e.printStackTrace();
        } catch (InterruptedException e) {
            Log.e("", "-----------------------InterruptedException e="+e.getMessage());
            e.printStackTrace();
        }finally {
            proc.destroy();
        }
        return false;
    }
    public static String getIPAddress(String httpStr)
    {
        Log.e("", "-----------------------httpStr="+httpStr);
        int start="http://".length();
        int end=0;
        if(httpStr.substring(start).contains(":"))
        {
            end=httpStr.indexOf(":",start);
        }
        else
        {
            end=httpStr.indexOf("/", start);
        }
        return httpStr.substring(start, end);
    }


}
