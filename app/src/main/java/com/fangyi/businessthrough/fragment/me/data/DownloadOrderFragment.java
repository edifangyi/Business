package com.fangyi.businessthrough.fragment.me.data;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.dao.ProtocolUtil;
import com.fangyi.businessthrough.fragment.me.data.task.TaskInfo;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WSReturnParam;
import com.fangyi.businessthrough.http.WebService;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeYYYY_MM_dd;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class DownloadOrderFragment extends BaseFragment {


    @BindView(R.id.downloadAll)
    Button downloadAll;

    private ListView lvDownloadList;
    private DownloadDataAdapter adapter;
    private List<TaskInfo> taskInfos;//所有在运行的进程列表
    private User LoginUser;
    private String ip;//IP地址
    private ProgressDialog dialog;

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
                    startDownload(taskInfos.get(msg.arg1), msg.arg1);
                    break;
                case 3:
                    dialog.dismiss();
                    KLog.e("====11232=========" + msg.arg1);
                    View view = getViewByPosition(msg.arg1, lvDownloadList);
                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.btnStart = (Button) view.findViewById(R.id.btn_start);
                    holder.btnStart.setText("下载完成");
                    break;

            }
        }
    };
    private boolean isDownload;

    public View getViewByPosition(int pos, ListView listView) {
        final int firstListItemPosition = listView.getFirstVisiblePosition();
        final int lastListItemPosition = firstListItemPosition + listView.getChildCount() - 1;

        if (pos < firstListItemPosition || pos > lastListItemPosition) {
            return listView.getAdapter().getView(pos, null, listView);
        } else {
            final int childIndex = pos - firstListItemPosition;
            return listView.getChildAt(childIndex);
        }
    }


    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        View view = View.inflate(getActivity(), R.layout.fragment_me_download, null);
        lvDownloadList = (ListView) view.findViewById(R.id.lv_download_list);

        //判断网络是否可用
        if (NetConnectionUtil.isNetworkAvailable(getContext())) {
            //判断服务器是否通信
            if (NetConnectionUtil.pingService(NetConnectionUtil.getIPAddress(PrefUtils.getString(getContext(), "is_service_address", null)))) {

                isDownload = true;
            }
        } else {
            isDownload = false;


        }

        ButterKnife.bind(this, view);
        fillData();
        setListener();
        return view;
    }

    /**
     * 获取传来的当前用户信息
     *
     * @param user
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUserBill(User user) {
        LoginUser = user;
    }


    /**
     * 编辑列表
     */
    private void fillData() {
        new Thread() {
            @Override
            public void run() {
                taskInfos = getAllTaskInfos();
                KLog.e("==00000000===" + taskInfos.size());
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    private List<TaskInfo> getAllTaskInfos() {

        List<TaskInfo> taskInfos = new ArrayList<>();
        String[] showName = {"订单表", "订单分录表"};
        String[] tableName = {"Seobill", "Seobillentry"};
        String userSysID = LoginUser.userSysID;
        String[] param = {userSysID + ",1999-01-01,2099-01-01", userSysID + ",1999-01-01,2099-01-01"};
        int ids[] = {R.mipmap.customer, R.mipmap.goods};

        for (int i = 0; i < tableName.length; i++) {
            TaskInfo taskInfo = new TaskInfo();
            taskInfo.setShowName(showName[i]);
            taskInfo.setTableName(tableName[i]);
            taskInfo.setUserSysID(userSysID);
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


    /**
     * 下载数据
     *
     * @param taskInfo
     * @param position
     */
    public void startDownload(final TaskInfo taskInfo, final int position) {

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("正在下载...");
        dialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);//样式更改为水平，带进度条的那种
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();



        new Thread() {
            @Override
            public void run() {
                WSReturnParam wsParam = new WSReturnParam();
                wsParam.result = 0; // 执行结果
                wsParam.totalPg = 0;// 总共还需执行页数
                Integer currentPg = 0; // 当前执行页数
                String tableName = taskInfo.getTableName(); //表名
                String param = taskInfo.getParam();

                DBManager dbManager = new DBManager(FYApplication.getContext());
                final String WSserviceAddress = PrefUtils.getString(getContext(), "is_service_address", null);
                WebService webService = new WebService(WSserviceAddress);
                dbManager.deleteDownloadData(tableName);

                String protoStr = webService.getData(wsParam, currentPg, tableName, param);
                List<String> customer = ProtocolUtil.getInsertSQL(tableName, protoStr);

                for (String s : customer) {
                    KLog.e("===111111111===" + s);
                }


                dbManager.insertDownloadData(customer);

                final int a = wsParam.totalPg;
                dialog.setMax(a);
                int i = 0;

                while (wsParam.totalPg > 1) {

                    String protoStrs = webService.getData(wsParam, currentPg, tableName, param);
                    List<String> customers = ProtocolUtil.getInsertSQL(tableName, protoStrs);


                    for (String s : customers) {
                        KLog.e("====22222222==" + s);
                    }


                    dbManager.insertDownloadData(customers);
                    dialog.setProgress(i++);
                }
                dbManager.closeDB();


                PrefUtils.setBoolean(FYApplication.getContext(), "is_frist_login_download", true);
                String time = getTimeYYYY_MM_dd(new Date());
                PrefUtils.setString(FYApplication.getContext(), "is_judge_overtime_download", time);


                Message message = new Message();
                message.what = 3;
                message.arg1 = position;
                handler.sendMessage(message);

            }
        }.start();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }

    public class DownloadDataAdapter extends BaseAdapter {

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
            final ViewHolder holder;
            if (convertView != null) {
                view = convertView;
                holder = (ViewHolder) view.getTag();
            } else {
                view = View.inflate(getContext(), R.layout.fragment_me_download_item, null);
                holder = new ViewHolder();
                holder.ivDownImg = (ImageView) view.findViewById(R.id.iv_down_img);
                holder.tvDownName = (TextView) view.findViewById(R.id.tv_down_name);
                holder.btnStart = (Button) view.findViewById(R.id.btn_start);
                view.setTag(holder);
            }
            holder.ivDownImg.setImageResource(taskInfos.get(position).getIcon());
            holder.tvDownName.setText(taskInfos.get(position).getShowName());

            //判断网络是否可用
            if (isDownload) {
                holder.btnStart.setEnabled(true);
            } else {
                holder.btnStart.setEnabled(false);
                holder.btnStart.setText("没有服务");
            }


            //通信
            holder.btnStart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    holder.btnStart.setText("正在下载");
                    holder.btnStart.setEnabled(false);
                    Message message = new Message();
                    message.arg1 = position;
                    message.what = 2;
                    handler.sendMessage(message);
                }
            });
            return view;
        }

    }

    static class ViewHolder {
        ImageView ivDownImg;
        TextView tvDownName;
        Button btnStart;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
