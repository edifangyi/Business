package com.fangyi.businessthrough.fragment.main;

import android.support.v4.app.Fragment;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.factory.FragmentFactory;
import com.fangyi.businessthrough.base.BasePagerFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class MainFragment extends BasePagerFragment {

    @Override
    protected Fragment setFragment(int position) {
        return FragmentFactory.createForMainTab(position);
    }

    @Override
    protected String[] setTitles() {
        return CommonUtils.getStringArray(R.array.main_tab);
    }

    @Override
    protected void setActionBar() {
        //不写
    }
}
