package com.fangyi.businessthrough.activity.business;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.adapter.business.MenuSumFragmentAdapter;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.bean.business.Summarizing;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.view.ListViewDecoration;
import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.application.FYApplication.getContext;


/**
 * Created by FANGYI on 2016/11/15.
 */

public class SumShowActivity extends BaseActivity {

    @BindView(R.id.action_bar_back)
    ImageView actionBarBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_right)
    TextView tvRight;
    @BindView(R.id.rl_actionbar)
    RelativeLayout rlActionbar;
    @BindView(R.id.iv_none)
    ImageView ivNone;
    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;


    private String sqlStrType = "";
    private String sqlDocumentType = "";
    private String strStartDate = "";
    private String strEndDate = "";
    private String strDept = "";
    private String strCustomerSysId = "";
    private String strGoodsSysId = "";
    private List<Summarizing> summarizings = new ArrayList<>();

    private MenuSumFragmentAdapter mAdapter;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    KLog.e("====2344353252");
                    if (mAdapter == null) {
                        mAdapter = new MenuSumFragmentAdapter(summarizings);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }

                    break;
            }
        }
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_order);
        ButterKnife.bind(this);
        readingData();
        assignViews();
        setInitView();
        setListener();
    }


    private void readingData() {
        sqlStrType = this.getIntent().getStringExtra("sqlStrType");
        sqlDocumentType = this.getIntent().getStringExtra("sqlDocumentType");
        strStartDate = this.getIntent().getStringExtra("strStartDate");
        strEndDate = this.getIntent().getStringExtra("strEndDate");
        strDept = this.getIntent().getStringExtra("strDept");
        strCustomerSysId = this.getIntent().getStringExtra("strCustomerSysId");
        strGoodsSysId = this.getIntent().getStringExtra("strGoodsSysId");

        KLog.e("====" + sqlStrType + "=======" + sqlDocumentType + "=======" + strStartDate + "=======" + strEndDate + "=======" + strDept + "=======" + strCustomerSysId + "=======" + strGoodsSysId);

//
        DBBusiness business = new DBBusiness(getApplication());
        summarizings = business.getSumShow(sqlStrType, sqlDocumentType, strStartDate, strEndDate, strDept, strCustomerSysId, strGoodsSysId);
        business.closeDB();



        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        handler.sendEmptyMessage(0);
    }

    private void assignViews() {

        if ("0".equals(sqlStrType)) {
            tvTitle.setText("商品");
        } else if ("1".equals(sqlStrType)) {
            tvTitle.setText("客户");
        } else if ("2".equals(sqlStrType)) {
            tvTitle.setText("客户 + 商品");
        }



    }


    private void setInitView() {

    }

    private void setListener() {

    }

}
