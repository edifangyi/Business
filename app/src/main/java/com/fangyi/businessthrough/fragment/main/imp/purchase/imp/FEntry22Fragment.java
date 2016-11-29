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
import android.text.InputType;
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.AddGoodsActivity;
import com.fangyi.businessthrough.adapter.business.MenuAddGoodsAdapter;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.bean.system.FEntry_22_Set;
import com.fangyi.businessthrough.parameter.SystemFieldValues;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.utils.system.RebootActivity;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WebUploadService;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.bluetoothprint.PrintDataService;
import com.fangyi.businessthrough.bluetoothprint.PrintUtil;
import com.fangyi.businessthrough.view.DrawableCenterButton;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.fangyi.businessthrough.view.FYEtItemView;
import com.fangyi.businessthrough.view.FYLayoutManager;
import com.fangyi.businessthrough.view.ListViewDecoration;
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
import static com.fangyi.businessthrough.utils.system.CommonUtils.getColor;
import static com.fangyi.businessthrough.utils.business.DateUtil.getStrToDate;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTime;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeNYR;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * 采购申请单
 * Created by FANGYI on 2016/8/27.
 */

public class FEntry22Fragment extends BaseFragment {
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
    private FEntry_22_Set fEntry22Set;


    /**
     * 基础信息
     */
    private Map<String, String> mapDepartment;//部门
    private Map<String, String> mapWareHouse;//仓库


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
                case 4:
                    btnAddGoods.setEnabled(true);
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
    private String strFPlanFetchDay_1;
    private String strFPlanFetchDay_2;
    private int intFBizTypeNum;
    private int intFDeptNum;

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);//退订
        super.onStop();
    }

    public FEntry22Fragment() {

    }


    public static FEntry22Fragment getInstance(String s) {
        FEntry22Fragment newFragment = new FEntry22Fragment();
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
        CommonUtils.setPurchaseTitle(getActivity(), "采购申请");
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
        fEntry22Set = manager.getFEntry_22_Set(new String[]{LoginUser.userSysID, "采购申请单"});
        manager.closeDB();


        DBBusiness business = new DBBusiness(getContext());
        orderId = CommonUtils.setPurchaseOrderId(business.selectPurchaseOrderMainCount(LoginUsers.kISID, businessType));
        
        mapDepartment = business.getParameter("部门");
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
        fyFFetchDayV.setTitle("申请日期");
        strOrderDate = getTime(new Date());
        fyFFetchDayV.setContent(getTime(strOrderDate));


        fyFPlanFetchDay1.setTitle("采购日期");
        strFPlanFetchDay_1 = getTimeNYR(Integer.parseInt(fEntry22Set.fPlanBeginDay));
        fyFPlanFetchDay1.setContent(getTime(strFPlanFetchDay_1));

        fyFPlanFetchDay2.setTitle("到货日期");
        strFPlanFetchDay_2 = getTimeNYR(Integer.parseInt(fEntry22Set.fPlanFetchDay));
        fyFPlanFetchDay2.setContent(getTime(strFPlanFetchDay_2));

        fyFBizType.setTitle("业务类型");
        fyFBizType.setContent(map.get(fEntry22Set.fBizTypeD));
        if ("0".equals(fEntry22Set.fBizTypeV)) {
            fyFBizType.setVisibility(View.GONE);
        }

        fyFRequester.setTitle("申请人");
        fyFRequester.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry22Set.fRequesterV)) {
            fyFRequester.setVisibility(View.GONE);
        }

        fyFDept.setTitle("部门");
        fyFDept.setContent(mapDepartment.get(fEntry22Set.fDeptD));
        if ("0".equals(fEntry22Set.fDeptV)) {
            fyFDept.setVisibility(View.GONE);
        }

        fyFDigest.setTitle("摘要");

        if ("0".equals(fEntry22Set.fSaFePrint)) {

            btnUpload.setEnabled(false);
            btnPrint.setEnabled(false);

        } else {

            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
            btnUpload.setEnabled(false);
        }

        if ("2".equals(fEntry22Set.fOnType)) {//批量
            btnUpload.setVisibility(View.GONE);
        }

        fyFPOStyle.setVisibility(View.GONE);
        fyFAreaPS.setVisibility(View.GONE);
        fyFSource.setVisibility(View.GONE);
        fyFSupplier.setVisibility(View.GONE);
        fyFSManager.setVisibility(View.GONE);
        fyFFManager.setVisibility(View.GONE);
        fyFDCStock.setVisibility(View.GONE);
        llFentrySum.setVisibility(View.GONE);

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
            goodsInfos.remove(position);
            mMenuAdapter.notifyItemRemoved(position);

        }

    };

    private void setListener() {
        if ("1".equals(fEntry22Set.fChooseDate)) {
            fyFFetchDayV.setOnClickListener(this);
            fyFPlanFetchDay1.setOnClickListener(this);
            fyFPlanFetchDay2.setOnClickListener(this);
        }

        fyFBizType.setOnClickListener(this);
        fyFDept.setOnClickListener(this);
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
            case R.id.fy_FPlanFetchDay1:
                setFPlanFetchDay1();
                break;
            case R.id.fy_FPlanFetchDay2:
                setFPlanFetchDay2();
                break;
            case R.id.fy_FBizType:
                setFBizType();
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

        fyFBizType.setEnabled(b);

        fyFDept.setEnabled(b);
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


            if ("2".equals(fEntry22Set.fDelType)) {//上传后不可以修改
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


        if ("1".equals(fEntry22Set.fDelType)) {//打印后不可以修改
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


        if ("0".equals(fEntry22Set.fSaFePrint)) {

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


        if ("0".equals(fEntry22Set.fUpdateType)) {//保存后不可以修改
            setClickEvent(false);
        }
    }

    /**
     * 保存单据
     */
    private void save() {
        String ID = orderId;
        String strFyFFetchDayV = getStrToDate(strOrderDate);
        String strFyFPlanFetchDay1 = getStrToDate(strFPlanFetchDay_1);
        String strFyFPlanFetchDay2 = getStrToDate(strFPlanFetchDay_2);
        String strFyFPOStyle = "";
        String strFyFAreaPS = "";
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
        String strFUpdateType = fEntry22Set.fUpdateType;
        String strFDelType = fEntry22Set.fDelType;
        String strPrintNum = String.valueOf(printNum);

        isSaveOrder = "1";

        Data.savePurchaseOrder(ID, strFyFFetchDayV, strFyFPlanFetchDay1, strFyFPlanFetchDay2, strFyFPOStyle, strFyFAreaPS,
                strFyFBizType, strFyFSource, strFyFSupplier, strFyFRequester, mapDepartment, strFyFDept, strFyFFManager, strFyFSManager,
                mapWareHouse, strFyFDCStock, strFyFDigest, strBusinessType, strIsUpLoad, strFUpdateType, strFDelType, strPrintNum, AllMoney, goodsInfos, map);
        Toast.makeText(getContext(), "保存完成", Toast.LENGTH_SHORT).show();

        CommonUtils.setPurchaseTitle(getActivity(), "采购申请（已保存）");
    }

    /**
     * 添加商品
     */
    private void startAddGoods() {
        Intent intent = new Intent(getActivity(), AddGoodsActivity.class);
        intent.putExtra("addType", "0");
        intent.putExtra("businessType", "1");//businessType
        intent.putExtra("fChooseAmount", "0");//允许修改单价金额
        intent.putExtra("fDCStockD", "0");//仓库
        intent.putExtra("fStockPosition", "0");//仓库 在添加页面
        intent.putExtra("fPlanPro", "0");//启用搭赠方案
        intent.putExtra("userSysID", LoginUsers.userSysID);//传登陆用户ID
        intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID
        intent.putExtra("fyFCustNameID", "");//传登陆用户KISID

        startActivityForResult(intent, ADD_GOODS_REQ_CODE);
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
                    goosMessage.setGoodsPrice("0");
                    goosMessage.setGoodsSum("0");
                    goosMessage.setGoodsUom(bundle.getString("goodsUom"));
                    goosMessage.setUnitID(bundle.getString("unitID"));
                    goosMessage.setGoodsType("4");
                    goosMessage.setWareHouseSysId("");
                    goosMessage.setListGoodsType("0");

                    goodsInfos.add(goosMessage);


                    handler.sendEmptyMessage(0);
                }
            }
        }
    }


    /**
     * 部门选择
     */
    private void setFDept() {

        final List<String> mapValuesList = new ArrayList<>(mapDepartment.values());

        final String[] arr = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry22Set.fCheckType)) {
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
     * 业务类型
     */
    private void setFBizType() {
        final String items[] = {map.get("12510"), map.get("12511")};
        if (!fEntry22Set.fCheckType.equals("1")) {
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
     * 到货日期
     */
    private void setFPlanFetchDay2() {
        TimePickerView pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setTitle("到货日期");
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                strFPlanFetchDay_2 = getTime(date);
                fyFPlanFetchDay2.setContent(getTime(strFPlanFetchDay_2));
            }
        });
        pvTime.show();

    }

    /**
     * 采购日期
     */
    private void setFPlanFetchDay1() {
        TimePickerView pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setCyclic(false);
        pvTime.setTitle("采购日期");
        pvTime.setCancelable(true);
        //时间选择后回调
        pvTime.setOnTimeSelectListener(new TimePickerView.OnTimeSelectListener() {

            @Override
            public void onTimeSelect(Date date) {
                strFPlanFetchDay_1 = getTime(date);
                fyFPlanFetchDay1.setContent(getTime(strFPlanFetchDay_1));
            }
        });
        pvTime.show();

    }


    /**
     * 订货日期
     */
    private void setFFetchDay() {
        TimePickerView pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setTitle("申请日期");
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
        int pageSize = Integer.valueOf(fEntry22Set.fPrintSame);
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
