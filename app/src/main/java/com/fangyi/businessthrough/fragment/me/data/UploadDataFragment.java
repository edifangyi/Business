package com.fangyi.businessthrough.fragment.me.data;

import android.view.View;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.base.BaseFragment;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class UploadDataFragment extends BaseFragment {


    @Override
    protected View getSuccessView() {
        View view = View.inflate(getActivity(), R.layout.fragment_me_download, null);

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
