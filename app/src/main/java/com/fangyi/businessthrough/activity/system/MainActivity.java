package com.fangyi.businessthrough.activity.system;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.adapter.system.MainPagerAdapter;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.jpush.PushNotificationActivity;
import com.fangyi.businessthrough.jpush.ExampleUtil;
import com.fangyi.businessthrough.view.NoScrollViewPager;
import com.socks.library.KLog;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.jpush.android.api.JPushInterface;

public class MainActivity extends BaseActivity {


    @BindView(R.id.home_title)
    TextView homeTitle;
    @BindView(R.id.push_notification)
    TextView pushNotification;
    @BindView(R.id.home_info)
    ImageView homeInfo;
    @BindView(R.id.viewpager)
    NoScrollViewPager viewpager;
    @BindView(R.id.bottom_main)
    RadioButton bottomMain;
    @BindView(R.id.bottom_me)
    RadioButton bottomMe;
    @BindView(R.id.bottom_set)
    RadioButton bottomSet;
    @BindView(R.id.radio_group)
    RadioGroup radioGroup;
    private MainPagerAdapter adapter;
    public String loginUserID;
    public static boolean isForeground = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        loginUserID = intent.getStringExtra("LoginUserID");

        ButterKnife.bind(this);

        initJPush();
        initView();
        registerMessageReceiver();
    }

    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJPush() {
        JPushInterface.init(getApplicationContext());
    }

    /**
     * 初始化布局
     */
    private void initView() {

        pushNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PushNotificationActivity.class);
                startActivity(intent);
            }
        });


        adapter = new MainPagerAdapter(getSupportFragmentManager());
        viewpager.setAdapter(adapter);
        viewpager.setOffscreenPageLimit(5);
        viewpager.setCurrentItem(0);
        homeTitle.setText(Html.fromHtml("<b>" + "应用" + "</b>"));
        bottomMain.setChecked(true);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.bottom_main:
                        viewpager.setCurrentItem(0, false);
                        homeTitle.setText(Html.fromHtml("<b>" + "应用" + "</b>"));
                        break;
                    case R.id.bottom_me:
                        viewpager.setCurrentItem(1, false);
                        homeTitle.setText(Html.fromHtml("<b>" + "数据" + "</b>"));
                        break;
                    case R.id.bottom_set:
                        viewpager.setCurrentItem(2, false);
                        homeTitle.setText(Html.fromHtml("<b>" + "设置" + "</b>"));
                        break;
                }
            }
        });
    }


    //for receive customer msg from jpush server
    private MessageReceiver mMessageReceiver;
    public static final String MESSAGE_RECEIVED_ACTION = "com.example.jpushdemo.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_TITLE = "title";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";


    public void registerMessageReceiver() {

        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        registerReceiver(mMessageReceiver, filter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String extras = intent.getStringExtra(KEY_EXTRAS);
                StringBuilder showMsg = new StringBuilder();
                showMsg.append(KEY_MESSAGE + " : " + messge + "\n");
                if (!ExampleUtil.isEmpty(extras)) {
                    showMsg.append(KEY_EXTRAS + " : " + extras + "\n");
                }
                KLog.e("===========" + showMsg.toString());
            }
        }
    }


    @Override
    protected void onResume() {
        isForeground = true;
        super.onResume();
    }


    @Override
    protected void onPause() {
        isForeground = false;
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        unregisterReceiver(mMessageReceiver);
        super.onDestroy();
    }


}
