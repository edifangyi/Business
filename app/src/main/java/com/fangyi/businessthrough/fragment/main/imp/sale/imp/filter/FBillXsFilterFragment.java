package com.fangyi.businessthrough.fragment.main.imp.sale.imp.filter;

import android.support.v4.app.Fragment;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.system.ClickSaleItemActivity;
import com.fangyi.businessthrough.factory.FragmentFactory;
import com.fangyi.businessthrough.base.BaseNoScrollViewPagerFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class FBillXsFilterFragment extends BaseNoScrollViewPagerFragment {


    @Override
    protected Fragment setFragment(int position) {
        return FragmentFactory.createForFBillXsFilterTab(position);

    }

    @Override
    protected String[] setTitles() {
        return CommonUtils.getStringArray(R.array.fbillxsfilter_ietm);
    }

    @Override
    protected void setActionBar() {
        ClickSaleItemActivity activity = (ClickSaleItemActivity) getActivity();
        activity.tvTitle.setText("销售查询");
    }
}
