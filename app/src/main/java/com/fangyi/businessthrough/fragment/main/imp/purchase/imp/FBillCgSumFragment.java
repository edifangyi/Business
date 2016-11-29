package com.fangyi.businessthrough.fragment.main.imp.purchase.imp;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class FBillCgSumFragment extends BaseFragment {
    @Override
    protected View getSuccessView() {
        CommonUtils.setPurchaseTitle(getActivity() ,"采购总汇");
        TextView view = new TextView(getActivity());
        view.setText("wo");
        view.setTextColor(Color.RED);
        view.setTextSize(50);
        return view;
    }

    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }
}
