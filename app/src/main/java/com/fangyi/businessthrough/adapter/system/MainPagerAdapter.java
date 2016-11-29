package com.fangyi.businessthrough.adapter.system;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.fangyi.businessthrough.factory.FragmentFactory;


/**
 * Created by FANGYI on 2016/8/17.
 */

public class MainPagerAdapter extends FragmentStatePagerAdapter {

    public MainPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return FragmentFactory.createForMain(position);
    }

    @Override
    public int getCount() {
        return 3;
    }
}
