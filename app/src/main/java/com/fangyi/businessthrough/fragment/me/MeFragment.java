package com.fangyi.businessthrough.fragment.me;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.factory.StartUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Date;
import java.util.HashMap;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.utils.business.DateUtil.calculateDays;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeYYYY_MM_dd;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class MeFragment extends BaseFragment {


    @BindView(R.id.me_convert_percent)
    TextView meConvertPercent;
    @BindView(R.id.me_use_percent)
    TextView meUsePercent;
    @BindView(R.id.me_online_park)
    TextView meOnlinePark;
    @BindView(R.id.me_offline_park)
    TextView meOfflinePark;
    @BindView(R.id.me_oc_image)
    ImageView meOcImage;
    @BindView(R.id.me_operation_center)
    RelativeLayout meOperationCenter;
    @BindView(R.id.me_cc_image)
    ImageView meCcImage;
    @BindView(R.id.me_consume_center)
    RelativeLayout meConsumeCenter;
    @BindView(R.id.me_mc_image)
    ImageView meMcImage;
    @BindView(R.id.me_manager_center)
    RelativeLayout meManagerCenter;
    @BindView(R.id.me_dc_image)
    ImageView meDcImage;
    @BindView(R.id.me_data_center)
    RelativeLayout meDataCenter;
    @BindView(R.id.me_month_income)
    TextView meMonthIncome;

    private Users LoginUsers;
    private HashMap<String, String> hashMap;

    private Handler hander = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    meConvertPercent.setText(hashMap.get("printNum"));
                    meUsePercent.setText(hashMap.get("isUpLoad"));
                    meOfflinePark.setText(calculateDays(PrefUtils.getString(FYApplication.getContext(), "is_judge_overtime_download", getTimeYYYY_MM_dd(new Date()))));
                    meOnlinePark.setText(calculateDays(PrefUtils.getString(FYApplication.getContext(), "is_judge_overtime_download", getTimeYYYY_MM_dd(new Date()))));
                    break;
            }
        }
    };

    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        View view = View.inflate(getActivity(), R.layout.fragment_me, null);
        ButterKnife.bind(this, view);
        getDate();
        setinitItemView();
        setListener();
        executeFixedRate();
        return view;
    }


    private void executeFixedRate() {
        new Thread() {
            @Override
            public void run() {
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
//                        KLog.e("============" + CommonUtils.getTimeYYYY_MM_DD_HH_MM_SS(new Date()));
                        getDate();
                        hander.sendEmptyMessage(0);
                    }
                };

                Timer timer = new Timer();
                long delay = 0;
                long intevalPeriod = 10 * 1000;
                timer.scheduleAtFixedRate(task, delay, intevalPeriod);
            }
        }.start();
    }

    public void getDate() {
        DBBusiness business = new DBBusiness(getContext());
        hashMap = business.notPrintNumber();
        business.closeDB();
    }


    private void setinitItemView() {
        meMonthIncome.setText(LoginUsers.kISName);
        meConvertPercent.setText(hashMap.get("printNum"));
        meUsePercent.setText(hashMap.get("isUpLoad"));
        meOfflinePark.setText(calculateDays(PrefUtils.getString(FYApplication.getContext(), "is_judge_overtime_download", getTimeYYYY_MM_dd(new Date()))));
        meOnlinePark.setText(calculateDays(PrefUtils.getString(FYApplication.getContext(), "is_judge_overtime_download", getTimeYYYY_MM_dd(new Date()))));

    }

    private void setListener() {
        meOperationCenter.setOnClickListener(this);
        meConsumeCenter.setOnClickListener(this);
        meManagerCenter.setOnClickListener(this);
        meDataCenter.setOnClickListener(this);
    }

    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {
        StartUtils.startActivityById(getActivity(), v.getId());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
    }


    /**
     * 获取登陆用户金蝶ID
     *
     * @param users
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUsers(Users users) {
        LoginUsers = users;
    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }


}
