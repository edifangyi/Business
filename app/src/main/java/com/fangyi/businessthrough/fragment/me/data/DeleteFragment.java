package com.fangyi.businessthrough.fragment.me.data;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.fragment.me.data.task.TaskInfo;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.inflate;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class DeleteFragment extends BaseFragment {


    @BindView(R.id.downloadAll)
    Button downloadAll;

    private ListView lvDownloadList;
    private DownloadDataAdapter adapter;
    private List<TaskInfo> taskInfos;//所有在运行的进程列表


    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    adapter = new DownloadDataAdapter();
                    lvDownloadList.setAdapter(adapter);
                    break;
                case 1:
                    Toast.makeText(getContext(), "当前未连接服务器", Toast.LENGTH_SHORT).show();
                    break;
                case 2:
//                    getViewByPosition(msg.arg1, lvDownloadList);
                    DBManager dbManager = new DBManager(getContext());
                    dbManager.emptyData(taskInfos.get(msg.arg1).getTableName());
                    dbManager.closeDB();
                    break;
                case 3:
//                    getViewByPosition(msg.arg1, lvDownloadList);

                    DBManager manager = new DBManager(getContext());
                    manager.deleteEmptyOrderData(taskInfos.get(msg.arg1).getTableName());
                    manager.closeDB();
                    break;

            }
        }
    };

    public void getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;
        View view;
        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            view = listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            view = listView.getChildAt(childIndex);
        }

        ViewHolder holder = (ViewHolder) view.getTag();
        holder.btnStart = (Button) view.findViewById(R.id.btn_start);
        holder.btnStart.setText("完成");
        holder.btnStart.setEnabled(false);
    }

    @Override
    protected View getSuccessView() {
        View view = inflate(getActivity(), R.layout.fragment_me_download, null);
        lvDownloadList = (ListView) view.findViewById(R.id.lv_download_list);
        ButterKnife.bind(this, view);
        fillData();
        setListener();
        return view;
    }


    /**
     * 编辑列表
     */
    private void fillData() {
        new Thread() {
            @Override
            public void run() {
                taskInfos = getAllTaskInfos();
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    private List<TaskInfo> getAllTaskInfos() {

        List<TaskInfo> taskInfos = new ArrayList<>();


        String[] showName = {"客户表", "商品表", "价格表", "联系人", "业务表", "参数表", "仓库表", "赠品方案", "赠品详细", "单位表",
                "消息列表"
                , "销售订单表", "销售订单分录表"
                , "销售数据库"
                , "采购数据库"};
        String[] tableName = {"Customer", "Goods", "ItemPrice", "Phone", "UserCustomer", "Parameter", "WareHouse", "FreeBill", "FreeBillDetail", "UnitGroup"
                , "PushNotification"
                , "SEOrderMain", "SEOrderDetail"
                , "OrderMain"
                , "PurchaseOrderMain"};

        String[] param = {"0", "0", "0", "0", "0", "0", "0", "0", "0", "0",
                "0",
                "0", "0",
                "1",
                "1"};
        int ids[] = {R.mipmap.customer, R.mipmap.goods, R.mipmap.iterprice, R.mipmap.phone, R.mipmap.usercustomer, R.mipmap.parameter, R.mipmap.warehouse, R.mipmap.freebill, R.mipmap.freebilldetail, R.mipmap.freebilldetail
                , R.mipmap.freebilldetail
                , R.mipmap.customer, R.mipmap.goods
                , R.mipmap.goods
                , R.mipmap.goods};

        for (int i = 0; i < tableName.length; i++) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setShowName(showName[i]);
            taskInfo.setTableName(tableName[i]);
            taskInfo.setParam(param[i]);
            taskInfo.setIcon(ids[i]);

            taskInfos.add(taskInfo);
        }
        return taskInfos;
    }


    private void setListener() {
        downloadAll.setOnClickListener(this);
    }


    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.downloadAll:

                break;
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    public class DownloadDataAdapter extends BaseAdapter {

        private List<String> flag = new ArrayList<>();

        @Override
        public int getCount() {
            return taskInfos.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view;
            final Holder holder;
            if (convertView != null && convertView instanceof LinearLayout) {
                view = convertView;
                holder = (Holder) view.getTag();
            } else {
                view = View.inflate(getContext(), R.layout.fragment_me_download_item, null);
                holder = new Holder();
                holder.ivDownImg = (ImageView) view.findViewById(R.id.iv_down_img);
                holder.tvDownName = (TextView) view.findViewById(R.id.tv_down_name);
                holder.btnStart = (Button) view.findViewById(R.id.btn_start);
                view.setTag(holder);
            }
            holder.ivDownImg.setImageResource(taskInfos.get(position).getIcon());
            holder.tvDownName.setText(taskInfos.get(position).getShowName());
            holder.btnStart.setText("清空");

            holder.btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (flag.contains(position + "")) {
                        holder.btnStart.setEnabled(true);
                    } else {
                        flag.add(position + "");
                        holder.btnStart.setText("完成");
                        holder.btnStart.setEnabled(false);
                        Message message = new Message();
                        message.arg1 = position;
                        if ("0".equals(taskInfos.get(position).getParam())) {
                            message.what = 2;
                            handler.sendMessage(message);
                        } else if ("1".equals(taskInfos.get(position).getParam())) {
                            message.what = 3;
                            handler.sendMessage(message);
                        }
                    }


                }
            });

            if (flag.contains(position + "")) {
                holder.btnStart.setEnabled(false);
            } else {
                holder.btnStart.setEnabled(true);
            }

            return view;

//            ViewHolder holder = null;
//            if (convertView == null) {
//                convertView = View.inflate(getContext(), R.layout.fragment_me_download_item, null);
//                holder = new ViewHolder(taskInfos, handler, flag);
//                holder.ivDownImg = (ImageView) convertView.findViewById(R.id.iv_down_img);
//                holder.tvDownName = (TextView) convertView.findViewById(R.id.tv_down_name);
//                holder.btnStart = (Button) convertView.findViewById(R.id.btn_start);
//                holder.btnStart.setOnClickListener(holder);
//                convertView.setTag(holder);
//            } else {
//                holder = (ViewHolder) convertView.getTag();
//            }
//
//            if (flag.contains(position + "")) {
//                holder.btnStart.setEnabled(false);
//            } else {
//                holder.btnStart.setEnabled(true);
//            }
//
//            holder.ivDownImg.setImageResource(taskInfos.get(position).getIcon());
//            holder.tvDownName.setText(taskInfos.get(position).getShowName());
//            holder.btnStart.setText("清空");
//            holder.setPosition(position);
//
//            return convertView;
        }

    }

    static class Holder {

        ImageView ivDownImg;
        TextView tvDownName;
        Button btnStart;

    }


    static class ViewHolder implements View.OnClickListener {


        ImageView ivDownImg;
        TextView tvDownName;
        Button btnStart;

        int position;
        private Handler handler;
        private List<TaskInfo> taskInfos;
        private List<String> flag = new ArrayList<>();

        public ViewHolder(List<TaskInfo> taskInfos, Handler handler, List<String> flag) {
            this.handler = handler;
            this.taskInfos = taskInfos;
            this.flag = flag;
        }

        public void setPosition(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.btn_start:
                    if (flag.contains(position + "")) {
                        btnStart.setText("完成");
                        btnStart.setEnabled(false);
                        Message message = new Message();

                        message.arg1 = position;
                        if ("0".equals(taskInfos.get(position).getParam())) {
                            message.what = 2;
                            handler.sendMessage(message);
                        } else if ("1".equals(taskInfos.get(position).getParam())) {
                            message.what = 3;
                            handler.sendMessage(message);
                        }
                        break;
                    }

            }
        }
    }

}
