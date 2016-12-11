package com.fangyi.businessthrough.fragment.me.data.upload.imp;

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
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.fragment.me.data.task.TaskInfo;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WebUploadService;
import com.fangyi.businessthrough.utils.system.PrefUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.socks.library.KLog.e;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class UploadItemFragment extends BaseFragment {


    @BindView(R.id.downloadAll)
    Button downloadAll;

    private ListView lvDownloadList;
    private DownloadDataAdapter adapter;
    private List<TaskInfo> taskInfos;//所有在运行的进程列表
    private User LoginUser;
    private ProgressDialog dialog;
    private String businessType;


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

                    View view = getViewByPosition(msg.arg1, lvDownloadList);
                    ViewHolder holder = (ViewHolder) view.getTag();
                    holder.btnStart = (Button) view.findViewById(R.id.btn_start);
                    if (msg.arg2 == 0) {
                        holder.btnStart.setText("上传失败");
                    } else if (msg.arg2 == 1) {
                        holder.btnStart.setText("上传完成");
                    }

                    break;

            }
        }
    };
    private boolean isDownload;

    public UploadItemFragment() {

    }

    public static UploadItemFragment getInstance(String s) {
        UploadItemFragment itemFragment = new UploadItemFragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessType", s);
        itemFragment.setArguments(bundle);
        return itemFragment;
    }

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
        ButterKnife.bind(this, view);

        Bundle args = getArguments();
        businessType = args.getString("businessType");

        setNetwork(view);
        fillData();
        setListener();
        return view;
    }

    /**
     * 判断网络环境
     *
     * @param view
     */
    private void setNetwork(View view) {
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
                e("==00000000===" + taskInfos.size());
                handler.sendEmptyMessage(0);
            }
        }.start();
    }


    private List<TaskInfo> getAllTaskInfos() {

        List<TaskInfo> taskInfos = new ArrayList<>();

        String[] showName = null;
        String[] tableName = null;
        String userSysID = null;
        String[] param = null;
        int[] ids = null;
        if ("0".equals(businessType)) {
            showName = new String[]{"销售订单", "销售出库", "退货通知", "销售退货"};
            tableName = new String[]{"1", "2", "3", "0"};
            userSysID = LoginUser.userSysID;
            param = new String[]{"销售订单", "销售出库", "退货通知", "销售退货"};

            ids = new int[]{R.mipmap.sale_26, R.mipmap.sale_27, R.mipmap.sale_28,
                    R.mipmap.sale_29};


        } else if ("1".equals(businessType)) {

            showName = new String[]{"采购申请", "采购入库", "退货通知", "采购退货"};
            tableName = new String[]{"4", "5", "6", "7"};
            userSysID = LoginUser.userSysID;
            param = new String[]{"销售订单", "销售出库", "退货通知", "销售退货"};

            ids = new int[]{R.mipmap.purchase_22, R.mipmap.purchase_23, R.mipmap.purchase_24,
                    R.mipmap.purchase_25};

        } else if ("2".equals(businessType)) {

            showName = new String[]{"销售单据"};
            tableName = new String[]{"8"};
            userSysID = LoginUser.userSysID;
            param = new String[]{"销售订单"};
            ids = new int[]{R.mipmap.ware_30};
        }

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


        String result = WebUploadService.uploadDateService(getActivity(), taskInfo.getTableName());

        int state;
        if ("上传失败".equals(result)) {
            dialog.dismiss();
            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
            state = 0;
        } else {
            dialog.dismiss();
            Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
            state = 1;
        }

        Message message = new Message();
        message.what = 3;
        message.arg1 = position;
        message.arg2 = state;
        handler.sendMessage(message);
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
            holder.btnStart.setText("上传");
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
                    holder.btnStart.setText("正在上传");
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
