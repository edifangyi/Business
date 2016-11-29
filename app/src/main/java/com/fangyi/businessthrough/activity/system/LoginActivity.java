package com.fangyi.businessthrough.activity.system;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.fangyi.businessthrough.R;
import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.base.BaseActivity;
import com.fangyi.businessthrough.bean.system.DeviceBill;
import com.fangyi.businessthrough.bean.system.DeviceUI;
import com.fangyi.businessthrough.bean.system.User;
import com.fangyi.businessthrough.bean.system.Users;
import com.fangyi.businessthrough.dao.DBManager;
import com.fangyi.businessthrough.dao.ProtocolUtil;
import com.fangyi.businessthrough.http.NetConnectionUtil;
import com.fangyi.businessthrough.http.WSReturnParam;
import com.fangyi.businessthrough.http.WebService;
import com.fangyi.businessthrough.utils.system.CommonUtils;
import com.fangyi.businessthrough.utils.system.PrefUtils;
import com.socks.library.KLog;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.fangyi.businessthrough.application.FYApplication.getContext;


/**
 * Created by FANGYI on 2016/8/15.
 */

public class LoginActivity extends BaseActivity {

    private static final int GO_HOME = 1000;
    private static final int GO_MAIN = 1001;
    private static final int SHOW_SERVICE_DIALOG = 1002;
    private static final int VALIDATION_FAILURE = 1003;
    private static final int PASSWORD_ERROR = 1004;
    private static final int USER_NOT_EXIST = 1005;
    private static final int CONNECT_SERVER_FAILED = 1006;
    private static final int SERVICE_NOT_SET_USER = 1007;
    @BindView(R.id.et_userpassword)
    EditText etUserPassword;
    @BindView(R.id.cb_antologin)
    CheckBox cbAntologin;
    @BindView(R.id.tv_change_service)
    TextView tvChangeService;
    @BindView(R.id.et_usersysid)
    EditText etUserSysId;
    @BindView(R.id.login_view)
    Button loginView;

    private String serviceAddress;//服务器地址
    private String password;//当前未登陆用户名
    private String userSysId;//当前未登陆用户密码
    private int userType;//当前未登陆用户验证返回码
    private String sysTimestamp;//系统时时间戳

    private ProgressDialog dialog;


    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {

            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            switch (msg.what) {
                case GO_HOME:
                    dialog.dismiss();
                    intent.putExtra("LoginUserID", userSysId);
                    startActivity(intent);
                    finish();
                    break;
                case GO_MAIN:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "登陆成功", Toast.LENGTH_SHORT).show();
                    intent.putExtra("LoginUserID", userSysId);
                    startActivity(intent);
                    finish();
                    break;
                case SHOW_SERVICE_DIALOG:
                    dialog.dismiss();
                    setServiceAddress();
                    break;
                case VALIDATION_FAILURE:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "用户信息服务验证失败", Toast.LENGTH_SHORT).show();
                    break;
                case CONNECT_SERVER_FAILED:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "连接服务器失败", Toast.LENGTH_SHORT).show();
                    break;

                case PASSWORD_ERROR:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "用户名密码错误", Toast.LENGTH_SHORT).show();
                    break;

                case USER_NOT_EXIST:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "数据库中未有此用户，请联网登陆", Toast.LENGTH_SHORT).show();
                    break;
                case SERVICE_NOT_SET_USER:
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "服务端未设置该用户", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        showServiceDialog();
    }


    /**
     * 第一次进入APK，设置服务器地址
     */
    private void showServiceDialog() {
        etUserSysId.setText(PrefUtils.getString(getContext(), "is_remember_user_name", ""));
        etUserPassword.setText(PrefUtils.getString(getContext(), "is_remember_user_password", ""));

        boolean isFristLogin = PrefUtils.getBoolean(this, "is_show_service_dialog", false);
        if (!isFristLogin) {
            setServiceAddress();
        }
        judgeAutoLogin();
    }


    /**
     * 输入服务器地址弹窗
     */
    private void setServiceAddress() {

        final EditText etService = new EditText(this);
        etService.setText(PrefUtils.getString(getContext(), "is_service_address", ""));
        etService.setFocusableInTouchMode(true);
        etService.requestFocus();

        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
        builder.setPositiveButton(
                "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        serviceAddress = etService.getText().toString();
                       PrefUtils.setString(FYApplication.getContext(), "is_service_address", serviceAddress);//保存服务器地址到本地设置数据库中

                        if (TextUtils.isEmpty(serviceAddress)) {
                            Toast.makeText(LoginActivity.this, "服务器地址不能为空", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        );

        builder.show();

    }

    /**
     * 判断是否是自动登录
     */
    private void judgeAutoLogin() {
        //4.判断是否开启自动登录
        boolean autoLogin = PrefUtils.getBoolean(LoginActivity.this, "is_auto_login", false);
        if (autoLogin) {
            userSysId = PrefUtils.getString(LoginActivity.this, "is_auto_login_userSysID", null);
            onEventBus(userSysId);
//            land();
        } else {
            land();
        }
    }

    /**
     * 自动登录时候调用的
     *
     * @param userSysId
     */
    private void onEventBus(final String userSysId) {
        new Thread() {
            @Override
            public void run() {
                DBManager dbManager = new DBManager(getContext());
                User user = dbManager.getUserInfo(new String[]{userSysId});
                Users users = dbManager.getUsersInfo(new String[]{userSysId});
                DeviceUI deviceUI = dbManager.getDeviceUI(new String[]{userSysId});
                DeviceBill deviceBill = dbManager.getDeviceBill(new String[]{userSysId});
                dbManager.closeDB();

                if (deviceUI != null && deviceBill != null) {
                    EventBus.getDefault().postSticky(user);
                    EventBus.getDefault().postSticky(users);
                    EventBus.getDefault().postSticky(deviceUI);
                    EventBus.getDefault().postSticky(deviceBill);
                    handler.sendEmptyMessage(GO_HOME);
                } else {
                    handler.sendEmptyMessage(SERVICE_NOT_SET_USER);
                }
            }
        }.start();
    }


    /**
     * 登陆准备
     */
    private void land() {
        //保存第一次验证通过后的,输入服务器地址
        PrefUtils.setBoolean(this, "is_show_service_dialog", true);

        //更改服务器地址
        tvChangeService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setServiceAddress();
            }
        });


        //登陆按钮操作
        loginView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {


                userSysId = etUserSysId.getText().toString();
                password = etUserPassword.getText().toString();


                dialog = new ProgressDialog(LoginActivity.this);
                dialog.setMessage("正在登陆...");
                dialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);//样式更改为水平，带进度条的那种
                dialog.show();


                //2.判断是否为空
                if (TextUtils.isEmpty(userSysId)) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                //3.判断是否为空
                if (TextUtils.isEmpty(password)) {
                    dialog.dismiss();
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                PrefUtils.setString(getContext(), "is_remember_user_name", userSysId);
                PrefUtils.setString(getContext(), "is_remember_user_password", password);

                //自动登陆
                if (!cbAntologin.isChecked()) {
                   PrefUtils.setBoolean(FYApplication.getContext(), "is_auto_login", false);
                    PrefUtils.setString(FYApplication.getContext(), "is_auto_login_userSysID", null);
                } else {
                    PrefUtils.setBoolean(FYApplication.getContext(), "is_auto_login", true);
                    PrefUtils.setString(FYApplication.getContext(), "is_auto_login_userSysID", userSysId);
                }

                final String WSserviceAddress = PrefUtils.getString(getContext(), "is_service_address", null);

                if (WSserviceAddress == null) {
                    Toast.makeText(LoginActivity.this, "服务器地址为空，请重新设置", Toast.LENGTH_SHORT).show();
                    return;
                }
                final String IP;
                try {
                    IP = NetConnectionUtil.getIPAddress(WSserviceAddress);
                } catch (Exception e) {
                    Toast.makeText(LoginActivity.this, "正确填写服务器地址", Toast.LENGTH_SHORT).show();
                    return;
                }


                new Thread() {
                    @Override
                    public void run() {
                        //判断网络是否可用
                        if (NetConnectionUtil.isNetworkAvailable(getContext())) {
                            //判断服务器是否通信
                            if (NetConnectionUtil.pingService(IP)) {
                                String deviceid = CommonUtils.getIMEI(LoginActivity.this);//手机IMEI码
//                                String deviceid = "99000716062075";

                                //服务器地址
                                //"http://192.168.137.1:9000/TDService.svc?wsdl"
                                //获取服务器地址
                                WebService webService = new WebService(WSserviceAddress);
                                userType = webService.authorize(userSysId, password, deviceid);//验证登陆
                                if (userType > 0) {
                                    //验证通过
                                    sysTimestamp = webService.getSysTimestamp();//获取系统时间戳
                                    KLog.e("=======时间戳======" + sysTimestamp);

                                    userUpdate(webService);//下载UI布局
                                } else {
                                    handler.sendEmptyMessage(VALIDATION_FAILURE);//用户信息服务验证失败
                                }
                            } else {
                                //不通信 本地验证
                                localValidation();
                            }

                        } else {
                            //不可用
                            localValidation();
                        }
                    }
                }.start();

            }

        });
    }

    /**
     * 本地验证
     */
    private void localValidation() {
        new Thread() {
            @Override
            public void run() {
                DBManager dbManager = new DBManager(getContext());

                String localPassword = dbManager.queryLogin(userSysId);
                if (localPassword == null) {
                    handler.sendEmptyMessage(USER_NOT_EXIST);//用户不存在
                } else if (localPassword.equals(password)) {

                    User user = dbManager.getUserInfo(new String[]{userSysId});
                    Users users = dbManager.getUsersInfo(new String[]{userSysId});
                    DeviceUI deviceUI = dbManager.getDeviceUI(new String[]{userSysId});
                    DeviceBill deviceBill = dbManager.getDeviceBill(new String[]{userSysId});
                    dbManager.closeDB();

                    if (users != null && deviceUI != null && deviceBill != null) {
                        EventBus.getDefault().postSticky(user);
                        EventBus.getDefault().postSticky(users);
                        EventBus.getDefault().postSticky(deviceUI);
                        EventBus.getDefault().postSticky(deviceBill);
                        handler.sendEmptyMessage(GO_MAIN);//登陆
                    } else {
                        handler.sendEmptyMessage(SERVICE_NOT_SET_USER);
                    }

                } else {
                    handler.sendEmptyMessage(PASSWORD_ERROR);//密码错误
                }
            }
        }.start();
    }

    /**
     * 下载UI布局，并且保存到数据库中
     *
     * @param webService
     */
    private void userUpdate(WebService webService) {
        DBManager dbManager = new DBManager(this);
        WSReturnParam wsParam = new WSReturnParam();
        String param = userSysId;//用户名
        Integer currentPg = 0; // 当前执行页数

        wsParam.result = 0; // 执行结果
        wsParam.totalPg = 0;// 总共还需执行页数
        String protoUsersStr = webService.getData(wsParam, currentPg, "Users", param);
        List<String> usersql = ProtocolUtil.getInsertSQL("Users", protoUsersStr);

        wsParam.result = 0;
        wsParam.totalPg = 0;
        String protoDeviceUIStr = webService.getData(wsParam, currentPg, "DeviceUI", param);
        List<String> deviceuisql = ProtocolUtil.getInsertSQL("DeviceUI", protoDeviceUIStr);

        wsParam.result = 0;
        wsParam.totalPg = 0;
        String protoDeviceBillSetStr = webService.getData(wsParam, currentPg, "DeviceBillSet", param);
        List<String> devicebillsetsql = ProtocolUtil.getInsertSQL("DeviceBillSet", protoDeviceBillSetStr);

        wsParam.result = 0;
        wsParam.totalPg = 0;
        String protoDeviceBillStr = webService.getData(wsParam, currentPg, "DeviceBill", param);
        List<String> devicebillsql = ProtocolUtil.getInsertSQL("DeviceBill", protoDeviceBillStr);


        String localPassword = dbManager.queryLogin(userSysId);
        if (localPassword == null) {
            //新用户首次登陆，保存信息到数据库中
            dbManager.setUserInfo(userSysId, password, String.valueOf(userType), sysTimestamp);
            dbManager.insertDownloadData(usersql);
            dbManager.insertDownloadData(deviceuisql);
            dbManager.insertDownloadData(devicebillsql);
            dbManager.insertDownloadData(devicebillsetsql);
        } else {
            dbManager.deleteDevice("Users", userSysId);//清空当前用户UI表
            dbManager.insertDownloadData(usersql);//重新插入新的
            dbManager.deleteDevice("DeviceUI", userSysId);
            dbManager.insertDownloadData(deviceuisql);
            dbManager.deleteDevice("DeviceBillSet", userSysId);
            dbManager.insertDownloadData(devicebillsetsql);
            dbManager.deleteDevice("DeviceBill", userSysId);
            dbManager.insertDownloadData(devicebillsql);
        }

        String localPassword2 = dbManager.queryLogin(userSysId);
        if (!localPassword2.equals(password)) {
            //老用户登陆，如果密码不同，修改密码
            dbManager.updataPassword(userSysId, password);
        }

        User user = dbManager.getUserInfo(new String[]{userSysId});
        Users users = dbManager.getUsersInfo(new String[]{userSysId});
        DeviceUI deviceUI = dbManager.getDeviceUI(new String[]{userSysId});
        DeviceBill deviceBill = dbManager.getDeviceBill(new String[]{userSysId});
        dbManager.closeDB();

        if (users != null && deviceUI != null && deviceBill != null) {
            EventBus.getDefault().postSticky(user);
            EventBus.getDefault().postSticky(users);
            EventBus.getDefault().postSticky(deviceUI);
            EventBus.getDefault().postSticky(deviceBill);
            handler.sendEmptyMessage(GO_MAIN);
        } else {
            handler.sendEmptyMessage(SERVICE_NOT_SET_USER);
        }

    }
}