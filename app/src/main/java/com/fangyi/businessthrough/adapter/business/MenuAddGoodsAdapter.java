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
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.List;

import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.numberInteger;


/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuAddGoodsAdapter extends SwipeMenuAdapter<MenuAddGoodsAdapter.DefaultViewHolder> {


    private List<AddGoodsMessage> goodsInfos;
    private OnItemClickListener mOnItemClickListener;

    public MenuAddGoodsAdapter(List<AddGoodsMessage> goodsInfos) {
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

        AddGoodsMessage message = goodsInfos.get(position);

        if ("0".equals(message.getGoodsType())) {

            holder.setTvGoodsType("赠品");
            holder.setTvGoodsName(message.getGoodsName());
            if (TextUtils.isEmpty(message.getNotNum()) && TextUtils.isEmpty(message.getOutNum())) {
                holder.setTvGoodsPromotionCount(numberInteger(Double.parseDouble(message.getGoodsNum())) + message.getGoodsUom());
            } else {
                holder.setTvGoodsPromotionCount(numberInteger(Double.parseDouble(message.getGoodsNum() + message.getGoodsUom())) + " 未送" + message.getNotNum());
            }
            holder.setLLGoodsMessageVisibility(View.GONE);

        } else if ("1".equals(message.getGoodsType())) {
            holder.setTvGoodsType("商品");
            holder.setTvGoodsName(message.getGoodsName());

            if (TextUtils.isEmpty(message.getNotNum()) && TextUtils.isEmpty(message.getOutNum())) {
                holder.setTvGoodsCount(numberInteger(Double.parseDouble(message.getGoodsNum())) + message.getGoodsUom());
            } else {
                holder.setTvGoodsCount(numberInteger(Double.parseDouble(message.getGoodsNum())) + message.getGoodsUom() + " 未送" + numberInteger(Double.parseDouble(message.getNotNum())));
            }

            holder.setTvGoodsPrice(AmountShow(Double.parseDouble(message.getGoodsPrice())));
            holder.setTvGoodsSum(AmountShow(AmountShowBug(message.getGoodsSum())));
            holder.setLLGoodsPromotionVisibility(View.GONE);
        } else if ("4".equals(message.getGoodsType())) {
            holder.setTvGoodsType("商品");
            holder.setTvGoodsName(message.getGoodsName());
            if (TextUtils.isEmpty(message.getNotNum()) && TextUtils.isEmpty(message.getOutNum())) {
                holder.setTvGoodsPromotionCount(numberInteger(Double.parseDouble(message.getGoodsNum())) + message.getGoodsUom());
            } else {
                holder.setTvGoodsPromotionCount(numberInteger(Double.parseDouble(message.getGoodsNum() + message.getGoodsUom())) + " 未送" + message.getNotNum());
            }
            holder.setLLGoodsMessageVisibility(View.GONE);
        }
        holder.setTvFNameVisibility(View.GONE);

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

        public void setTvFNameVisibility(int visible) {
            this.tvFName.setVisibility(visible);
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
