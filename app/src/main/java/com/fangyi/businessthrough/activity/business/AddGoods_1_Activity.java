package com.fangyi.businessthrough.activity.business;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.Html;
import android.text.InputType;
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
import com.fangyi.businessthrough.utils.business.CalculationUtils;
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
import static com.fangyi.businessthrough.utils.business.CalculationUtils.div;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.mul;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.StringFilter;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * 带价格政策的 添加商品
 * Created by FANGYI on 2016/9/22.
 */

public class AddGoods_1_Activity extends BaseActivity implements View.OnClickListener {


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
    @BindView(R.id.fy_SumMoney)
    FYEtItemView fySumMoney;
    @BindView(R.id.fy_Standard)
    FYBtnRadioView fyStandard;
    @BindView(R.id.btn_save_add_goods)
    Button btnSaveAddGoods;
    @BindView(R.id.fy_FDCStock)
    FYBtnRadioView fyFDCStock;


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


    private String goodsSysID;
    private String goodsName;
    private String barcode;
    private String standard;
    private String conversion;
    private String uom;
    private String unitID;
    private String unitGroupID;
    private String uBase;


    private String strGoodsNumber = "0";
    private String strPrice = "0";
    private String strSumMoney = "0";

    private String key0 = "3";
    private String key1 = "3";
    private String key2 = "3";
    private String changNumber;
    private String changPrice;
    private String changSumMoney;


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
        tvTitle.setText("添加商品");


        fyGoodsName.setTitle("商品");
        fyGoodsName.setContent("必须填写");
        fyGoodsName.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyBarcode.setTitle("条码");
        fyBarcode.setHint("请输入条形码");
        fyBarcode.setInputTypeNumber();
        fyUom.setTitle("单位");

        fyStandard.setTitle("规格");

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(mapWareHouse.get(fDCStockD));
        if ("0".equals(fStockPosition)) {
            fyFDCStock.setVisibility(View.GONE);
        }

        fyGoodsNumber.setTitle("数量");
        fyGoodsNumber.setHint("请输入数量");
        fyGoodsNumber.setInputTypeNumber();

        fyPrice.setTitle("单价");
        fyPrice.setHint("请输入单价");
        fyPrice.setInputTypeNumber();

        fySumMoney.setTitle("金额");
        fySumMoney.setHint("请输入金额");
        fySumMoney.setInputTypeNumber();


        fyGoodsNumber.setInputType(InputType.TYPE_NULL);
        fyPrice.setInputType(InputType.TYPE_NULL);
        fySumMoney.setInputType(InputType.TYPE_NULL);

        if ("0".equals(businessType)) {
            fyGoodsName.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyBarcode.setTextColor(CommonUtils.getColor(R.color.text_user_account));
//            fyBarcode.setHintColor(CommonUtils.getColor(R.color.text_user_account));
            fyStandard.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyFDCStock.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyUom.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyGoodsNumber.setTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyPrice.setTextColor(CommonUtils.getColor(R.color.text_user_account));
            fySumMoney.setTextColor(CommonUtils.getColor(R.color.text_user_account));
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

                if (!"0".equals(key0)) {
                    return;
                }

                String editable = fyGoodsNumber.getInputIsEmpty();
                strGoodsNumber = StringFilter(editable.toString());
//                KLog.e("===AAA====" + editable);
//                KLog.e("===BBB====" + str);
                if (!editable.equals(strGoodsNumber)) {
                    fyGoodsNumber.setText(strGoodsNumber);
                    fyGoodsNumber.setSelection(strGoodsNumber.length()); //光标置后
                }
                KLog.e("=111111===" + strGoodsNumber);
                calculateSum("0", strGoodsNumber);

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
                strPrice = StringFilter(editable.toString());
                if (!"1".equals(key1)) {
                    return;
                }
                if (!editable.equals(strPrice)) {
                    fyPrice.setText(strPrice);
                    fyPrice.setSelection(strPrice.length()); //光标置后
                }
                KLog.e("=222222===" + strPrice);
                calculateSum("1", strPrice);

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


//                if (!"".equals(s.toString())) {
                String editable = fySumMoney.getInputIsEmpty();
                strSumMoney = StringFilter(editable.toString());

                if (!"2".equals(key2)) {
                    return;
                }
                if (!editable.equals(strSumMoney)) {

                    fySumMoney.setText(strSumMoney);
                    fySumMoney.setSelection(strSumMoney.length()); //光标置后
                }

                KLog.e("=3333333===" + strSumMoney);
                calculateSum("2", strSumMoney);
            }
//            }

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


        DBBusiness business = new DBBusiness(getContext());
        price = business.getBaseGoodsMessagePrice(strGoodsNumber, fyFCustNameID, goodsSysID, kISID, unitID, conversion);

        KLog.e("+================" + price);

        changNumber = fyGoodsNumber.getInput().toString();
        changPrice = fyPrice.getInput().toString();
        changSumMoney = fySumMoney.getInput().toString();


        if ("0".equals(judge)) {//数量
            key0 = "0";
            key1 = "3";
            key2 = "3";
            if ("0".equals(str)) {
                fyPrice.setText(AmountShow(Double.parseDouble(price)));
                fySumMoney.setText("");
                fySumMoney.setHint("请输入金额");
            } else {

                if (TextUtils.isEmpty(changSumMoney)) {
                    fyPrice.setText(AmountShow(Double.parseDouble(price)));
                    fySumMoney.setHint(AmountShow(mul(str, strPrice)));
                } else {
                    KLog.e("strSumMoney===" + strSumMoney);
                    KLog.e("strGoodsNumber===" + str);
                    fyPrice.setText(AmountShow(div(strSumMoney, str, 2)));
                }

            }

            key0 = "0";
            key1 = "1";
            key2 = "2";

        }

        if ("1".equals(judge)) {//单价
            key0 = "0";
            key1 = "1";
            key2 = "3";
            if ("0".equals(str)) {
                fySumMoney.setText("");
                fySumMoney.setHint("请输入金额");
//                fyPrice.setText(AmountShow(Double.parseDouble(price)));
//                fyPrice.setSelection(AmountShow(Double.parseDouble(price)).length()); //光标置后

            } else {

                if (TextUtils.isEmpty(changNumber)) {
                    fySumMoney.setText("");
                    fySumMoney.setHint("请输入金额");
                } else {
                    fySumMoney.setHint(AmountShow(mul(strGoodsNumber, str)));
                }
            }

            key0 = "0";
            key1 = "1";
            key2 = "2";

        }

        if ("2".equals(judge)) {//总金额
            key0 = "0";
            key1 = "3";
            key2 = "2";
            if ("0".equals(str)) {
                fyPrice.setText(AmountShow(Double.parseDouble(price)));
                fySumMoney.setHint(AmountShow(mul(strGoodsNumber, price)));

            } else {

                if (TextUtils.isEmpty(changNumber)) {
                    fyPrice.setText(AmountShow(Double.parseDouble(price)));
                } else {
                    fyPrice.setText(AmountShow(div(str, strGoodsNumber, 2)));
                }
            }

            key0 = "0";
            key1 = "1";
            key2 = "2";
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

        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoods_1_Activity.this);
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
        price = business.getBaseGoodsMessagePrice(strGoodsNumber, fyFCustNameID, goodsSysID, kISID, unitID, conversion);
        business.closeDB();


        key0 = "0";
        key1 = "3";
        key2 = "3";

        changNumber = fyGoodsNumber.getInput().toString();

        if (!TextUtils.isEmpty(changNumber)) {
            fyPrice.setText(AmountShow(Double.parseDouble(price)));
            fySumMoney.setText("");
            fySumMoney.setHint(AmountShow(mul(strGoodsNumber, price)));
        }

        key0 = "0";
        key1 = "1";
        key2 = "2";
    }


    /**
     * 获取商品信息
     */
    private void setGoodsName() {
        Intent intent = new Intent(AddGoods_1_Activity.this, SearchActivity.class);
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
                setResult(ADD_PROMOTION_REQ_CODE, AddGoods_1_Activity.this.getIntent().putExtras(bundle));//执行回调事件;

                AddGoods_1_Activity.this.finish();

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


        goodsMessage = new GoodsMessage();
        goodsMessage.setGoodsSysID(goodsSysID);
        goodsMessage.setGoodsName(goodsName);
        goodsMessage.setBarcode(barcode);
        goodsMessage.setStandard(standard);
        goodsMessage.setConversion(conversion);
        goodsMessage.setUom(uom);
        goodsMessage.setUnitID(unitID);
        goodsMessage.setUnitGroupID(unitGroupID);

        fyGoodsNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);

        if ("1".equals(fChooseAmount)) {//可以改价格
            fyPrice.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            fySumMoney.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        }

        price = business.getBaseGoodsMessagePrice(strGoodsNumber, fyFCustNameID, goodsSysID, kISID, unitID, conversion);

        fyBarcode.setHint(barcode);
        fyStandard.setContent(standard);
        fyUom.setContent(uom);

        fyGoodsNumber.setText("");
        fyPrice.setText(AmountShow(Double.parseDouble(price)));
        fySumMoney.setText("");

        key0 = "0";
        key1 = "1";
        key2 = "2";

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
        setResult(-1, AddGoods_1_Activity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoods_1_Activity.this.finish();
    }


    /**
     * 保存并退出
     */
    private void saveAddGoods() {
        if (fyGoodsName.getText().equals("必须填写")) {
            Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
            return;
        }
        strGoodsNumber = fyGoodsNumber.getInputIsEmpty();
        strPrice = fyPrice.getInputIsEmpty();


        if (CalculationUtils.compare(strGoodsNumber, "0") != 1) {
            Toast.makeText(this, "数量不能为 0", Toast.LENGTH_SHORT).show();
            return;
        }
        if (CalculationUtils.compare(strPrice, "0") != 1) {
            Toast.makeText(this, "单价不能为 0", Toast.LENGTH_SHORT).show();
            return;
        }


        Bundle bundle = new Bundle();

        changSumMoney = fySumMoney.getInput().toString();
        strSumMoney = fySumMoney.getInputIsEmpty();

        KLog.e("===== " + changSumMoney + " ===== " + strSumMoney + " =====" + fySumMoney.getHint());

        if (TextUtils.isEmpty(changSumMoney)) {

            if (CalculationUtils.compare(AmountShowBug(fySumMoney.getHint()), 0) != 1) {
                Toast.makeText(this, "金额不能为 0", Toast.LENGTH_SHORT).show();
                return;
            }

            bundle.putString("goodsSum", fySumMoney.getHint());

        } else {
            if (CalculationUtils.compare(changSumMoney, "0") != 1) {
                Toast.makeText(this, "金额不能为 0", Toast.LENGTH_SHORT).show();
                return;
            }

            bundle.putString("goodsSum", strSumMoney);
        }


        bundle.putString("goodsType", "1");
        bundle.putString("goodsSysID", goodsSysID);
        bundle.putString("goodsName", goodsName);
        bundle.putString("goodsNum", strGoodsNumber);
        bundle.putString("goodsPrice", strPrice);
        bundle.putString("goodsUom", uom);
        bundle.putString("unitID", unitID);


        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
            if (stringStringEntry.getValue().equals(fyFDCStock.getText())) {
                bundle.putString("WareHouseSysId", stringStringEntry.getKey().toString()); //仓库id
            }
        }


        setResult(ADD_GOODS_REQ_CODE, AddGoods_1_Activity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoods_1_Activity.this.finish();

    }


}
