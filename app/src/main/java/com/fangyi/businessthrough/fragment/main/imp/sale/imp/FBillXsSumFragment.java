package com.fangyi.businessthrough.fragment.main.imp.sale.imp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.activity.business.SumShowActivity;
import com.fangyi.businessthrough.activity.system.SearchActivity;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.view.DrawableCenterButton;
import com.fangyi.businessthrough.view.FYBtnRadioView;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import cn.qqtheme.framework.picker.DatePicker;

import static com.fangyi.businessthrough.application.FYApplication.ADD_HISTORY_CUSTOMER_REQ_CODE;
import static com.fangyi.businessthrough.application.FYApplication.ADD_HISTORY_GOODS_REQ_CODE;
import static com.fangyi.businessthrough.utils.business.StrFormatUtils.getStringsFormat;


/**
 * Created by FANGYI on 2016/8/27.
 */

public class FBillXsSumFragment extends BaseFragment {


    private FYBtnRadioView fyStartDate;
    private FYBtnRadioView fyEndDate;
    private FYBtnRadioView fyCustomerName;
    private FYBtnRadioView fyEmp;
    private FYBtnRadioView fyDept;
    private FYBtnRadioView fyGoods;

    private DrawableCenterButton btnSummarizing;


    private View view;


    private User LoginUser;//登陆用户信息
    private Users LoginUsers;//登陆用户信息金蝶对照表
    private Map<String, String> mapDepartment;//部门

    String strStartDate = "";
    String strEndDate = "";
    final Calendar now = Calendar.getInstance();
    private int intFSaleStyleNum = 0;
    private String strCustomerSysId = "";
    private String strGoodsSysId = "";
    private String strDept = "";

    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);//订阅
        CommonUtils.setSaleTitle(getActivity(), "销售总汇");
        view = View.inflate(getContext(), R.layout.fragment_main_sale_fbill_xs_sum, null);
        assignViews();

        return view;
    }

    private void assignViews() {
        DBBusiness business = new DBBusiness(getContext());
        mapDepartment = business.getParameter("部门");
        business.closeDB();


        fyStartDate = (FYBtnRadioView) view.findViewById(R.id.fy_start_date);
        fyEndDate = (FYBtnRadioView) view.findViewById(R.id.fy_end_date);
        fyCustomerName = (FYBtnRadioView) view.findViewById(R.id.fy_customer_name);
        fyEmp = (FYBtnRadioView) view.findViewById(R.id.fy_emp);
        fyDept = (FYBtnRadioView) view.findViewById(R.id.fy_dept);
        fyGoods = (FYBtnRadioView) view.findViewById(R.id.fy_goods);

        rb26 = (CheckBox) view.findViewById(R.id.rb_26);
        rb28 = (CheckBox) view.findViewById(R.id.rb_28);
        cb27 = (CheckBox) view.findViewById(R.id.cb_27);
        cb29 = (CheckBox) view.findViewById(R.id.cb_29);
        cbGoods = (CheckBox) view.findViewById(R.id.cb_goods);
        cbCustomer = (CheckBox) view.findViewById(R.id.cb_customer);

        cbGoodsCustomer = (CheckBox) view.findViewById(R.id.cb_goods_customer);
        btnSummarizing = (DrawableCenterButton) view.findViewById(R.id.btn_summarizing);


        fyStartDate.setTitle("开始日期");
        fyStartDate.setTitleColor(CommonUtils.getColor(R.color.text_payway));

        fyEndDate.setTitle("结束日期");
        fyEndDate.setTitleColor(CommonUtils.getColor(R.color.text_payway));

        fyCustomerName.setTitle("客户名称");
        fyCustomerName.setTitleColor(CommonUtils.getColor(R.color.text_payway));

        fyEmp.setTitle("业务员");
        fyEmp.setContent(LoginUser.userName);
        fyEmp.setTitleColor(CommonUtils.getColor(R.color.text_payway));

        fyDept.setTitle("部门");
        fyDept.setTitleColor(CommonUtils.getColor(R.color.text_payway));

        fyGoods.setTitle("商品");
        fyGoods.setTitleColor(CommonUtils.getColor(R.color.text_payway));

        fyStartDate.setOnClickListener(this);
        fyEndDate.setOnClickListener(this);
        fyCustomerName.setOnClickListener(this);
        fyEmp.setOnClickListener(this);
        fyDept.setOnClickListener(this);
        fyGoods.setOnClickListener(this);
        btnSummarizing.setOnClickListener(this);


        rb26.setOnClickListener(this);
        rb28.setOnClickListener(this);
        cb27.setOnClickListener(this);
        cb29.setOnClickListener(this);

        cbGoods.setOnClickListener(this);
        cbCustomer.setOnClickListener(this);
        cbGoodsCustomer.setOnClickListener(this);


    }

    private CheckBox rb26;
    private CheckBox rb28;
    private CheckBox cb27;
    private CheckBox cb29;
    private CheckBox cbGoods;
    private CheckBox cbCustomer;
    private CheckBox cbGoodsCustomer;

    @Override
    protected Object requestData() {
        return null;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fy_start_date:
                selectStartDate();
                break;
            case R.id.fy_end_date:
                selectEndDate();
                break;
            case R.id.fy_customer_name:
                setGoods();
                break;
            case R.id.fy_emp:

                break;
            case R.id.fy_dept:
                setFDept();
                break;
            case R.id.fy_goods:
                setGoods();
                break;
            case R.id.rb_26:

                if (rb26.isChecked() == true) {
                    rb28.setChecked(false);
                    cb27.setChecked(false);
                    cb29.setChecked(false);
                } else {
                    rb26.setChecked(true);
                    cb27.setChecked(false);
                    cb29.setChecked(false);
                    rb28.setChecked(false);
                }


                break;
            case R.id.rb_28:

                if (rb28.isChecked() == true) {
                    rb26.setChecked(false);
                    cb27.setChecked(false);
                    cb29.setChecked(false);
                } else {
                    rb26.setChecked(false);
                    cb27.setChecked(false);
                    cb29.setChecked(false);
                    rb28.setChecked(true);
                }


                break;
            case R.id.cb_27:

                if (cb27.isChecked() == true) {
                    rb26.setChecked(false);
                    rb28.setChecked(false);
                } else {
                    rb26.setChecked(false);
                    rb28.setChecked(false);
                    if (cb29.isChecked() == false) {
                        cb29.setChecked(true);
                    }
                }


                break;
            case R.id.cb_29:
                if (cb29.isChecked() == true) {
                    rb26.setChecked(false);
                    rb28.setChecked(false);
                } else {
                    rb26.setChecked(false);
                    rb28.setChecked(false);
                    if (cb27.isChecked() == false) {
                        cb27.setChecked(true);
                    }
                }

                break;
            case R.id.cb_goods:
                if (cbGoods.isChecked() == true) {
                    cbCustomer.setChecked(false);
                    cbGoodsCustomer.setChecked(false);
                } else {
                    cbGoods.setChecked(true);
                }


                break;
            case R.id.cb_customer:
                if (cbCustomer.isChecked() == true) {
                    cbGoods.setChecked(false);
                    cbGoodsCustomer.setChecked(false);
                } else {
                    cbCustomer.setChecked(true);
                }


                break;
            case R.id.cb_goods_customer:
                if (cbGoodsCustomer.isChecked() == true) {
                    cbGoods.setChecked(false);
                    cbCustomer.setChecked(false);
                } else {
                    cbGoodsCustomer.setChecked(true);
                }


                break;
            case R.id.btn_summarizing:
                summarizing();
                break;
        }
    }

    /**
     * 选择商品
     */
    private void setGoods() {
        Intent intent = new Intent(getActivity(), SearchActivity.class);
        intent.putExtra("searchType", ADD_HISTORY_CUSTOMER_REQ_CODE);
        intent.putExtra("loginUserSysID", "1");
        startActivityForResult(intent, ADD_HISTORY_CUSTOMER_REQ_CODE);
    }


    /**
     * 部门
     */
    private void setFDept() {

        final List<String> mapValuesList = new ArrayList<>(mapDepartment.values());
        final String[] arr = getStringsFormat(mapValuesList);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("部门选择");
        builder.setSingleChoiceItems(arr, intFSaleStyleNum, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                intFSaleStyleNum = which;
                //2.设置
                fyDept.setContent(arr[which]);
                strDept = arr[which];
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消", null);
        builder.show();
    }


    /**
     * 结束日期选择
     */
    private void selectEndDate() {
        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
        picker.setSelectedItem(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        picker.setRangeStart(2010, 1, 1);//开始范围
        picker.setRangeEnd(2022, 1, 1);//结束范围
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                fyEndDate.setContent(year + "年" + month + "月" + day + "日");
                strEndDate = year + "年" + month + "月" + day + "日";
            }
        });
        picker.show();
    }

    /**
     * 开始日期选择
     */
    private void selectStartDate() {
        DatePicker picker = new DatePicker(getActivity(), DatePicker.YEAR_MONTH_DAY);
        picker.setSelectedItem(now.get(Calendar.YEAR), now.get(Calendar.MONTH) + 1, now.get(Calendar.DAY_OF_MONTH));
        picker.setRangeStart(2010, 1, 1);//开始范围
        picker.setRangeEnd(2022, 1, 1);//结束范围
        picker.setOnDatePickListener(new DatePicker.OnYearMonthDayPickListener() {
            @Override
            public void onDatePicked(String year, String month, String day) {
                fyStartDate.setContent(year + "年" + month + "月" + day + '日');
                strStartDate = year + "年" + month + "月" + day + "日";
            }
        });
        picker.show();
    }

    /**
     * 汇总查询
     */
    private void summarizing() {
        String sqlStrType = "";

        if (cbGoods.isChecked() == true) {
            sqlStrType = "0";
        } else if (cbCustomer.isChecked() == true) {
            sqlStrType = "1";
        } else if (cbGoodsCustomer.isChecked() == true) {
            sqlStrType = "2";
        }

        String sqlDocumentType = "";
        if (rb26.isChecked() == true) {
            sqlDocumentType = "1";
        }
        if (rb28.isChecked() == true) {
            sqlDocumentType = "3";
        }

        if (cb27.isChecked() == true) {
            sqlDocumentType = "2";
        }

        if (cb29.isChecked() == true) {
            sqlDocumentType = sqlDocumentType + "0";
        }

        KLog.e("====" + sqlStrType + "=======" + sqlDocumentType + "=======" + strStartDate + "=======" + strEndDate + "=======" + strDept + "=======" + strCustomerSysId + "=======" + strGoodsSysId);

        Intent intent = new Intent(getActivity(), SumShowActivity.class);
        intent.putExtra("sqlStrType", sqlStrType);
        intent.putExtra("sqlDocumentType", sqlDocumentType);
        intent.putExtra("strStartDate", strStartDate);
        intent.putExtra("strEndDate", strEndDate);
        intent.putExtra("strDept", strDept);
        intent.putExtra("strCustomerSysId", strCustomerSysId);
        intent.putExtra("strGoodsSysId", strGoodsSysId);
        startActivity(intent);

    }


    /**
     * Activity数据回调
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        final Bundle bundle = data.getExtras();

        //搜索商品返回的信息
        if (resultCode != -1) {

            if (requestCode == ADD_HISTORY_GOODS_REQ_CODE) {//添加商品的回调
                fyGoods.setContent(bundle.getString("name"));
                strGoodsSysId = bundle.getString("sysid");
            } else if (requestCode == ADD_HISTORY_CUSTOMER_REQ_CODE) {//添加促销品的回调

                fyCustomerName.setContent(bundle.getString("name"));
                strCustomerSysId = bundle.getString("sysid");
                KLog.e("==========" + strCustomerSysId);

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // TODO: inflate a fragment view
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        ButterKnife.bind(this, rootView);
        return rootView;
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

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);//退订
        super.onStop();
    }
}
