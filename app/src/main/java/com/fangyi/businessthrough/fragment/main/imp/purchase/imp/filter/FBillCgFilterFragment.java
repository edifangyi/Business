package com.fangyi.businessthrough.fragment.main.imp.purchase.imp.filter;

import android.support.v4.app.Fragment;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.system.ClickPurchaseItemActivity;
import com.fangyi.businessthrough.factory.FragmentFactory;
import com.fangyi.businessthrough.base.BaseNoScrollViewPagerFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class FBillCgFilterFragment extends BaseNoScrollViewPagerFragment {

    @Override
    protected Fragment setFragment(int position) {
        return FragmentFactory.createForFBillCgFilterTab(position);

    }

    @Override
    protected String[] setTitles() {
        return CommonUtils.getStringArray(R.array.fbillcgFilter_ietm);
    }

    @Override
    protected void setActionBar() {
        ClickPurchaseItemActivity activity = (ClickPurchaseItemActivity) getActivity();
        activity.tvTitle.setText("采购查询");
    }
}
