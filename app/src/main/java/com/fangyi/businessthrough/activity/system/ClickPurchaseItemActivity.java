package com.fangyi.businessthrough.activity.system;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.factory.FragmentFactory;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * 所有能点击的按钮全部跳转到这个页面
 * Created by zs on 2015/11/3.
 */
public class ClickPurchaseItemActivity extends BaseActivity {


    FragmentManager fm;
    public Intent intent;
    public FragmentTransaction ft;

    public int resId;
    public String[] item;
    public String id;
    public String parkName;

    @BindView(R.id.action_bar_back)
    public ImageView actionBarBack;
    @BindView(R.id.tv_title)
    public TextView tvTitle;
    @BindView(R.id.iv_right)
    public ImageView ivRight;
    @BindView(R.id.tv_right)
    public TextView tvRight;
    @BindView(R.id.rl_actionbar)
    public RelativeLayout rlActionbar;
    @BindView(R.id.fl_click_button)
    public FrameLayout flClickButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_click_button);
        ButterKnife.bind(this);
        // 获取传递过来的资源id值
        intent = getIntent();

        resId = intent.getIntExtra("resId", 0);
        item = intent.getStringArrayExtra("item");
        if (intent.getExtras() != null) {
            resId = intent.getExtras().getInt("resId");
            item = intent.getExtras().getStringArray("item");
        }
        id = intent.getStringExtra("id");
        parkName = intent.getStringExtra("parkName");
        /**
         * 根据传递过来的不同的资源id值设置不同的fragment
         */
        fm = getSupportFragmentManager();
        ft = fm.beginTransaction();
        ft.replace(R.id.fl_click_button, FragmentFactory.createByPurchaseItem(resId, item));
        ft.commit();

        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClickPurchaseItemActivity.this.finish();
            }
        });
    }
}

