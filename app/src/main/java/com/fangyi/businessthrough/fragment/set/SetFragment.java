package com.fangyi.businessthrough.fragment.set;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseFragment;
import com.fangyi.businessthrough.bean.system.DeviceBill;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.fangyi.businessthrough.view.SettingClickView;
import com.fangyi.businessthrough.view.SettingItemView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by FANGYI on 2016/8/27.
 */

public class SetFragment extends BaseFragment {

    @BindView(R.id.siv_auto_login)
    SettingItemView sivAutoLogin;
    @BindView(R.id.siv_message)
    SettingItemView sivMessage;
    //    @BindView(R.id.siv_select_dpartment)
//    SettingItemView sivSelectDpartment;
//    @BindView(R.id.siv_select_warehouse)
//    SettingItemView sivSelectWarehouse;
//    @BindView(R.id.scv_default_warehouse_address)
//    SettingClickView scvDefaultWarehouseAddress;
    @BindView(R.id.scv_change_service_address)
    SettingClickView scvChangeServiceAddress;
    @BindView(R.id.scv_change_fcompany_name)
    SettingClickView scvChangeFcompanyName;
    @BindView(R.id.scv_printed_page)
    SettingClickView scvPrintedPage;
    @BindView(R.id.scv_about)
    SettingClickView scvAbout;


    private DeviceBill bill;
    private User LoginUser;


    @Override
    protected View getSuccessView() {
        EventBus.getDefault().register(this);
        View view = View.inflate(getActivity(), R.layout.fragment_set, null);
        ButterKnife.bind(this, view);
        initView();
        return view;

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUser(User user) {
        LoginUser = user;
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getLoginUserBill(DeviceBill deviceBill) {
        bill = deviceBill;
    }

    /**
     * 初始化控件
     */
    private void initView() {
        //设置自动登录
        setAutoLogin();
        //设置系统通知
        setMessage();

//        //选用部门设置
//        setSelectDpartment();
//        //选用仓库设置
//        setSelectWarehouse();
//        //更改默认仓库地址
//        changeDefaultWarehouseAddress();

        //更改服务器地址
        changeServerAddress();
        //更改公司名称
        changeFCompanyName();
        //打印单据字号
        showPrintedPage();
        About();
    }

    private void About() {
        scvAbout.setTitle("关于");
        scvAbout.setDescription("房一");
        scvAbout.setIvClickStatusVisibility(View.GONE);
    }

    /**
     * 打印单据页面设置
     */
    private void showPrintedPage() {


        scvPrintedPage.setTitle("打印单据页面设置");
        scvPrintedPage.setDescription("标题、表题、页眉、页脚");
        scvPrintedPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("打印单据页面设置");
                builder.setItems(new String[]{"标题字号：" + bill.fTitleSize + "号", "表体字号：" + bill.fEntrySize + "号", "页眉字号：" + bill.fHeaderSize + "号", "页脚字号：" + bill.fFooterSize + "号"}, null);
                builder.show();
            }
        });

    }

    /**
     * 更改公司名称
     */
    private void changeFCompanyName() {
        scvChangeFcompanyName.setTitle("公司名称");
        scvChangeFcompanyName.setDescription(bill.fCompanyName);
        scvChangeFcompanyName.setIvClickStatusVisibility(View.GONE);
        PrefUtils.setString(FYApplication.getContext(), "is_company_name", bill.fCompanyName);//保存服务器地址到本地设置数据库中

    }


//    /**
//     * 更改默认仓库地址
//     */
//    private void changeDefaultWarehouseAddress() {
//
//        final String items[] = {"粉色", "绿色", "蓝色", "紫色"};
//
//        int which = PrefUtils.getInt(SYApplication.getContext(), LoginUser.userSysID + "is_default_warehouse_address", 0);
//
//        scvDefaultWarehouseAddress.setDescription(items[which]);
//
//        scvDefaultWarehouseAddress.setTitle("更改默认仓库地址");
//        scvDefaultWarehouseAddress.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                int tt = PrefUtils.getInt(SYApplication.getContext(), LoginUser.userSysID + "is_default_warehouse_address", 0);
//                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//                builder.setTitle("归属地提示框风格");
//                builder.setSingleChoiceItems(items, tt, new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //1.保存
//                        PrefUtils.setInt(SYApplication.getContext(), LoginUser.userSysID + "is_default_warehouse_address", which);
//                        //2.设置
//                        scvDefaultWarehouseAddress.setDescription(items[which]);
//                        dialog.dismiss();
//                    }
//                });
//                builder.setNegativeButton("取消", null);
//                builder.show();
//            }
//        });
//    }
//
//
//    /**
//     * 选用仓库设置
//     */
//    private void setSelectWarehouse() {
//        final boolean selectDpartment = PrefUtils.getBoolean(SYApplication.getContext(), LoginUser.userSysID + "is_select_warehouse", false);
//        sivSelectWarehouse.setChecked(selectDpartment);
//        sivSelectWarehouse.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //得到他是否被勾选
//                if (sivSelectWarehouse.isChecked()) {
//                    //变为非勾选
//                    sivSelectWarehouse.setChecked(false);
//                    PrefUtils.setBoolean(SYApplication.getContext(), LoginUser.userSysID + "is_select_warehouse", false);
//                } else {
//                    //变为勾选
//                    sivSelectWarehouse.setChecked(true);
//                    PrefUtils.setBoolean(SYApplication.getContext(), LoginUser.userSysID + "is_select_warehouse", true);
//                }
//            }
//        });
//    }

//    /**
//     * 选用部门设置
//     */
//    private void setSelectDpartment() {
//
//        final boolean selectDpartment = PrefUtils.getBoolean(SYApplication.getContext(), LoginUser.userSysID + "is_select_dpartment", false);
//        sivSelectDpartment.setChecked(selectDpartment);
//        sivSelectDpartment.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //得到他是否被勾选
//                if (sivSelectDpartment.isChecked()) {
//                    //变为非勾选
//                    sivSelectDpartment.setChecked(false);
//                    PrefUtils.setBoolean(SYApplication.getContext(), LoginUser.userSysID + "is_select_dpartment", false);
//                } else {
//                    //变为勾选
//                    sivSelectDpartment.setChecked(true);
//                    PrefUtils.setBoolean(SYApplication.getContext(), LoginUser.userSysID + "is_select_dpartment", true);
//                }
//            }
//        });
//    }


    /**
     * 更改服务器地址
     */
    private void changeServerAddress() {

        scvChangeServiceAddress.setTitle("更改服务器地址");
        scvChangeServiceAddress.setDescription(PrefUtils.getString(FYApplication.getContext(), "is_service_address", null));//取服务器地址

        scvChangeServiceAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final EditText etService = new EditText(getActivity());
                etService.setFocusableInTouchMode(true);
                etService.requestFocus();

                etService.setText(PrefUtils.getString(FYApplication.getContext(), "is_service_address", null));
                final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("请输入服务器地址");

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
                builder.setView(etService, 60, 0, 60, 0);
                builder.setPositiveButton( "确定", null);
                final AlertDialog dialog = builder.create();
                dialog.show();
                dialog.getButton(AlertDialog.BUTTON_POSITIVE).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        String etServiceAdd = etService.getText().toString();
                        if (TextUtils.isEmpty(etServiceAdd)) {
                            Toast.makeText(getActivity(), "服务器地址不能为空", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        PrefUtils.setString(FYApplication.getContext(), "is_service_address", etServiceAdd);//保存服务器地址到本地设置数据库中
                        scvChangeServiceAddress.setDescription(etServiceAdd);
                        dialog.dismiss();
                    }
                });
            }
        });
    }


    /**
     * 设置系统通知
     */
    private void setMessage() {
        if (bill.fnotice.equals("1")) {
            final boolean systemMessage = PrefUtils.getBoolean(FYApplication.getContext(), LoginUser.userSysID + "is_system_message", true);
            sivMessage.setChecked(systemMessage);
            sivMessage.requestFocusFromTouch();
        }
    }

    /**
     * 设置自动登录
     */
    private void setAutoLogin() {
        if (bill.fAutoLanding.equals("1")) {
            PrefUtils.setBoolean(FYApplication.getContext(), "is_auto_login", true);
            PrefUtils.setString(FYApplication.getContext(), "is_auto_login_userSysID", LoginUser.userSysID);
        }
        final boolean autoLogin = PrefUtils.getBoolean(FYApplication.getContext(), "is_auto_login", false);
        sivAutoLogin.setChecked(autoLogin);
        sivAutoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //得到他是否被勾选
                if (sivAutoLogin.isChecked()) {
                    //变为非勾选
                    sivAutoLogin.setChecked(false);
                    PrefUtils.setBoolean(FYApplication.getContext(), "is_auto_login", false);
                } else {
                    //变为勾选
                    sivAutoLogin.setChecked(true);
                    PrefUtils.setBoolean(FYApplication.getContext(), "is_auto_login", true);
                    PrefUtils.setString(FYApplication.getContext(), "is_auto_login_userSysID", LoginUser.userSysID);
                }
            }
        });
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

    @Override
    public void onStop() {
        EventBus.getDefault().unregister(this);
        super.onStop();
    }
}
