package com.fangyi.businessthrough.activity.business;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.text.InputType;
import android.text.TextUtils;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.application.FYApplication.ADD_GOODS_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.getContext;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * 添加赠品  没有价格的
 * Created by FANGYI on 2016/9/22.
 */

public class AddGoods_3_Activity extends BaseActivity implements View.OnClickListener {


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
    private String fDCStockD;//仓库
    private String fStockPosition;//仓库页面
    private String kISID;//用户登陆id

    private Map<String, String> mapWareHouse;//仓库
    private GoodsMessage goodsMessage;//所选商品信息

    private int intUomNum = 1;
    private int intFDCStockNum = 0;

    private List<Map<String, String>> aloneGoodsMessageList;

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

        businessType = this.getIntent().getStringExtra("businessType");
        fDCStockD = this.getIntent().getStringExtra("fDCStockD");
        fStockPosition = this.getIntent().getStringExtra("fStockPosition");
        kISID = this.getIntent().getStringExtra("kISID");

        DBBusiness business = new DBBusiness(getContext());
        mapWareHouse = business.getWareHouse(kISID);
        business.closeDB();

    }

    /**
     * 初始化控件
     */
    private void setinitItemView() {
        tvTitle.setText("添加赠品");
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
        fyGoodsNumber.setInputType(InputType.TYPE_NULL);

        fyStandard.setTitle("规格");

        fyPrice.setVisibility(View.GONE);
        fySumMoney.setVisibility(View.GONE);

        if ("4".equals(businessType)) {
            fyFDCStock.setVisibility(View.GONE);
        }


        if ("0".equals(businessType) || "7".equals(businessType)) {
            fyGoodsName.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyBarcode.setTextColor(CommonUtils.getColor(R.color.text_user_account));
//            fyBarcode.setHintColor(CommonUtils.getColor(R.color.text_user_account));
            fyStandard.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyFDCStock.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyUom.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
            fyGoodsNumber.setTextColor(CommonUtils.getColor(R.color.text_user_account));
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

        AlertDialog.Builder builder = new AlertDialog.Builder(AddGoods_3_Activity.this);
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
    }


    /**
     * 获取商品信息
     */
    private void setGoodsName() {
        Intent intent = new Intent(AddGoods_3_Activity.this, SearchActivity.class);
        intent.putExtra("searchType", ADD_GOODS_REQ_CODE);
        startActivityForResult(intent, ADD_GOODS_REQ_CODE);
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
                //添加商品信息在当前页面
                setGoodsPage(bundle, business);

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

        fyGoodsNumber.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);


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

        fyGoodsNumber.setText("");


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
        setResult(-1, AddGoods_3_Activity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoods_3_Activity.this.finish();
    }


    /**
     * 保存并退出
     */
    private void saveAddGoods() {
        if (fyGoodsName.getText().equals("必须填写")) {
            Toast.makeText(this, "请选择商品", Toast.LENGTH_SHORT).show();
            return;
        }

        Bundle bundle = new Bundle();
        String strGoodsNumber = fyGoodsNumber.getInputIsEmpty();

        bundle.putString("goodsUom", uom);
        bundle.putString("unitID", unitID);
        bundle.putString("goodsType", "0");
        bundle.putString("goodsSysID", goodsSysID);
        bundle.putString("goodsName", goodsName);
        bundle.putString("goodsNum", strGoodsNumber);
        bundle.putString("goodsPrice", "0");
        bundle.putString("goodsSum", "0");

        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
            if (stringStringEntry.getValue().equals(fyFDCStock.getText())) {
                bundle.putString("WareHouseSysId", stringStringEntry.getKey().toString()); //仓库id
            }
        }


        setResult(ADD_GOODS_REQ_CODE, AddGoods_3_Activity.this.getIntent().putExtras(bundle));//执行回调事件;
        AddGoods_3_Activity.this.finish();

    }


}
