package com.fangyi.businessthrough.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

/**
 * Created by FANGYI on 2016/8/17.
 */

public abstract class BaseFragment extends Fragment implements View.OnClickListener {



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        getSystemData();
        return getSuccessView();
    }

    /**
     * 返回据的fragment填充的具体View
     */
    protected abstract View getSuccessView();

    /**
     * 返回请求服务器的数据
     */
    protected abstract Object requestData();

    public int sysYear;
    public int sysMonth;
    public int sysDay;

    /**
     * 获取系统时间
     */
    private void getSystemData() {
        Calendar calendar = Calendar.getInstance();
        sysYear = calendar.get(Calendar.YEAR);
        sysMonth = calendar.get(Calendar.MONTH);
        sysDay = calendar.get(Calendar.DAY_OF_MONTH);
    }
}
