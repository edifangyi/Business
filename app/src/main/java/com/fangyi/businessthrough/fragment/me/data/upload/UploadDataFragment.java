package com.fangyi.businessthrough.fragment.me.data.upload;

import android.support.v4.app.Fragment;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.system.ClickButtonActivity;
import com.fangyi.businessthrough.base.BasePagerFragment;
import com.fangyi.businessthrough.factory.FragmentFactory;
import com.fangyi.businessthrough.utils.system.CommonUtils;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class UploadDataFragment extends BasePagerFragment {

    @Override
    protected Fragment setFragment(int position) {
        return FragmentFactory.createForUploadTab(position);

    }

    @Override
    protected String[] setTitles() {
        return CommonUtils.getStringArray(R.array.upload_ietm);
    }

    @Override
    protected void setActionBar() {
        ClickButtonActivity activity = (ClickButtonActivity) getActivity();
        activity.tvTitle.setText("单据上传");
    }
}
