package com.fangyi.businessthrough.fragment.main.imp.stock.imp;

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
import android.text.TextUtils;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.Toast;

import com.bigkoo.pickerview.TimePickerView;
import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.AddGoods_4_Activity;
import com.fangyi.businessthrough.adapter.business.MenuAddGoodsAdapter30;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.system.FEntry_30_Set;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.bluetoothprint.PrintDataService;
import com.fangyi.businessthrough.bluetoothprint.PrintUtil;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.data.Data;
import com.fangyi.businessthrough.events.AddGoodsMessage30;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WebUploadService;
import com.fangyi.businessthrough.parameter.SystemFieldValues;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.utils.system.RebootActivity;
import com.fangyi.businessthrough.view.DrawableCenterButton;
import com.fangyi.businessthrough.view.FYBtnRadioView;
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
import static com.fangyi.businessthrough.utils.business.DateUtil.getStrToDate;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTime;
import static com.fangyi.businessthrough.utils.business.DateUtil.getTimeYYYY_MM_DD_HH_MM_SS;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;
import static com.fangyi.businessthrough.utils.system.CommonUtils.getColor;

/**
 * 调拔单
 * Created by FANGYI on 2016/8/27.
 */

public class FEntry30Fragment extends BaseFragment {

    private PullToRefreshView refreshView;
    private FYBtnRadioView fyFFetchDayV;//日期
    private FYBtnRadioView fyFDCStock;//调出仓库
    private FYBtnRadioView fyFSaleStyle;//调入仓库
    private FYBtnRadioView fyFEmp;//业务员
    private FYBtnRadioView fyFFManager;
    private FYBtnRadioView fyFSManager;
    private DrawableCenterButton btnAddGoods;
    private SwipeMenuRecyclerView recyclerView1;
    private DrawableCenterButton btnSaveOrder;
    private DrawableCenterButton btnPrint;
    private DrawableCenterButton btnUpload;

    private String businessType;
    private View view;
    private Map<String, String> map;//系统参数
    private User LoginUser;//登陆用户信息
    private Users LoginUsers;//登陆用户信息金蝶对照表
    private FEntry_30_Set fEntry30Set;




    private int intFSaleStyleNum = 0;
    private int intFDCStockNum = 0;

    /**
     * 基础信息
     */
    private Map<String, String> mapWareHouse;//仓库
    private Map<String, String> mapRequester;//业务员


    private List<AddGoodsMessage30> goodsInfos = new ArrayList<>();//所有商品单

    private double sums0;
    private double sums2;


    private MenuAddGoodsAdapter30 mMenuAdapter;


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
                        mMenuAdapter = new MenuAddGoodsAdapter30(goodsInfos);
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
    private String orderId;
    private int printNum;
    private ProgressDialog pd;
    private String strOrderDate;
    private String isSaveOrder = "0";
    private String isUpload;
    private int intFFManagerNum;
    private int intFSManagerNum;

    public FEntry30Fragment() {

    }

    public static FEntry30Fragment getInstance(String s) {
        FEntry30Fragment newFragment = new FEntry30Fragment();
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
    public void onStop() {
        EventBus.getDefault().unregister(this);//退订
        super.onStop();
    }

    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        CommonUtils.setStockTitle(getActivity(), "仓库调拔单");
        view = View.inflate(getActivity(), R.layout.fragment_main_sale_fentry_30, null);
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
        fEntry30Set = manager.getFEntry_30_Set(new String[]{LoginUser.userSysID, "调拔单"});
        KLog.e("=========" + fEntry30Set.toString());

        DBBusiness business = new DBBusiness(getContext());
        orderId = CommonUtils.setStockOrderId(business.selectOrderMainCount(LoginUsers.kISID, businessType));
        mapRequester = business.getSupplier("业务员");
        mapWareHouse = business.getWareHouse(LoginUsers.kISID);
        business.closeDB();

    }

    /**
     * 初始化列表
     */
    private void assignViews() {
        refreshView = (PullToRefreshView) view.findViewById(R.id.refreshView);
        fyFFetchDayV = (FYBtnRadioView) view.findViewById(R.id.fy_FFetchDayV);
        fyFDCStock = (FYBtnRadioView) view.findViewById(R.id.fy_FDCStock);
        fyFSaleStyle = (FYBtnRadioView) view.findViewById(R.id.fy_FSaleStyle);
        fyFEmp = (FYBtnRadioView) view.findViewById(R.id.fy_FEmp);
        fyFFManager = (FYBtnRadioView) view.findViewById(R.id.fy_fFManager);
        fyFSManager = (FYBtnRadioView) view.findViewById(R.id.fy_fSManager);
        btnAddGoods = (DrawableCenterButton) view.findViewById(R.id.btn_add_goods);
        recyclerView1 = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view_1);
        btnSaveOrder = (DrawableCenterButton) view.findViewById(R.id.btn_save_order);
        btnPrint = (DrawableCenterButton) view.findViewById(R.id.btn_print);
        btnUpload = (DrawableCenterButton) view.findViewById(R.id.btn_upload);

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
        fyFFetchDayV.setTitle("调拨日期");
        strOrderDate = getTime(new Date());
        fyFFetchDayV.setContent(getTime(strOrderDate));


        fyFDCStock.setTitle("调出仓库");
        fyFDCStock.setContent(mapWareHouse.get(fEntry30Set.fDCStockD));
        if ("0".equals(fEntry30Set.fDCStockV)) {
            fyFDCStock.setVisibility(View.GONE);
        }

        if ("0".equals(fEntry30Set.fDStockPosition)) {
            fyFDCStock.setVisibility(View.GONE);
        }


        fyFSaleStyle.setTitle("调入仓库");
        fyFSaleStyle.setContent(mapWareHouse.get(fEntry30Set.fDCStockD));
        if ("0".equals(fEntry30Set.fDCStockV)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        if ("1".equals(fEntry30Set.fDStockPosition)) {
            fyFSaleStyle.setVisibility(View.GONE);
        }

        fyFEmp.setTitle("业务员");
        fyFEmp.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry30Set.fDCStockV)) {
            fyFEmp.setVisibility(View.GONE);
        }

        fyFFManager.setTitle("验收");
        fyFFManager.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry30Set.fDCStockV)) {
            fyFFManager.setVisibility(View.GONE);
        }

        fyFSManager.setTitle("保管");
        fyFSManager.setContent(LoginUsers.kISName);
        if ("0".equals(fEntry30Set.fDCStockV)) {
            fyFSManager.setVisibility(View.GONE);
        }

        if ("0".equals(fEntry30Set.fSaFePrint)) {
            btnUpload.setEnabled(false);
            btnPrint.setEnabled(false);
        } else {

            btnPrint.setVisibility(View.GONE);
            btnSaveOrder.setText("保存并打印");
            btnUpload.setText("上传单据");
            btnUpload.setEnabled(false);
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
            goodsInfos.remove(position);
            mMenuAdapter.notifyItemRemoved(position);
        }
    };


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
            case R.id.fy_FDCStock:
                setFDCStock();
                break;
            case R.id.fy_FSaleStyle:
                setFSaleStyle();
                break;
            case R.id.fy_fFManager:
                setFFManager();
                break;
            case R.id.fy_fSManager:
                setFSManager();
                break;
            case R.id.btn_add_goods:
                startAddGoods();
                break;
            case R.id.btn_save_order:
                saveOrder();
                break;
            case R.id.btn_print:
                setBtnPrint();
                break;
            case R.id.btn_upload:
                uploadOrder();
                break;
        }
    }

    private void setListener() {

        //允许选择日期
        if (fEntry30Set.fChooseDate.equals("1")) {
            fyFFetchDayV.setOnClickListener(this);

        }
        fyFDCStock.setOnClickListener(this);
        fyFSaleStyle.setOnClickListener(this);


        fyFEmp.setOnClickListener(this);
        fyFFManager.setOnClickListener(this);
        fyFSManager.setOnClickListener(this);

        btnAddGoods.setOnClickListener(this);

        btnUpload.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnSaveOrder.setOnClickListener(this);

    }

    public void setClickEvent(boolean clickEvent) {
        fyFFetchDayV.setEnabled(clickEvent);
        fyFDCStock.setEnabled(clickEvent);
        fyFSaleStyle.setEnabled(clickEvent);
        fyFEmp.setEnabled(clickEvent);
        fyFFManager.setEnabled(clickEvent);
        fyFSManager.setEnabled(clickEvent);

        btnAddGoods.setEnabled(clickEvent);
        recyclerView1.setItemViewSwipeEnabled(clickEvent);
    }


    /**
     * 上传订单
     */
    private void uploadOrder() {

        pd = ProgressDialog.show(getContext(), "数据上传", "上传中，请稍后……");
        pd.setCancelable(false);
        pd.setCanceledOnTouchOutside(false);
        pd.show();


        String result = WebUploadService.uploadStockDateService(getActivity(), orderId, businessType);

        if ("上传失败".equals(result)) {
            pd.dismiss();
            Toast.makeText(getContext(), "上传失败", Toast.LENGTH_SHORT).show();
        } else {
            pd.dismiss();
            Toast.makeText(getContext(), "上传成功", Toast.LENGTH_SHORT).show();
            btnUpload.setEnabled(false);

            if ("2".equals(fEntry30Set.fDelType)) {//上传后不可以修改
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

        if ("1".equals(fEntry30Set.fDelType)) {//打印后不可以修改
            setClickEvent(false);
        }
    }


    /**
     * 保存订单
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

    private void saveSettings() {
        btnSaveOrder.setEnabled(false);


        if ("0".equals(fEntry30Set.fSaFePrint)) {

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

        if ("0".equals(fEntry30Set.fUpdateType)) {//保存后不可以修改
            setClickEvent(false);
        }
    }


    /**
     * 保存OrderMain表
     */
    private void save() {


        String ID = orderId; //订单ID
        String OrderDate = getStrToDate(strOrderDate); //单据业务日期

        String WareHouseName_1 = fyFDCStock.getText(); //调入仓库
        String WareHouseName_2 = fyFSaleStyle.getText(); //调出仓库

        String Emp = LoginUsers.kISID;  //业务员
        String FFManager = fyFFManager.getText();   //验收
        String FSManager = fyFSManager.getText();   //保管


        String UserSysID = LoginUsers.kISID; //业务员id kisid

        String SaveTime = getTimeYYYY_MM_DD_HH_MM_SS(new Date()); //单据保存日期

        String IsUpLoad = isUpload; //是否上传
        String BusinessType = businessType; //订单类型
        String FUpdateType = fEntry30Set.fUpdateType; //修改控制
        String FDelType = fEntry30Set.fDelType; //删除控制
        String PrintNum = String.valueOf(printNum); //订单类型

        Data.saveStockOrder(ID, OrderDate, mapWareHouse, WareHouseName_1, WareHouseName_2, mapRequester, Emp, FFManager, FSManager, UserSysID, SaveTime, IsUpLoad, BusinessType, FUpdateType, FDelType, PrintNum, goodsInfos);
        Toast.makeText(getContext(), "保存完成", Toast.LENGTH_SHORT).show();
        isSaveOrder = "1";
        CommonUtils.setStockTitle(getActivity(), "调拨单（已保存）");

    }

    /**
     * 打开添加商品页面
     */
    private void startAddGoods() {
        Intent intent = new Intent(getActivity(), AddGoods_4_Activity.class);
//
        intent.putExtra("businessType", businessType);//businessType
        intent.putExtra("fChooseAmount", fEntry30Set.fChooseAmount);//允许修改单价金额
        intent.putExtra("fDCStockD", fEntry30Set.fDCStockD);//仓库
        intent.putExtra("fStockPosition", fEntry30Set.fDStockPosition);//仓库 在添加页面
        intent.putExtra("userSysID", LoginUsers.userSysID);//传登陆用户ID
        intent.putExtra("kISID", LoginUsers.kISID);//传登陆用户KISID

        startActivityForResult(intent, ADD_GOODS_REQ_CODE);

    }

    /**
     * "保管"
     */
    private void setFSManager() {

        final List<String> mapValuesList = new ArrayList<>(mapRequester.values());

        final String[] arr = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry30Set.fCheckType)) {
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

        if (!fEntry30Set.fCheckType.equals("1")) {
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
     * 调入仓库
     */
    private void setFDCStock() {

        final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

        final String[] arrWareHouse = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry30Set.fCheckType)) {
            if (intFDCStockNum <= mapWareHouse.size()) {
                fyFDCStock.setContent(mapValuesList.get(intFDCStockNum));
                intFDCStockNum++;
                if (intFDCStockNum == mapWareHouse.size()) {
                    intFDCStockNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("调入仓库");
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
     * 调出仓库
     */
    private void setFSaleStyle() {


        final List<String> mapValuesList = new ArrayList<>(mapWareHouse.values());

        final String[] arrWareHouse = getStringsFormat(mapValuesList);

        if (!"1".equals(fEntry30Set.fCheckType)) {
            if (intFSaleStyleNum <= mapWareHouse.size()) {
                fyFSaleStyle.setContent(mapValuesList.get(intFSaleStyleNum));
                intFSaleStyleNum++;
                if (intFSaleStyleNum == mapWareHouse.size()) {
                    intFSaleStyleNum = 0;
                }
            }
        } else {
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("调出仓库");
            builder.setSingleChoiceItems(arrWareHouse, intFSaleStyleNum, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    intFSaleStyleNum = which;
                    //2.设置
                    fyFSaleStyle.setContent(arrWareHouse[which]);
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        }
    }


    /**
     * 调拨日期
     */
    private void setFFetchDay() {
        TimePickerView pvTime = new TimePickerView(getContext(), TimePickerView.Type.YEAR_MONTH_DAY);
        pvTime.setTime(new Date());
        pvTime.setTitle("调拨日期");
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
            if (resultCode == ADD_GOODS_REQ_CODE) {//选择商品

                if (!TextUtils.isEmpty(bundle.getString("goodsUom"))) {

                    AddGoodsMessage30 goosMessage = new AddGoodsMessage30();
                    goosMessage.setGoodsSysID(bundle.getString("goodsSysID"));
                    goosMessage.setGoodsName(bundle.getString("goodsName"));
                    goosMessage.setGoodsNum(bundle.getString("goodsNum"));
                    goosMessage.setGoodsPrice(bundle.getString("goodsPrice"));
                    goosMessage.setGoodsSum(bundle.getString("goodsSum"));
                    goosMessage.setGoodsUom(bundle.getString("goodsUom"));
                    goosMessage.setUnitID(bundle.getString("unitID"));
                    goosMessage.setGoodsType(bundle.getString("goodsType"));
                    goosMessage.setWareHouseSysId1(bundle.getString("WareHouseSysId1"));
                    goosMessage.setWareHouseSysId2(bundle.getString("WareHouseSysId2"));
                    goosMessage.setListGoodsType("0");

                    goodsInfos.add(goosMessage);


                    handler.sendEmptyMessage(0);
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
        PrintOrderMain orderMain = manager.getOrderInfo(orderId, businessType);
        manager.closeDB();
        int pageSize = Integer.valueOf(fEntry30Set.fPrintSame);
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

        ArrayList<Object> infoList = PrintUtil.getPrintInfo_3(titleType, companyName, orderMain, businessType, LoginUsers);
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
