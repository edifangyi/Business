package com.fangyi.businessthrough.adapter.system;

import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangyi.businessthrough.R;

import java.util.List;

import static com.fangyi.businessthrough.application.FYApplication.getContext;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class GridViewAdapter extends BaseAdapter {
    private List<String> uiName;//解析后，要显示的名称
    private List<Integer> uiIcon;//解析后，要显示的图标

    public GridViewAdapter(List<String> uiName, List<Integer> uiIcon) {
        this.uiName = uiName;
        this.uiIcon = uiIcon;
    }

    //返回长度
    @Override
    public int getCount() {
        return uiName.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = View.inflate(getContext(), R.layout.activity_main_item, null);
        ImageView ivIcon = (ImageView) view.findViewById(R.id.iv_icon);
        TextView tvName = (TextView) view.findViewById(R.id.tv_name);
        ivIcon.setImageResource(uiIcon.get(position));
        tvName.setText(uiName.get(position));

        return view;
    }
}
