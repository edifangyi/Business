package com.fangyi.businessthrough.jpush;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.adapter.system.BaseAdapter;
import com.fangyi.businessthrough.adapter.system.BaseViewHolder;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.view.DefaultItemTouchHelpCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class PushNotificationActivity extends BaseActivity {


    private ImageView actionBarBack;
    private TextView tvTitle;
    private RecyclerView rv;

    private DragSwipeAdapter mAdapter;
    private DBManager manager;
    private List<Map<String, String>> mData = new ArrayList<>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_push_notification);

        assignViews();
        loadData();
        init();
//        addListener();


    }

    private void loadData() {


//
//        Intent intent = getIntent();
//        if (null != intent) {
//            Bundle bundle = getIntent().getExtras();
//
//            if (null != bundle) {
//                String content = bundle.getString(JPushInterface.EXTRA_ALERT);
//                Map<String, String> map = new HashMap<>();
//                map.put(CommonUtils.getTimeNYR(new Date()), content);
//                mData.add(map);
//                manager.insertPush(new Date(), content);
//            }
//        }

        new Thread() {
            @Override
            public void run() {
                manager = new DBManager(FYApplication.getContext());
                mData = manager.selectPush();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }

    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mAdapter == null) {
                        mAdapter = new DragSwipeAdapter(FYApplication.getContext(), mData, R.layout.item_push_notification);
                        rv.setLayoutManager(new LinearLayoutManager(FYApplication.getContext()));
                        rv.setAdapter(mAdapter);
                    }else {
                        mAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1:
//                    setCalculation(msg);
                    break;
            }
        }
    };

    private void assignViews() {
        actionBarBack = (ImageView) findViewById(R.id.action_bar_back);
        tvTitle = (TextView) findViewById(R.id.tv_title);
        rv = (RecyclerView) findViewById(R.id.rv);
        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PushNotificationActivity.this.finish();
            }
        });
    }


    private void init() {

        tvTitle.setText("系统通知");
        setItemTouchHelper();

    }

    private void setItemTouchHelper() {
        DefaultItemTouchHelpCallback mCallback = new DefaultItemTouchHelpCallback(new DefaultItemTouchHelpCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int adapterPosition) {
                // 滑动删除的时候，从数据库、数据源移除，并刷新UI
                if (mData != null) {


                    manager.deletePush(mData.get(adapterPosition).get("content"));//删除数据库
                    mData.remove(adapterPosition);
                    mAdapter.notifyItemRemoved(adapterPosition);
                }
            }

            @Override
            public boolean onMove(int srcPosition, int targetPosition) {
//                if (mData != null) {
//
//                    // 更换数据源中的数据Item的位置
//                    Collections.swap(mData, srcPosition, targetPosition);
//                    // 更新UI中的Item的位置，主要是给用户看到交互效果
//                    mAdapter.notifyItemMoved(srcPosition, targetPosition);
//                    return true;
//                }
                return true;
            }
        });
        mCallback.setDragEnable(true);
        mCallback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(mCallback);
        itemTouchHelper.attachToRecyclerView(rv);
    }


//    private void addListener() {
//        mAdapter.setOnItemClickListener(new BaseAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(View view, int position) {
//                Toast.makeText(PushNotificationActivity.this, "click" + position, Toast.LENGTH_SHORT).show();
//            }
//        });
//    }

    class DragSwipeAdapter extends BaseAdapter<Map<String, String>> {


        public DragSwipeAdapter(Context mContext, List<Map<String, String>> mDatas, int mLayoutId) {
            super(mContext, mDatas, mLayoutId);
        }

        @Override
        protected void convert(Context mContext, BaseViewHolder holder, Map<String, String> stringStringMap) {

            holder.setText(R.id.tvdate, stringStringMap.get("date"));
            holder.setText(R.id.tvcontent, stringStringMap.get("content"));

        }
    }

}
