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
import com.fangyi.businessthrough.bean.business.Summarizing;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuSumFragmentAdapter extends SwipeMenuAdapter<MenuSumFragmentAdapter.DefaultViewHolder> {


    private List<Summarizing> orderMainList;
    private OnItemClickListener mOnItemClickListener;

    public MenuSumFragmentAdapter(List<Summarizing> orderMainList) {
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
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_sum, parent, false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {

        Summarizing orderMain = orderMainList.get(position);

        holder.setTvName(orderMain.getCustomerName() + " "+ orderMain.getGoodsName());
        holder.setTvStandard(orderMain.getStandard());
        holder.setTvAllmoney(orderMain.getAllMoney());

        holder.setOnItemClickListener(mOnItemClickListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView tvName;
        private TextView tvStandard;
        private TextView tvAllmoney;

        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvName = (TextView) itemView.findViewById(R.id.tv_name);
            tvStandard = (TextView) itemView.findViewById(R.id.tv_standard);
            tvAllmoney = (TextView) itemView.findViewById(R.id.tv_allmoney);


        }

        public void setTvStandard(String string) {
            this.tvStandard.setText(string);
        }


        public void setTvName(String string) {
            this.tvName.setText(string);
        }

        public void setTvAllmoney(String string) {
            this.tvAllmoney.setText(string);
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
