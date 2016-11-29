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
import com.fangyi.businessthrough.events.AddGoodsPromotion;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.view.FYLayoutManager;
import com.fangyi.businessthrough.view.NpScrollSwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.application.FYApplication.getContext;


/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuAddPromotionAdapter extends SwipeMenuAdapter<MenuAddPromotionAdapter.DefaultViewHolder> {


    private List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos = new ArrayList<>();//所有商品单

    private OnItemClickListener mOnItemClickListener;

    public MenuAddPromotionAdapter(List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos) {
        this.goodsPromotionInfos = goodsPromotionInfos;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return goodsPromotionInfos == null ? 0 : goodsPromotionInfos.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_goods_promotion, parent, false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {

        HashMap<String, AddGoodsPromotion> ccc = goodsPromotionInfos.get(position);
        for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : ccc.entrySet()) {
            holder.setData(stringAddGoodsPromotionEntry.getValue().getfName());
        }


        if (goodsPromotionInfos.size() != 0) {
            holder.setRvSpecificAdapter(new MenuAddPromotionSpecificAdapter(goodsPromotionInfos.get(position)), goodsPromotionInfos.get(position).size());
        }



        holder.setOnItemClickListener(mOnItemClickListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvTitle;
        NpScrollSwipeMenuRecyclerView rvSpecific;
        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            rvSpecific = (NpScrollSwipeMenuRecyclerView) itemView.findViewById(R.id.srv_specific);
        }

        public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
            this.mOnItemClickListener = onItemClickListener;
        }

        public void setData(String title) {
            this.tvTitle.setText(title);
        }

        public void setRvSpecificAdapter(MenuAddPromotionSpecificAdapter menuAddPromotionSpecificAdapter, int size) {
            FYLayoutManager fyLayoutManager = new FYLayoutManager(getContext(), rvSpecific, size);
            rvSpecific.setLayoutManager(fyLayoutManager);
            rvSpecific.setAdapter(menuAddPromotionSpecificAdapter);
        }

        @Override
        public void onClick(View v) {
            if (mOnItemClickListener != null) {
                mOnItemClickListener.onItemClick(getAdapterPosition());
            }
        }
    }

}
