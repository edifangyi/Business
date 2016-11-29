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
import com.fangyi.businessthrough.events.AddGoodsPromotion;
import com.fangyi.businessthrough.listener.OnItemClickListener;

import com.fangyi.businessthrough.utils.business.CalculationUtils;
import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.numberInteger;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MenuAddPromotionSpecificAdapter extends SwipeMenuAdapter<MenuAddPromotionSpecificAdapter.DefaultViewHolder> {


    private ArrayList<AddGoodsPromotion> addGoodsPromotionArrayList = new ArrayList<>();
    private OnItemClickListener mOnItemClickListener;

    public MenuAddPromotionSpecificAdapter(HashMap<String, AddGoodsPromotion> goodsPromotionInfos) {

        for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : goodsPromotionInfos.entrySet()) {
            this.addGoodsPromotionArrayList.add(stringAddGoodsPromotionEntry.getValue());
        }
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }


    @Override
    public int getItemCount() {
        return addGoodsPromotionArrayList == null ? 0 : addGoodsPromotionArrayList.size() + 1;
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


//            KLog.e("==========" + position + "==========" + addGoodsPromotionArrayList.size());
//            KLog.e("===@@@@@@@@@===" + position + "===@@@@@@@@@@@===" + addGoodsPromotionArrayList.get(0).toString());

        if (position == addGoodsPromotionArrayList.size()) {
            holder.setTvGoodsName(addGoodsPromotionArrayList.get(position - 1).getPromotionName());
            holder.setTvGoodsType("赠品");
            String b = addGoodsPromotionArrayList.get(0).getPromotionCount();
            for (AddGoodsPromotion addGoodsPromotion : addGoodsPromotionArrayList) {
                if (CalculationUtils.compare(addGoodsPromotion.getPromotionCount(), b) > 0) {
                    b = addGoodsPromotion.getPromotionCount();
                }
            }
            if (TextUtils.isEmpty(addGoodsPromotionArrayList.get(position - 1).getNotNum()) && TextUtils.isEmpty(addGoodsPromotionArrayList.get(position - 1).getOutNum())) {
                KLog.e("=======0000000000======" + addGoodsPromotionArrayList.get(position - 1).toString());
                KLog.e("=======1111111111======" + addGoodsPromotionArrayList.get(position - 1).getPromotionCount());
                KLog.e("=======2222222222======" + addGoodsPromotionArrayList.get(position - 1).getPromotionUnit());
                holder.setTvGoodsPromotionCount(numberInteger(Double.parseDouble(b)) + addGoodsPromotionArrayList.get(position - 1).getPromotionUnit());
            } else {
                holder.setTvGoodsPromotionCount(numberInteger(Double.parseDouble(addGoodsPromotionArrayList.get(position - 1).getPromotionCount())) + addGoodsPromotionArrayList.get(position - 1).getPromotionUnit() + " 未送" + numberInteger(Double.parseDouble(addGoodsPromotionArrayList.get(position - 1).getPromotionNotNum())));
            }

            holder.setLLGoodsMessageVisibility(View.GONE);

        } else {
            holder.setTvGoodsName(addGoodsPromotionArrayList.get(position).getGoodsName());
            holder.setTvGoodsType("商品");


            if (TextUtils.isEmpty(addGoodsPromotionArrayList.get(position).getNotNum()) && TextUtils.isEmpty(addGoodsPromotionArrayList.get(position).getOutNum())) {
                holder.setTvGoodsCount(numberInteger(Double.parseDouble(addGoodsPromotionArrayList.get(position).getGoodsNumber())) + addGoodsPromotionArrayList.get(position).getGoodsUnit());
            } else {
                holder.setTvGoodsCount(numberInteger(Double.parseDouble(addGoodsPromotionArrayList.get(position).getGoodsNumber())) + addGoodsPromotionArrayList.get(position).getGoodsUnit() + " 未送" + numberInteger(Double.parseDouble(addGoodsPromotionArrayList.get(position).getNotNum())));
            }

            holder.setTvGoodsPrice(AmountShow(Double.parseDouble(addGoodsPromotionArrayList.get(position).getGoodsPrice())));
            holder.setTvGoodsSum(AmountShow(Double.parseDouble(addGoodsPromotionArrayList.get(position).getGoodsSumPrice())));

            holder.setLLGoodsPromotionVisibility(View.GONE);
        }
        holder.setTvFNameVisibility(View.GONE);


        holder.setOnItemClickListener(mOnItemClickListener);

    }


    static class DefaultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvGoodsName;
        TextView tvGoodsType;
        LinearLayout llGoodsMessage;
        TextView tvGoodsCount;
        TextView tvGoodsPrice;
        TextView tvGoodsSum;
        LinearLayout llGoodsPromotion;
        TextView tvGoodsPromotionCount;
        TextView tvFName;
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

        public void setTvGoodsPromotionCount(String string) {
            this.tvGoodsPromotionCount.setText(string);
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

