package com.fangyi.businessthrough.fragment.main.imp.sale.imp;

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
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.AddGoodsActivity;
import com.fangyi.businessthrough.activity.business.SearchSEOrderMainActivity;
import com.fangyi.businessthrough.activity.system.SearchActivity;
import com.fangyi.businessthrough.adapter.business.MenuAddGoodsAdapter;
import com.fangyi.businessthrough.adapter.business.MenuAddPromotionAdapter;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.business.SEOrderMain;
import com.fangyi.businessthrough.bean.system.FEntry_28_Set;
import com.fangyi.businessthrough.parameter.SystemFieldValues;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.events.AddGoodsPromotion;
import com.fangyi.businessthrough.utils.system.RebootActivity;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WebUploadService;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.bluetoothprint.PrintDataService;
import com.fangyi.businessthrough.bluetoothprint.PrintUtil;
import com.fangyi.businessthrough.utils.system.SerializableMap;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.fangyi.businessthrough.view.FYEtItemView;
import com.fangyi.businessthrough.view.FYLayoutManager;
import com.fangyi.businessthrough.view.ListViewDecoration;
import com.socks.library.KLog;
import com.song.refresh_view.PullToRefreshView;
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

import cn.qqtheme.framework.picker.NumberPicker;
import cn.qqtheme.framework.picker.OptionPicker;

import static com.fangyi.businessthrough.application.FYApplication.ADD_CUSTOMER_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_GOODS_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_HISTORY_ORDER;
import static com.fangyi.businessthrough.application.FYApplication.ADD_PROMOTION_REQ_CODE;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add2;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.divAddGoods;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.mulss;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.sub;
import static com.fangyi.businessthrough.utils.system.CommonUtils.getColor;
import static com.fangyi.businessthrough.utils.business.DateUtil.getStrToDate;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTime;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeYYYY_MM_DD_HH_MM_SS;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowPolishing;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.StringFilter;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class FEntry28Fragment extends BaseFragment {


    private FYBtnRadioView fyFFetchDayV;//通知日期
    private FYBtnRadioView fyFPlanFetchDay;
    private FYBtnRadioView fyFAreaPS;//销售范围
    private FYBtnRadioView fyFSaleStyle;//销售方式
    private FYBtnRadioView fyFCustName;//客户名称
    private FYBtnRadioView fyFCustAdd;//客户地址
    private FYBtnRadioView fyFCustPho;//客户电话
    private FYBtnRadioView fyFSource;//选择源单
    private FYBtnRadioView fyFEmp;//业务员
    private FYBtnRadioView fyFDept;//部门
    private FYBtnRadioView fyFDCStock;//仓库
    private FYEtItemView fyFDigest;//摘要
    private Button btnAddGoods;//添加商品
    private SwipeMenuRecyclerView recyclerView1;
    private SwipeMenuRecyclerView recyclerView2;
    private Button btnAddDonation;//添加赠品
    private TextView tvFentrySum;//合计金额

    private Button btnSaveOrder;//保存订单
    private Button btnUpload;//上传
    private Button btnPrint;//打印

    private PullToRefreshView refreshView;

    private ProgressDialog pd;
    private View view;



    /**
     * 设置信息
     */
    private Map<String, String> map;//系统参数
    private User LoginUser;//登陆用户信息
    private Users LoginUsers;//登陆用户信息金蝶对照表
    private FEntry_28_Set fEntry28Set;

    /**
     * 基础信息
     */
    private Map<String, String> mapDepartment;//部门
    private Map<String, String> mapWareHouse;//仓库


    /**
     * 订单相关信息
     */
    private String businessType = "2"; // 单据类型 1-订货单 0-退货单
    private String orderId; // 订单号
    private String isUpload = "0"; // 是否上传 0-未上传 1-已上传
    private String isSaveOrder = "0";//是否保存 0-未保存 1-已保存
    /**
     * 返回值
     */
    private List<AddGoodsMessage> goodsInfos = new ArrayList<>();//所有商品单
    private List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos = new ArrayList<>();//所有商品单

    /**
     * SwipeMenuRecyclerView控件
     */

    private MenuAddGoodsAdapter mMenuAdapter;
    private MenuAddPromotionAdapter menuAddPromotionAdapter;

    /**
     * 蓝牙
     */
    private PrintDataService printDataService = new PrintDataService();

    /**
     * 临时变量
     */
    private int intFAreaPSNum = 0;
    private int intFSaleStyleNum = 0;
    private int intFDeptNum = 0;
    private int intFDCStockNum = 0;
    private int printNum = 0;//打印次数
    private int listGoodsType = 0;//商品列表标记
    private double sums0 = 0.0;//总价
    private double sums1 = 0.0;
    private double sums2 = 0.0;
    private String isSourceOrderID = "0";//保存用

    private String strOrderDate;//订货日期
    private String fyFCustNameID;//客户名称id
    private String sourceOrderID = "";//源单ID

    private SEOrderMain seOrderMain;

    private Handler handler = new Handler() {
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
//                        menuAddPromotionAdapter.setOnItemClickListener(onItemClickListener);
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

                case 5:
                    if ("0".equals(isSaveOrder)) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                        builder.setTitle("温馨提示");
                        builder.setMessage("当前单据未保存，是否放弃！");
                        builder.setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                reboot();

                            }
                        });
                        builder.setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                refreshView.setRefreshing(false);
                            }
                        });
                        builder.show();

                    } else {
                        reboot();
                    }
                    break;
            }
        }

    };
    private String changeNum;
    private String changePrice;
    private String changeSum;


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);//退订
        super.onStop();
    }

    /**
     * 重启Activity
     */
    private void reboot() {
        refreshView.setRefreshing(false);
        Toast.makeText(getContext(), "新建订单", Toast.LENGTH_SHORT).show();
        RebootActivity.reboot(getActivity());
    }


    public FEntry28Fragment() {

    }

    public static FEntry28Fragment getInstance(String s) {
        FEntry28Fragment newFragment = new FEntry28Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessType", s);
        newFragment.setArguments(bundle);
        return newFragment;
    }


    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        CommonUtils.setSaleTitle(getActivity(), "退货通知");
        view = View.inflate(getActivity(), R.layout.fragment_main_sale_fentry_26, null);
        map = SystemFieldValues.getSystemValues();

        Bundle args = getArguments();
        businessType = args.getString("businessType");

        readingData();
        assignViews();
        setinitItemView();
        setListener();
        return view;
    }

    /**
     * 读取表单设置数据
     */
    private void readingData() {
        DBManager manager = new DBManager(getContext());
        fEntry28Set = manager.getFEntry_28_Set(new String[]{LoginUser.userSysID, "销售退货通知单"});
        manager.closeDB();


        DBBusiness business = new DBBusiness(getContext());
        orderId = CommonUtils.setOrderId(business.selectOrderMainCount(LoginUsers.kISID, businessType));
        mapDepartment = business.getParameter("部门");
        mapWareHouse = business.getWareHouse(LoginUsers.kISID);
        business.closeDB();
    }


    /**
     * 初始化列表
     */
    private void assignViews() {
        fyFFetchDayV = (FYBtnRadioView) view.findViewById(R.id.fy_FFetchDayV);
        fyFPlanFetchDay = (FYBtnRadioView) view.findViewById(R.id.fy_FPlanFetchDay);
        fyFAreaPS = (FYBtnRadioView) view.findViewById(R.id.fy_FAreaPS);
        fyFSaleStyle = (FYBtnRadioView) view.findViewById(R.id.fy_FSaleStyle);
        fyFCustName = (FYBtnRadioView) view.findViewById(R.id.fy_FCustName);
        fyFCustAdd = (FYBtnRadioView) view.findViewById(R.id.fy_FCustAdd);
        fyFCustPho = (FYBtnRadioView) view.findViewById(R.id.fy_FCustPho);
        fyFSource = (FYBtnRadioView) view.findViewById(R.id.fy_FSource);
        fyFEmp = (FYBtnRadioView) view.findViewById(R.id.fy_FEmp);
        fyFDept = (FYBtnRadioView) view.findViewById(R.id.fy_FDept);
        fyFDCStock = (FYBtnRadioView) view.findViewById(R.id.fy_FDCStock);
        fyFDigest = (FYEtItemView) view.findViewById(R.id.fy_FDigest);
        btnAddGoods = (Button) view.findViewById(R.id.btn_add_goods);
        btnAddDonation = (Button) view.findViewById(R.id.btn_add_donation);
        recyclerView1 = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view_1);
        recyclerView2 = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view_2);
        tvFentrySum = (TextView) view.findViewById(R.id.tv_fentry_sum);
        btnSaveOrder = (Button) view.findViewById(R.id.btn_save_order);
        btnUpload = (Button) view.findViewById(R.id.btn_upload);
        btnPrint = (Button) view.findViewById(R.id.btn_print);
        refreshView = (PullToRefreshView) view.findViewById(R.id.refreshView);


        refreshView.setColorSchemeColors(getColor(R.color.main_theme_color), getColor(R.color.main_theme_color)); // 颜色
        refreshView.setSmileStrokeWidth(8); // 设置绘制的笑脸的宽度
        refreshView.setSmileInterpolator(new LinearInterpolator()); // 笑脸动画转动的插值器
        refreshView.setSmileAnimationDuration(1000); // 设置笑脸旋转动画的时长
        //设置下拉刷新监听
        refreshView.setOnRefreshListener(new PullToRefreshView.OnRefreshListener() {
            @Override
            public void onRefresh() {

                new Thread() {
                    @Override
                    public void run() {

                        try {
                            Thread.sleep(800);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }


                        handler.sendEmptyMessage(5);

                    }
                }.start();


            }

        });

    }


    /**
     * 初始化项目视图
     */
    private void setinitItemView() {

        fyFFetchDayV.setTitle("通知日期");
        strOrderDate = getTime(new Date());
        fyFFetchDayV.setContent(getTime(strOrderDate));

        fyFPlanFetchDay.setVisibility(View.GONE);


        fyFAreaPS.setTitle("销售范围");
        fyFAreaPS.setContent(map.get(fEntry28Set.fAreaPSD));//默认
        if ("0".equals(fEntry28Set.fAreaPSV)) {
            fyFAreaPS.setVisibility(View.GONE);
        }


        fyFSaleStyle.setTitle("销售方式");
        fyFSaleStyle.setContent(map.get(fEntry28Set.fSaleStyleD));//默认
        if ("0".equals(fEntry28Set.fSaleStyleD)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        fyFCustName.setTitle("客户名称");
        fyFCustName.setContent("请点击选择客户名称");
        fyFCustName.setContentTextColor(getColor(R.color.text_green));

        fyFCustAdd.setTitle("客户地址");
        fyFCustAdd.setContent("");
        if ("0".equals(fEntry28Set.fCustAddV)) {
            fyFCustAdd.setVisibility(View.GONE);
        }

        fyFCustPho.setTitle("客户电话");
        fyFCustPho.setContent("");
        if ("0".equals(fEntry28Set.fCustPhoV)) {
            fyFCustPho.setVisibility(View.GONE);
        }

        fyFSource.setTitle("选择源单");
        fyFSource.setContent("请点击选择源单");
        fyFSource.setContentTextColor(getColor(R.color.text_green));

        fyFEmp.setTitle("业务员");
        fyFEmp.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry28Set.fEmpV)) {
            fyFEmp.setVisibility(View.GONE);
        }


        fyFDept.setTitle("部门");
        fyFDept.setContent(mapDepartment.get(fEntry28Set.fDeptD));
        if ("0".equals(fEntry28Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(mapWareHouse.get(fEntry28Set.fDCStockD));
        if ("0".equals(fEntry28Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }
        if ("1".equals(fEntry28Set.fStockPosition)) {//仓库位置在添加商品页面
            fyFDCStock.setVisibility(View.GONE);
        }


        fyFDigest.setTitle("摘要");

        tvFentrySum.setText(AmountShow(sums0));

        if ("0".equals(fEntry28Set.fWholePro)) {//关闭整单搭赠
            btnAddDonation.setVisibility(View.GONE);
            fEntry28Set.fPlanPro = "1";
        } else {                                //启动整单搭赠
            fEntry28Set.fPlanPro = "0";
        }

        if ("0".equals(fEntry28Set.fSaFePrint)) {

            btnUpload.setEnabled(false);
            btnPrint.setEnabled(false);

        } else {

            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
            btnUpload.setEnabled(false);
        }


        if ("2".equals(fEntry28Set.fOnType)) {//批量
            btnUpload.setVisibility(View.GONE);
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

            isSourceOrderID = "0";
        }
    }

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


    private FYBtnRadioView fyNum;
    private FYBtnRadioView fyNoPrice;
    private FYBtnRadioView fyNoSumMoney;
    private FYEtItemView fyPrice;
    private FYEtItemView fySumMoney;

    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(final int position) {

            if ("1".equals(isSaveOrder)) {
                KLog.e("====2222111111111111111111111111111111");
                return;
            }


            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("更改");

            View view = View.inflate(getContext(), R.layout.dialog_sale_seorder_item_change, null);
            fyNum = (FYBtnRadioView) view.findViewById(R.id.fy_num);
            fyNoPrice = (FYBtnRadioView) view.findViewById(R.id.fy_NoPrice);
            fyNoSumMoney = (FYBtnRadioView) view.findViewById(R.id.fy_NoSumMoney);
            fyPrice = (FYEtItemView) view.findViewById(R.id.fy_Price);
            fySumMoney = (FYEtItemView) view.findViewById(R.id.fy_SumMoney);

            fyNum.setTitle("数量");
            fyNum.setContent(goodsInfos.get(position).getNotNum());


            if ("1".equals(fEntry28Set.fChooseAmount)) {//可以改价格
                fyPrice.setTitle("单价");
                fyPrice.setInputTypeNumber();
                fyPrice.setHint(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                fySumMoney.setTitle("金额");
                fySumMoney.setInputTypeNumber();
                fySumMoney.setHint(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsSum())));
                fyNoSumMoney.setVisibility(View.GONE);
                fyNoPrice.setVisibility(View.GONE);
            } else {
                fyNoPrice.setTitle("单价");
                fyNoPrice.setContent(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                fyNoSumMoney.setTitle("金额");
                fyNoSumMoney.setContent(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsSum())));

                fySumMoney.setVisibility(View.GONE);
                fyPrice.setVisibility(View.GONE);
            }

            changeNum = "0";
            changePrice = "0";
            changeSum = "0";

            fyNum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    NumberPicker picker = new NumberPicker(getActivity());
                    picker.setOffset(2);//偏移量
                    picker.setRange(1, Integer.parseInt(goodsInfos.get(position).getNotNum()), 1);
                    picker.setSelectedItem(Integer.parseInt(goodsInfos.get(position).getNotNum()));

                    picker.setOnOptionPickListener(new OptionPicker.OnOptionPickListener() {
                        @Override
                        public void onOptionPicked(int positions, String option) {
                            fyNum.setContent(option);
                            calculateSum("0", position, option);
                        }


                    });
                    picker.show();
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
                    calculateSum("1", position, str);
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

                    calculateSum("2", position, str);
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

            builder.setPositiveButton(
                    "确定", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                            over(position);
                        }
                    }
            );

            builder.setNegativeButton("取消", null);
            builder.show();



        }
    };

    /**
     * 保存
     * @param position
     */
    private void over(int position) {
        changeNum = fyNum.getText();
        changePrice = AmountShowPolishing(fyPrice.getInputIsEmpty());
        changeSum = AmountShowPolishing(fySumMoney.getInputIsEmpty());

        String hintPrice = fyPrice.getHint();
        String hintSum = fySumMoney.getHint();
        String noPrice = fyNoPrice.getText();
        String noSum = fyNoPrice.getText();

        if ("0".equals(fyPrice.getInput())) {
            Toast.makeText(getContext(), "单价不为0", Toast.LENGTH_SHORT).show();
            return;
        }

        if ("0".equals(fySumMoney.getInput())) {
            Toast.makeText(getContext(), "金额不为0", Toast.LENGTH_SHORT).show();
            return;
        }


        goodsInfos.get(position).setNotNum(changeNum);

        if ("1".equals(fEntry28Set.fChooseAmount)) {//更改单价金额

            if ("0".equals(changeSum)) {

                if ("0".equals(changePrice)) {
                    goodsInfos.get(position).setGoodsPrice(hintPrice);
                    goodsInfos.get(position).setGoodsSum(hintSum);
                } else {
                    goodsInfos.get(position).setGoodsPrice(changePrice);
                    goodsInfos.get(position).setGoodsSum(hintSum);
                }

            } else {


                if ("0".equals(changePrice)) {
                    goodsInfos.get(position).setGoodsPrice(hintPrice);
                    goodsInfos.get(position).setGoodsSum(changeSum);

                } else {
                    goodsInfos.get(position).setGoodsPrice(changePrice);
                    goodsInfos.get(position).setGoodsSum(changeSum);
                }
            }


        } else {
            goodsInfos.get(position).setGoodsPrice(noPrice);
            goodsInfos.get(position).setGoodsSum(noSum);
        }

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

        handler.sendEmptyMessage(0);
    }

    /**
     * 计算修改价格
     */
    private void calculateSum(String judge, int position, String option) {

        changeNum = fyNum.getText();
        changePrice = fyPrice.getInputIsEmpty();
        changeSum = fySumMoney.getInputIsEmpty();

        KLog.e("==@@@22222===" + changePrice);
        KLog.e("==@@@33333===" + changeNum);
        KLog.e("==@@@44444===" + changeSum);

        if ("0".equals(judge)) {//数量

            if ("0".equals(changeSum)) {

                if ("0".equals(changePrice)) {
                    fyPrice.setHint(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                    fyNoPrice.setContent(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                    fySumMoney.setHint(AmountShow(mulss(option, goodsInfos.get(position).getGoodsPrice())));
                    fyNoSumMoney.setContent(AmountShow(mulss(option, goodsInfos.get(position).getGoodsPrice())));

                } else {
                    fySumMoney.setHint(AmountShow(mulss(option, changePrice)));
                    fyNoSumMoney.setContent(AmountShow(mulss(option, changePrice)));
                }
            } else {

                if ("0".equals(changePrice)) {
                    fyPrice.setHint(AmountShow(divAddGoods(changeSum, option)));
                } else {

                    fySumMoney.setHint(AmountShow(mulss(option, changePrice)));
                    fyNoSumMoney.setContent(AmountShow(mulss(option, changePrice)));
                }
            }
        }

        if ("1".equals(judge)) {//单价
            if ("0".equals(option)) {
                if ("0".equals(changeSum)) {
                    fySumMoney.setHint(AmountShow(mulss(changeNum, goodsInfos.get(position).getGoodsPrice())));
                    fyNoSumMoney.setContent(AmountShow(mulss(changeNum, goodsInfos.get(position).getGoodsPrice())));
                } else {

                    fySumMoney.setHint(AmountShow(mulss(changeNum, goodsInfos.get(position).getGoodsPrice())));
                    fyNoSumMoney.setContent(AmountShow(mulss(changeNum, goodsInfos.get(position).getGoodsPrice())));
                }


            } else {
                if ("0".equals(changeSum)) {
                    fySumMoney.setHint(AmountShow(mulss(changeNum, option)));
                    fyNoSumMoney.setContent(AmountShow(mulss(changeNum, option)));
                } else {
                    fySumMoney.setText("");
                    fySumMoney.setHint(AmountShow(mulss(changeNum, option)));
                    fyNoSumMoney.setContent(AmountShow(mulss(changeNum, option)));
                }

            }
        }

        if ("2".equals(judge)) {//总金额
            if ("0".equals(option)) {
                if ("0".equals(changePrice)) {
                    fyPrice.setHint(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                    fyNoPrice.setContent(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                } else {
                    fyPrice.setHint(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                    fyNoPrice.setContent(AmountShow(AmountShowBug(goodsInfos.get(position).getGoodsPrice())));
                }

            } else {

                if ("0".equals(changePrice)) {
                    fyPrice.setHint(AmountShow(divAddGoods(option, changeNum)));
                    fyNoPrice.setContent(AmountShow(divAddGoods(option, changeNum)));
                } else {
                    fyPrice.setText("");
                    fyPrice.setHint(AmountShow(divAddGoods(option, changeNum)));
                    fyNoPrice.setContent(AmountShow(divAddGoods(option, changeNum)));
                }

            }
        }


    }

    private void setListener() {

        //允许选择日期
        if ("1".equals(fEntry28Set.fChooseDate)) {
            fyFFetchDayV.setOnClickListener(this);
        }

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
        btnSaveOrder.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
    }

    /**
     * 设置点击事件
     *
     * @param b
     */
    private void setClickEvent(boolean b) {

        fyFFetchDayV.setEnabled(b);

        fyFAreaPS.setEnabled(b);
        fyFSaleStyle.setEnabled(b);
        fyFCustName.setEnabled(b);

        fyFSource.setEnabled(b);

        fyFEmp.setEnabled(b);
        fyFDept.setEnabled(b);
        fyFDCStock.setEnabled(b);

        btnAddGoods.setEnabled(b);
        btnAddDonation.setEnabled(b);

        btnSaveOrder.setEnabled(b);
        recyclerView1.setItemViewSwipeEnabled(b);
        recyclerView2.setItemViewSwipeEnabled(b);
        fyFDigest.setInputType(InputType.TYPE_NULL);

    }


    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fy_FFetchDayV:
                setFFetchDay();//销货日期
                break;
            case R.id.fy_FAreaPS:
                setFAreaPS();//业务方式
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
            case R.id.fy_FEmp://业务员 - 写死
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
     * 选择源单
     */
    private void setFSource() {
        if (fyFCustNameID != null) {
            Intent intent = new Intent(getActivity(), SearchSEOrderMainActivity.class);
            intent.putExtra("userSysID", LoginUser.userSysID);
            intent.putExtra("fyFCustNameID", fyFCustNameID);
            intent.putExtra("businessType", "1");
            startActivityForResult(intent, ADD_HISTORY_ORDER);
        } else {
            Toast.makeText(getContext(), "请先选择客户", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 上传订单
     */
    private void uploadOrder() {

        KLog.e("==========================================");

        pd = ProgressDialog.show(getContext(), "数据上传", "上传中，请稍后……");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();

        String result = WebUploadService.uploadSaleDateService(getActivity(), orderId, businessType);

        if ("上传失败".equals(result)) {
            pd.dismiss();
            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
        } else {
            pd.dismiss();
            Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();

            btnUpload.setEnabled(false);


            if ("2".equals(fEntry28Set.fDelType)) {//上传后不可以修改
                setClickEvent(false);
            }
        }
    }


    /**
     * 打印
     */
    private void setBtnPrint() {

        if (!printDataService.isBluetoothEnable()) {
            Toast.makeText(getContext(), "手机蓝牙未打开", Toast.LENGTH_LONG).show();
            return;
        }

        BluetoothTask bluetoothTask = new BluetoothTask();
        bluetoothTask.execute();

        if ("1".equals(fEntry28Set.fDelType)) {//打印后不可以修改
            setClickEvent(false);
        }
    }


    /**
     * 保存订单
     */
    private void saveOrder() {

        if (TextUtils.isEmpty(fyFCustNameID)) {
            Toast.makeText(getContext(), "请选择客户", Toast.LENGTH_SHORT).show();
            return;
        }

//        if (TextUtils.isEmpty(sourceOrderID)) {
//            Toast.makeText(getContext(), "请选择源单", Toast.LENGTH_SHORT).show();
//            return;
//        }

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

    private void saveSettings() {
        btnSaveOrder.setEnabled(false);


        if ("0".equals(fEntry28Set.fSaFePrint)) {//保存后立刻打印

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

        } else { //保存后立刻打印

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


        if ("0".equals(fEntry28Set.fUpdateType)) {//保存后不可以修改
            setClickEvent(false);
        }
    }


    /**
     * 保存OrderMain表
     */
    private void save() {


        String ID = orderId; //订单ID
        String OrderDate = getStrToDate(strOrderDate); //单据业务日期
        String DeliveryDate = ""; //交货日期
        String CustomerSySID = fyFCustNameID; //客户id即cusotmer里的sysid
        String WareHouseName;
        if ("1".equals(fEntry28Set.fStockPosition)) {//仓库位置在添加商品页面
            WareHouseName = ""; //仓库名称
        } else {
            WareHouseName = fyFDCStock.getText();
        }

        String UserSysID = LoginUsers.kISID; //业务员id kisid
        String SaveTime = getTimeYYYY_MM_DD_HH_MM_SS(new Date()); //单据保存日期
        String IsUpLoad = isUpload; //是否上传
        String Message = fyFDigest.getInput(); //备注
        String DeptID = fyFDept.getText(); //部门id
        String BusinessType = businessType; //订单类型
        String FAreaPS = fyFAreaPS.getText(); //销售范围
        String FSaleType = fyFSaleStyle.getText(); //销售方式
        String AllMoney = tvFentrySum.getText().toString(); //总价格
        String FUpdateType = fEntry28Set.fUpdateType; //修改控制
        String FDelType = fEntry28Set.fDelType; //删除控制
        String PrintNum = String.valueOf(printNum); //打印数量

        KLog.e("===!!!!!!!!!!!!!!!!!!!!!===" + sourceOrderID);
        String FyFSource = sourceOrderID;//源单

        Data.saveOrder(ID, OrderDate, DeliveryDate, CustomerSySID, WareHouseName, mapWareHouse, UserSysID, SaveTime, IsUpLoad, Message, DeptID, mapDepartment, BusinessType, FAreaPS, FSaleType, map, AllMoney, FUpdateType, FDelType, PrintNum, FyFSource, goodsInfos, goodsPromotionInfos);

        Toast.makeText(getContext(), "保存完成", Toast.LENGTH_SHORT).show();
        isSaveOrder = "1";
        CommonUtils.setSaleTitle(getActivity(), "退货通知（已保存）");
//
//        //更改源单标识，保存选择以后不会再去关联
//        DBBusiness business = new DBBusiness(getContext());
//        business.upDateSEOrderSELECTID(sourceOrderID, businessType);
//        business.closeDB();
    }

    /**
     * 打开添加赠品页面
     */
    private void startAddDonation() {

        if (fyFCustNameID != null) {
            Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
            intent.putExtra("addType", "0");
            intent.putExtra("businessType", businessType);//businessType
            intent.putExtra("fChooseAmount", fEntry28Set.fChooseAmount);//允许修改单价金额
            intent.putExtra("fDCStockD", fEntry28Set.fDCStockD);//仓库
            intent.putExtra("fStockPosition", fEntry28Set.fStockPosition);//仓库 在添加页面
            intent.putExtra("fPlanPro", fEntry28Set.fPlanPro);//启用搭赠方案
            intent.putExtra("userSysID", LoginUsers.userSysID);//传登陆用户ID
            intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID

            startActivityForResult(intent, ADD_GOODS_REQ_CODE);
        } else {
            Toast.makeText(getContext(), "请先选择客户", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 打开添加商品页面
     */
    private void startAddGoods() {
        if (fyFCustNameID != null) {
            Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
            intent.putExtra("addType", "1");
            intent.putExtra("businessType", businessType);//businessType
            intent.putExtra("fChooseAmount", fEntry28Set.fChooseAmount);//允许修改单价金额
            intent.putExtra("fDCStockD", fEntry28Set.fDCStockD);//仓库
            intent.putExtra("fStockPosition", fEntry28Set.fStockPosition);//仓库 在添加页面
            intent.putExtra("fPlanPro", fEntry28Set.fPlanPro);//启用搭赠方案
            intent.putExtra("userSysID", LoginUsers.userSysID);//传登陆用户ID
            intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID

            startActivityForResult(intent, ADD_GOODS_REQ_CODE);
        } else {
            Toast.makeText(getContext(), "请先选择客户", Toast.LENGTH_SHORT).show();
        }
    }


    /**
     * 仓库选择
     */
    private void setFDCStock() {

        final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

        final String[] arrWareHouse = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry28Set.fCheckType)) {
            if (intFDCStockNum <= mapWareHouse.size()) {
                fyFDCStock.setContent(mapValuesList.get(intFDCStockNum));
                intFDCStockNum++;
                if (intFDCStockNum == mapWareHouse.size()) {
                    intFDCStockNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


    /**
     * 部门选择
     */
    private void setFDept() {

        final List<String> mapValuesList = new ArrayList<>(mapDepartment.values());

        final String[] arr = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry28Set.fCheckType)) {
            if (intFDeptNum <= mapDepartment.size()) {
                fyFDept.setContent(mapValuesList.get(intFDeptNum));
                intFDeptNum++;
                if (intFDeptNum == mapDepartment.size()) {
                    intFDeptNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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

    /**
     * 客户名称
     */
    private void setFCustName() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("searchType", ADD_CUSTOMER_REQ_CODE);
        intent.putExtra("loginUserSysID", LoginUsers.kISID);
        startActivityForResult(intent, ADD_CUSTOMER_REQ_CODE);

    }

    /**
     * 销售方式
     */
    private void setFSaleStyle() {

        final String items[] = {map.get("100"), map.get("101"), map.get("102"), map.get("103"), map.get("20297")};

        if (!"1".equals(fEntry28Set.fCheckType)) {
            if (intFSaleStyleNum <= items.length) {
                fyFSaleStyle.setContent(items[intFSaleStyleNum]);
                intFSaleStyleNum++;
                if (intFSaleStyleNum == items.length) {
                    intFSaleStyleNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("销售方式");
            builder.setSingleChoiceItems(items, intFSaleStyleNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFSaleStyleNum = which;
                    //2.设置
                    fyFSaleStyle.setContent(items[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }

    }

    /**
     * 销售范围
     */
    private void setFAreaPS() {

        final String items[] = {map.get("20302"), map.get("20303")};
        if (!fEntry28Set.fCheckType.equals("1")) {
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
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("销售范围");
            builder.setSingleChoiceItems(items, intFAreaPSNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFAreaPSNum = which;
                    //2.设置
                    fyFAreaPS.setContent(items[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }


    }


    /**
     * 销货日期
     */
    private void setFFetchDay() {
        TimePickerView pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setTitle("退货通知日期");
        pvTime.setCyclic(false);
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                strOrderDate = getTime(date);
                fyFFetchDayV.setContent(getTime(strOrderDate));
            }
        });
        pvTime.show();
    }


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
                    if (!"0".equals(stringAddGoodsPromotionEntry.getValue().getGoodsNumber())) {
                        stringAddGoodsPromotionEntry.getValue().setListGoodsType(String.valueOf(listGoodsType));
                        sums1 = add(sums1, Double.parseDouble(stringAddGoodsPromotionEntry.getValue().getGoodsSumPrice()));
                        goodsSysID = stringAddGoodsPromotionEntry.getValue().getGoodsSysID();
                    } else {
                        addGoodsPromotionMap.remove(stringAddGoodsPromotionEntry.getKey());
                    }
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
                    fyFCustName.setContentTextColor(getColor(R.color.text_green));

                    handler.sendEmptyMessage(4);
                }
            }
        }
    }

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



    class BluetoothTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getContext(), "连接蓝牙打印机", "连接中.......");
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
//            btnPrint1.setEnabled(false);
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
//            btnPrint1.setEnabled(true);
            super.onPostExecute(result);
            if (result != null)
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            else {
                PrintDataTask printTask = new PrintDataTask();
                printTask.execute();
            }
        }
    }

    //
    class PrintDataTask extends AsyncTask<Object, Integer, String> {

        @Override
        protected void onPreExecute() {
            pd = ProgressDialog.show(getContext(), "执行数据打印", "打印中.......");
//            btnPrint1.setEnabled(false);
            pd.setCancelable(false);
            pd.setCanceledOnTouchOutside(false);
//            btnPrint1.setEnabled(false);
            pd.show();
            super.onPreExecute();
        }

        @Override
        protected void onPostExecute(String result) {
            pd.dismiss();
//            btnPrint1.setEnabled(true);
            super.onPostExecute(result);
            if (result != null) {
                Toast.makeText(getContext(), result, Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "执行数据打印完毕", Toast.LENGTH_LONG).show();
                printNum++;
                DBBusiness business = new DBBusiness(getContext());
                business.upDateOrderMainPrintNum(printNum, orderId, businessType);
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
        PrintOrderMain orderMain = manager.getOrderInfo(orderId, businessType);
        manager.closeDB();
        int pageSize = Integer.valueOf(fEntry28Set.fPrintSame);
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
