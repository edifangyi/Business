package com.fangyi.businessthrough.activity.business;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AbsListView;
import android.widget.ExpandableListView;
import android.widget.ImageView;
import android.widget.TextView;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.adapter.business.GoodsPromotionAdapter;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.events.AddGoodsPromotion;
import com.fangyi.businessthrough.events.GoodsPromotionChild;
import com.fangyi.businessthrough.events.GoodsUnitGroup;
import com.fangyi.businessthrough.utils.business.CalculationUtils;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.SerializableMap;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.application.FYApplication.ADD_PROMOTION_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.getContext;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.compare;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.div2;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.getBigDecimal_0;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.getBigDecimal_6;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;


/**
 * Created by FANGYI on 2016/9/28.
 */

public class AddGoodsPromotionActivity extends BaseActivity {

    @BindView(R.id.action_bar_back)
    ImageView actionBarBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    @BindView(R.id.elv)
    ExpandableListView elv;


    @BindView(R.id.tv_number)
    TextView tvNumber;
    @BindView(R.id.tv_sum)
    TextView tvSum;
    @BindView(R.id.tv_promotionCount)
    TextView tvPromotionCount;



    private String strGoodsNameID;//要促销的商品ID
    private String userSysID;
    private String fChooseAmount;//允许修改单价金额

    private List<Map<String, String>> goodsPromotion;//促销方案
    private List<Map<String, String>> goodsPromotionChild;//促销方案详情
    private List<Map<String, String>> aloneGoodsUnitGroup;//单位

    private Map<String, String> mapWareHouse;//仓库

    private ArrayList<ArrayList<GoodsPromotionChild>> goodsPromotionGroupList;//促销方案组合
    private ArrayList<GoodsUnitGroup> goodsUnitGroupList;//单位list

    public HashMap<String, AddGoodsPromotion> addGoodsPromotionMap = new HashMap<>();//回调到上一个Activity的内容


    private GoodsPromotionAdapter adapter;


    private String fDCStockD;//仓库
    private String fStockPosition;//仓库页面
    private String kISID;//用户登陆id


    private String fName;
    private String fProUnit;
    private String fProQyPromotionSysID;
    private String fProQyPromotionName;
    private String fProQyPromotionCount;//促销品数量是用来计算总数量的
    private String fProQyPromotionUnitID;
    private String fPromotionWareHouseSysId;


    private boolean sumNumberCondition = false;
    private boolean sumPriceCondition = false;



    public Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 0:
                    adapter = new GoodsPromotionAdapter(AddGoodsPromotionActivity.this, fChooseAmount, fDCStockD, fStockPosition, mapWareHouse, goodsPromotionGroupList, goodsUnitGroupList);
                    elv.setAdapter(adapter);
                    elv.setFocusable(false);
                    elv.setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
                    break;
                case 1:
                    setCalculation(msg);
                    break;
                case 2:
                    Bundle bundle = msg.getData();
                    fName = bundle.getString("fName");
                    fProUnit = bundle.getString("fProUnit");
                    fProQyPromotionSysID = bundle.getString("fProQyPromotionSysID");
                    fProQyPromotionName = bundle.getString("fProQyPromotionName");
                    fProQyPromotionCount = bundle.getString("fProQyPromotionCount");
                    fProQyPromotionUnitID = bundle.getString("fProUnitID");
                    fPromotionWareHouseSysId = bundle.getString("fPromotionWareHouseSysId");

                    break;
            }
        }
    };


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_promotion);
        ButterKnife.bind(this);
        readingData();
        setListener();

        elv.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                if (scrollState == SCROLL_STATE_TOUCH_SCROLL) {
                    View currentView = AddGoodsPromotionActivity.this.getCurrentFocus();
                    if (currentView != null) {
                        currentView.clearFocus();
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem,
                                 int visibleItemCount, int totalItemCount) {
                // TODO Auto-generated method stub

            }
        });

    }

    private void setListener() {
        //点击某列时,其他列为关闭状态
        elv.setOnGroupExpandListener(new ExpandableListView.OnGroupExpandListener() {

            @Override
            public void onGroupExpand(int groupPosition) {

                //逻辑是点击这个组别时,返回的这个值是否是在组的数组中,是则开,否则闭
                for (int i = 0; i < goodsPromotionGroupList.size(); i++) {

                    if (groupPosition != i) {
                        //关闭
                        elv.collapseGroup(i);
                    }
                }


            }
        });

        elv.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {

                InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(),
                            0);
                }

                tvNumber.setText(String.valueOf(""));
                tvSum.setText(String.valueOf(""));
                tvPromotionCount.setText(String.valueOf(""));

                addGoodsPromotionMap.clear();

                setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), true, true);
                tvPromotionCount.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));


                tvSum.setText("合计金额");
                tvNumber.setText("合计数量");
                tvPromotionCount.setText("赠品数量");


                return false;
            }
        });


        tvPromotionCount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (sumPriceCondition || sumNumberCondition) {
                    setCallbackData(ADD_PROMOTION_REQ_CODE);
//                    for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : addGoodsPromotionMap.entrySet()) {
//                        AddGoodsPromotion ss = stringAddGoodsPromotionEntry.getValue();
//
//                        KLog.e("=" + stringAddGoodsPromotionEntry.getKey() + "====" + ss.getGoodsSysID() + "=" + ss.getGoodsNumber() + "=" + ss.getGoodsPrice() + "=" + ss.getGoodsSumPrice() + "=" + ss.getPromotionID() + "=" + ss.getPromotionCount());
//                    }
//                    addGoodsPromotionMap.clear();

                }
            }
        });

        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setCallbackData(0);

            }
        });

    }

    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        setCallbackData(0);
        super.onBackPressed();
    }


    /**
     * 默认数据给上一个Activity
     *
     * @param resultCode
     */
    private void setCallbackData(int resultCode) {
        Bundle bundle = new Bundle();

        if (resultCode == 0) {
            addGoodsPromotionMap.clear();
        }

        SerializableMap MapAddGoodsPromotion = new SerializableMap();
        MapAddGoodsPromotion.setMapAddGoodsPromotion(addGoodsPromotionMap);//将map数据添加到封装的myMap中

        bundle.putSerializable("addGoodsPromotionMap", MapAddGoodsPromotion);

        setResult(resultCode, AddGoodsPromotionActivity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoodsPromotionActivity.this.finish();//当前事件结束;
    }


    private void readingData() {
        strGoodsNameID = this.getIntent().getStringExtra("goodsNameID");
//        strGoodsNameID = "276";
        userSysID = this.getIntent().getStringExtra("userSysID");
//        userSysID = "02";
        fChooseAmount = this.getIntent().getStringExtra("fChooseAmount");
        fChooseAmount = "0";
        fDCStockD = this.getIntent().getStringExtra("fDCStockD");
        fStockPosition = this.getIntent().getStringExtra("fStockPosition");
//        fStockPosition = "1";
        kISID = this.getIntent().getStringExtra("kISID");
//        kISID = "283";
        DBBusiness business = new DBBusiness(getContext());
        mapWareHouse = business.getWareHouse(kISID);
        business.closeDB();

        new Thread() {
            @Override
            public void run() {

                final DBBusiness business = new DBBusiness(FYApplication.getContext());
                goodsPromotion = business.getGoodsPromotion(new String[]{strGoodsNameID, userSysID});


                goodsPromotionGroupList = new ArrayList<>();
                for (Map<String, String> map : goodsPromotion) {
                    goodsPromotionChild = business.getGoodsPromotionChild(new String[]{map.get("FIndex"), userSysID});

                    ArrayList<GoodsPromotionChild> goodsPromotionChildList = new ArrayList<>();
                    for (Map<String, String> stringStringMap : goodsPromotionChild) {


                        GoodsPromotionChild goodsPromotionChild = new GoodsPromotionChild();
                        goodsPromotionChild.setGoodsName(business.selectSQLGoodsName(stringStringMap.get("FPRoItem")));
                        goodsPromotionChild.setGoodsPrice(String.valueOf(getBigDecimal_6(business.selectSQLMinPrice(stringStringMap.get("FPRoItem")))));
                        goodsPromotionChild.setFbdFPRoItem(stringStringMap.get("FPRoItem"));
                        goodsPromotionChild.setFbdFIndex(stringStringMap.get("FIndex"));
                        goodsPromotionChild.setFbdFproUnit(business.selectSQLUom(stringStringMap.get("FproUnit")));
                        goodsPromotionChild.setFbdFProQty(String.valueOf(getBigDecimal_0(stringStringMap.get("FProQty"))));

                        goodsPromotionChild.setFbdFproUnitID(String.valueOf(stringStringMap.get("FproUnit")));

                        goodsPromotionChild.setFbdFProAmount(String.valueOf(getBigDecimal_6(stringStringMap.get("FProAmount"))));
                        goodsPromotionChild.setFbdFFlag(stringStringMap.get("FFlag"));
                        goodsPromotionChild.setFbFName(stringStringMap.get("FName"));
                        goodsPromotionChild.setFbFSaleType(stringStringMap.get("FSaleType"));
                        goodsPromotionChild.setFbFSumQty(String.valueOf(getBigDecimal_0(stringStringMap.get("FSumQty"))));
                        goodsPromotionChild.setFbFSumAmount(String.valueOf(getBigDecimal_6(stringStringMap.get("FSumAmount"))));

                        goodsPromotionChild.setFbFDate(stringStringMap.get("FDate"));
                        goodsPromotionChild.setFbFStarDate(stringStringMap.get("FStarDate"));
                        goodsPromotionChild.setFbFEndDate(stringStringMap.get("FEndDate"));
                        goodsPromotionChild.setFbFSuit(stringStringMap.get("FSuit"));
                        goodsPromotionChild.setFbFMultiple(stringStringMap.get("FMultiple"));

                        goodsPromotionChildList.add(goodsPromotionChild);
                    }
                    goodsPromotionGroupList.add(goodsPromotionChildList);
                }


                //商品单位
                aloneGoodsUnitGroup = business.getUnitGroup(new String[]{strGoodsNameID});

                goodsUnitGroupList = new ArrayList<>();
                for (Map<String, String> map : aloneGoodsUnitGroup) {
                    GoodsUnitGroup goodsUnitGroup = new GoodsUnitGroup();
                    goodsUnitGroup.setUom(map.get("Uom"));
                    goodsUnitGroup.setConversionb(map.get("Conversion"));

                    goodsUnitGroupList.add(goodsUnitGroup);
                }

                handler.sendEmptyMessage(0);
            }
        }.start();


    }


    /**
     * 计算总额
     *
     * @param msg
     */
    private void setCalculation(Message msg) {
        Bundle bundle = msg.getData();
        int groupPosition = bundle.getInt("groupPosition");
        int childPosition = bundle.getInt("childPosition");
        int num = bundle.getInt("num");


        SerializableMap MapNumber = (SerializableMap) bundle.get("mapNumber");
        HashMap<String, String> mapNumber = MapNumber.getMap();


        SerializableMap MapPrice = (SerializableMap) bundle.get("mapPrice");
        HashMap<String, String> mapPrice = MapPrice.getMap();


        SerializableMap MapASum = (SerializableMap) bundle.get("mapASum");
        HashMap<String, String> mapASum = MapASum.getMap();


        SerializableMap MapSysID = (SerializableMap) bundle.get("mapSysID");
        HashMap<String, String> mapSysID = MapSysID.getMap();


        SerializableMap MapUnit = (SerializableMap) bundle.get("mapUnit");
        HashMap<String, String> mapUnit = MapUnit.getMap();

        SerializableMap MapUnitID = (SerializableMap) bundle.get("mapUnitID");
        HashMap<String, String> mapUnitID = MapUnitID.getMap();

        AddGoodsPromotion addGoodsPromotion = new AddGoodsPromotion();


        //销售商品总数量
        double sumNumber = 0;

        double goodsPrice = 0;//单个销售商品单价
        //销售商品总金额
        double sumPrice = 0;
        double promotionCount = 0;//促销品数量

        for (int i = 0; i < num - 1; i++) {
            if (mapNumber.get("" + groupPosition + i) != null && !mapNumber.get("" + groupPosition + i).equals("")) {
                sumNumber += Integer.parseInt(mapNumber.get("" + groupPosition + i));

                KLog.e("===sumNumber=====" + sumNumber);
                addGoodsPromotion.setSumNumber(String.valueOf(sumNumber));//所有销售商品总数量
            }


            if (mapASum.get("" + groupPosition + i) != null && !mapASum.get("" + groupPosition + i).equals("")) {
                sumPrice = add(AmountShowBug(mapASum.get("" + groupPosition + i)), sumPrice);
                KLog.e("===sumPrice======" + sumPrice);
                addGoodsPromotion.setSumPrice(String.valueOf(AmountShowBug(String.valueOf(sumPrice))));//所有销售商品总价
            }

        }


        if (mapNumber.get("" + groupPosition + childPosition) != null && !mapNumber.get("" + groupPosition + childPosition).equals("")) {

            addGoodsPromotion.setGoodsNumber(mapNumber.get("" + groupPosition + childPosition));//商品数量

            KLog.e("====()()()()() ====" + groupPosition + " === " + childPosition + " ===== " + mapNumber.get("" + groupPosition + childPosition) + "=====" + sumNumber);
        }

        if (mapPrice.get("" + groupPosition + childPosition) != null && !mapPrice.get("" + groupPosition + childPosition).equals("")) {

            addGoodsPromotion.setGoodsPrice(String.valueOf(AmountShowBug(mapPrice.get("" + groupPosition + childPosition))));//商品单价
            KLog.e("====()()()()()()()() ====" + groupPosition + " === " + childPosition + " ===== " + mapPrice.get("" + groupPosition + childPosition));

        }

        if (mapASum.get("" + groupPosition + childPosition) != null && !mapASum.get("" + groupPosition + childPosition).equals("")) {

            addGoodsPromotion.setGoodsSumPrice(String.valueOf(AmountShowBug(mapASum.get("" + groupPosition + childPosition))));//单个商品总价

            KLog.e("====()()()()()()()()()()() ====" + groupPosition + " === " + childPosition + " ===== " + mapASum.get("" + groupPosition + childPosition) + "=====" + sumPrice);

        }


        if (mapSysID.get("" + groupPosition + childPosition) != null && !mapSysID.get("" + groupPosition + childPosition).equals("")) {
            addGoodsPromotion.setGoodsSysID(mapSysID.get("" + groupPosition + childPosition));//商品ID
        }

        if (mapUnit.get("" + groupPosition + childPosition) != null && !mapUnit.get("" + groupPosition + childPosition).equals("")) {
            addGoodsPromotion.setGoodsUnit(mapUnit.get("" + groupPosition + childPosition));//商品单位
        }

        if (mapUnitID.get("" + groupPosition + childPosition) != null && !mapUnitID.get("" + groupPosition + childPosition).equals("")) {
            addGoodsPromotion.setGoodsUnitID(mapUnitID.get("" + groupPosition + childPosition));//商品单位
        }


        String fSaleType = goodsPromotionGroupList.get(groupPosition).get(0).getFbFSaleType();
        //套装组合
        String fSuit = goodsPromotionGroupList.get(groupPosition).get(0).getFbFSuit();
        //倍增
        String fMultiple = goodsPromotionGroupList.get(groupPosition).get(0).getFbFMultiple();
        //单位
        String fProUnit = goodsPromotionGroupList.get(groupPosition).get(0).getFbdFproUnit();

        String fProUnitID = goodsPromotionGroupList.get(groupPosition).get(0).getFbdFproUnitID();

        //fbd表数量
        String fProQy = goodsPromotionGroupList.get(groupPosition).get(0).getFbdFProQty();
        //fbd表金额
        String fProAmount = goodsPromotionGroupList.get(groupPosition).get(0).getFbdFProAmount();
        //fb数量
        String fSumQty = goodsPromotionGroupList.get(groupPosition).get(0).getFbFSumQty();
        //fb金额
        String fSumAmount = goodsPromotionGroupList.get(groupPosition).get(0).getFbFSumAmount();


        tvPromotionCount.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_orange));

        if ("1".equals(fSaleType)) {//数量
            tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_purple_pressed));

            if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增

                if (compare(sumNumber, Double.parseDouble(fProQy)) > -1) {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                    sumNumberCondition = true;
                    sumPriceCondition = false;
                } else {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));
                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }


            } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增

                if (compare(sumNumber, Double.parseDouble(fProQy)) > -1) {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumNumber, Double.parseDouble(fProQy)));
                    sumNumberCondition = true;
                    sumPriceCondition = false;
                } else {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));
                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }

            } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增

                if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1) {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                    sumNumberCondition = true;
                    sumPriceCondition = false;
                } else {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));
                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }

            } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增
                if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1) {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumNumber, Double.parseDouble(fSumQty)));
                    sumNumberCondition = true;
                    sumPriceCondition = false;
                } else {
                    tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));
                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }
            }


        } else if ("2".equals(fSaleType)) {//金额
            tvNumber.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_purple_pressed));

            if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增

                if (compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                    sumNumberCondition = false;
                    sumPriceCondition = true;
                } else {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));

                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }

            } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增

                if (compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumPrice, Double.parseDouble(fProAmount)));
                    sumNumberCondition = false;
                    sumPriceCondition = true;
                } else {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));

                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }

            } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增

                if (compare(sumPrice, Double.parseDouble(fSumAmount)) > -1) {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                    sumNumberCondition = false;
                    sumPriceCondition = true;
                } else {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));

                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }

            } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增

                if (compare(sumPrice, Double.parseDouble(fSumAmount)) > -1) {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green));
                    promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumPrice, Double.parseDouble(fSumAmount)));
                    sumNumberCondition = false;
                    sumPriceCondition = true;
                } else {
                    tvSum.setBackground(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray));

                    sumNumberCondition = false;
                    sumPriceCondition = false;
                }
            }

        } else if ("3".equals(fSaleType)) {//数量或金额

            if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增

                if (compare(sumNumber, Double.parseDouble(fProQy)) > -1 || compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }

                /**
                 *
                 * =============================当数量和金额都满足条件   结果走金额=============================================================================================================================
                 *
                 */
            } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增

                if (compare(sumNumber, Double.parseDouble(fProQy)) > -1 || compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);

                    if (compare(sumNumber, Double.parseDouble(fProQy)) > -1) {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumNumber, Double.parseDouble(fProQy)));
                    }

                    if (compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumPrice, Double.parseDouble(fProAmount)));
                    }

                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }

            } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增

                if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1 || compare(sumPrice, Double.parseDouble(fSumAmount)) > -1) {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }

            } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增

                if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1 || compare(sumPrice, Double.parseDouble(fSumAmount)) > -1) {

                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);
                    if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1) {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumNumber, Double.parseDouble(fSumQty)));
                    } else {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumPrice, Double.parseDouble(fSumAmount)));
                    }

                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }
            }

        } else if ("4".equals(fSaleType)) {//数量且金额

            if ("0".equals(fSuit) && "0".equals(fMultiple)) {//无套装 无倍增

                if (compare(sumNumber, Double.parseDouble(fProQy)) > -1 && compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }

            } else if ("0".equals(fSuit) && "1".equals(fMultiple)) {//无套装 有倍增

                if (compare(sumNumber, Double.parseDouble(fProQy)) > -1 && compare(sumPrice, Double.parseDouble(fProAmount)) > -1) {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);

                    if (compare(div2(sumNumber, Double.parseDouble(fProQy)), div2(sumPrice, Double.parseDouble(fProAmount))) > -1) {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumPrice, Double.parseDouble(fProAmount)));
                    } else {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumNumber, Double.parseDouble(fProQy)));
                    }

                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }

            } else if ("1".equals(fSuit) && "0".equals(fMultiple)) {//有套装 无倍增

                if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1 && compare(sumPrice, Double.parseDouble(fSumAmount)) > -1) {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);
                    promotionCount = Double.parseDouble(fProQyPromotionCount);
                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }

            } else if ("1".equals(fSuit) && "1".equals(fMultiple)) {//有套装 有倍增

                if (compare(sumNumber, Double.parseDouble(fSumQty)) > -1 && compare(sumPrice, Double.parseDouble(fSumAmount)) > -1) {

                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_green), true, true);

                    if (compare(div2(sumNumber, Double.parseDouble(fSumQty)), div2(sumPrice, Double.parseDouble(fSumAmount))) > -1) {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumPrice, Double.parseDouble(fSumAmount)));
                    } else {
                        promotionCount = CalculationUtils.muldd(Double.parseDouble(fProQyPromotionCount), div2(sumNumber, Double.parseDouble(fSumQty)));
                    }
                } else {
                    setTvSynchronousChangeColor(CommonUtils.getDrawable(R.drawable.shap_btn_bg_gray), false, false);
                }
            }
        }

        addGoodsPromotion.setGoodsName(goodsPromotionGroupList.get(groupPosition).get(childPosition).getGoodsName());


        addGoodsPromotion.setGoodsType(goodsPromotionGroupList.get(groupPosition).get(0).getFbdFFlag());
        addGoodsPromotion.setfName(fName);


        addGoodsPromotion.setPromotionUnit(fProUnit);
        addGoodsPromotion.setPromotionID(fProQyPromotionSysID);
        addGoodsPromotion.setPromotionName(fProQyPromotionName);
        addGoodsPromotion.setPromotionCount(String.valueOf(promotionCount));
        addGoodsPromotion.setPromotionUnitID(fProQyPromotionUnitID);
        addGoodsPromotion.setPromotionWareHouseSysId(fPromotionWareHouseSysId);

        addGoodsPromotionMap.put("" + groupPosition + childPosition, addGoodsPromotion);

//
//        for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : addGoodsPromotionMap.entrySet()) {
//            sumNumber = add(Double.parseDouble(stringAddGoodsPromotionEntry.getValue().getGoodsNumber()), sumNumber);
//        }

        tvNumber.setText("总数量\n" + String.valueOf(sumNumber));

        tvSum.setText("总金额\n" + String.valueOf(sumPrice));

        tvPromotionCount.setText("促销品\n" + promotionCount);

    }

    /**
     * 同步换色块
     *
     * @param drawable
     * @param b
     * @param b1
     */
    private void setTvSynchronousChangeColor(Drawable drawable, boolean b, boolean b1) {
        sumNumberCondition = b;
        sumPriceCondition = b1;
        tvNumber.setBackground(drawable);//有组合套装 有倍数搭赠 每满
        tvSum.setBackground(drawable);
    }
}
