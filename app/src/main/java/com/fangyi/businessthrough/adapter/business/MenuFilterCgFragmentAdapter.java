/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fangyi.businessthrough.adapter.business;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuFilterCgFragmentAdapter extends SwipeMenuAdapter<MenuFilterCgFragmentAdapter.DefaultViewHolder> {


    private List<PrintPurchaseOrderMain> orderMainList;
    private OnItemClickListener mOnItemClickListener;

    public MenuFilterCgFragmentAdapter(List<PrintPurchaseOrderMain> orderMainList) {
        this.orderMainList = orderMainList;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return orderMainList == null ? 0 : orderMainList.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_filter, parent, false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {

        PrintPurchaseOrderMain orderMain = orderMainList.get(position);


        if ("4".equals(orderMain.businessType)) {
            holder.setTvName("业务员：" + orderMain.fyFRequester);

        } else {
            holder.setTvName(orderMain.fyFSupplier);
            holder.setTvWarehouse(orderMain.fyFDCStock);
            holder.setTvAllmoney(orderMain.allMoney);
        }

        holder.setTvTime(orderMain.fyFFetchDayV);
        if ("0".equals(orderMain.isUpLoad)) {
            holder.setTvIsupload("未上传");
            holder.setTvIsuploadTextColor(R.color.colorAccent);
        } else {
            holder.setTvIsupload("已上传");
            holder.setTvIsuploadTextColor(R.color.text_green);
        }
        holder.setTvPrintNum("打印次数：" + orderMain.printNum);


        holder.setOnItemClickListener(mOnItemClickListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tvWarehouse;
        private TextView tvAllmoney;
        private TextView tvTime;
        private TextView tvIsupload;
        private TextView tvPrintNum;

        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvWarehouse = (TextView) itemView.findViewById(R.id.tv_warehouse);
            tvAllmoney = (TextView) itemView.findViewById(R.id.tv_allmoney);
            tvTime = (TextView) itemView.findViewById(R.id.tv_time);

            tvIsupload = (TextView) itemView.findViewById(R.id.tv_isupload);
            tvPrintNum = (TextView) itemView.findViewById(R.id.tv_printnum);

        }

        public void setTvName(String string) {
            this.tvName.setText(string);
        }


        public void setTvWarehouse(String string) {
            this.tvWarehouse.setText(string);
        }


        public void setTvAllmoney(String string) {
            this.tvAllmoney.setText(string);
        }


        public void setTvTime(String string) {
            this.tvTime.setText(string);
        }


        public void setTvIsupload(String string) {
            this.tvIsupload.setText(string);
        }

        public void setTvIsuploadTextColor(int text_green) {
            this.tvIsupload.setTextColor(CommonUtils.getColor(text_green));
        }

        public void setTvPrintNum(String string) {
            this.tvPrintNum.setText(string);
        }


        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
