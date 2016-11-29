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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.bean.business.Goods;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuOrderGoodsAdapter extends SwipeMenuAdapter<MenuOrderGoodsAdapter.DefaultViewHolder> {


    private List<Goods> goodsInfos;
    private OnItemClickListener mOnItemClickListener;

    public MenuOrderGoodsAdapter(List<Goods> goodsInfos) {
        this.goodsInfos = goodsInfos;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    @Override
    public int getItemCount() {
        return goodsInfos == null ? 0 : goodsInfos.size();
    }

    @Override
    public View onCreateContentView(ViewGroup parent, int viewType) {
        return LayoutInflater.from(parent.getContext()).inflate(R.layout.item_add_goods_promotion_specific, parent, false);
    }

    @Override
    public DefaultViewHolder onCompatCreateViewHolder(View realContentView, int viewType) {
        return new DefaultViewHolder(realContentView);
    }

    @Override
    public void onBindViewHolder(DefaultViewHolder holder, int position) {

        Goods message = goodsInfos.get(position);
        holder.setTvGoodsName(message.goodsName);
        holder.setTvGoodsType(message.goodsType);


        if (!"商品".equals(message.goodsType)) {
            holder.setLLGoodsMessageVisibility(View.GONE);

            holder.setTvGoodsPromotionCount(message.orderNum + message.unit);
        } else {
            holder.setLLGoodsPromotionVisibility(View.GONE);
            holder.setTvGoodsCount(message.orderNum + message.unit);
            holder.setTvGoodsPrice(message.orderPrice);
            holder.setTvGoodsSum(message.orderCounter);
        }

        holder.setTvFName(message.fName);

        holder.setOnItemClickListener(mOnItemClickListener);
    }

    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvGoodsName;
        LinearLayout llGoodsMessage;
        TextView tvGoodsCount;
        TextView tvGoodsType;
        TextView tvGoodsPrice;
        TextView tvGoodsSum;
        TextView tvFName;
        LinearLayout llGoodsPromotion;
        TextView tvGoodsPromotionCount;

        OnItemClickListener mOnItemClickListener;

        public DefaultViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);

            tvGoodsName = (TextView) itemView.findViewById(R.id.tv_goods_name);
            tvGoodsType = (TextView) itemView.findViewById(R.id.tv_goods_type);
            llGoodsMessage = (LinearLayout) itemView.findViewById(R.id.ll_goods_message);
            tvGoodsCount = (TextView) itemView.findViewById(R.id.tv_goods_count);
            tvGoodsPrice = (TextView) itemView.findViewById(R.id.tv_goods_price);
            tvGoodsSum = (TextView) itemView.findViewById(R.id.tv_goods_sum);
            llGoodsPromotion = (LinearLayout) itemView.findViewById(R.id.ll_goods_promotion);
            tvGoodsPromotionCount = (TextView) itemView.findViewById(R.id.tv_goods_promotion_count);
            tvFName = (TextView) itemView.findViewById(R.id.tv_fName);

        }

        public void setLLGoodsMessageVisibility(int visible) {
            this.llGoodsMessage.setVisibility(visible);
        }

        public void setLLGoodsPromotionVisibility(int visible) {
            this.llGoodsPromotion.setVisibility(visible);
        }

        public void setTvGoodsPromotionCount(String string) {
            this.tvGoodsPromotionCount.setText(string);
        }


        public void setTvFName(String string) {
            this.tvFName.setText(string);
        }


        public void setTvGoodsType(String string) {
            this.tvGoodsType.setText(string);
        }


        public void setTvGoodsName(String string) {
            this.tvGoodsName.setText(string);
        }


        public void setTvGoodsCount(String string) {
            this.tvGoodsCount.setText(string);
        }


        public void setTvGoodsSum(String string) {
            this.tvGoodsSum.setText(string);
        }


        public void setTvGoodsPrice(String string) {
            this.tvGoodsPrice.setText(string);
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
