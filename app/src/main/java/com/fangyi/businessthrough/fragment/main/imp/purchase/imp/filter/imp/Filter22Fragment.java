package com.fangyi.businessthrough.fragment.main.imp.purchase.imp.filter.imp;


import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.FilterChangeOrderActivity;
import com.fangyi.businessthrough.adapter.business.MenuFilterCgFragmentAdapter;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.bean.system.SearchCondition;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.listener.OnItemClickListener;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.fangyi.businessthrough.view.ListViewDecoration;
import com.socks.library.KLog;
import com.yanzhenjie.recyclerview.swipe.Closeable;
import com.yanzhenjie.recyclerview.swipe.OnSwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

import static com.fangyi.businessthrough.application.FYApplication.ADD_HISTORY_CUSTOMER_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.THE_CALLBACK_TO_REFRESH;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.add;
import static com.fangyi.businessthrough.utils.business.CalculationUtils.sub;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.AmountShowBug;


/**
 * 销售订货单
 * Created by FANGYI on 2016/10/29.
 */

public class Filter22Fragment extends BaseFragment {




    private Users LoginUsers;//登陆用户信息金蝶对照表
    private String businessType;

    SwipeMenuRecyclerView recyclerView;
    SuperTextView superTv;
    TextView btnQuery;
    private ImageView ivNone;

    private FYBtnRadioView fyStartDate;
    private FYBtnRadioView fyEndDate;
    private FYBtnRadioView fyWareHouse;
    private FYBtnRadioView fyCustomerName;

    private MenuFilterCgFragmentAdapter mAdapter;
    private List<PrintPurchaseOrderMain> orderMainList = new ArrayList<>();

    private ProgressDialog dialog;
    private double sumD = 0;
    private int sumOrderD = 0;
    private int sumIsUpLoadD = 0;

    private String strCustomerSysId = "";

    private SearchCondition conditioncc;
    private List<String> wareHouseNameList = new ArrayList<>();


    public Filter22Fragment() {

    }

    public static Filter22Fragment getInstance(String s) {
        Filter22Fragment newFragment = new Filter22Fragment();
        Bundle bundle = new Bundle();
        bundle.putString("businessType", s);
        newFragment.setArguments(bundle);
        return newFragment;
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            Bundle bundle = msg.getData();
            switch (msg.what) {
                case 0:
                    superTv.setLeftString("" + bundle.get("sum"));
                    superTv.setRightString("" + bundle.get("sumTab"));

                    if (mAdapter == null) {
                        mAdapter = new MenuFilterCgFragmentAdapter(orderMainList);
                        mAdapter.setOnItemClickListener(onItemClickListener);
                        recyclerView.setAdapter(mAdapter);
                    } else {
                        mAdapter.notifyDataSetChanged();
                    }

                    dialog.dismiss();
                    break;
            }
        }
    };

    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        View view = View.inflate(getContext(), R.layout.fragment_main_sale_fbill_xs_filtern, null);
        recyclerView = (SwipeMenuRecyclerView) view.findViewById(R.id.recycler_view);
        superTv = (SuperTextView) view.findViewById(R.id.super_tv);
        btnQuery = (TextView) view.findViewById(R.id.btn_query);
        ivNone = (ImageView) view.findViewById(R.id.iv_none);

        Bundle args = getArguments();
        businessType = args.getString("businessType");

        //初始化展示信息
        SearchCondition condition = new SearchCondition();
        initData(condition);



        setRecyclerView();
        setBtnQuery();

        return view;
    }

    /**
     * 设置RecyclerView
     */
    private void setRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));// 布局管理器。
        recyclerView.setItemAnimator(new DefaultItemAnimator());// 设置Item默认动画，加也行，不加也行。
        recyclerView.addItemDecoration(new ListViewDecoration());// 添加分割线。
        recyclerView.setSwipeMenuCreator(swipeMenuCreator);
        recyclerView.setSwipeMenuItemClickListener(menuItemClickListener);
    }


    /**
     * 设置SuperTV
     */
    private void setBtnQuery() {

        btnQuery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                conditioncc = new SearchCondition();

                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("查询条件");

                View view = View.inflate(getContext(), R.layout.dialog_sale_fentry_super_tv, null);
                fyStartDate = (FYBtnRadioView) view.findViewById(R.id.fy_start_date);
                fyEndDate = (FYBtnRadioView) view.findViewById(R.id.fy_end_date);
                fyWareHouse = (FYBtnRadioView) view.findViewById(R.id.fy_ware_house);
                fyCustomerName = (FYBtnRadioView) view.findViewById(R.id.fy_customer_name);

                fyStartDate.setTitle("起始日期");
                fyEndDate.setTitle("结束日期");
                fyWareHouse.setTitle("仓库");
                fyCustomerName.setTitle("经销商");

                if ("4".equals(businessType)) {
                    fyWareHouse.setVisibility(View.GONE);
                    fyCustomerName.setVisibility(View.GONE);
                }

                final Calendar now = Calendar.getInstance();

                fyStartDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
                        picker.setSelectedItem(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
                        picker.setRangeStart(2010, 1, 1);//开始范围
                        picker.setRangeEnd(2022, 1, 1);//结束范围
                        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                            @Override
                            public void onDatePicked(String year, String month, String day) {
                                fyStartDate.setContent(year + "年" + month + "月" + day + "日");
                                conditioncc.StartDate = fyStartDate.getText();
                            }
                        });
                        picker.show();
                    }
                });

                fyEndDate.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
                        picker.setSelectedItem(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
                        picker.setRangeStart(2010, 1, 1);//开始范围
                        picker.setRangeEnd(2022, 1, 1);//结束范围
                        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
                            @Override
                            public void onDatePicked(String year, String month, String day) {
                                fyEndDate.setContent(year + "年" + month + "月" + day + "日");
                                conditioncc.EndDate = fyEndDate.getText();
                            }
                        });
                        picker.show();
                    }
                });

                fyWareHouse.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

//                        DBBusiness business = new DBBusiness(getContext());
//                        wareHouseNameList.clear();
//                        wareHouseNameList.addAll(business.getOrderedWareHouseName(businessType));
//                        business.closeDB();
//
//                        final String[] arrWareHouse = getStringsFormat(wareHouseNameList);
//
//                        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                        builder.setTitle("仓库选择");
//                        builder.setSingleChoiceItems(arrWareHouse, 0, new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                fyWareHouse.setContent(wareHouseNameList.get(which));
//                                conditioncc.wareHouseName = wareHouseNameList.get(which);
//                                dialog.dismiss();
//                            }
//                        });
//                        builder.setNegativeButton("取消", null);
//                        builder.show();
                    }
                });


                fyCustomerName.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

//                        if (orderMainList.size() > 0) {
//                            Intent intent = new Intent(getActivity(), SearchActivity.class);
//                            intent.putExtra("searchType", ADD_HISTORY_CUSTOMER_REQ_CODE);
//                            intent.putExtra("loginUserSysID", businessType);
//                            startActivityForResult(intent, ADD_HISTORY_CUSTOMER_REQ_CODE);
//                        } else {
//                            Toast.makeText(getContext(), "无订单", Toast.LENGTH_SHORT).show();
//                        }
                    }
                });


                builder.setView(view, 15, 30, 0, 0);
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
                                if (!TextUtils.isEmpty(strCustomerSysId)) {
                                    conditioncc.CustomerSysId = strCustomerSysId;
                                }
                                initData(conditioncc);
                                conditioncc = null;
                            }
                        }
                );

                builder.setNegativeButton("取消", null);
                builder.show();
            }
        });

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle bundle = data.getExtras();
        if (requestCode != -1) {
            if (resultCode == ADD_HISTORY_CUSTOMER_REQ_CODE) {
                fyCustomerName.setContent(bundle.getString("name"));
                strCustomerSysId = bundle.getString("sysid");
                KLog.e("==========" + strCustomerSysId);
            } else if (resultCode == THE_CALLBACK_TO_REFRESH) {

                initData(conditioncc);
            }
        }
    }

    /**
     * 返回的是OrderMain对象的集合;
     *
     * @param condition
     */
    private void initData(final SearchCondition condition) {
        double sum = 0;
        int sumOrder = 0;
        int sumIsUpLoad = 0;

        dialog = new ProgressDialog(getContext());
        dialog.setMessage("正在加载...");
        dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//样式更改为水平，带进度条的那种
        dialog.show();




        DBBusiness dbManager = new DBBusiness(getContext());
        orderMainList.clear();
        orderMainList.addAll(dbManager.getPurchaseOrderOrderList(businessType, condition));


        dbManager.closeDB();

        if (orderMainList.size() == 0) {
            sum = 0;
            sumOrder = 0;
            sumIsUpLoad = 0;
            ivNone.setVisibility(View.VISIBLE);

        } else {
            for (PrintPurchaseOrderMain orderMain : orderMainList) {
                if (!"4".equals(businessType)) {
                    sum = add(sum, AmountShowBug(orderMain.allMoney));
                    sumOrder++;
                    if ("0".equals(orderMain.isUpLoad)) {
                        sumIsUpLoad++;
                    }
                }

            }

            ivNone.setVisibility(View.GONE);
        }

        sumD = sum;
        sumOrderD = sumOrder;
        sumIsUpLoadD = sumIsUpLoad;


        Message message = new Message();
        Bundle bundle = new Bundle();
//
//        if ("4".equals(businessType)) {
//            bundle.putString("sum", "退货金额:" + sum);
//        } else if ("5".equals(businessType)) {
//            bundle.putString("sum", "采购金额:" + sum);
//        } else if ("6".equals(businessType)) {
//            bundle.putString("sum", "销售金额:" + sum);
//        } else if ("7".equals(businessType)) {
//            bundle.putString("sum", "退货金额:" + sum);
//        }

        bundle.putString("sumTab", "总单数:" + sumOrder + " (未传单数:" + sumIsUpLoad + ")");

        message.what = 0;
        message.setData(bundle);
        handler.handleMessage(message);

    }


    /**
     * 点击事件
     */
    private OnItemClickListener onItemClickListener = new OnItemClickListener() {
        @Override
        public void onItemClick(int position) {

            Intent intent = new Intent(getActivity(), FilterChangeOrderActivity.class);
            intent.putExtra("modify", "0");
            intent.putExtra("orderMainID", orderMainList.get(position).id);
            intent.putExtra("printNum", orderMainList.get(position).printNum);
            intent.putExtra("isUpLoad", orderMainList.get(position).isUpLoad);
            intent.putExtra("businessType", businessType);

            startActivityForResult(intent, THE_CALLBACK_TO_REFRESH);
        }
    };

    /**
     * 菜单创建器。在Item要创建菜单的时候调用。
     */
    private SwipeMenuCreator swipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int viewType) {
            int width = getResources().getDimensionPixelSize(R.dimen.item_height);

            // MATCH_PARENT 自适应高度，保持和内容一样高；也可以指定菜单具体高度，也可以用WRAP_CONTENT。
            int height = ViewGroup.LayoutParams.MATCH_PARENT;


            {
                SwipeMenuItem addItem = new SwipeMenuItem(getContext())
                        .setBackgroundDrawable(R.drawable.selector_green)// 点击的背景。
                        .setImage(R.mipmap.ic_action_change) // 图标。
                        .setText("修改") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width) // 宽度。
                        .setHeight(height); // 高度。
                swipeLeftMenu.addMenuItem(addItem); // 添加一个按钮到左侧菜单。

            }


            // 添加右侧的，如果不添加，则右侧不会出现菜单。
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(getContext())
                        .setBackgroundDrawable(R.drawable.selector_red)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText("删除") // 文字，还可以设置文字颜色，大小等。。
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
            }
        }
    };


    /**
     * 菜单点击监听。
     */
    private OnSwipeMenuItemClickListener menuItemClickListener = new OnSwipeMenuItemClickListener() {
        /**
         * Item的菜单被点击的时候调用。
         * @param closeable       closeable. 用来关闭菜单。
         * @param adapterPosition adapterPosition. 这个菜单所在的item在Adapter中position。
         * @param menuPosition    menuPosition. 这个菜单的position。比如你为某个Item创建了2个MenuItem，那么这个position可能是是 0、1，
         * @param direction       如果是左侧菜单，值是：SwipeMenuRecyclerView#LEFT_DIRECTION，如果是右侧菜单，值是：SwipeMenuRecyclerView#RIGHT_DIRECTION.
         */
        @Override
        public void onItemClick(Closeable closeable, final int adapterPosition, int menuPosition, int direction) {


            closeable.smoothCloseMenu();// 关闭被点击的菜单。

            if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION && menuPosition == 0) {
                String fUpdateType = orderMainList.get(adapterPosition).fUpdateType;

                if ("0".equals(fUpdateType)) {
                    Toast.makeText(getContext(), "保存后不可以修改", Toast.LENGTH_SHORT).show();
                    return;
                } else if ("1".equals(fUpdateType)) {
                    if (Integer.valueOf(orderMainList.get(adapterPosition).printNum) > 0) {
                        Toast.makeText(getContext(), "打印后不可以修改", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent(getActivity(), FilterChangeOrderActivity.class);
                        intent.putExtra("modify", "1");
                        intent.putExtra("orderMainID", orderMainList.get(adapterPosition).id);
                        intent.putExtra("printNum", orderMainList.get(adapterPosition).printNum);
                        intent.putExtra("isUpLoad", orderMainList.get(adapterPosition).isUpLoad);
                        intent.putExtra("businessType", businessType);

                        startActivityForResult(intent, THE_CALLBACK_TO_REFRESH);
                    }
                } else if ("2".equals(fUpdateType)) {
                    if ("1".equals(orderMainList.get(adapterPosition).isUpLoad)) {
                        Toast.makeText(getContext(), "已上传后不可以修改", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Intent intent = new Intent(getActivity(), FilterChangeOrderActivity.class);
                        intent.putExtra("modify", "1");
                        intent.putExtra("orderMainID", orderMainList.get(adapterPosition).id);
                        intent.putExtra("printNum", orderMainList.get(adapterPosition).printNum);
                        intent.putExtra("isUpLoad", orderMainList.get(adapterPosition).isUpLoad);
                        intent.putExtra("businessType", businessType);

                        startActivityForResult(intent, THE_CALLBACK_TO_REFRESH);
                    }
                }


            }

            // TODO 这里特别注意，如果这里删除了Item，不要调用Adapter.notifyItemRemoved(position)，因为RecyclerView有个bug，调用这个方法后，后面的position会错误！
            // TODO 删除Item后调用Adapter.notifyDataSetChanged()，下面是事例代码：
            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION && menuPosition == 0) {// 删除按钮被点击。

                switch (orderMainList.get(adapterPosition).fDelType) {
                    case "0":
                        setDelete(adapterPosition);
                        Toast.makeText(getContext(), "删除完成", Toast.LENGTH_SHORT).show();
                        break;
                    case "1":
                        if (Integer.parseInt(orderMainList.get(adapterPosition).printNum) > 0) {
                            setDelete(adapterPosition);
                            Toast.makeText(getContext(), "删除完成", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "未打印不可删除", Toast.LENGTH_SHORT).show();
                        }

                        break;
                    case "2":
                        if ("1".equals(orderMainList.get(adapterPosition).isUpLoad)) {
                            setDelete(adapterPosition);
                            Toast.makeText(getContext(), "删除完成", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getContext(), "未上传不可删除", Toast.LENGTH_SHORT).show();
                        }
                        break;
                }

            }
        }
    };

    /**
     * 删除数据
     *
     * @param adapterPosition
     */
    private void setDelete(int adapterPosition) {
        if (orderMainList.size() == 0) {
            sumOrderD = 0;
            sumIsUpLoadD = 0;
            sumD = 0;
        } else {
            sumOrderD--;
            if ("0".equals(orderMainList.get(adapterPosition).isUpLoad)) {
                sumIsUpLoadD--;
            }
            sumD = sub(AmountShowBug(String.valueOf(sumD)), AmountShowBug(orderMainList.get(adapterPosition).allMoney));
        }

//
//        if ("0".equals(businessType)) {
//            superTv.setLeftString("退货金额:" + sumD);
//        } else if ("1".equals(businessType)) {
//            superTv.setLeftString("销售金额:" + sumD);
//        } else if ("2".equals(businessType)) {
//            superTv.setLeftString("销售金额:" + sumD);
//        } else if ("3".equals(businessType)) {
//            superTv.setLeftString("退货金额:" + sumD);
//        }

        superTv.setRightString("总单数:" + sumOrderD + " (未传单数:" + sumIsUpLoadD + ")");

        DBBusiness business = new DBBusiness(getContext());
        business.delOrderList(orderMainList.get(adapterPosition).id);

        orderMainList.remove(adapterPosition);
        mAdapter.notifyItemRemoved(adapterPosition);
    }


    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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

}
