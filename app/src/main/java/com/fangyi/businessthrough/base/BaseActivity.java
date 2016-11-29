package com.fangyi.businessthrough.base;


import android.support.v7.app.AppCompatActivity;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class BaseActivity extends AppCompatActivity {

    public BaseActivity() {
    }

    public void onStart() {
        super.onStart();
    }

    protected void onResume() {
        super.onResume();
        JPushInterface.onResume(this);
    }

    protected void onPause() {
        super.onPause();
        JPushInterface.onPause(this);
    }

    public void onStop() {
        super.onStop();
    }


}
