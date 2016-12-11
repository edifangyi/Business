package com.fangyi.businessthrough.activity.business;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.system.SearchActivity;
import com.fangyi.businessthrough.adapter.business.MenuAddGoodsAdapter;
import com.fangyi.businessthrough.adapter.business.MenuAddPromotionAdapter;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.business.SEOrderMain;
import com.fangyi.businessthrough.bean.system.FEntry_26_Set;
import com.fangyi.businessthrough.bean.system.FEntry_27_Set;
import com.fangyi.businessthrough.bean.system.FEntry_28_Set;
import com.fangyi.businessthrough.bean.system.FEntry_29_Set;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.bluetoothprint.PrintDataService;
import com.fangyi.businessthrough.bluetoothprint.PrintUtil;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.events.AddGoodsPromotion;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WebUploadService;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.parameter.SystemFieldValues;
import com.fangyi.businessthrough.utils.business.CalculationUtils;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.utils.system.SerializableMap;
import com.fangyi.businessthrough.view.DrawableCenterButton;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.fangyi.businessthrough.view.FYEtItemView;
import com.fangyi.businessthrough.view.FYLayoutManager;
import com.fangyi.businessthrough.view.ListViewDecoration;
import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.touch.OnItemMoveListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.application.FYApplication.ADD_CUSTOMER_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_GOODS_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_HISTORY_ORDER;
import static com.fangyi.businessthrough.application.FYApplication.ADD_PROMOTION_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.getContext;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add2;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.div;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.mul;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.sub;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTime;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeYYYY_MM_DD_HH_MM_SS;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.StringFilter;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * Created by FANGYI on 2016/10/31.
 */

public class FilterChangeOrderActivity extends BaseActivity implements View.OnClickListener {


    @BindView(R.id.action_bar_back)
    ImageView actionBarBack;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_isupLoad_printnum)
    TextView tvIsupLoadPrintnum;
    @BindView(R.id.fy_FFetchDayV)
    FYBtnRadioView fyFFetchDayV;
    @BindView(R.id.fy_FPlanFetchDay)
    FYBtnRadioView fyFPlanFetchDay;
    @BindView(R.id.fy_FAreaPS)
    FYBtnRadioView fyFAreaPS;
    @BindView(R.id.fy_FSaleStyle)
    FYBtnRadioView fyFSaleStyle;
    @BindView(R.id.fy_FCustName)
    FYBtnRadioView fyFCustName;
    @BindView(R.id.fy_FCustAdd)
    FYBtnRadioView fyFCustAdd;
    @BindView(R.id.fy_FCustPho)
    FYBtnRadioView fyFCustPho;
    @BindView(R.id.fy_FSource)
    FYBtnRadioView fyFSource;
    @BindView(R.id.fy_FEmp)
    FYBtnRadioView fyFEmp;
    @BindView(R.id.fy_FDept)
    FYBtnRadioView fyFDept;
    @BindView(R.id.fy_FDCStock)
    FYBtnRadioView fyFDCStock;
    @BindView(R.id.fy_FDigest)
    FYEtItemView fyFDigest;
    @BindView(R.id.btn_add_goods)
    DrawableCenterButton btnAddGoods;
    @BindView(R.id.btn_add_donation)
    DrawableCenterButton btnAddDonation;
    @BindView(R.id.recycler_view_1)
    SwipeMenuRecyclerView recyclerView1;
    @BindView(R.id.recycler_view_2)
    SwipeMenuRecyclerView recyclerView2;
    @BindView(R.id.tv_fentry_sum)
    TextView tvFentrySum;
    @BindView(R.id.btn_save_order)
    DrawableCenterButton btnSaveOrder;
    @BindView(R.id.btn_print)
    DrawableCenterButton btnPrint;
    @BindView(R.id.btn_upload)
    DrawableCenterButton btnUpload;
    @BindView(R.id.ll_save_uplpad_print)
    LinearLayout llSaveUplpadPrint;

    private String modify;
    private String orderMainID;
    private int printNum;
    private String isUpLoad;
    private String businessType;




    /**
     * 系统用户信息
     */
    private User LoginUser;//登陆用户信息
    private Users LoginUsers;//登陆用户信息金蝶对照表

    /**
     * 基础信息
     */
    private Map<String, String> systemMap;//销售系统参数
    private Map<String, String> mapDepartment;//部门
    private Map<String, String> mapWareHouse;//仓库

    /**
     * 订单
     */
    private PrintOrderMain printOrderMain;//订单

    private List<AddGoodsMessage> goodsInfos = new ArrayList<>();
    private List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos = new ArrayList<>();
    private SEOrderMain seOrderMain;
    /**
     * 控件
     */
    private MenuAddGoodsAdapter mMenuAdapter;
    private MenuAddPromotionAdapter menuAddPromotionAdapter;

    /**
     * 打印
     */
    private PrintDataService printDataService = new PrintDataService();


    /**
     * 临时变量
     */
    private double sums0;
    private double sums1;
    private double sums2;


    Handler handler = new Handler() {



        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:
                    if (mMenuAdapter == null) {
                        mMenuAdapter = new MenuAddGoodsAdapter(goodsInfos);
                        mMenuAdapter.setOnItemClickListener(onItemClickListener);
                        recyclerView1.setAdapter(mMenuAdapter);

                    } else {
                        FYLayoutManager fyLayoutManager = new FYLayoutManager(getContext(), recyclerView1, goodsInfos.size());
                        recyclerView1.setLayoutManager(fyLayoutManager);
                        mMenuAdapter.notifyDataSetChanged();
                    }
                    break;
                case 1:

                    if (menuAddPromotionAdapter == null) {
                        menuAddPromotionAdapter = new MenuAddPromotionAdapter(goodsPromotionInfos);
                        recyclerView2.setAdapter(menuAddPromotionAdapter);
                    } else {
                        FYLayoutManager fyLayoutManager = new FYLayoutManager(getContext(), recyclerView2, goodsPromotionInfos.size());
                        recyclerView2.setLayoutManager(fyLayoutManager);
                        menuAddPromotionAdapter.notifyDataSetChanged();
                    }
                    break;
                case 3:

                    DBBusiness business = new DBBusiness(getContext());
                    Map<String, String> customer = business.selectSQLCustomerAddressTel(fyFCustNameID);

                    for (Map.Entry<String, String> stringStringEntry : customer.entrySet()) {
                        fyFCustAdd.setContent(stringStringEntry.getKey());
                        fyFCustPho.setContent(stringStringEntry.getValue());
                    }

                    fyFSource.setContent("点击请选择源单");
                    sourceOrderID = "";

                    business.closeDB();
                    sums0 = 0;
                    sums1 = 0;
                    sums2 = 0;
                    tvFentrySum.setText(AmountShow(sums2));

                    goodsInfos.clear();
                    goodsPromotionInfos.clear();
                    handler.sendEmptyMessage(0);
                    handler.sendEmptyMessage(1);
                    break;
                case 4:
                    DBBusiness business1 = new DBBusiness(getContext());
                    sums0 = 0;
                    sums1 = 0;
                    sums2 = 0;
                    goodsInfos.clear();
                    goodsPromotionInfos.clear();

                    seOrderMain = business1.getSEOrder(LoginUser.userSysID, sourceOrderID, businessType);


                    KLog.e("=======" + seOrderMain.toString());

                    Data.ArrangeSEOrder(seOrderMain, goodsInfos, goodsPromotionInfos);


                    for (AddGoodsMessage goodsInfo : goodsInfos) {
                        sums2 = add(sums2, Double.valueOf(goodsInfo.getGoodsSum()));
                    }

                    for (HashMap<String, AddGoodsPromotion> goodsPromotionInfo : goodsPromotionInfos) {
                        for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : goodsPromotionInfo.entrySet()) {
                            sums2 = add(sums2, Double.valueOf(stringAddGoodsPromotionEntry.getValue().getGoodsSumPrice()));
                        }
                    }
                    tvFentrySum.setText(AmountShow(sums2));


                    btnAddGoods.setEnabled(false);
                    btnAddDonation.setEnabled(false);

                    business1.closeDB();
                    handler.sendEmptyMessage(0);
                    handler.sendEmptyMessage(1);
                    break;
            }
        }
    };
    private FEntry_29_Set fEntry29Set;
    private FEntry_26_Set fEntry26Set;
    private FEntry_27_Set fEntry27Set;
    private FEntry_28_Set fEntry28Set;
    private String strOrderDate;
    private String strFPlanFetchDay;
    private ProgressDialog pd;

    private String fCheckType;
    private String areaPSName;
    private String saleStyleName;


    private String fyFCustNameID;//客户名称id
    private String sourceOrderID = "";//源单ID


    private int intFAreaPSNum;
    private int intFSaleStyleNum;
    private int intFDeptNum;
    private int intFDCStockNum;
    private String fChooseAmount;
    private String fDCStockD;
    private String fStockPosition;
    private String fPlanPro;
    private String fDelType;
    private String isSaveOrder = "0";
    private String fUpdateType;
    private String fSaFePrint;
    private int listGoodsType;
    private String isSourceOrderID;
    private String changeNum;
    private String changePrice;
    private String changeSum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);//订阅

        setContentView(R.layout.activity_filter_change_order);
        ButterKnife.bind(this);

        loadData();
        assignViews();
        setGlobalEnabled();
        setListener();


    }

    /**
     * 初始化数据
     */
    private void loadData() {
        Intent intent = this.getIntent();
        modify = intent.getStringExtra("modify");
        orderMainID = intent.getStringExtra("orderMainID");
        printNum = Integer.parseInt(intent.getStringExtra("printNum"));
        isUpLoad = intent.getStringExtra("isUpLoad");
        businessType = intent.getStringExtra("businessType");

        systemMap = SystemFieldValues.getSystemValues();


        DBBusiness business = new DBBusiness(getContext());

        printOrderMain = business.getOrderInfo(orderMainID, businessType);//读取单据

        KLog.e("=====@@@@@@@@@@@@@====" + printOrderMain.toString());

        mapDepartment = business.getParameter("部门");
        mapWareHouse = business.getWareHouse(LoginUsers.kISID);
        business.closeDB();

        fyFCustNameID = printOrderMain.customerSysId;
        sourceOrderID = printOrderMain.source;
        strOrderDate = printOrderMain.orderDate;
        strFPlanFetchDay = printOrderMain.deliveryDate;

        sums2 = AmountShowBug(printOrderMain.allMoney);
        Data.ArrangeOrder(printOrderMain, goodsInfos, goodsPromotionInfos);


        DBManager manager = new DBManager(getContext());
        if ("0".equals(businessType)) {//退货单
            fEntry29Set = manager.getFEntry_29_Set(new String[]{printOrderMain.userId, "销售退货单"});
        } else if ("1".equals(businessType)) {//订货
            fEntry26Set = manager.getFEntry_26_Set(new String[]{printOrderMain.userId, "销售订货单"});
        } else if ("2".equals(businessType)) {//出库
            fEntry27Set = manager.getFEntry_27_Set(new String[]{printOrderMain.userId, "销售出库单"});
        } else if ("3".equals(businessType)) {//退货通知
            fEntry28Set = manager.getFEntry_28_Set(new String[]{printOrderMain.userId, "销售退货通知单"});
        }
        manager.closeDB();


        if ("0".equals(businessType)) {//退货单
            fCheckType = fEntry29Set.fCheckType;
            fChooseAmount = fEntry29Set.fChooseAmount;
            fDCStockD = fEntry29Set.fDCStockD;
            fStockPosition = fEntry29Set.fStockPosition;
            fPlanPro = fEntry29Set.fPlanPro;
            fDelType = fEntry29Set.fDelType;
            fUpdateType = fEntry29Set.fUpdateType;
            fSaFePrint = fEntry29Set.fSaFePrint;
            areaPSName = "销售范围";
            saleStyleName = "业务类型";

        } else if ("1".equals(businessType)) {//订货
            fCheckType = fEntry26Set.fCheckType;
            fChooseAmount = fEntry26Set.fChooseAmount;
            fDCStockD = fEntry26Set.fDCStockD;
            fStockPosition = fEntry26Set.fStockPosition;
            fPlanPro = fEntry26Set.fPlanPro;
            fDelType = fEntry26Set.fDelType;
            fUpdateType = fEntry26Set.fUpdateType;
            fSaFePrint = fEntry26Set.fSaFePrint;
            areaPSName = "销售范围";
            saleStyleName = "销售方式";
        } else if ("2".equals(businessType)) {//出库
            fCheckType = fEntry27Set.fCheckType;
            fChooseAmount = fEntry27Set.fChooseAmount;
            fDCStockD = fEntry27Set.fDCStockD;
            fStockPosition = fEntry27Set.fStockPosition;
            fPlanPro = fEntry27Set.fPlanPro;
            fDelType = fEntry27Set.fDelType;
            fUpdateType = fEntry27Set.fUpdateType;
            fSaFePrint = fEntry27Set.fSaFePrint;
            areaPSName = "业务类型";
            saleStyleName = "销售方式";
        } else if ("3".equals(businessType)) {//退货通知
            fCheckType = fEntry28Set.fCheckType;
            fChooseAmount = fEntry28Set.fChooseAmount;
            fDCStockD = fEntry28Set.fDCStockD;
            fStockPosition = fEntry28Set.fStockPosition;
            fPlanPro = fEntry28Set.fPlanPro;
            fDelType = fEntry28Set.fDelType;
            fUpdateType = fEntry28Set.fUpdateType;
            fSaFePrint = fEntry28Set.fSaFePrint;
            areaPSName = "销售范围";
            saleStyleName = "销售方式";
        }


        if (goodsInfos.size() != 0) {
            handler.sendEmptyMessage(0);
        }

        if (goodsPromotionInfos.size() != 0) {
            handler.sendEmptyMessage(1);
        }
    }

    /**
     * 初始化控件
     */
    private void assignViews() {

        tvTitle.setText("订单详情");
        if ("0".equals(printOrderMain.isUpLoad)) {
            tvIsupLoadPrintnum.setText("未上传，打印：" + printNum + " ");
        } else {
            tvIsupLoadPrintnum.setText("已上传，打印：" + printNum + " ");
        }


        if ("0".equals(businessType)) {//退货单
            set29View();

        } else if ("1".equals(businessType)) {//订货
            set26View();

        } else if ("2".equals(businessType)) {//出库

            set27View();

        } else if ("3".equals(businessType)) {//退货通知
            set28View();
        }


        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView1.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView1.addItemDecoration(new ListViewDecoration());// 添加分割线。
        recyclerView1.setItemViewSwipeEnabled(true);
        recyclerView1.setOnItemMoveListener(onItemMoveListener_1);// 监听拖拽，更新UI。


        recyclerView2.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        recyclerView2.addItemDecoration(new ListViewDecoration());
        recyclerView2.setItemViewSwipeEnabled(true);
        recyclerView2.setOnItemMoveListener(onItemMoveListener_2);// 监听拖拽，更新UI。


    }

    private void set28View() {
        fyFFetchDayV.setTitle("通知日期");
        fyFFetchDayV.setContent(getTime(printOrderMain.orderDate));

        fyFPlanFetchDay.setVisibility(View.GONE);

        fyFAreaPS.setTitle(areaPSName);
        fyFAreaPS.setContent(systemMap.get(printOrderMain.areaPS));//默认
        if ("0".equals(fEntry28Set.fAreaPSV)) {
            fyFAreaPS.setVisibility(View.GONE);
        }


        fyFSaleStyle.setTitle(saleStyleName);
        fyFSaleStyle.setContent(systemMap.get(printOrderMain.saleType));//默认
        if ("0".equals(fEntry28Set.fSaleStyleD)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        fyFCustName.setTitle("客户名称");
        fyFCustName.setContent(printOrderMain.customerName);
        fyFCustName.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyFCustAdd.setTitle("客户地址");
        fyFCustAdd.setContent(printOrderMain.customerAddress);
        if ("0".equals(fEntry28Set.fCustAddV)) {
            fyFCustAdd.setVisibility(View.GONE);
        }

        fyFCustPho.setTitle("客户电话");
        fyFCustPho.setContent(printOrderMain.customerTel);
        if ("0".equals(fEntry28Set.fCustPhoV)) {
            fyFCustPho.setVisibility(View.GONE);
        }

        fyFSource.setTitle("选择源单");
        fyFSource.setContent(printOrderMain.source);
        fyFSource.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyFEmp.setTitle("业务员");
        fyFEmp.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry28Set.fEmpV)) {
            fyFEmp.setVisibility(View.GONE);
        }


        fyFDept.setTitle("部门");
        fyFDept.setContent(printOrderMain.deptName);
        if ("0".equals(fEntry28Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(printOrderMain.wareHouseName);
        if ("0".equals(fEntry28Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }
        if ("1".equals(fEntry28Set.fStockPosition)) {//仓库位置在添加商品页面
            fyFDCStock.setVisibility(View.GONE);
        }

        fyFDigest.setTitle("摘要");
        fyFDigest.setText(printOrderMain.message);

        tvFentrySum.setText(printOrderMain.allMoney);

        if ("0".equals(fEntry28Set.fWholePro)) {//关闭整单搭赠
            btnAddDonation.setVisibility(View.GONE);
            fEntry28Set.fPlanPro = "1";
        } else {                                //启动整单搭赠
            fEntry28Set.fPlanPro = "0";
        }

        if (!"0".equals(fEntry28Set.fSaFePrint)) {
            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
        }


        if ("2".equals(fEntry28Set.fOnType)) {//批量
            btnUpload.setVisibility(View.GONE);
        }
    }

    private void set27View() {
        fyFFetchDayV.setTitle("销货日期");
        fyFFetchDayV.setContent(getTime(printOrderMain.orderDate));

        fyFPlanFetchDay.setVisibility(View.GONE);

        fyFAreaPS.setTitle(areaPSName);
        fyFAreaPS.setContent(systemMap.get(printOrderMain.areaPS));//默认
        if ("0".equals(fEntry27Set.fBizTypeD)) {
            fyFAreaPS.setVisibility(View.GONE);
        }


        fyFSaleStyle.setTitle(saleStyleName);
        fyFSaleStyle.setContent(systemMap.get(printOrderMain.saleType));//默认
        if ("0".equals(fEntry27Set.fSaleStyleD)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        fyFCustName.setTitle("客户名称");
        fyFCustName.setContent(printOrderMain.customerName);
        fyFCustName.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyFCustAdd.setTitle("客户地址");
        fyFCustAdd.setContent(printOrderMain.customerAddress);
        if ("0".equals(fEntry27Set.fCustAddV)) {
            fyFCustAdd.setVisibility(View.GONE);
        }

        fyFCustPho.setTitle("客户电话");
        fyFCustPho.setContent(printOrderMain.customerTel);
        if ("0".equals(fEntry27Set.fCustPhoV)) {
            fyFCustPho.setVisibility(View.GONE);
        }

        fyFSource.setTitle("选择源单");
        fyFSource.setContent(printOrderMain.source);
        fyFSource.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyFEmp.setTitle("业务员");
        fyFEmp.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry27Set.fEmpV)) {
            fyFEmp.setVisibility(View.GONE);
        }


        fyFDept.setTitle("部门");
        fyFDept.setContent(printOrderMain.deptName);
        if ("0".equals(fEntry27Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(printOrderMain.wareHouseName);
        if ("0".equals(fEntry27Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }
        if ("1".equals(fEntry27Set.fStockPosition)) {//仓库位置在添加商品页面
            fyFDCStock.setVisibility(View.GONE);
        }


        fyFDigest.setTitle("摘要");
        fyFDigest.setText(printOrderMain.message);


        tvFentrySum.setText(printOrderMain.allMoney);


        if ("0".equals(fEntry27Set.fWholePro)) {//关闭整单搭赠
            btnAddDonation.setVisibility(View.GONE);
            fEntry27Set.fPlanPro = "1";
        } else {                                //启动整单搭赠
            fEntry27Set.fPlanPro = "0";
        }

        if (!"0".equals(fEntry27Set.fSaFePrint)) {
            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
        }


        if ("2".equals(fEntry27Set.fOnType)) {//批量
            btnUpload.setVisibility(View.GONE);
        }
    }

    private void set26View() {
        fyFFetchDayV.setTitle("订货日期");
        fyFFetchDayV.setContent(getTime(printOrderMain.orderDate));


        fyFPlanFetchDay.setTitle("交货日期");
        fyFPlanFetchDay.setContent(getTime(printOrderMain.deliveryDate));
        if ("0".equals(fEntry26Set.FFetchDayV)) {
            fyFPlanFetchDay.setVisibility(View.GONE);
        }


        fyFAreaPS.setTitle(areaPSName);
        fyFAreaPS.setContent(systemMap.get(printOrderMain.areaPS));//默认
        if ("0".equals(fEntry26Set.fAreaPSV)) {
            fyFAreaPS.setVisibility(View.GONE);
        }


        fyFSaleStyle.setTitle(saleStyleName);
        fyFSaleStyle.setContent(systemMap.get(printOrderMain.saleType));//默认
        if ("0".equals(fEntry26Set.FSaleStyleV)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        fyFCustName.setTitle("客户名称");
        fyFCustName.setContent(printOrderMain.customerName);
        fyFCustName.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyFCustAdd.setTitle("客户地址");
        fyFCustAdd.setContent(printOrderMain.customerAddress);
        if ("0".equals(fEntry26Set.fCustAddV)) {
            fyFCustAdd.setVisibility(View.GONE);
        }

        fyFCustPho.setTitle("客户电话");
        fyFCustPho.setContent(printOrderMain.customerTel);
        if ("0".equals(fEntry26Set.fCustPhoV)) {
            fyFCustPho.setVisibility(View.GONE);
        }

        fyFSource.setVisibility(View.GONE);

        fyFEmp.setTitle("业务员");
        fyFEmp.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry26Set.fEmpV)) {
            fyFEmp.setVisibility(View.GONE);
        }


        fyFDept.setTitle("部门");
        fyFDept.setContent(printOrderMain.deptName);
        if ("0".equals(fEntry26Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(printOrderMain.wareHouseName);
        if ("0".equals(fEntry26Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }
        if ("1".equals(fEntry26Set.fStockPosition)) {//仓库位置在添加商品页面
            fyFDCStock.setVisibility(View.GONE);
        }


        fyFDigest.setTitle("摘要");
        fyFDigest.setText(printOrderMain.message);

        tvFentrySum.setText(printOrderMain.allMoney);

        if ("0".equals(fEntry26Set.fWholePro)) {
            btnAddDonation.setVisibility(View.GONE);
            fEntry26Set.fPlanPro = "1";//关闭搭赠方案
        } else {
            fEntry26Set.fPlanPro = "0";//启动搭赠方案
        }

        if (!"0".equals(fEntry26Set.fSaFePrint)) {
            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
        }

        if ("2".equals(fEntry26Set.fOnType)) {//批量
            btnUpload.setVisibility(View.GONE);
        }
    }

    private void set29View() {
        fyFFetchDayV.setTitle("退货日期");
        fyFFetchDayV.setContent(getTime(printOrderMain.orderDate));
        fyFFetchDayV.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));


        fyFPlanFetchDay.setVisibility(View.GONE);


        fyFAreaPS.setTitle(areaPSName);
        fyFAreaPS.setContent(systemMap.get(printOrderMain.areaPS));//默认
        fyFAreaPS.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        if ("0".equals(fEntry29Set.fBizTypeD)) {
            fyFAreaPS.setVisibility(View.GONE);
        }


        fyFSaleStyle.setTitle(saleStyleName);
        fyFSaleStyle.setContent(systemMap.get(printOrderMain.saleType));//默认
        fyFSaleStyle.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        if ("0".equals(fEntry29Set.fSaleStyleD)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        fyFCustName.setTitle("客户名称");
        fyFCustName.setContent(printOrderMain.customerName);
        fyFCustName.setContentTextColor(CommonUtils.getColor(R.color.text_green));

        fyFCustAdd.setTitle("客户地址");
        fyFCustAdd.setContent(printOrderMain.customerAddress);
        fyFCustAdd.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        if ("0".equals(fEntry29Set.fCustAddV)) {
            fyFCustAdd.setVisibility(View.GONE);
        }

        fyFCustPho.setTitle("客户电话");
        fyFCustPho.setContent(printOrderMain.customerTel);
        fyFCustPho.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));

        if ("0".equals(fEntry29Set.fCustPhoV)) {
            fyFCustPho.setVisibility(View.GONE);
        }

        fyFSource.setVisibility(View.GONE);

        fyFEmp.setTitle("业务员");
        fyFEmp.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        fyFEmp.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry29Set.fEmpV)) {
            fyFEmp.setVisibility(View.GONE);
        }


        fyFDept.setTitle("部门");
        fyFDept.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        fyFDept.setContent(printOrderMain.deptName);
        if ("0".equals(fEntry29Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContentTextColor(CommonUtils.getColor(R.color.text_user_account));
        fyFDCStock.setContent(printOrderMain.wareHouseName);
        if ("0".equals(fEntry29Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }
        if ("1".equals(fEntry29Set.fStockPosition)) {//仓库位置在添加商品页面
            fyFDCStock.setVisibility(View.GONE);
        }


        fyFDigest.setTitle("摘要");
        fyFDigest.setText(printOrderMain.message);
        fyFDigest.setTextColor(CommonUtils.getColor(R.color.text_user_account));


        fyFDigest.setText(printOrderMain.message);

        tvFentrySum.setText(printOrderMain.allMoney);

        if ("0".equals(fEntry29Set.fWholePro)) {
            btnAddDonation.setVisibility(View.GONE);
            fEntry29Set.fPlanPro = "1";//关闭搭赠方案
        } else {
            fEntry29Set.fPlanPro = "0";//启动搭赠方案
        }

        if (!"0".equals(fEntry29Set.fSaFePrint)) {
            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
        }


        if ("2".equals(fEntry29Set.fOnType)) {//批量

            btnUpload.setVisibility(View.GONE);
        }
    }


    /**
     * 设置全局按钮点击控制
     */
    private void setGlobalEnabled() {

        if ("0".equals(modify)) {
            setClickEvent(false);
            btnUpload.setEnabled(true);
            btnPrint.setEnabled(true);

        } else {

            switch (fUpdateType) {
                case "0":
                    setClickEvent(false);
                    btnUpload.setEnabled(false);
                    btnPrint.setEnabled(false);
                    break;
                case "1":
                    if (printNum > 0) {
                        setClickEvent(false);
                    } else {
                        setClickEvent(true);
                        btnUpload.setEnabled(false);
                        btnPrint.setEnabled(false);
                    }
                    break;
                case "2":
                    if ("1".equals(isUpLoad)) {
                        setClickEvent(false);
                    } else {
                        setClickEvent(true);
                        btnUpload.setEnabled(false);
                        btnPrint.setEnabled(false);
                    }
                    break;
            }
        }

        /**
         * 未修改，重复订单
         */
        if ("0".equals(isSaveOrder) && "1".equals(isUpLoad)) {
            btnUpload.setEnabled(false);
        }
    }

    /**
     * 按钮设置
     *
     * @param clickEvent
     */

    public void setClickEvent(boolean clickEvent) {
        actionBarBack.setEnabled(clickEvent);

        fyFFetchDayV.setEnabled(clickEvent);
        fyFPlanFetchDay.setEnabled(clickEvent);

        fyFAreaPS.setEnabled(clickEvent);
        fyFSaleStyle.setEnabled(clickEvent);

        fyFCustName.setEnabled(clickEvent);

        fyFSource.setEnabled(clickEvent);

        fyFEmp.setEnabled(clickEvent);
        fyFDept.setEnabled(clickEvent);
        fyFDCStock.setEnabled(clickEvent);

        btnAddGoods.setEnabled(clickEvent);
        btnAddDonation.setEnabled(clickEvent);

        btnUpload.setEnabled(clickEvent);
        btnPrint.setEnabled(clickEvent);
        btnSaveOrder.setEnabled(clickEvent);


        recyclerView1.setItemViewSwipeEnabled(clickEvent);
        recyclerView2.setItemViewSwipeEnabled(clickEvent);
        fyFDigest.setInputType(InputType.TYPE_NULL);
    }

    /**
     ********************************************************************************************
     ********************************************************************************************
     */


    /**
     * 点击事件
     */
    private void setListener() {

        actionBarBack.setOnClickListener(this);

        fyFFetchDayV.setOnClickListener(this);
        fyFPlanFetchDay.setOnClickListener(this);

        fyFAreaPS.setOnClickListener(this);
        fyFSaleStyle.setOnClickListener(this);

        fyFCustName.setOnClickListener(this);

        fyFSource.setOnClickListener(this);

        fyFEmp.setOnClickListener(this);
        fyFDept.setOnClickListener(this);
        fyFDCStock.setOnClickListener(this);

        btnAddGoods.setOnClickListener(this);
        btnAddDonation.setOnClickListener(this);

        btnUpload.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnSaveOrder.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.action_bar_back://商品名称
                setCallback();
                break;
            case R.id.fy_FFetchDayV:
                setFFetchDay();//订单日期
                break;
            case R.id.fy_FPlanFetchDay:
                setFPlanFetchDay();//交货日期
                break;
            case R.id.fy_FAreaPS:
                setFAreaPS();//销售范围
                break;
            case R.id.fy_FSaleStyle:
                setFSaleStyle();//销售方式
                break;
            case R.id.fy_FCustName://客户名称
                setFCustName();
                break;
            case R.id.fy_FSource://选择源单
                setFSource();
                break;
            case R.id.fy_FDept://部门
                setFDept();
                break;
            case R.id.fy_FDCStock://仓库
                setFDCStock();
                break;
            case R.id.btn_add_goods://添加商品
                startAddGoods();
                break;
            case R.id.btn_add_donation://添加赠品
                startAddDonation();
                break;
            case R.id.btn_print://打印
                setBtnPrint();
                break;
            case R.id.btn_upload://上传
                uploadOrder();
                break;
            case R.id.btn_save_order://保存
                saveOrder();
                break;
        }
    }



    /**
     * 返回键
     */
    @Override
    public void onBackPressed() {
        setCallback();
        super.onBackPressed();
    }

    private void setCallback() {
        Bundle bundle = new Bundle();
        setResult(FYApplication.THE_CALLBACK_TO_REFRESH, FilterChangeOrderActivity.this.getIntent().putExtras(bundle));//执行回调事件;
        FilterChangeOrderActivity.this.finish();
    }

    /**
     * 当Item移动的时候。
     */
    private OnItemMoveListener onItemMoveListener_1 = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(goodsInfos, fromPosition, toPosition);
            mMenuAdapter.notifyItemMoved(fromPosition, toPosition);
            return true;
        }

        @Override
        public void onItemDismiss(int position) {


            if (goodsInfos.get(position).getGoodsSum() != null) {
                sums2 = sub(sums2, AmountShowBug(goodsInfos.get(position).getGoodsSum()));
                tvFentrySum.setText(AmountShow(sums2));
            }

            goodsInfos.remove(position);
            mMenuAdapter.notifyItemRemoved(position);

            invalidOrder();

        }


    };


    /**
     * 当Item移动的时候。
     */
    private OnItemMoveListener onItemMoveListener_2 = new OnItemMoveListener() {
        @Override
        public boolean onItemMove(int fromPosition, int toPosition) {
            Collections.swap(goodsPromotionInfos, fromPosition, toPosition);
            menuAddPromotionAdapter.notifyItemMoved(fromPosition, toPosition);

            return true;
        }

        @Override
        public void onItemDismiss(int position) {

            if (goodsPromotionInfos.size() != 0) {
                HashMap<String, AddGoodsPromotion> ccc = goodsPromotionInfos.get(position);
                for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : ccc.entrySet()) {

                    sums1 = add(sums1, Double.parseDouble(stringAddGoodsPromotionEntry.getValue().getGoodsSumPrice()));
                }

                sums2 = sub(sums2, sums1);
                sums1 = 0;
            }

            tvFentrySum.setText(AmountShow(sums2));

            goodsPromotionInfos.remove(position);
            menuAddPromotionAdapter.notifyItemRemoved(position);

            invalidOrder();
        }


    };

    /***
     * 无效订单
     */
    private void invalidOrder() {
        if (goodsInfos.size() == 0 && goodsPromotionInfos.size() == 0) {

            setClickEvent(true);
            btnSaveOrder.setEnabled(true);

            isSaveOrder = "0";
            fyFCustNameID = "";//客户名称id
            sourceOrderID = "";//源单ID

            fyFCustName.setContent("请点击选择客户名称");
            fyFSource.setContent("请点击选择源单");

            goodsInfos.clear();
            goodsPromotionInfos.clear();

            Toast.makeText(getContext(), "请重新选择客户或源单", Toast.LENGTH_SHORT).show();

            isSourceOrderID = "";
        }
    }


    private FYEtItemView fyNum;
    private FYEtItemView fyPrice;
    private FYEtItemView fySumMoney;

    private String key0 = "3";
    private String key1 = "3";
    private String key2 = "3";


    private String strGoodsNumber = "0";
    private String strPrice = "0";
    private String strSumMoney = "0";

    private String price;
    private String sumMoney;
    private String changNumber;
    private String changPrice;
    private String changSumMoney;
    private String notNum;

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {

            if ("0".equals(businessType) || "1".equals(businessType)) {
                return;
            }


            if ("0".equals(modify)) {
                return;

            } else {

                if ("0".equals(fUpdateType)) {
                    return;
                } else if ("1".equals(fUpdateType)) {
                    if (printNum > 0) {
                        return;
                    }
                } else if ("2".equals(fUpdateType)) {
                    if ("1".equals(isUpLoad)) {
                        return;
                    }
                }
            }


            if ("1".equals(isSaveOrder)) {
                return;
            }


            final AlertDialog.Builder builder = new AlertDialog.Builder(FilterChangeOrderActivity.this);
            builder.setTitle("更改");

            View view = View.inflate(getContext(), R.layout.dialog_sale_seorder_item_change, null);
            fyNum = (FYEtItemView) view.findViewById(R.id.fy_num);
            fyPrice = (FYEtItemView) view.findViewById(R.id.fy_Price);
            fySumMoney = (FYEtItemView) view.findViewById(R.id.fy_SumMoney);

            fyNum.setTitle("数量");
            fyNum.setHint("请输入 ≤ " + goodsInfos.get(position).getNotNum());
            fyNum.setText(goodsInfos.get(position).getNotNum());
            fyNum.setInputTypeNumber();
            fyNum.setSelection(goodsInfos.get(position).getNotNum().length()); //光标置后

            fyPrice.setTitle("单价");
            fyPrice.setHint("请输入单价");
            fyPrice.setText(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
            fyPrice.setInputTypeNumber();

            fySumMoney.setTitle("金额");
            fySumMoney.setHint("请输入金额");
            fySumMoney.setText(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsSum())));
            fySumMoney.setInputTypeNumber();

            if (!"1".equals(fChooseAmount)) {//不可以改价格
                fyPrice.setInputType(InputType.TYPE_NULL);
                fySumMoney.setInputType(InputType.TYPE_NULL);
            }

            fyNum.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {


                    if (!"0".equals(key0)) {
                        return;
                    }


                    String editable = fyNum.getInputIsEmpty();
                    strGoodsNumber = StringFilter(editable.toString());

                    if (!editable.equals(strGoodsNumber)) {
                        fyNum.setText(strGoodsNumber);
                        fyNum.setSelection(strGoodsNumber.length()); //光标置后
                    }
                    KLog.e("=111111===" + strGoodsNumber);
                    calculateSum("0", position, strGoodsNumber);
                }

                @Override
                public void afterTextChanged(Editable editable) {

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

                    calculateSum("1", position, strPrice);
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
                    strSumMoney = StringFilter(editable.toString());

                    if (!"2".equals(key2)) {
                        return;
                    }
                    if (!editable.equals(strSumMoney)) {

                        fySumMoney.setText(strSumMoney);
                        fySumMoney.setSelection(strSumMoney.length()); //光标置后
                    }

                    calculateSum("2", position, strSumMoney);
                }

                @Override
                public void afterTextChanged(Editable s) {

                }
            });


            builder.setView(view, 15, 30, 15, 0);
            builder.setOnKeyListener(new DialogInterface.OnKeyListener() {
                                         public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                                             if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
                                                 return true;
                                             } else {
                                                 return false;
                                             }
                                         }
                                     }
            );

            builder.setCancelable(false);


            builder.setPositiveButton("确定", null);
            builder.setNegativeButton("取消", null);

            final AlertDialog dialog = builder.create();
            dialog.show();
            dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    over(position, dialog);
                }
            });


        }
    };

    /**
     * 保存
     */
    private void over(int position, AlertDialog dialog) {

        strGoodsNumber = fyNum.getInputIsEmpty();
        strPrice = fyPrice.getInputIsEmpty();
        strSumMoney = fySumMoney.getInputIsEmpty();

        if (CalculationUtils.compare(strGoodsNumber, "0") != 1) {
            Toast.makeText(getContext(), "数量不能为 0", Toast.LENGTH_SHORT).show();


            return;
        }
        if (CalculationUtils.compare(strPrice, "0") != 1) {
            Toast.makeText(getContext(), "单价不能为 0", Toast.LENGTH_SHORT).show();
            return;
        }
        changSumMoney = fySumMoney.getInput().toString();
        if (TextUtils.isEmpty(changSumMoney)) {

            if (CalculationUtils.compare(AmountShowBug(fySumMoney.getHint()), 0) != 1) {
                Toast.makeText(getContext(), "金额不能为 0", Toast.LENGTH_SHORT).show();
                return;
            }

            goodsInfos.get(position).setGoodsSum(fySumMoney.getHint());

        } else {
            if (CalculationUtils.compare(changSumMoney, "0") != 1) {
                Toast.makeText(getContext(), "金额不能为 0", Toast.LENGTH_SHORT).show();
                return;
            }

            goodsInfos.get(position).setGoodsSum(strSumMoney);
        }

        goodsInfos.get(position).setGoodsPrice(strPrice);
        goodsInfos.get(position).setNotNum(strGoodsNumber);



        sums2 = 0;
        for (AddGoodsMessage goodsInfo : goodsInfos) {
            sums2 = add(sums2, Double.valueOf(goodsInfo.getGoodsSum()));
        }

        for (HashMap<String, AddGoodsPromotion> goodsPromotionInfo : goodsPromotionInfos) {
            for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : goodsPromotionInfo.entrySet()) {
                sums2 = add(sums2, Double.valueOf(stringAddGoodsPromotionEntry.getValue().getGoodsSumPrice()));
            }
        }

        tvFentrySum.setText(AmountShow(sums2));
        key0 = "3";
        key1 = "3";
        key2 = "3";
        dialog.dismiss();

        handler.sendEmptyMessage(0);
    }

    /**
     * 计算修改价格
     */
    private void calculateSum(String judge, int position, String str) {
        notNum = goodsInfos.get(position).getNotNum();
        price = AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice()));
        sumMoney = AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsSum()));


        changNumber = fyNum.getInput().toString();
        changPrice = fyPrice.getInput().toString();
        changSumMoney = fySumMoney.getInput().toString();

        if ("0".equals(judge)) {//数量
            key0 = "0";
            key1 = "3";
            key2 = "3";


            if ("0".equals(str)) {

                if (!"1".equals(fChooseAmount)) {//不可以改价格
                    fyPrice.setText(AmountShow(Double.parseDouble(price)));
                    fySumMoney.setHint(AmountShow(mul(str, strPrice)));

                } else {

                    fyPrice.setText("");
                    fySumMoney.setText("");
                    fySumMoney.setHint("请输入金额");

                }

            } else {

                if (CalculationUtils.compare(notNum, changNumber) == -1) {

                    fyNum.setText("");
                    fyPrice.setText(price);
                    Toast.makeText(getContext(), "请输入 ≤ " + notNum, Toast.LENGTH_SHORT).show();

                } else {


                    if (!"1".equals(fChooseAmount)) {//不可以改价格

                        fyPrice.setText(AmountShow(Double.parseDouble(price)));
                        fySumMoney.setHint(AmountShow(mul(str, strPrice)));

                    } else {
                        if (TextUtils.isEmpty(changSumMoney)) {
                            fyPrice.setText(AmountShow(Double.parseDouble(price)));
                            fySumMoney.setHint(AmountShow(mul(str, strPrice)));
                        } else {
                            fyPrice.setText(AmountShow(div(strSumMoney, str, 2)));
                        }
                    }
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



    /**
     * *******************************************************************************************
     * *******************************************************************************************
     */

    private void uploadOrder() {


        if ("0".equals(isSaveOrder) && "1".equals(isUpLoad)) {
            Toast.makeText(this, "订单重复上传", Toast.LENGTH_SHORT).show();
            return;
        }


        String result = WebUploadService.uploadSaleDateService(FilterChangeOrderActivity.this, orderMainID, businessType);


        if ("上传失败".equals(result)) {
            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();

            btnUpload.setEnabled(false);
            isUpLoad = "1";
            tvIsupLoadPrintnum.setText("已上传，打印：" + printNum + " ");

            if ("2".equals(fUpdateType)) {//上传后不可以修改
                setClickEvent(false);
                btnPrint.setEnabled(true);
            }
        }

    }

    private void setBtnPrint() {
        if (!printDataService.isBluetoothEnable()) {
            Toast.makeText(getContext(), "手机蓝牙未打开", Toast.LENGTH_LONG).show();
            return;
        }

        if ("0".equals(isSaveOrder)) {
            Toast.makeText(this, "订单未保存", Toast.LENGTH_SHORT).show();
            return;
        }

        BluetoothTask bluetoothTask = new BluetoothTask();
        bluetoothTask.execute();

        if ("1".equals(fUpdateType)) {//打印后不可以修改
            setClickEvent(false);
            btnUpload.setEnabled(true);
        }
    }

    private void saveOrder() {
        if (TextUtils.isEmpty(fyFCustNameID)) {
            Toast.makeText(getContext(), "请选择客户", Toast.LENGTH_SHORT).show();
            return;
        }

        if (goodsInfos.size() == 0 && goodsPromotionInfos.size() == 0) {
            Toast.makeText(getContext(), "请添加商品", Toast.LENGTH_SHORT).show();
            return;
        }


        if ("0".equals(isSaveOrder)) {
            save();
            saveSettings();
        } else {
            Toast.makeText(getContext(), "已经保存完成，请不要再保存", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 保存OrderMain表
     */
    private void save() {


        String ID = orderMainID; //订单ID
        String OrderDate = strOrderDate; //单据业务日期
        String DeliveryDate = strFPlanFetchDay; //
        String CustomerSySID = fyFCustNameID; //客户id即cusotmer里的sysid
        String WareHouseName;
        if ("1".equals(fEntry26Set.fStockPosition)) {//仓库位置在添加商品页面
            WareHouseName = ""; //仓库名称
        } else {
            WareHouseName = fyFDCStock.getText();
        }
        String UserSysID = printOrderMain.userSysId; //业务员id kisid
        String SaveTime = getTimeYYYY_MM_DD_HH_MM_SS(new Date()); //单据保存日期
        String IsUpLoad = isUpLoad; //是否上传
        String Message = fyFDigest.getInput(); //备注
        String DeptID = fyFDept.getText(); //部门id
        String BusinessType = businessType; //订单类型
        String FAreaPS = fyFAreaPS.getText(); //销售范围
        String FSaleType = fyFSaleStyle.getText(); //销售方式
        String AllMoney = tvFentrySum.getText().toString(); //总价格
        String FUpdateType = fUpdateType; //修改控制
        String FDelType = fDelType; //删除控制
        String PrintNum = String.valueOf(printNum); //订单类型
        String fyFSource = sourceOrderID; //源单

        Data.saveOrder(ID, OrderDate, DeliveryDate, CustomerSySID, WareHouseName, mapWareHouse, UserSysID, SaveTime, IsUpLoad, Message, DeptID, mapDepartment, BusinessType, FAreaPS, FSaleType, systemMap, AllMoney, FUpdateType, FDelType, PrintNum, fyFSource, goodsInfos, goodsPromotionInfos);

        Toast.makeText(getContext(), "保存完成", Toast.LENGTH_SHORT).show();
        isSaveOrder = "1";
        tvTitle.setText("订单已就修改");
    }

    private void saveSettings() {
        btnSaveOrder.setEnabled(false);


        if ("0".equals(fSaFePrint)) {

            btnPrint.setEnabled(true);

            //判断网络是否可用
            if (NetConnectionUtil.isNetworkAvailable(getContext())) {
                //判断服务器是否通信
                if (NetConnectionUtil.pingService(NetConnectionUtil.getIPAddress(PrefUtils.getString(getContext(), "is_service_address", null)))) {
                    btnUpload.setEnabled(true);
                }
            } else {
                btnUpload.setEnabled(false);
            }

        } else {
            setBtnPrint();
            //判断网络是否可用
            if (NetConnectionUtil.isNetworkAvailable(getContext())) {
                //判断服务器是否通信
                if (NetConnectionUtil.pingService(NetConnectionUtil.getIPAddress(PrefUtils.getString(getContext(), "is_service_address", null)))) {
                    btnUpload.setEnabled(true);
                }
            } else {
                btnUpload.setEnabled(false);
            }
        }

        if ("0".equals(fUpdateType)) {//保存后不可以修改
            setClickEvent(false);
        }
    }

    private void setFSource() {
        if (fyFCustNameID != null) {
            Intent intent = new Intent(getContext(), SearchSEOrderMainActivity.class);
            intent.putExtra("userSysID", LoginUser.userSysID);
            intent.putExtra("fyFCustNameID", fyFCustNameID);
            intent.putExtra("businessType", "1");
            startActivityForResult(intent, ADD_HISTORY_ORDER);
        } else {
            Toast.makeText(getContext(), "请先选择客户", Toast.LENGTH_SHORT).show();
        }
    }


    private void startAddDonation() {
        if (fyFCustNameID != null) {
            Intent intent = new Intent(getContext(), AddGoods_3_Activity.class);

            intent.putExtra("businessType", businessType);//businessType
            intent.putExtra("fDCStockD", fDCStockD);//仓库
            intent.putExtra("fStockPosition", fStockPosition);//仓库 在添加页面
            intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID


            startActivityForResult(intent, ADD_GOODS_REQ_CODE);
        } else {
            Toast.makeText(getContext(), "请先选择客户", Toast.LENGTH_SHORT).show();
        }
    }

    private void startAddGoods() {

        if (fyFCustNameID != null) {
            Intent intent = new Intent(getContext(), AddGoods_1_Activity.class);

            intent.putExtra("businessType", businessType);//businessType
            intent.putExtra("fChooseAmount", fChooseAmount);//允许修改单价金额
            intent.putExtra("fDCStockD", fDCStockD);//仓库
            intent.putExtra("fStockPosition", fStockPosition);//仓库 在添加页面
            intent.putExtra("fPlanPro", fPlanPro);//启用搭赠方案
            intent.putExtra("userSysID", LoginUsers.userSysID);//传登陆用户ID
            intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID

            startActivityForResult(intent, ADD_GOODS_REQ_CODE);
        } else {
            Toast.makeText(getContext(), "请先选择客户", Toast.LENGTH_SHORT).show();
        }
    }

    private void setFDCStock() {
        final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

        final String[] arrWareHouse = getStringsFormat(mapValuesList);

        if (!"1".equals(fCheckType)) {
            if (intFDCStockNum <= mapWareHouse.size()) {
                fyFDCStock.setContent(mapValuesList.get(intFDCStockNum));
                intFDCStockNum++;
                if (intFDCStockNum == mapWareHouse.size()) {
                    intFDCStockNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FilterChangeOrderActivity.this);
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
    }

    private void setFDept() {

        final List<String> mapValuesList = new ArrayList<>(mapDepartment.values());

        final String[] arr = getStringsFormat(mapValuesList);

        if (!"1".equals(fCheckType)) {
            if (intFDeptNum <= mapDepartment.size()) {
                fyFDept.setContent(mapValuesList.get(intFDeptNum));
                intFDeptNum++;
                if (intFDeptNum == mapDepartment.size()) {
                    intFDeptNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(FilterChangeOrderActivity.this);
            builder.setTitle("部门选择");
            builder.setSingleChoiceItems(arr, intFDeptNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFSaleStyleNum = which;
                    //2.设置
                    fyFDept.setContent(arr[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }

    }

    private void setFCustName() {
        Intent intent = new Intent(getContext(), SearchActivity.class);
        intent.putExtra("searchType", ADD_CUSTOMER_REQ_CODE);
        intent.putExtra("loginUserSysID", LoginUsers.kISID);
        startActivityForResult(intent, ADD_CUSTOMER_REQ_CODE);
    }

    private void setFSaleStyle() {
        final String items[] = {systemMap.get("100"), systemMap.get("101"), systemMap.get("102"), systemMap.get("103"), systemMap.get("20297")};


        if (!"1".equals(fCheckType)) {
            //滑动
            if (intFSaleStyleNum <= items.length) {
                fyFSaleStyle.setContent(items[intFSaleStyleNum]);
                intFSaleStyleNum++;
                if (intFSaleStyleNum == items.length) {
                    intFSaleStyleNum = 0;
                }
            }
        } else {
            //弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(FilterChangeOrderActivity.this);

            builder.setTitle(saleStyleName);
            final String[] finalItems = items;
            builder.setSingleChoiceItems(items, intFSaleStyleNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFSaleStyleNum = which;
                    //2.设置
                    fyFSaleStyle.setContent(finalItems[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }

    }

    private void setFAreaPS() {
        String[] items = {};

        if ("0".equals(businessType) || "2".equals(businessType)) {
            items = new String[]{systemMap.get("12530"), systemMap.get("12531")};
        } else if ("1".equals(businessType) || "3".equals(businessType)) {
            items = new String[]{systemMap.get("20302"), systemMap.get("20303")};
        }


        if (!"1".equals(fCheckType)) {
            //滑动
            if (intFAreaPSNum <= items.length) {
                fyFAreaPS.setContent(items[intFAreaPSNum]);
                intFAreaPSNum++;
                if (intFAreaPSNum == items.length) {
                    intFAreaPSNum = 0;
                }
            }
        } else {
            //弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(FilterChangeOrderActivity.this);

            builder.setTitle(areaPSName);
            final String[] finalItems = items;
            builder.setSingleChoiceItems(items, intFAreaPSNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFAreaPSNum = which;
                    //2.设置
                    fyFAreaPS.setContent(finalItems[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }

    private void setFPlanFetchDay() {
        TimePickerView pvTime = new TimePickerView(FilterChangeOrderActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setTitle("交货日期");
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                strFPlanFetchDay = getTimeYYYY_MM_DD_HH_MM_SS(date);
                fyFPlanFetchDay.setContent(getTime(date));
            }
        });
        pvTime.show();
    }

    private void setFFetchDay() {
        TimePickerView pvTime = new TimePickerView(FilterChangeOrderActivity.this, TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());


        if ("0".equals(businessType)) {//退货单
            pvTime.setTitle("退货日期");
        } else if ("1".equals(businessType)) {//订货
            pvTime.setTitle("订货日期");
        } else if ("2".equals(businessType)) {//出库
            pvTime.setTitle("销货日期");
        } else if ("3".equals(businessType)) {//退货通知
            pvTime.setTitle("通知日期");
        }


        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                strOrderDate = getTimeYYYY_MM_DD_HH_MM_SS(date);
                fyFFetchDayV.setContent(getTime(date));
            }
        });
        pvTime.show();
    }
    /**
     ********************************************************************************************
     ********************************************************************************************
     */

    /**
     * Activity回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();

        if (requestCode != -1) {

            if (resultCode == ADD_CUSTOMER_REQ_CODE) {//选择客户


                fyFCustName.setContent(bundle.getString("name"));
                fyFCustNameID = bundle.getString("sysid");

                handler.sendEmptyMessage(3);


            } else if (resultCode == ADD_GOODS_REQ_CODE) {//选择商品
                if (!TextUtils.isEmpty(bundle.getString("goodsUom"))) {

                    AddGoodsMessage goosMessage = new AddGoodsMessage();
                    goosMessage.setGoodsSysID(bundle.getString("goodsSysID"));
                    goosMessage.setGoodsName(bundle.getString("goodsName"));
                    goosMessage.setGoodsNum(bundle.getString("goodsNum"));
                    goosMessage.setGoodsPrice(bundle.getString("goodsPrice"));
                    goosMessage.setGoodsSum(bundle.getString("goodsSum"));
                    goosMessage.setGoodsUom(bundle.getString("goodsUom"));
                    goosMessage.setUnitID(bundle.getString("unitID"));
                    goosMessage.setGoodsType(bundle.getString("goodsType"));
                    goosMessage.setWareHouseSysId(bundle.getString("WareHouseSysId"));
                    goosMessage.setListGoodsType("0");


                    sums0 = add(sums0, AmountShowBug(bundle.getString("goodsSum")));
                    sums2 = add2(sums2, sums0, sums1);
                    tvFentrySum.setText(AmountShow(sums2));
                    sums0 = 0;


                    goodsInfos.add(goosMessage);
                    handler.sendEmptyMessage(0);
                }

            } else if (resultCode == ADD_PROMOTION_REQ_CODE) {//促销品

                listGoodsType++;

                SerializableMap MapAddGoodsPromotion = (SerializableMap) bundle.get("addGoodsPromotionMap");
                HashMap<String, AddGoodsPromotion> addGoodsPromotionMap = MapAddGoodsPromotion.getMapAddGoodsPromotion();


                String goodsSysID = null;
                for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : addGoodsPromotionMap.entrySet()) {
                    stringAddGoodsPromotionEntry.getValue().setListGoodsType(String.valueOf(listGoodsType));
                    sums1 = add(sums1, Double.parseDouble(stringAddGoodsPromotionEntry.getValue().getGoodsSumPrice()));
                    goodsSysID = stringAddGoodsPromotionEntry.getValue().getGoodsSysID();
                }

                sums2 = add2(sums2, sums0, sums1);
                sums1 = 0;

                tvFentrySum.setText(AmountShow(sums2));

                if (!TextUtils.isEmpty(goodsSysID)) {
                    goodsPromotionInfos.add(addGoodsPromotionMap);
                    handler.sendEmptyMessage(1);
                }

            } else if (resultCode == ADD_HISTORY_ORDER) {//查找订单号

                if (!"请点击选择源单".equals(bundle.getString("orderID"))) {
                    sourceOrderID = bundle.getString("orderID");

                    fyFSource.setContent("" + sourceOrderID);
                    fyFSource.setContentTextColor(CommonUtils.getColor(R.color.text_green));

                    handler.sendEmptyMessage(4);
                }
            }
        }
    }


    /**
     ********************************************************************************************
     ********************************************************************************************
     */

    /**
     * 获取登陆用户ID
     *
     * @param user
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUser(User user) {
        LoginUser = user;
    }

    /**
     * 获取登陆用户金蝶ID
     *
     * @param users
     */
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUsers(Users users) {
        LoginUsers = users;
    }


    /**
     ********************************************************************************************
     ********************************************************************************************
     */

    /**
     * 蓝牙打印
     */
    class BluetoothTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getContext(), "连接蓝牙打印机", "连接中.......");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(Object... params) {
            String res = printDataService.connect();
            return res;
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            super.onPostExecute(result);
            if (result != null)
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            else {
                PrintDataTask printTask = new PrintDataTask();
                printTask.execute();
            }
        }
    }

    class PrintDataTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getContext(), "执行数据打印", "打印中.......");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "执行数据打印完毕", Toast.LENGTH_LONG).show();
                printNum++;

                if ("0".equals(isUpLoad)) {
                    tvIsupLoadPrintnum.setText("未上传，打印：" + printNum + " ");
                } else {
                    tvIsupLoadPrintnum.setText("已上传，打印：" + printNum + " ");
                }

                DBBusiness business = new DBBusiness(getContext());
                business.upDateOrderMainPrintNum(printNum, orderMainID, businessType);
                business.closeDB();
                handler.sendEmptyMessage(4);
            }
            printDataService.disconnect();
        }

        @Override
        protected String doInBackground(Object... params) {
            String res = printOrder();
            return res;
        }

    }

    private String printOrder() {
        String res = null;

        DBBusiness manager = new DBBusiness(getContext());
        PrintOrderMain orderMain = manager.getOrderInfo(orderMainID, businessType);
        manager.closeDB();

        int pageSize = 0;

        if ("0".equals(businessType)) {//退货单
            pageSize = Integer.valueOf(fEntry29Set.fPrintSame);
        } else if ("1".equals(businessType)) {//订货
            pageSize = Integer.valueOf(fEntry26Set.fPrintSame);
        } else if ("2".equals(businessType)) {//出库
            pageSize = Integer.valueOf(fEntry27Set.fPrintSame);
        } else if ("3".equals(businessType)) {//退货通知
            pageSize = Integer.valueOf(fEntry28Set.fPrintSame);
        }

        switch (pageSize) {
            case 0:
                res = printSameOrder("", orderMain, businessType);
                break;
            case 1:
                res = printSameOrder("主联", orderMain, businessType);
                if (res == null)
                    res = printSameOrder("副联", orderMain, businessType);
                break;
            case 2:
                res = printSameOrder("主联", orderMain, businessType);
                if (res == null)
                    res = printSameOrder("副一", orderMain, businessType);
                if (res == null)
                    res = printSameOrder("副二", orderMain, businessType);
                break;
        }

        return res;
    }


    private String printSameOrder(String titleType, PrintOrderMain orderMain, String businessType) {
        String res = null;
        String companyName = PrefUtils.getString(getContext(), "is_company_name", "");

        ArrayList<Object> infoList = PrintUtil.getPrintInfo(titleType, companyName, orderMain, businessType, LoginUsers);
        for (Object obj : infoList) {
            if (obj instanceof String) {
                res = printDataService.send(obj.toString());
            } else {
                res = printDataService.setCommand((byte[]) obj);
            }
            if (res != null)
                break;
        }
        return res;
    }


}
