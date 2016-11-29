package com.fangyi.businessthrough.activity.business;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.adapter.business.MenuSearchSEOrderAdapter;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.bean.business.SEOrderMain;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.fangyi.businessthrough.view.ListViewDecoration;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import static com.fangyi.businessthrough.application.FYApplication.ADD_HISTORY_ORDER;
import static com.fangyi.businessthrough.application.FYApplication.getContext;


/**
 * Created by FANGYI on 2016/9/19.
 */

public class SearchSEOrderMainActivity extends BaseActivity implements View.OnClickListener {


    private ImageView actionBarBack;
    private TextView tvTitle;
    private TextView tvRight;
    private ImageView ivNone;
    private SwipeMenuRecyclerView recyclerView;

    private FYBtnRadioView fyStartDate;
    private FYBtnRadioView fyEndDate;
    private FYBtnRadioView fyWareHouse;
    private FYBtnRadioView fyCustomerName;

    private String businessType = "";
    private String custNameID = "";
    private String userSysID = "";


    private List<SEOrderMain> orderMainList = new ArrayList<>();

    private MenuSearchSEOrderAdapter mAdapter;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            switch (msg.what) {
                case 0:
                    if (mAdapter == null) {
                        mAdapter = new MenuSearchSEOrderAdapter(orderMainList);
                        mAdapter.setOnItemClickListener(onItemClickListener);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }

    };
    private List<SEOrderMain> seorderlist;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        userSysID = this.getIntent().getStringExtra("userSysID");
        custNameID = this.getIntent().getStringExtra("fyFCustNameID");
        businessType = this.getIntent().getStringExtra("businessType");


        assignViews();
        initData();
        setInitView();

    }


    private void assignViews() {
        actionBarBack = (ImageView) findViewById(R.id.action_bar_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        tvRight = (TextView) findViewById(R.id.tv_right);
        recyclerView = (SwipeMenuRecyclerView) findViewById(R.id.recycler_view);
        ivNone = (ImageView) findViewById(R.id.iv_none);
        if ("0".equals(businessType)) {
            tvTitle.setText("销售退货单");
        } else {
            tvTitle.setText("销售订货单");
        }
    }

    private void initData() {

        DBBusiness dbManager = new DBBusiness(getContext());
        if ("1".equals(businessType)) {
            seorderlist = dbManager.getSEOrderList(userSysID, custNameID, businessType);
        } else if ("4".equals(businessType)) {
            seorderlist = dbManager.getSEOrderList(userSysID, custNameID, businessType);
        }

        orderMainList.clear();
        orderMainList.addAll(seorderlist);

        if (orderMainList.size() == 0) {
            ivNone.setVisibility(View.VISIBLE);
        }


        dbManager.closeDB();
        handler.sendEmptyMessage(0);

    }


    private void setInitView() {

        recyclerView.setLayoutManager(new LinearLayoutManager(FYApplication.getContext()));// 布局管理器。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。

        actionBarBack.setOnClickListener(this);

        tvRight.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back://商品名称
                setCallbackData("请点击选择源单");
                break;
            case R.id.tv_right:
                break;
        }
    }


    /**
     * 点击事件
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

            setCallbackData(orderMainList.get(position).getID());
        }
    };

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        setCallbackData("请点击选择源单");
        super.onBackPressed();
    }

    /**
     * 返回事件
     */
    private void setCallbackData(String orderID) {

        Bundle bundle = new Bundle();
        bundle.putString("orderID", orderID);
        setResult(ADD_HISTORY_ORDER, SearchSEOrderMainActivity.this.getIntent().putExtras(bundle));//执行回调事件;
        SearchSEOrderMainActivity.this.finish();
    }

}
