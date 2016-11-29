package com.fangyi.businessthrough.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;

import com.fangyi.businessthrough.R;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by FANGYI on 2016/9/4.
 */

public class DBHelper {

    private final int BUFFER_SIZE = 400000;
    private static final String DB_NAME = "business.db";
    public static final String PACKAGE_NAME = "com.fangyi.business";
//    public static final String DB_PATH = "/data"
//            + Environment.getDataDirectory().getAbsolutePath() + "/"
//            + PACKAGE_NAME;  //在手机里存放数据库的位置
    //SD卡目录
    public static final String DB_PATH = String.valueOf(Environment.getExternalStorageDirectory());
    private SQLiteDatabase database;
    private Context context;

    public DBHelper(Context context) {

        this.context = context;
    }

    public SQLiteDatabase openDatabase() {
        this.database = this.openDatabase(DB_PATH + "/"
                + DB_NAME);
        return database;
    }

    private SQLiteDatabase openDatabase(String dbfile) {
        try {
            if (!(new File(dbfile).exists())) {
                InputStream is = this.context.getResources().openRawResource(
                        R.raw.business); //欲导入的数据库
                FileOutputStream fos = new FileOutputStream(dbfile);
                byte[] buffer = new byte[BUFFER_SIZE];
                int count = 0;
                while ((count = is.read(buffer)) > 0) {
                    fos.write(buffer, 0, count);
                }
                fos.close();
                is.close();
            }
            SQLiteDatabase db = SQLiteDatabase.openOrCreateDatabase(dbfile,
                    null);
            return db;
        } catch (FileNotFoundException e) {
            Log.e("Database", "File not found");
            e.printStackTrace();
        } catch (IOException e) {
            Log.e("Database", "IO exception");
            e.printStackTrace();
        }
        return null;
    }

    public void closeDatabase() {
        this.database.close();
    }
}
