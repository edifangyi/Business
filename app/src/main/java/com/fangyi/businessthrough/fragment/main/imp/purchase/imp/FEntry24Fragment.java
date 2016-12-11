package com.fangyi.businessthrough.fragment.main.imp.purchase.imp;

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
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.AddGoods_2_Activity;
import com.fangyi.businessthrough.activity.system.SearchActivity;
import com.fangyi.businessthrough.adapter.business.MenuAddGoodsAdapter;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.bean.system.FEntry_24_Set;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.bluetoothprint.PrintDataService;
import com.fangyi.businessthrough.bluetoothprint.PrintUtil;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WebUploadService;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.parameter.SystemFieldValues;
import com.fangyi.businessthrough.utils.business.CalculationUtils;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.utils.system.RebootActivity;
import com.fangyi.businessthrough.view.DrawableCenterButton;
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
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.application.FYApplication.ADD_GOODS_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_SUPPLIER_REQ_CODE;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.div;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.mul;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.sub;
import static com.fangyi.businessthrough.utils.business.DateUtil.getStrToDate;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTime;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShow;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.StringFilter;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;
import static com.fangyi.businessthrough.utils.system.CommonUtils.getColor;


/**
 * 采购入库单
 * Created by FANGYI on 2016/8/27.
 */

public class FEntry24Fragment extends BaseFragment {
    private PullToRefreshView refreshView;
    private FYBtnRadioView fyFFetchDayV;
    private FYBtnRadioView fyFPlanFetchDay1;
    private FYBtnRadioView fyFPlanFetchDay2;
    private FYBtnRadioView fyFPOStyle;
    private FYBtnRadioView fyFAreaPS;
    private FYBtnRadioView fyFBizType;
    private FYBtnRadioView fyFSource;
    private FYBtnRadioView fyFSupplier;
    private FYBtnRadioView fyFRequester;
    private FYBtnRadioView fyFDept;
    private FYBtnRadioView fyFFManager;
    private FYBtnRadioView fyFSManager;
    private FYBtnRadioView fyFDCStock;
    private FYEtItemView fyFDigest;
    private DrawableCenterButton btnAddGoods;
    private SwipeMenuRecyclerView recyclerView1;
    private LinearLayout llFentrySum;
    private TextView tvFentrySum;
    private LinearLayout llSaveUplpadPrint;
    private DrawableCenterButton btnSaveOrder;
    private DrawableCenterButton btnPrint;
    private DrawableCenterButton btnUpload;


    private View view;


    /**
     * 设置信息
     */
    private Map<String, String> map;//系统参数
    private User LoginUser;//登陆用户信息
    private Users LoginUsers;//登陆用户信息金蝶对照表
    private FEntry_24_Set fEntry24Set;


    /**
     * 基础信息
     */
    private Map<String, String> mapDepartment;//部门
    private Map<String, String> mapWareHouse;//仓库
    private Map<String, String> mapRequester;//业务员


    /**
     * 订单相关信息
     */
    private String businessType; // 单据类型 1-订货单 0-退货单
    private String orderId; // 订单号
    private String isUpload = "0"; // 是否上传 0-未上传 1-已上传
    private String isSaveOrder = "0";//是否保存 0-未保存 1-已保存


    private List<AddGoodsMessage> goodsInfos = new ArrayList<>();//所有商品单

    private MenuAddGoodsAdapter mMenuAdapter;


    /**
     * 蓝牙
     */
    private PrintDataService printDataService = new PrintDataService();


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {


            switch (msg.what) {
                case 0:
                    if (mMenuAdapter == null) {
                        mMenuAdapter = new MenuAddGoodsAdapter(goodsInfos);
                        recyclerView1.setAdapter(mMenuAdapter);

                    } else {
                        FYLayoutManager fyLayoutManager = new FYLayoutManager(getContext(), recyclerView1, goodsInfos.size());
                        recyclerView1.setLayoutManager(fyLayoutManager);
                        mMenuAdapter.notifyDataSetChanged();
                    }
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
    private String strOrderDate;
    private int intFBizTypeNum;
    private int intFAreaPSNum;
    private int intFDeptNum;
    private int intFDCStockNum;
    private int intFPOStyleNum;
    private int intFFManagerNum;
    private int intFSManagerNum;
    private String fyFSupplierID;
    private double sums0;
    private double sums2;
    private String sourceOrderID;
    private String isSourceOrderID;
    private String changeNum;


    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);//退订
        super.onStop();
    }

    public FEntry24Fragment() {

    }


    public static FEntry24Fragment getInstance(String s) {
        FEntry24Fragment newFragment = new FEntry24Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessType", s);
        newFragment.setArguments(bundle);
        return newFragment;
    }
    /**
     * 重启Activity
     */
    private void reboot() {
        refreshView.setRefreshing(false);
        Toast.makeText(getContext(), "新建订单", Toast.LENGTH_SHORT).show();
        RebootActivity.reboot(getActivity());
    }


    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        CommonUtils.setPurchaseTitle(getActivity(), "采购退货通知");
        view = View.inflate(getActivity(), R.layout.fragment_main_sale_fentry_22, null);
        map = SystemFieldValues.getSystemValues();

        Bundle args = getArguments();
        businessType = args.getString("businessType");

        readingData();
        assignViews();
        setinitItemView();
        setListener();


        return view;
    }


    private void readingData() {
        DBManager manager = new DBManager(getContext());
        fEntry24Set = manager.getFEntry_24_Set(new String[]{LoginUser.userSysID, "采购退货通知"});
        manager.closeDB();


        DBBusiness business = new DBBusiness(getContext());
        orderId = CommonUtils.setOrderId(business.selectPurchaseOrderMainCount(LoginUsers.kISID, businessType));
        mapDepartment = business.getParameter("部门");
        mapRequester = business.getSupplier("业务员");
        mapWareHouse = business.getWareHouse(LoginUsers.kISID);
        business.closeDB();
    }

    private void assignViews() {

        fyFFetchDayV = (FYBtnRadioView) view.findViewById(R.id.fy_FFetchDayV);
        fyFPlanFetchDay1 = (FYBtnRadioView) view.findViewById(R.id.fy_FPlanFetchDay1);
        fyFPlanFetchDay2 = (FYBtnRadioView) view.findViewById(R.id.fy_FPlanFetchDay2);
        fyFPOStyle = (FYBtnRadioView) view.findViewById(R.id.fy_FPOStyle);
        fyFAreaPS = (FYBtnRadioView) view.findViewById(R.id.fy_FAreaPS);
        fyFBizType = (FYBtnRadioView) view.findViewById(R.id.fy_FBizType);
        fyFSource = (FYBtnRadioView) view.findViewById(R.id.fy_FSource);
        fyFSupplier = (FYBtnRadioView) view.findViewById(R.id.fy_FSupplier);
        fyFRequester = (FYBtnRadioView) view.findViewById(R.id.fy_FRequester);
        fyFDept = (FYBtnRadioView) view.findViewById(R.id.fy_FDept);
        fyFFManager = (FYBtnRadioView) view.findViewById(R.id.fy_FFManager);
        fyFSManager = (FYBtnRadioView) view.findViewById(R.id.fy_FSManager);
        fyFDCStock = (FYBtnRadioView) view.findViewById(R.id.fy_FDCStock);
        fyFDigest = (FYEtItemView) view.findViewById(R.id.fy_FDigest);

        btnAddGoods = (DrawableCenterButton) view.findViewById(R.id.btn_add_goods);
        recyclerView1 = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view_1);

        llFentrySum = (LinearLayout) view.findViewById(R.id.ll_fentry_sum);

        llSaveUplpadPrint = (LinearLayout) view.findViewById(R.id.ll_save_uplpad_print);
        tvFentrySum = (TextView) view.findViewById(R.id.tv_fentry_sum);

        btnSaveOrder = (DrawableCenterButton) view.findViewById(R.id.btn_save_order);
        btnPrint = (DrawableCenterButton) view.findViewById(R.id.btn_print);
        btnUpload = (DrawableCenterButton) view.findViewById(R.id.btn_upload);

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

    private void setinitItemView() {
        fyFFetchDayV.setTitle("通知日期");
        strOrderDate = getTime(new Date());
        fyFFetchDayV.setContent(getTime(strOrderDate));

        fyFPlanFetchDay1.setVisibility(View.GONE);
        fyFPlanFetchDay2.setVisibility(View.GONE);


        fyFBizType.setTitle("业务类型");
        fyFBizType.setContent(map.get(fEntry24Set.fBizTypeD));
        if ("0".equals(fEntry24Set.fBizTypeV)) {
            fyFBizType.setVisibility(View.GONE);
        }

        fyFAreaPS.setTitle("采购范围");
        fyFAreaPS.setContent(map.get(fEntry24Set.fAreaPSD));
        if ("0".equals(fEntry24Set.fAreaPSV)) {
            fyFAreaPS.setVisibility(View.GONE);
        }


        fyFPOStyle.setTitle("采购方式");

        KLog.e("====" + fEntry24Set.fPOStyleD);

        fyFPOStyle.setContent(map.get(fEntry24Set.fPOStyleD));
        if ("0".equals(fEntry24Set.fPOStyleV)) {
            fyFPOStyle.setVisibility(View.GONE);
        }

        fyFSupplier.setTitle("供应商");
        fyFSupplier.setContent("请点击选择供应商");

        fyFRequester.setVisibility(View.GONE);

        fyFDept.setTitle("部门");
        fyFDept.setContent(mapDepartment.get(fEntry24Set.fDeptD));
        if ("0".equals(fEntry24Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }
        fyFSource.setTitle("选择源单");


        fyFFManager.setTitle("验收");
        fyFFManager.setContent(mapRequester.get(fEntry24Set.fFManagerD));
        if ("0".equals(fEntry24Set.fFManagerV)) {
            fyFDept.setVisibility(View.GONE);
        }


        fyFSManager.setTitle("保管");
        fyFSManager.setContent(mapRequester.get(fEntry24Set.fSManagerD));
        if ("0".equals(fEntry24Set.fSManagerV)) {
            fyFSManager.setVisibility(View.GONE);
        }

        fyFDCStock.setTitle("仓库");
        fyFDCStock.setContent(mapWareHouse.get(fEntry24Set.fDCStockD));
        if ("0".equals(fEntry24Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }

        if ("1".equals(fEntry24Set.fStockPosition)) {//仓库位置在添加商品页面
            fyFDCStock.setVisibility(View.GONE);
        }

        fyFDigest.setTitle("摘要");

        tvFentrySum.setText(AmountShow(0));

        if ("0".equals(fEntry24Set.fSaFePrint)) {

            btnUpload.setEnabled(false);
            btnPrint.setEnabled(false);

        } else {

            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
            btnUpload.setEnabled(false);
        }

        if ("2".equals(fEntry24Set.fOnType)) {//批量
            btnUpload.setVisibility(View.GONE);
        }

        recyclerView1.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView1.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView1.addItemDecoration(new ListViewDecoration());// 添加分割线。
        recyclerView1.setItemViewSwipeEnabled(true);
        recyclerView1.setOnItemMoveListener(onItemMoveListener_1);// 监听拖拽，更新UI。

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
        if (goodsInfos.size() == 0) {

            setClickEvent(true);
            btnSaveOrder.setEnabled(true);

            isSaveOrder = "0";
            fyFSupplierID = "";//客户名称id
            sourceOrderID = "";//源单ID

            fyFSupplier.setContent("请点击选择供应商");
            fyFSource.setContent("请点击选择源单");

            goodsInfos.clear();


            Toast.makeText(getContext(), "请重新选择供应商或源单", Toast.LENGTH_SHORT).show();

            isSourceOrderID = "0";
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

            if ("1".equals(isSaveOrder)) {
                KLog.e("====2222111111111111111111111111111111");
                return;
            }


            key0 = "0";
            key1 = "1";
            key2 = "2";


            final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
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


            if (!"1".equals(fEntry24Set.fChooseAmount)) {//不可以改价格
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
                    over(position,dialog);
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

                if (!"1".equals(fEntry24Set.fChooseAmount)) {//不可以改价格
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


                    if (!"1".equals(fEntry24Set.fChooseAmount)) {//不可以改价格

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


    private void setListener() {
        if ("1".equals(fEntry24Set.fChooseDate)) {
            fyFFetchDayV.setOnClickListener(this);
        }
        fyFAreaPS.setOnClickListener(this);
        fyFBizType.setOnClickListener(this);
        fyFPOStyle.setOnClickListener(this);
        fyFSupplier.setOnClickListener(this);

        fyFDept.setOnClickListener(this);
        fyFSource.setOnClickListener(this);
        fyFFManager.setOnClickListener(this);
        fyFSManager.setOnClickListener(this);
        fyFDCStock.setOnClickListener(this);
        btnAddGoods.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnUpload.setOnClickListener(this);
        btnSaveOrder.setOnClickListener(this);


    }


    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fy_FFetchDayV:
                setFFetchDay();
                break;
            case R.id.fy_FAreaPS:
                setFAreaPS();
                break;
            case R.id.fy_FBizType:
                setFBizType();
                break;
            case R.id.fy_FPOStyle:
                setFPOStyle();
                break;
            case R.id.fy_FSupplier:
                setFSupplier();
                break;
            case R.id.fy_FSource:
                setFSource();
                break;
            case R.id.fy_FFManager:
                setFFManager();
                break;
            case R.id.fy_FSManager:
                setFFSManager();
                break;
            case R.id.fy_FDCStock:
                setFDCStock();
                break;
            case R.id.fy_FDept:
                setFDept();
                break;
            case R.id.btn_add_goods://添加商品
                startAddGoods();
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
     * 设置点击事件
     *
     * @param b
     */
    private void setClickEvent(boolean b) {


        fyFFetchDayV.setEnabled(b);
        fyFPlanFetchDay1.setEnabled(b);
        fyFPlanFetchDay2.setEnabled(b);
        fyFPOStyle.setEnabled(b);
        fyFAreaPS.setEnabled(b);
        fyFBizType.setEnabled(b);
        fyFSource.setEnabled(b);
        fyFSupplier.setEnabled(b);
        fyFRequester.setEnabled(b);
        fyFDept.setEnabled(b);
        fyFFManager.setEnabled(b);
        fyFSManager.setEnabled(b);
        fyFDCStock.setEnabled(b);

        btnAddGoods.setEnabled(b);
        btnSaveOrder.setEnabled(b);
        recyclerView1.setItemViewSwipeEnabled(b);
        fyFDigest.setInputType(InputType.TYPE_NULL);
    }


    private ProgressDialog pd;
    private int printNum = 0;//打印次数

    /**
     * 上传
     */
    private void uploadOrder() {

        String result = WebUploadService.uploadPurchaseDataService(getActivity(), orderId, businessType);

        if ("上传失败".equals(result)) {

            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
        } else {

            Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
            btnUpload.setEnabled(false);


            if ("2".equals(fEntry24Set.fDelType)) {//上传后不可以修改
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


        if ("1".equals(fEntry24Set.fDelType)) {//打印后不可以修改
            setClickEvent(false);
        }

    }

    /**
     * 保存
     */
    private void saveOrder() {

        if (goodsInfos.size() == 0) {
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
     * 保存设置
     */
    private void saveSettings() {
        btnSaveOrder.setEnabled(false);


        if ("0".equals(fEntry24Set.fSaFePrint)) {

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

        KLog.e("==========" + fEntry24Set.fUpdateType);

        if ("0".equals(fEntry24Set.fUpdateType)) {//保存后不可以修改
            setClickEvent(false);
        }
    }

    /**
     * 保存单据
     */
    private void save() {
        String ID = orderId;
        String strFyFFetchDayV = getStrToDate(strOrderDate);
        String strFyFPlanFetchDay1 = "";
        String strFyFPlanFetchDay2 = "";
        String strFyFPOStyle = fyFPOStyle.getText();
        String strFyFAreaPS = fyFAreaPS.getText();
        String strFyFBizType = fyFBizType.getText();
        String strFyFSource = "";
        String strFyFSupplier = "";
        String strFyFRequester = LoginUsers.kISID;
        String strFyFDept = fyFDept.getText();
        String strFyFFManager = "";
        String strFyFSManager = "";
        String strFyFDCStock = "";
        String strFyFDigest = fyFDigest.getInput();
        String strBusinessType = businessType;
        String AllMoney = "";
        String strIsUpLoad = isUpload;
        String strFUpdateType = fEntry24Set.fUpdateType;
        String strFDelType = fEntry24Set.fDelType;
        String strPrintNum = String.valueOf(printNum);


        Data.savePurchaseOrder(ID, strFyFFetchDayV, strFyFPlanFetchDay1, strFyFPlanFetchDay2, strFyFPOStyle, strFyFAreaPS,
                strFyFBizType, strFyFSource, strFyFSupplier, strFyFRequester, mapDepartment, strFyFDept, strFyFFManager, strFyFSManager,
                mapWareHouse, strFyFDCStock, strFyFDigest, strBusinessType, strIsUpLoad, strFUpdateType, strFDelType, strPrintNum, AllMoney, goodsInfos, map);
        Toast.makeText(getContext(), "保存完成", Toast.LENGTH_SHORT).show();
        isSaveOrder = "1";
        CommonUtils.setPurchaseTitle(getActivity(), "采购退货通知（已保存）");
    }

    /**
     * 添加商品
     */
    private void startAddGoods() {
        if (fyFSupplierID != null) {
            Intent intent = new Intent(getActivity(), AddGoods_2_Activity.class);
            intent.putExtra("businessType", businessType);//businessType
            intent.putExtra("fChooseAmount", fEntry24Set.fChooseAmount);//允许修改单价金额
            intent.putExtra("fDCStockD", fEntry24Set.fDCStockD);//仓库
            intent.putExtra("fStockPosition", fEntry24Set.fStockPosition);//仓库 在添加页面
            intent.putExtra("userSysID", LoginUsers.userSysID);//传登陆用户ID
            intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID

            startActivityForResult(intent, ADD_GOODS_REQ_CODE);
        } else {
            Toast.makeText(getContext(), "请点击选择供应商", Toast.LENGTH_SHORT).show();
        }
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
            if (resultCode == ADD_GOODS_REQ_CODE) {//选择商品

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

                    goodsInfos.add(goosMessage);


                    sums0 = add(sums0, AmountShowBug(bundle.getString("goodsSum")));
                    sums2 = add(sums2, sums0);
                    tvFentrySum.setText(AmountShow(sums2));
                    sums0 = 0;

                    handler.sendEmptyMessage(0);
                }
            } else if (resultCode == ADD_SUPPLIER_REQ_CODE) {
                fyFSupplier.setContent(bundle.getString("name"));
                fyFSupplierID = bundle.getString("sysid");
            }
        }
    }

    /**
     * 仓库选择
     */
    private void setFDCStock() {

        final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

        final String[] arrWareHouse = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry24Set.fCheckType)) {
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

        if (!"1".equals(fEntry24Set.fCheckType)) {
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
                    intFDeptNum = which;
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
     * 保管
     */
    private void setFFSManager() {

        final List<String> mapValuesList = new ArrayList<>(mapRequester.values());

        final String[] arr = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry24Set.fCheckType)) {
            if (intFSManagerNum <= mapRequester.size()) {
                fyFSManager.setContent(mapValuesList.get(intFSManagerNum));
                intFSManagerNum++;
                if (intFSManagerNum == mapRequester.size()) {
                    intFSManagerNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("保管");
            builder.setSingleChoiceItems(arr, intFSManagerNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFSManagerNum = which;
                    //2.设置
                    fyFSManager.setContent(arr[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }


    /**
     * 验收
     */
    private void setFFManager() {
        final List<String> mapValuesList = new ArrayList<>(mapRequester.values());
        final String[] arr = getStringsFormat(mapValuesList);

        if (!fEntry24Set.fCheckType.equals("1")) {
            //滑动
            if (intFFManagerNum <= mapRequester.size()) {
                fyFFManager.setContent(mapValuesList.get(intFFManagerNum));
                intFFManagerNum++;
                if (intFFManagerNum == mapRequester.size()) {
                    intFFManagerNum = 0;
                }
            }
        } else {
            //弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("验收");
            builder.setSingleChoiceItems(arr, intFFManagerNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFFManagerNum = which;
                    //2.设置
                    fyFFManager.setContent(arr[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }

    }


    /**
     * 供应商
     */
    private void setFSupplier() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("searchType", ADD_SUPPLIER_REQ_CODE);
        intent.putExtra("loginUserSysID", "供应商");
        startActivityForResult(intent, ADD_SUPPLIER_REQ_CODE);
    }

    /**
     * 选择源单
     */
    private void setFSource() {

    }


    /**
     * 采购方式
     */
    private void setFPOStyle() {
        final String items[] = {map.get("20301"), map.get("252"), map.get("251")};

        if (!fEntry24Set.fCheckType.equals("1")) {
            //滑动
            if (intFPOStyleNum <= items.length) {
                fyFPOStyle.setContent(items[intFPOStyleNum]);
                intFPOStyleNum++;
                if (intFPOStyleNum == items.length) {
                    intFPOStyleNum = 0;
                }
            }
        } else {
            //弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("采购方式");
            builder.setSingleChoiceItems(items, intFPOStyleNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFPOStyleNum = which;
                    //2.设置
                    fyFPOStyle.setContent(items[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }

    }


    /**
     * 业务类型
     */
    private void setFBizType() {

        final String items[] = {map.get("12510"), map.get("12511")};
        if (!fEntry24Set.fCheckType.equals("1")) {
            //滑动
            if (intFBizTypeNum <= items.length) {
                fyFBizType.setContent(items[intFBizTypeNum]);
                intFBizTypeNum++;
                if (intFBizTypeNum == items.length) {
                    intFBizTypeNum = 0;
                }
            }
        } else {
            //弹窗
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("业务类型");
            builder.setSingleChoiceItems(items, intFBizTypeNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFBizTypeNum = which;
                    //2.设置
                    fyFBizType.setContent(items[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }

    }


    /**
     * 采购范围
     */
    private void setFAreaPS() {
        final String items[] = {map.get("20302"), map.get("20303")};

        if (!fEntry24Set.fCheckType.equals("1")) {
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
            builder.setTitle("采购范围");
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
     * 通知日期
     */
    private void setFFetchDay() {
        TimePickerView pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setTitle("通知日期");
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

    //
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
                DBBusiness business = new DBBusiness(getContext());
                business.upDatePurchaseOrderMainPrintNum(printNum, orderId, businessType);
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
        PrintPurchaseOrderMain orderMain = manager.getPurchaseOrderInfo(orderId, businessType);
        manager.closeDB();
        int pageSize = Integer.valueOf(fEntry24Set.fPrintSame);
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


    private String printSameOrder(String titleType, PrintPurchaseOrderMain orderMain, String businessType) {
        String res = null;
        String companyName = PrefUtils.getString(getContext(), "is_company_name", "");

        ArrayList<Object> infoList = PrintUtil.getPrintInfo_2(titleType, companyName, orderMain, businessType, LoginUsers);
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
