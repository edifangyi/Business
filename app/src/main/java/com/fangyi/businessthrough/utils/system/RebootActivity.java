package com.fangyi.businessthrough.utils.system;

import android.content.Intent;
import android.support.v4.app.FragmentActivity;

/**
 * Created by FANGYI on 2016/11/9.
 */

public class RebootActivity {

    public static void reboot(FragmentActivity activity) {
        Intent intent = activity.getIntent();
        activity.overridePendingTransition(0, 0);
        activity.finish();
        activity.overridePendingTransition(0, 0);
        activity.startActivity(intent);
    }
}
