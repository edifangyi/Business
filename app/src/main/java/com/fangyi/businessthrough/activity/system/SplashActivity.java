package com.fangyi.businessthrough.activity.system;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.socks.library.KLog;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by FANGYI on 2016/8/15.
 */


public class SplashActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init();

    }

    private boolean isFirstIn = false;
    private static final String DB_NAME = "business.db";
    public static final String PACKAGE_NAME = "com.fangyi.business";
    public static final String DB_PATH = String.valueOf(Environment.getExternalStorageDirectory());//sd卡目录

    private void init() {

        isFirstIn = PrefUtils.getBoolean(this, "isFirstIn", true);
        if (!isFirstIn) {
            KLog.e("=====111");
            loadTask();//进入主页面
        } else {
            KLog.e("=====222");
            PrefUtils.setBoolean(this, "isFirstIn", false);
//            delAllFile(DB_PATH + "/" + PACKAGE_NAME + "/" + DB_NAME);//进入引导页
            copyDB(DB_NAME);
            loadTask();//进入主页面

        }

    }

    /**
     * 把assets目录下的 NumeberAddressQuery.db 拷贝到 path = "/data/data/com.fangyi.mobilesafe/files/NumeberAddressQuery.db"
     */
    private void copyDB(String dbname) {
        File file = new File(DB_PATH, dbname);
        if (file.exists() && file.length() > 0) {
            //数据库已经存在
            Log.e("数据库已经存在", "数据库已经存在");
            delFolder(String.valueOf(file));
            CreateFile(file);
        } else {
            Log.e("数据库正在拷贝", "数据库正在拷贝");
            CreateFile(file);
        }

    }

    /**
     * 创建
     *
     * @param file
     */
    private void CreateFile(File file) {
        try {
            InputStream is = CommonUtils.getResources().openRawResource(R.raw.business);
            FileOutputStream fos = new FileOutputStream(file);


            int len = 0;
            byte buffer[] = new byte[1024];
            while ((len = is.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            is.close();
            fos.close();

            Log.e("拷贝完成", "拷贝完成");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除以存在的文件
     */
    public void delAllFile(String path) {
        File file = new File(path);
        if (!file.exists()) {
            return;
        }
        if (!file.isDirectory()) {
            return;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            } else {
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
                delFolder(path + "/" + tempList[i]);// 再删除空文件夹
            }
        }
    }

    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); // 删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); // 删除空文件夹
        } catch (Exception e) {
            System.out.println("删除文件夹操作出错");
            e.printStackTrace();
        }
    }

    /**
     * 加载任务
     */
    private void loadTask() {


        TestNetwork();

        //延迟1秒进入主界面
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                goLogin();
            }
        }, 800);
    }

    /**
     * 测试网络连接
     */
    private void TestNetwork() {
        //判断网络是否可用
        if (NetConnectionUtil.isNetworkAvailable(this)) {
            Toast.makeText(this, "网络可用", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "网络不可用", Toast.LENGTH_SHORT).show();
        }

    }


    private void goLogin() {
        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
        startActivity(intent);
        finish();
        overridePendingTransition(0, 0);
    }


}