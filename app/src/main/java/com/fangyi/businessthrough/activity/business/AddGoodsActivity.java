package com.fangyi.businessthrough.activity.business;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.system.SearchActivity;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.events.GoodsMessage;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.fangyi.businessthrough.view.FYEtItemView;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.application.FYApplication.ADD_GOODS_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_PROMOTION_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.getContext;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.divAddGoods;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.mulss;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowPolishing;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.StringFilter;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * Created by FANGYI on 2016/9/22.
 */

public class AddGoodsActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.action_bar_back)
    ImageView actionBarBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.fy_GoodsName)
    FYBtnRadioView fyGoodsName;
    @BindView(R.id.fy_Barcode)
    FYEtItemView fyBarcode;
    @BindView(R.id.fy_Uom)
    FYBtnRadioView fyUom;
    @BindView(R.id.fy_GoodsNumber)
    FYEtItemView fyGoodsNumber;
    @BindView(R.id.fy_Price)
    FYEtItemView fyPrice;
    @BindView(R.id.fy_No_Price)
    FYBtnRadioView fyNoPrice;
    @BindView(R.id.fy_SumMoney)
    FYEtItemView fySumMoney;
    @BindView(R.id.fy_Standard)
    FYBtnRadioView fyStandard;
    @BindView(R.id.btn_save_add_goods)
    Button btnSaveAddGoods;
    @BindView(R.id.fy_FDCStock)
    FYBtnRadioView fyFDCStock;
    @BindView(R.id.fy_No_SumMoney)
    FYBtnRadioView fyNoSumMoney;


    private String addType;
    private String businessType;
    private String fChooseAmount;//允许修改单价金额
    private String fDCStockD;//仓库
    private String fStockPosition;//仓库页面
    private String fPlanPro;//启用搭赠方案
    private String userSysID;//用户登陆id
    private String kISID;//用户登陆id
    private String fyFCustNameID;//客户ID

    private Map<String, String> mapWareHouse;//仓库
    private GoodsMessage goodsMessage;//所选商品信息


    private int intUomNum = 1;
    private int intFDCStockNum = 0;


    private List<Map<String, String>> aloneGoodsMessageList;

    private String price;
    private String changeNum;
    private String changePrice;
    private String changeSum;

    private String goodsSysID;
    private String goodsName;
    private String barcode;
    private String standard;
    private String conversion;
    private String uom;
    private String unitID;
    private String unitGroupID;
    private String uBase;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_goods);
        ButterKnife.bind(this);
        readingData();
        setinitItemView();
        setListener();
    }

    /**
     * 获取数据
     */
    private void readingData() {

        //接受前文传过来的intent;赋值给search_code;
        addType = this.getIntent().getStringExtra("addType");
        businessType = this.getIntent().getStringExtra("businessType");
        fChooseAmount = this.getIntent().getStringExtra("fChooseAmount");
        fDCStockD = this.getIntent().getStringExtra("fDCStockD");
        fStockPosition = this.getIntent().getStringExtra("fStockPosition");
        fPlanPro = this.getIntent().getStringExtra("fPlanPro");
        userSysID = this.getIntent().getStringExtra("userSysID");
        kISID = this.getIntent().getStringExtra("kISID");
        fyFCustNameID = this.getIntent().getStringExtra("fyFCustNameID");

        DBBusiness business = new DBBusiness(getContext());
        mapWareHouse = business.getWareHouse(kISID);
        business.closeDB();

    }

    /**
     * 初始化控件
     */
    private void setinitItemView() {

        fyGoodsName.setTitle("商品");
        fyGoodsName.setContent("必须填写");
        fyGoodsName.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyBarcode.setTitle("条码");
        fyBarcode.setHint("请输入条形码");
        fyBarcode.setInputTypeNumber();
        fyUom.setTitle("单位");

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(mapWareHouse.get(fDCStockD));
        if ("0".equals(fStockPosition)) {
            fyFDCStock.setVisibility(View.GONE);
        }

        fyGoodsNumber.setTitle("数量");
        fyGoodsNumber.setHint("请输入数量");
        fyGoodsNumber.setInputTypeNumber();
//        fyGoodsNumber.setInputTypeNumberDecimal();


        if ("1".equals(addType)) {//进入添加商品界面

            tvTitle.setText("添加商品");


            if ("1".equals(fChooseAmount)) {//可以改价格
                fyPrice.setTitle("单价");
                fyPrice.setHint("请输入单价");
                fySumMoney.setTitle("金额");
//                fyPrice.setInputTypeNumberDecimal();
//                fySumMoney.setInputTypeNumberDecimal();
                fyPrice.setInputTypeNumber();
                fySumMoney.setInputTypeNumber();
                fyNoSumMoney.setVisibility(View.GONE);
                fyNoPrice.setVisibility(View.GONE);
            } else {
                fyNoPrice.setTitle("单价");
                fyNoSumMoney.setTitle("金额");
                fySumMoney.setVisibility(View.GONE);
                fyPrice.setVisibility(View.GONE);
            }
        } else {

            fyPrice.setVisibility(View.GONE);
            fyNoPrice.setVisibility(View.GONE);
            fySumMoney.setVisibility(View.GONE);
            fyNoSumMoney.setVisibility(View.GONE);

            tvTitle.setText("添加赠品");
        }


        fyStandard.setTitle("规格");

        if ("0".equals(businessType)) {
            fyGoodsName.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyBarcode.setTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyBarcode.setHintColor(CommonUtils.getColor(R.color.text_user_account));
            fyStandard.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyFDCStock.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyUom.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));

            fyGoodsNumber.setTextColor(CommonUtils.getColor(R.color.text_user_account));

            fyPrice.setTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyNoPrice.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        }



    }



    /**
     * 设置点击事件
     */
    private void setListener() {
        fyGoodsName.setOnClickListener(this);
        fyBarcode.setOnClickListener(this);
        fyUom.setOnClickListener(this);
        fyFDCStock.setOnClickListener(this);
        btnSaveAddGoods.setOnClickListener(this);

        actionBarBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setCallbackData();
            }
        });


        fyGoodsNumber.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = fyGoodsNumber.getInputIsEmpty();
                String str = StringFilter(editable.toString());
                if (!editable.equals(str)) {
                    fyGoodsNumber.setText(str);
                    fyGoodsNumber.setSelection(str.length()); //光标置后
                }


                KLog.e("=111111===" + str);
                calculateSum("0", str);

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fyPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String editable = fyPrice.getInputIsEmpty();
                String str = StringFilter(editable.toString());
                if (!editable.equals(str)) {
                    fyPrice.setText(str);
                    fyPrice.setSelection(str.length()); //光标置后
                }

                KLog.e("=222222===" + str);
                calculateSum("1", str);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        fySumMoney.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                String editable = fySumMoney.getInputIsEmpty();
                String str = StringFilter(editable.toString());
                if (!editable.equals(str)) {
                    fySumMoney.setText(str);
                    fySumMoney.setSelection(str.length()); //光标置后
                }

                KLog.e("=3333333===" + str);

                calculateSum("2", str);

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }



    /**
     * 计算价格
     *
     * @param judge
     * @param str
     */

    private void calculateSum(String judge, String str) {

        changePrice = fyPrice.getInputIsEmpty();
        changeNum = fyGoodsNumber.getInputIsEmpty();
        changeSum = fySumMoney.getInputIsEmpty();

//
//        KLog.e("==@@@11111===" + str);
//        KLog.e("==@@@22222===" + changePrice);
//        KLog.e("==@@@33333===" + changeNum);
//        KLog.e("==@@@44444===" + changeSum);


        DBBusiness business = new DBBusiness(getContext());
        price = business.getBaseGoodsMessagePrice(changeNum, fyFCustNameID, goodsSysID, kISID, unitID, conversion);
        business.closeDB();


        if ("0".equals(judge)) {//数量
            if ("0".equals(str)) {

                if ("0".equals(changePrice)) {
                    fyPrice.setHint(AmountShow(0));
                    fyNoPrice.setContent(AmountShow(0));
                    fySumMoney.setHint(AmountShow(0));
                    fyNoSumMoney.setContent(AmountShow(0));
                } else {
                    fyPrice.setHint(AmountShow(Double.parseDouble(price)));
                    fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));
                    fySumMoney.setHint(AmountShow(0));
                    fyNoSumMoney.setContent(AmountShow(0));
                }

            } else {

                if ("0".equals(changePrice)) {
                    if ("0".equals(changeSum)) {
                        fyPrice.setHint(AmountShow(Double.parseDouble(price)));
                        fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));
                        fySumMoney.setHint((AmountShow(mulss(str, price))));
                        fyNoSumMoney.setContent(AmountShow(mulss(str, price)));
                    } else {
                        fyPrice.setHint(AmountShow(divAddGoods(changeSum, str)));
                        fyNoPrice.setContent(AmountShow(divAddGoods(changeSum, str)));
                    }

                } else {

                    if ("0".equals(changeSum)) {
                        fySumMoney.setHint(AmountShow(mulss(str, changePrice)));
                        fyNoSumMoney.setContent(AmountShow(mulss(str, changePrice)));
                    } else {
                        fyPrice.setText("");
                        fyPrice.setHint(AmountShow(divAddGoods(changeSum, str)));
                        fyNoPrice.setContent(AmountShow(divAddGoods(changeSum, str)));
                    }

                }
            }
        }

        if ("1".equals(judge)) {//单价

            if ("0".equals(str)) {
                if ("0".equals(changeNum)) {
                    fyPrice.setHint(AmountShow(Double.parseDouble(price)));
                    fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));
                    fySumMoney.setHint(AmountShow(0));
                    fyNoSumMoney.setContent(AmountShow(0));
                } else {
                    fyPrice.setHint(AmountShow(Double.parseDouble(price)));
                    fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));
                    fySumMoney.setHint(AmountShow(mulss(changeNum, price)));
                    fyNoSumMoney.setContent(AmountShow(mulss(changeNum, price)));
                }
            } else {
                if ("0".equals(changeNum)) {
                    fySumMoney.setHint(AmountShow(0));
                    fyNoSumMoney.setContent(AmountShow(0));
                } else {
                    fySumMoney.setText("");
                    fySumMoney.setHint(AmountShow(mulss(changeNum, str)));
                    fyNoSumMoney.setContent(AmountShow(mulss(changeNum, str)));
                }
            }

        }

        if ("2".equals(judge)) {//总金额

            if ("0".equals(str)) {

                if ("0".equals(changeNum)) {
                    if ("0".equals(changePrice)) {
                        fyPrice.setHint(AmountShow(0));
                        fyNoPrice.setContent(AmountShow(0));
                        fySumMoney.setHint(AmountShow(0));
                        fyNoSumMoney.setContent(AmountShow(0));
                    } else {
                        fyPrice.setHint(AmountShow(0));
                        fyNoPrice.setContent(AmountShow(0));
                        fySumMoney.setHint(AmountShow(0));
                        fyNoSumMoney.setContent(AmountShow(0));
                    }

                } else {

                    if ("0".equals(changePrice)) {
                        fyPrice.setText("");
                        fyPrice.setHint(AmountShow(Double.parseDouble(price)));
                        fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));
                        fySumMoney.setHint(AmountShow(mulss(changeNum, price)));
                        fyNoSumMoney.setContent(AmountShow(mulss(changeNum, price)));
                    } else {

                        fySumMoney.setHint(AmountShow(mulss(changeNum, changePrice)));
                        fyNoSumMoney.setContent(AmountShow(mulss(changeNum, changePrice)));
                    }

                }

            } else {
                if ("0".equals(changeNum)) {
                    if ("0".equals(changePrice)) {
                        fyPrice.setHint(AmountShow(0));
                        fyNoPrice.setContent(AmountShow(0));
                    } else {

                    }

                } else {

                    if ("0".equals(changePrice)) {
                        fyPrice.setText("");
                        fyPrice.setHint(AmountShow(divAddGoods(str, changeNum)));
                        fyNoPrice.setContent(AmountShow(divAddGoods(str, changeNum)));
                    } else {
                        fyPrice.setText("");
                        fyPrice.setHint(AmountShow(divAddGoods(str, changeNum)));
                        fyNoPrice.setContent(AmountShow(divAddGoods(str, changeNum)));
                    }

                }
            }


        }


    }



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fy_GoodsName://商品名称
                setGoodsName();
                break;
            case R.id.fy_Barcode://条形码
                break;
            case R.id.fy_FDCStock://仓库
                setFDCStock();
                break;
            case R.id.fy_Uom://单位
                setUom();
                break;
            case R.id.btn_save_add_goods:
                saveAddGoods();
                break;
        }
    }

    /**
     * 仓库选择
     */
    private void setFDCStock() {

        final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

        final String[] arrWareHouse = getStringsFormat(mapValuesList);

        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoodsActivity.this);
        builder.setTitle("仓库选择");
        builder.setSingleChoiceItems(arrWareHouse, intFDCStockNum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intFDCStockNum = which;
                //2.设置
                fyFDCStock.setContent(arrWareHouse[which]);
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();

    }


    /**
     * 单位
     */
    private void setUom() {

        if (TextUtils.isEmpty(goodsSysID)) {
            Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
            return;
        }

        if (aloneGoodsMessageList.size() != 1) {
            uom = aloneGoodsMessageList.get(intUomNum).get("Uom");
            unitID = aloneGoodsMessageList.get(intUomNum).get("UnitID");
            conversion = aloneGoodsMessageList.get(intUomNum).get("Conversion");

            fyUom.setContent(uom);
            intUomNum++;//1
            if (intUomNum >= aloneGoodsMessageList.size()) {
                intUomNum = 0;
            }
        }


        DBBusiness business = new DBBusiness(getContext());
        price = business.getBaseGoodsMessagePrice(changeNum, fyFCustNameID, goodsSysID, kISID, unitID, conversion);
        business.closeDB();

        changePrice = fyPrice.getInputIsEmpty();
        changeNum = fyGoodsNumber.getInputIsEmpty();

        fyPrice.setText("");
        fyPrice.setHint(AmountShow(Double.parseDouble(price)));
        fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));

        fySumMoney.setText("");

        if ("0".equals(changePrice)) {
            fySumMoney.setHint(AmountShow(mulss(changeNum, price)));
            fyNoSumMoney.setContent(AmountShow(mulss(changeNum, price)));
        } else {
            fySumMoney.setHint(AmountShow(mulss(changeNum, changePrice)));
            fyNoSumMoney.setContent(AmountShow(mulss(changeNum, changePrice)));
        }


    }



    /**
     * 获取商品信息
     */
    private void setGoodsName() {
        Intent intent = new Intent(AddGoodsActivity.this, SearchActivity.class);
        intent.putExtra("searchType", ADD_GOODS_REQ_CODE);
        startActivityForResult(intent, ADD_GOODS_REQ_CODE);
    }

    /**
     * 打开促销品订单页
     *
     * @param bundle
     */
    private void setGoodsPromotion(Bundle bundle) {
        Intent intent = new Intent(getApplication(), AddGoodsPromotionActivity.class);
        intent.putExtra("goodsNameID", bundle.getString("sysid"));
        intent.putExtra("userSysID", userSysID);
        intent.putExtra("fChooseAmount", fChooseAmount);//允许修改单价金额
        intent.putExtra("fDCStockD", fDCStockD);//仓库
        intent.putExtra("fStockPosition", fStockPosition);//仓库 在添加页面
        intent.putExtra("kISID", kISID);//传登陆用户KISID


        startActivityForResult(intent, ADD_PROMOTION_REQ_CODE);
    }

    /**
     * Activity数据回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Bundle bundle = data.getExtras();

        //搜索商品返回的信息
        if (resultCode != -1) {

            if (requestCode == ADD_GOODS_REQ_CODE) {//添加商品的回调

                final DBBusiness business = new DBBusiness(this);
                //根据GoodsSysID获取商品促销方案
                List<Map<String, String>> goodsPromotion = business.getGoodsPromotion(new String[]{bundle.getString("sysid"), userSysID});

                if (goodsPromotion.size() != 0 && "1".equals(fPlanPro)) {//有促销
                    //弹窗，判断是否跳到促销页面
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setCancelable(false);

                    builder.setTitle(Html.fromHtml("商品：[" + "<font color=#30A070>" + bundle.getString("name") + "</font>] 有促销方案"));

                    builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {//启用搭赠方案

                            setGoodsPromotion(bundle);
                        }
                    });

                    builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //添加商品信息在当前页面
                            setGoodsPage(bundle, business);
                            return;
                        }
                    });

                    builder.show();
                } else {
                    //添加商品信息在当前页面
                    setGoodsPage(bundle, business);
                }
            } else if (requestCode == ADD_PROMOTION_REQ_CODE) {//添加促销品的回调
                /**
                 * 返回到订单页
                 */
                setResult(ADD_PROMOTION_REQ_CODE, AddGoodsActivity.this.getIntent().putExtras(bundle));//执行回调事件;

                AddGoodsActivity.this.finish();

            }
        }
    }


    /**
     * 添加商品信息在当前页面
     *
     * @param bundle
     * @param business
     */
    private void setGoodsPage(Bundle bundle, DBBusiness business) {
        fyGoodsName.setContent(bundle.getString("name"));//商品名称

        //商品信息
        aloneGoodsMessageList = business.getBaseGoodsMessage(bundle.getString("sysid"));

        for (Map<String, String> aloneGoodsMessage : aloneGoodsMessageList) {
            if ("1".equals(aloneGoodsMessage.get("Ubase"))) {
                goodsSysID = aloneGoodsMessage.get("GoodsSysID");
                goodsName = aloneGoodsMessage.get("GoodsName");
                barcode = aloneGoodsMessage.get("Barcode");
                standard = aloneGoodsMessage.get("Standard");
                conversion = aloneGoodsMessage.get("Conversion");
                uom = aloneGoodsMessage.get("Uom");
                unitID = aloneGoodsMessage.get("UnitID");
                unitGroupID = aloneGoodsMessage.get("UnitGroupID");
                uBase = aloneGoodsMessage.get("Ubase");
            }
        }

        changeNum = fyGoodsNumber.getInputIsEmpty();
        changePrice = fyPrice.getInputIsEmpty();
        price = business.getBaseGoodsMessagePrice(changeNum, fyFCustNameID, goodsSysID, kISID, unitID, conversion);





        goodsMessage = new GoodsMessage();
        goodsMessage.setGoodsSysID(goodsSysID);
        goodsMessage.setGoodsName(goodsName);
        goodsMessage.setBarcode(barcode);
        goodsMessage.setStandard(standard);
        goodsMessage.setConversion(conversion);
        goodsMessage.setUom(uom);
        goodsMessage.setUnitID(unitID);
        goodsMessage.setUnitGroupID(unitGroupID);


        fyBarcode.setHint(barcode);
        fyStandard.setContent(standard);
        fyUom.setContent(uom);
        fyGoodsNumber.setHint("请输入数量");
        fyGoodsNumber.setText("");


        fyPrice.setHint(AmountShow(Double.parseDouble(price)));
        fyNoPrice.setContent(AmountShow(Double.parseDouble(price)));

        fyPrice.setText("");

        fySumMoney.setHint(AmountShow(0));
        fyNoSumMoney.setContent(AmountShow(0));
    }


    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        setCallbackData();
        super.onBackPressed();
    }

    /**
     * 回调默认数据给上一个Activity
     */
    private void setCallbackData() {
        Bundle bundle = new Bundle();
        bundle.putString("goodsSysID", goodsSysID);
        setResult(-1, AddGoodsActivity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoodsActivity.this.finish();
    }


    /**
     * 保存并退出
     */
    private void saveAddGoods() {
        if (fyGoodsName.getText().equals("必须填写")) {
            Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
            return;
        }

        changeNum = AmountShowPolishing(fyGoodsNumber.getInputIsEmpty());
        changePrice = AmountShowPolishing(fyPrice.getInputIsEmpty());
        changeSum = AmountShowPolishing(fySumMoney.getInputIsEmpty());



        String hintPrice = fyPrice.getHint();
        String noPrice = fyNoPrice.getText();

        String hintSum = fySumMoney.getHint();
        String noSum = fyNoSumMoney.getText();




        if ("0".equals(changeNum)) {
            Toast.makeText(this, "数量不为0", Toast.LENGTH_SHORT).show();
            return;
        }
//        if ("0".equals(fyPrice.getInput())) {
//            Toast.makeText(this, "单价不为0", Toast.LENGTH_SHORT).show();
//            return;
//        }
//
//        if ("0".equals(fySumMoney.getInput())) {
//            Toast.makeText(this, "金额不为0", Toast.LENGTH_SHORT).show();
//            return;
//        }

        Bundle bundle = new Bundle();


        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
            if (stringStringEntry.getValue().equals(fyFDCStock.getText())) {
                bundle.putString("WareHouseSysId", stringStringEntry.getKey().toString()); //仓库id
            }
        }


        bundle.putString("goodsUom", uom);
        bundle.putString("unitID", unitID);

        if ("0".equals(addType)) {//赠品

            bundle.putString("goodsType", "0");
            bundle.putString("goodsSysID", goodsSysID);
            bundle.putString("goodsName", goodsName);
            bundle.putString("goodsNum", changeNum);
            bundle.putString("goodsPrice", "0");
            bundle.putString("goodsSum", "0");

        } else {//


            if ("0.00".equals(hintPrice)) {
                Toast.makeText(this, "单价不为0", Toast.LENGTH_SHORT).show();
                return;
            }

            if ("0.00".equals(noPrice)) {
                Toast.makeText(this, "单价不为0", Toast.LENGTH_SHORT).show();
                return;
            }

//            if ("0.00".equals(hintSum)) {
//                Toast.makeText(this, "金额不为0", Toast.LENGTH_SHORT).show();
//                return;
//            }
//
//            if ("0.00".equals(noSum)) {
//                Toast.makeText(this, "金额不为0", Toast.LENGTH_SHORT).show();
//                return;
//            }


            bundle.putString("goodsType", "1");
            bundle.putString("goodsSysID", goodsSysID);
            bundle.putString("goodsName", goodsName);
            bundle.putString("goodsNum", AmountShowPolishing(changeNum));


            if ("1".equals(fChooseAmount)) {//更改单价金额

                if ("0".equals(changeSum)) {

                    if ("0".equals(changePrice)) {
                        bundle.putString("goodsPrice", hintPrice);
                        bundle.putString("goodsSum", hintSum);
                    } else {

                        bundle.putString("goodsPrice", changePrice);
                        bundle.putString("goodsSum", hintSum);
                    }

                } else {


                    if ("0".equals(changePrice)) {
                        bundle.putString("goodsPrice", hintPrice);
                        bundle.putString("goodsSum", changeSum);
                    } else {
                        bundle.putString("goodsPrice", changePrice);
                        bundle.putString("goodsSum", changeSum);
                    }
                }


            } else {

                bundle.putString("goodsPrice", noPrice);
                bundle.putString("goodsSum", noSum);

            }

        }


        setResult(ADD_GOODS_REQ_CODE, AddGoodsActivity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoodsActivity.this.finish();

    }


}
