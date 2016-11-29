package com.fangyi.businessthrough.bean.search;

import java.util.Comparator;

/**
 * Created by FANGYI on 2016/9/21.
 */

public class PinyinComparator implements Comparator<GroupMemberBean> {

    public int compare(GroupMemberBean o1, GroupMemberBean o2) {
        if (o1.getSortLetters().equals("@")
                || o2.getSortLetters().equals("#")) {
            return -1;
        } else if (o1.getSortLetters().equals("#")
                || o2.getSortLetters().equals("@")) {
            return 1;
        } else {
            return o1.getSortLetters().compareTo(o2.getSortLetters());
        }
    }

}