package com.fangyi.businessthrough.fragment.main.imp.purchase;

import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.adapter.system.GridViewAdapter;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.bean.system.DeviceUI;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.factory.StartUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.utils.business.DateUtil.judgeOverTime;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class PurchaseFragment extends BaseFragment {

    @BindView(R.id.gv_purchase)
    GridView gvPurchase;

    private String[] item;//获取的UI字符串 格式 010 ，0表示隐藏，1表示显示
    private List<String> uiName;//解析后，要显示的名称
    private List<Integer> uiIcon;//解析后，要显示的图标
    private static final String names[] = CommonUtils.getStringArray(R.array.purchase_ietm);

    private static final int ids[] = {R.mipmap.purchase_22, R.mipmap.purchase_23,
            R.mipmap.purchase_24, R.mipmap.purchase_25, R.mipmap.purchase_26, R.mipmap.purchase_27};
    private DeviceUI ui;

    //    private static final int ids[] = {R.mipmap.purchase_request, R.mipmap.purchase_into_ware,
//            R.mipmap.purchase_return_notification, R.mipmap.purchase_order,
//            R.mipmap.purchase_search, R.mipmap.purchase_sum};

    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        View view = View.inflate(getActivity(), R.layout.fragment_main_purchase, null);
        ButterKnife.bind(this, view);
        getUI();
        init();
        setListener();
        return view;
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUserUI(DeviceUI deviceUI) {
        ui = deviceUI;
    }

    public void getUI() {
        item = new String[]{ui.fEntry22, ui.fEntry23, ui.fEntry24, ui.fEntry25, ui.fBilCgFilter, ui.fBillCgSum};
        uiName = new ArrayList<>();
        uiIcon = new ArrayList<>();
        for (int i = 0; i < item.length; i++) {
            if ("1".equals(item[i])) {
                uiName.add(names[i]);
                uiIcon.add(ids[i]);
            }
        }
    }


    private void init() {
        gvPurchase.setAdapter(new GridViewAdapter(uiName, uiIcon));
//        gvPurchase.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    private void setListener() {

        gvPurchase.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isFristLoginDownload = PrefUtils.getBoolean(FYApplication.getContext(), "is_frist_login_download", false);

                String isOvertimeDownload = PrefUtils.getString(FYApplication.getContext(), "is_judge_overtime_download", null);
                boolean b = judgeOverTime(isOvertimeDownload);
                if (true) {
                    if (true) {
                        StartUtils.startActivityByPurchaseItem(getActivity(), position, item);
                    } else {
                        Toast.makeText(getContext(), "超时，请更新数据库", Toast.LENGTH_SHORT).show();
                    }

                } else {
                    Toast.makeText(getContext(), "请先下载数据", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
