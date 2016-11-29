package com.fangyi.businessthrough.adapter.business;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.AddGoodsPromotionActivity;
import com.fangyi.businessthrough.parameter.SystemFieldValues;
import com.fangyi.businessthrough.events.GoodsPromotionChild;
import com.fangyi.businessthrough.events.GoodsUnitGroup;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.SerializableMap;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.utils.business.CalculationUtils.mulss;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowPolishing;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.StringFilter;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getInputIsEmpty;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.numberInteger;


/**
 * Created by FANGYI on 2016/10/5.
 */


public class GoodsPromotionAdapter extends BaseExpandableListAdapter {

    Map<String, String> map = SystemFieldValues.getSystemValues();


    private ArrayList<ArrayList<GoodsPromotionChild>> goodsPromotionGroupList;//促销方案组合
    private ArrayList<GoodsUnitGroup> goodsUnitGroupList;//单位list
    private AddGoodsPromotionActivity addGoodsPromotionActivity;
    private String fChooseAmount;//允许修改单价金额
    private String fDCStockD;//仓库页面
    private String fStockPosition;//仓库

    private Map<String, String> mapWareHouse;//仓库
    private String changePrice;
    private String changeNum;
    private String price;


    public GoodsPromotionAdapter(AddGoodsPromotionActivity addGoodsPromotionActivity, String fChooseAmount, String fDCStockD, String fStockPosition, Map<String, String> mapWareHouse, ArrayList<ArrayList<GoodsPromotionChild>> goodsPromotionGroupList, ArrayList<GoodsUnitGroup> goodsUnitGroupList) {
        this.addGoodsPromotionActivity = addGoodsPromotionActivity;

        this.fChooseAmount = fChooseAmount;
        this.fDCStockD = fDCStockD;
        this.fStockPosition = fStockPosition;
        this.mapWareHouse = mapWareHouse;

        this.goodsPromotionGroupList = goodsPromotionGroupList;
        this.goodsUnitGroupList = goodsUnitGroupList;
    }


    //设计几组
    @Override
    public int getGroupCount() {
        return goodsPromotionGroupList.size();
    }

    //一组里几个孩子
    @Override
    public int getChildrenCount(int groupPosition) {
        return goodsPromotionGroupList.get(groupPosition).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return null;
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return null;
    }

    //分组的id
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    //孩子的id
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    //是否准许id相同
    @Override
    public boolean hasStableIds() {
        return false;
    }

    //组的样式
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        View view;
        ViewGroupHolder holder;


        if (convertView != null && convertView instanceof RelativeLayout) {
            view = convertView;
            holder = (ViewGroupHolder) view.getTag();
        } else {
            view = View.inflate(addGoodsPromotionActivity, R.layout.item_promotion_group, null);
            holder = new ViewGroupHolder();
            holder.tvPromotionGroupFbFName = (TextView) view.findViewById(R.id.tv_promotion_group_fbFName);
            holder.tvPromotionGroupFbFSaleType = (TextView) view.findViewById(R.id.tv_promotion_group_fbFSaleType);
            holder.tvPromotionGroupFbFStarDate = (TextView) view.findViewById(R.id.tv_promotion_group_fbFStarDate);
            holder.tvPromotionGroupFbFEndDate = (TextView) view.findViewById(R.id.tv_promotion_group_fbFEndDate);
            holder.tvPromotionGroupFbdFProQty = (TextView) view.findViewById(R.id.tv_promotion_group_fbdFProQty);
            holder.tvPromotionGroupFbdFProAmount = (TextView) view.findViewById(R.id.tv_promotion_group_fbdFProAmount);
            holder.tvPromotionGroupFbFSumQty = (TextView) view.findViewById(R.id.tv_promotion_group_fbFSumQty);
            holder.tvPromotionGroupFbFSumAmount = (TextView) view.findViewById(R.id.tv_promotion_group_fbFSumAmount);
            view.setTag(holder);
        }
        holder.tvPromotionGroupFbFName.setText(goodsPromotionGroupList.get(groupPosition).get(0).getFbFName());
        holder.tvPromotionGroupFbFSaleType.setText("促销方式：" + map.get(goodsPromotionGroupList.get(groupPosition).get(0).getFbFSaleType() + "fst"));
        holder.tvPromotionGroupFbFSaleType.setText(map.get(goodsPromotionGroupList.get(groupPosition).get(0).getFbFSaleType() + "fst"));
//        holder.tvPromotionGroupFbFStarDate.setText("生效日期：" + goodsPromotionGroupList.get(groupPosition).get(0).getFbFStarDate());
//        holder.tvPromotionGroupFbFEndDate.setText("失效日期：" + goodsPromotionGroupList.get(groupPosition).get(0).getFbFEndDate());


        view.setPadding(CommonUtils.dip2px(addGoodsPromotionActivity, 25), 0, 0, 0);
        return view;
    }

    public HashMap<String, String> mapSysID = new HashMap<>();
    public HashMap<String, String> mapNumber = new HashMap<>();
    public HashMap<String, String> mapPrice = new HashMap<>();
    public HashMap<String, String> mapASum = new HashMap<>();
    public HashMap<String, String> mapUnit = new HashMap<>();
    public HashMap<String, String> mapUnitID = new HashMap<>();
    public HashMap<String, String> mapWHID = new HashMap<>();
    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final View view;
        final ViewChildHolder holder;


        if (convertView != null && convertView instanceof RelativeLayout) {
            view = convertView;
            holder = (ViewChildHolder) view.getTag();
        } else {
            view = View.inflate(addGoodsPromotionActivity, R.layout.item_promotion_child, null);
            holder = new ViewChildHolder();
            holder.tvPromotionChildFFlag = (TextView) view.findViewById(R.id.tv_promotion_child_FFlag);
            holder.tvPromotionChildGoodsName = (TextView) view.findViewById(R.id.tv_promotion_child_GoodsName);
            holder.tvPromotionChildUom = (TextView) view.findViewById(R.id.tv_promotion_child_Uom);

            holder.lvTvPromotionChildFDCStock = (LinearLayout) view.findViewById(R.id.lv_tv_promotion_child_FDCStock);
            holder.tvPromotionChildFDCStock = (TextView) view.findViewById(R.id.tv_promotion_child_FDCStock);

            holder.lvPromotionChildGoodsNumber = (LinearLayout) view.findViewById(R.id.lv_promotion_child_GoodsNumber);
            holder.etPromotionChildGoodsNumber = (EditText) view.findViewById(R.id.et_promotion_child_GoodsNumber);

            holder.lvTvPromotionChildGoodsPrice = (LinearLayout) view.findViewById(R.id.lv_tv_promotion_child_GoodsPrice);
            holder.tvPromotionChildGoodsPrice = (TextView) view.findViewById(R.id.tv_promotion_child_GoodsPrice);

            holder.lvEtPromotionChildGoodsPrice = (LinearLayout) view.findViewById(R.id.lv_et_promotion_child_GoodsPrice);
            holder.etPromotionChildGoodsPrice = (EditText) view.findViewById(R.id.et_promotion_child_GoodsPrice);

            holder.lvPromotionChildGoodsSumPrice = (LinearLayout) view.findViewById(R.id.lv_promotion_child_GoodsSumPrice);
            holder.tvPromotionChildGoodsSumPrice = (TextView) view.findViewById(R.id.tv_promotion_child_GoodsSumPrice);

            holder.lvPromotionChildIntroductions = (LinearLayout) view.findViewById(R.id.lv_promotion_child_Introductions);
            holder.tvPromotionChildIntroductions = (TextView) view.findViewById(R.id.tv_promotion_child_Introductions);
            view.setTag(holder);

        }

        if ("0".equals(fStockPosition)) {
            holder.lvTvPromotionChildFDCStock.setVisibility(View.GONE);
        }

        holder.tvPromotionChildFDCStock.setText(mapWareHouse.get(fDCStockD));
        holder.tvPromotionChildFDCStock.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

                final String[] arrWareHouse = getStringsFormat(mapValuesList);

                AlertDialog.Builder builder = new AlertDialog.Builder(addGoodsPromotionActivity);
                builder.setTitle("仓库选择");
                builder.setSingleChoiceItems(arrWareHouse, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
//                        intFDCStockNum = which;
                        //2.设置
                        holder.tvPromotionChildFDCStock.setText(arrWareHouse[which]);
                        dialog.dismiss();
                    }
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

        holder.tvPromotionChildFFlag.setText(map.get(goodsPromotionGroupList.get(groupPosition).get(childPosition).getFbdFFlag() + "ff"));
        holder.tvPromotionChildGoodsName.setText(goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsName());
        holder.tvPromotionChildUom.setText(goodsPromotionGroupList.get(groupPosition).get(childPosition).getFbdFproUnit());

        holder.etPromotionChildGoodsNumber.setHint("请输入数量");
        holder.etPromotionChildGoodsNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        price = goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsPrice();

        if (!fChooseAmount.equals("1")) {//不允许更改单价
            holder.lvEtPromotionChildGoodsPrice.setVisibility(View.GONE);
            holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(price)));
            holder.tvPromotionChildGoodsPrice.setTextColor(CommonUtils.getColor(R.color.black));

        } else {//允许更改单价
            holder.lvTvPromotionChildGoodsPrice.setVisibility(View.GONE);
            holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(price)));
            holder.etPromotionChildGoodsPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }


        if (goodsPromotionGroupList.get(groupPosition).get(childPosition).getFbdFFlag().equals("0")) {//促销品
            holder.lvPromotionChildGoodsNumber.setVisibility(View.GONE);//隐藏数量
            holder.lvTvPromotionChildGoodsPrice.setVisibility(View.GONE);//隐藏单价
            holder.lvEtPromotionChildGoodsPrice.setVisibility(View.GONE);//隐藏输入单价
            holder.lvPromotionChildGoodsSumPrice.setVisibility(View.GONE);//隐藏总价
            holder.lvPromotionChildIntroductions.setVisibility(View.VISIBLE);//显示说明


            String fSaleType = goodsPromotionGroupList.get(groupPosition).get(0).getFbFSaleType();

            //套装组合
            String fSuit = goodsPromotionGroupList.get(groupPosition).get(0).getFbFSuit();
            //倍增
            String fMultiple = goodsPromotionGroupList.get(groupPosition).get(0).getFbFMultiple();
            //单位
            String fProUnit = goodsPromotionGroupList.get(groupPosition).get(0).getFbdFproUnit();
            String fProUnitID = goodsPromotionGroupList.get(groupPosition).get(0).getFbdFproUnitID();

            //fbd表数量
            String fProQy = numberInteger(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(0).getFbdFProQty()));
            String fProQyPromotionSysID = goodsPromotionGroupList.get(groupPosition).get(childPosition).getFbdFPRoItem();
            String fProQyPromotionName = goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsName();
            String fProQyPromotionCount = numberInteger(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(childPosition).getFbdFProQty()));

            //fbd表金额
            String fProAmount = AmountShow(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(0).getFbdFProAmount()));
            //fb数量
            String fSumQty = numberInteger(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(0).getFbFSumQty()));
            //fb金额
            String fSumAmount = AmountShow(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(0).getFbFSumAmount()));
            //方案名称
            String fName = goodsPromotionGroupList.get(groupPosition).get(0).getFbFName();

            String fPromotionWareHouseSysId = holder.tvPromotionChildFDCStock.getText().toString();

            Message message = new Message();
            Bundle bundle = new Bundle();


            bundle.putString("fName", fName);
            bundle.putString("fProUnit", fProUnit);
            bundle.putString("fProUnitID", fProUnitID);
            bundle.putString("fProQyPromotionSysID", fProQyPromotionSysID);
            bundle.putString("fProQyPromotionName", fProQyPromotionName);
            bundle.putString("fProQyPromotionCount", fProQyPromotionCount);
            bundle.putString("fPromotionWareHouseSysId", "");

            message.setData(bundle);
            message.what = 2;
            addGoodsPromotionActivity.handler.handleMessage(message);

            if ("1".equals(fSaleType)) {//数量

                if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 无倍数搭赠，销售商品数量满" + fProQy + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 有倍数搭赠，销售商品总数量每满" + fProQy + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 无倍数搭赠，销售商品总数量满" + fSumQty + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 有倍数搭赠，销售商品总数量每满" + fSumQty + "，即搭赠数量" + fProQyPromotionCount);
                }

            } else if ("2".equals(fSaleType)) {//金额

                if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 无倍数搭赠，销售商品金额满" + fProAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 有倍数搭赠，销售商品总金额每满" + fProAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 无倍数搭赠，销售商品总金额满" + fSumAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 有倍数搭赠，销售商品总金额每满" + fSumAmount + "，即搭赠数量" + fProQyPromotionCount);
                }

            } else if ("3".equals(fSaleType)) {//数量或金额

                if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 无倍数搭赠，销售商品数量满" + fProQy + " 或 金额满" + fProAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 有倍数搭赠，销售商品数量每满" + fProQy + " 或 金额每满" + fProAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 无倍数搭赠，销售商品数量满" + fSumQty + " 或 金额满" + fSumAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 有倍数搭赠，销售商品数量每满" + fSumQty + " 或 金额每满" + fSumAmount + "，即搭赠数量" + fProQyPromotionCount);
                }

            } else if ("4".equals(fSaleType)) {//数量且金额

                if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 无倍数搭赠，销售商品数量满" + fProQy + " 且 金额满" + fProAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("无组合套装 有倍数搭赠，销售商品数量每满" + fProQy + " 且 金额每满" + fProAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 无倍数搭赠，销售商品数量满" + fSumQty + " 且 金额满" + fSumAmount + "，即搭赠数量" + fProQyPromotionCount);
                } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增
                    holder.tvPromotionChildIntroductions.setText("有组合套装 有倍数搭赠，销售商品数量每满" + fSumQty + " 且 金额每满" + fSumAmount + "，即搭赠数量" + fProQyPromotionCount);
                }
            }

        }

        view.setPadding(CommonUtils.dip2px(addGoodsPromotionActivity, 25), 10, CommonUtils.dip2px(addGoodsPromotionActivity, 25), 10);


        holder.etPromotionChildGoodsNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {

            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                String editable = getInputIsEmpty(holder.etPromotionChildGoodsNumber.getText().toString());


                String str = StringFilter(editable.toString());
                if (!editable.equals(str)) {
                    holder.etPromotionChildGoodsNumber.setText(str);
                    holder.etPromotionChildGoodsNumber.setSelection(str.length()); //光标置后
                }


                calculateSum("0", str, groupPosition, childPosition, holder);
//                setCalculation(s, true, groupPosition, childPosition, holder);
            }


        });

        if (fChooseAmount.equals("1")) {//允许修改价格
            holder.etPromotionChildGoodsPrice.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    String editable = getInputIsEmpty(holder.etPromotionChildGoodsPrice.getText().toString());


                    String str = StringFilter(editable.toString());
                    if (!editable.equals(str)) {
                        holder.etPromotionChildGoodsPrice.setText(str);
                        holder.etPromotionChildGoodsPrice.setSelection(str.length()); //光标置后
                    }

                    KLog.e("=3333333===" + str);

                    calculateSum("1", str, groupPosition, childPosition, holder);

//                    setCalculation(s, false, groupPosition, childPosition, holder);
                }
            });
        }

        return view;
    }

    /**
     * 计算 和 回调
     *
     * @param s
     * @param str
     * @param groupPosition
     * @param childPosition
     * @param holder
     */
    private void calculateSum(String s, String str, int groupPosition, int childPosition, ViewChildHolder holder) {

        price = goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsPrice();
        changePrice = AmountShowPolishing(getInputIsEmpty(holder.etPromotionChildGoodsPrice.getText().toString()));
        changeNum = AmountShowPolishing(getInputIsEmpty(holder.etPromotionChildGoodsNumber.getText().toString()));


//        KLog.e("==@@@11111=== " + groupPosition + " === " + childPosition + " ======" + str);
//        KLog.e("==@@@22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
//        KLog.e("==@@@33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

        holder.etPromotionChildGoodsNumber.setHint("请输入数量");
        holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsPrice())));
        holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsPrice())));


        if ("0".equals(s)) {//数字

            if ("0".equals(str)) {

                if ("0".equals(changePrice)) {
                    holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(mulss(str, price)));


                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, str);
                    mapPrice.put("" + groupPosition + childPosition, price);
                    mapASum.put("" + groupPosition + childPosition, AmountShow(mulss(str, price)));
                } else {
                    holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(0));

                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, str);
                    mapPrice.put("" + groupPosition + childPosition, changePrice);
                    mapASum.put("" + groupPosition + childPosition, "0");
                }

            } else {

                if ("0".equals(changePrice)) {
                    holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsSumPrice.setText((AmountShow(mulss(str, price))));
//
//                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
//                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
//                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, str);
                    mapPrice.put("" + groupPosition + childPosition, price);
                    mapASum.put("" + groupPosition + childPosition, AmountShow(mulss(str, price)));


                } else {
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(mulss(str, changePrice)));

//                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
//                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
//                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, str);
                    mapPrice.put("" + groupPosition + childPosition, changePrice);
                    mapASum.put("" + groupPosition + childPosition, AmountShow(mulss(str, changePrice)));
                }
            }

        }


        if ("1".equals(s)) {//单价
            if ("0".equals(str)) {
                if ("0".equals(changeNum)) {
                    holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(0));

                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, changeNum);
                    mapPrice.put("" + groupPosition + childPosition, str);
                    mapASum.put("" + groupPosition + childPosition, "0");

                } else {
                    holder.etPromotionChildGoodsPrice.setHint(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsPrice.setText(AmountShow(Double.parseDouble(price)));
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(mulss(changeNum, price)));

                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, changeNum);
                    mapPrice.put("" + groupPosition + childPosition, str);
                    mapASum.put("" + groupPosition + childPosition, AmountShow(mulss(changeNum, price)));
                }
            } else {
                if ("0".equals(changeNum)) {
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(0));

                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, changeNum);
                    mapPrice.put("" + groupPosition + childPosition, str);
                    mapASum.put("" + groupPosition + childPosition, "0");
                } else {
                    holder.tvPromotionChildGoodsSumPrice.setText(AmountShow(mulss(changeNum, str)));

                    KLog.e("==%%%%%11111=== " + groupPosition + " === " + childPosition + " ======" + str);
                    KLog.e("==%%%%%22222===" + groupPosition + " === " + childPosition + " ======" + changePrice);
                    KLog.e("==%%%%%33333===" + groupPosition + " === " + childPosition + " ======" + changeNum);

                    mapNumber.put("" + groupPosition + childPosition, changeNum);
                    mapPrice.put("" + groupPosition + childPosition, str);
                    mapASum.put("" + groupPosition + childPosition, AmountShow(mulss(changeNum, str)));
                }
            }
        }


        mapSysID.put("" + groupPosition + childPosition, goodsPromotionGroupList.get(groupPosition).get(childPosition).getFbdFPRoItem());
        mapUnit.put("" + groupPosition + childPosition, goodsPromotionGroupList.get(groupPosition).get(0).getFbdFproUnit());
        mapUnitID.put("" + groupPosition + childPosition, goodsPromotionGroupList.get(groupPosition).get(0).getFbdFproUnitID());
//
//        for (Map.Entry<String, String> stringStringEntry : mapNumber.entrySet()) {
//            KLog.e("======^^^^^^=======" + stringStringEntry.toString());
//        }


        Message message = new Message();
        Bundle bundle = new Bundle();
        bundle.putInt("groupPosition", groupPosition);
        bundle.putInt("childPosition", childPosition);
        bundle.putInt("num", goodsPromotionGroupList.get(groupPosition).size());

        final SerializableMap MapSysID = new SerializableMap();
        MapSysID.setMap(mapSysID);//将map数据添加到封装的myMap中
        bundle.putSerializable("mapSysID", MapSysID);

        final SerializableMap MapNumber = new SerializableMap();
        MapNumber.setMap(mapNumber);//将map数据添加到封装的myMap中
        bundle.putSerializable("mapNumber", MapNumber);

        final SerializableMap MapPrice = new SerializableMap();
        MapPrice.setMap(mapPrice);//将map数据添加到封装的myMap中
        bundle.putSerializable("mapPrice", MapPrice);

        final SerializableMap MapASum = new SerializableMap();
        MapASum.setMap(mapASum);//将map数据添加到封装的myMap中
        bundle.putSerializable("mapASum", MapASum);

        final SerializableMap MapUnit = new SerializableMap();
        MapUnit.setMap(mapUnit);//将map数据添加到封装的myMap中
        bundle.putSerializable("mapUnit", MapUnit);

        final SerializableMap MapUnitID = new SerializableMap();
        MapUnitID.setMap(mapUnitID);//将map数据添加到封装的myMap中
        bundle.putSerializable("mapUnitID", MapUnitID);

        message.setData(bundle);
        message.what = 1;

        addGoodsPromotionActivity.handler.handleMessage(message);

    }



    //孩子是否被选择 true 选择，false不能被选择
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return false;
    }

    /**
     * 容器
     */
    static class ViewGroupHolder {
        TextView tvPromotionGroupFbFName;
        TextView tvPromotionGroupFbFSaleType;
        TextView tvPromotionGroupFbFStarDate;
        TextView tvPromotionGroupFbFEndDate;
        TextView tvPromotionGroupFbdFProQty;
        TextView tvPromotionGroupFbdFProAmount;
        TextView tvPromotionGroupFbFSumQty;
        TextView tvPromotionGroupFbFSumAmount;
    }


    static class ViewChildHolder {
        TextView tvPromotionChildFFlag;
        TextView tvPromotionChildGoodsName;
        TextView tvPromotionChildUom;

        LinearLayout lvTvPromotionChildFDCStock;
        TextView tvPromotionChildFDCStock;

        LinearLayout lvPromotionChildGoodsNumber;
        EditText etPromotionChildGoodsNumber;

        LinearLayout lvTvPromotionChildGoodsPrice;
        TextView tvPromotionChildGoodsPrice;

        LinearLayout lvEtPromotionChildGoodsPrice;
        EditText etPromotionChildGoodsPrice;

        LinearLayout lvPromotionChildGoodsSumPrice;
        TextView tvPromotionChildGoodsSumPrice;

        LinearLayout lvPromotionChildIntroductions;
        TextView tvPromotionChildIntroductions;
    }


}