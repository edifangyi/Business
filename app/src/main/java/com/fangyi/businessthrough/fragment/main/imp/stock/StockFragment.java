package com.fangyi.businessthrough.fragment.main.imp.stock;

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

public class StockFragment extends BaseFragment {

    @BindView(R.id.gv_stock)
    GridView gvStock;
    private String[] item;//获取的UI字符串 格式 010 ，0表示隐藏，1表示显示
    private List<String> uiName;//解析后，要显示的名称
    private List<Integer> uiIcon;//解析后，要显示的图标
    private DeviceUI ui;

    private static final String names[] = CommonUtils.getStringArray(R.array.stock_ietm);
    private static final int ids[] = {R.mipmap.ware_30, R.mipmap.ware_a,
            R.mipmap.ware_b};

    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅

        View view = View.inflate(getActivity(), R.layout.fragment_main_stock, null);
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

    private void getUI() {
        item = new String[]{ui.fEntry30, ui.fBillDbFilter, ui.fBillKcFilter};
//        item = new String[]{"1", "1", "1", "1"};
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
        gvStock.setAdapter(new GridViewAdapter(uiName, uiIcon));
//        gvStock.setSelector(new ColorDrawable(Color.TRANSPARENT));
    }


    private void setListener() {
        gvStock.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                boolean isFristLoginDownload = PrefUtils.getBoolean(FYApplication.getContext(), "is_frist_login_download", false);
                String isOvertimeDownload = PrefUtils.getString(FYApplication.getContext(), "is_judge_overtime_download", null);
                boolean b = judgeOverTime(isOvertimeDownload);
                if (true) {
                    if (true) {
                        StartUtils.startActivityByStockItem(getActivity(), position, item);
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
