package com.fangyi.businessthrough.fragment.main.imp.stock.imp;

import android.graphics.Color;
import android.view.View;
import android.widget.TextView;

import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class FBillKcFilterFragment extends BaseFragment {
    @Override
    protected View getSuccessView() {
        CommonUtils.setStockTitle(getActivity() ,"及时库存查询");
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
