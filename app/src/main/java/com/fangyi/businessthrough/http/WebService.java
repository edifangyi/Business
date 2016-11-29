package com.fangyi.businessthrough.http;

import android.util.Log;

import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by FANGYI on 2016/11/28.
 */

public class WebService {
    // WSDL文档中的命名空间
    private static final String targetNameSpace = "http://tempuri.org/";
    // WSDL文档中的URL
    //private static  String WSDL = "http://"+SaleApplication.serviceIPAddress+"/TDService.svc?wsdl";
    //private static final String WSDL="http://"+SaleApplication.serviceIPAddress+":8888/azh/TDService.svc?wsdl";
    //private static final String WSDL = "http://172.27.35.2/YWTService/Service.svc?wsdl";
    //private static final String WSDL = "http://hchuohuo.gnway.cc/YWTService/TDService.svc?wsdl";
    //private static final String WSDL="http://"+SaleApplication.serviceIPAddress+"/YWTService/TDService.svc?wsdl";
    private static String WSDL;


    // 用户验证方法
    private static final String AUTH_METHOD = "Authorize";
    //下载数据方法
    private static final String GETDATA_METHOD = "GetData";
    //上传数据方法
    private static final String REQUEST_PROCESS_METHOD = "RequestProcess";

    private static final String GET_TIME_METHOD = "GetDateTime";

    private SoapSerializationEnvelope envelop = new SoapSerializationEnvelope(
            SoapEnvelope.VER11);//// 使用soap协议创建envelop对象  ;

    private HttpTransportSE httpSE;


    public WebService(String wsdl)//接到传过来的Wsdl;
    {
        WSDL = wsdl;
        envelop.dotNet = true;
        httpSE = new HttpTransportSE(WSDL, 2 * 60 * 1000);
    }

    /**
     * 登陆验证
     *
     * @param user     用户编码
     * @param pwd      用户密码
     * @param deviceid 手机IMEI码
     * @return -1 失败，0 没有此用户 ，1 业务员，2 配货员，3 送货员
     */
    public int authorize(String user, String pwd, String deviceid) {
        SoapObject soapObject = new SoapObject(targetNameSpace,
                AUTH_METHOD);
        soapObject.addProperty("user", user);
        soapObject.addProperty("pwd", pwd);
        soapObject.addProperty("deviceid", deviceid);

        envelop.setOutputSoapObject(soapObject);
        try {
            httpSE.call(
                    targetNameSpace + "ITDService/" + AUTH_METHOD,
                    envelop);
            SoapObject sobj = (SoapObject) envelop.bodyIn;
            int res = Integer.parseInt(sobj.getProperty(0).toString());
            return res;
        } catch (IOException e) {
            Log.e("", e.getMessage());
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            Log.e("", e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * 获取系统数据
     *
     * @param currentPg 当前要下载的页数
     * @param tableName 要下载的表名称
     * @param param     下载参数
     * @return 协议数据
     */
    public String getData(WSReturnParam retParam, Integer currentPg, String tableName, String param) {
        SoapObject soapObject = new SoapObject(targetNameSpace,
                GETDATA_METHOD);//第一个参数表示WebService的命名空间，可以从WSDL文档中找到WebService的命名空间。第二个参数表示要调用的WebService方法名。
        soapObject.addProperty("result", retParam.result);//设置调用方法的参数值;五个参数相对应表格;执行结果
        soapObject.addProperty("totalPg", retParam.totalPg);//还需下载的数据总页数;
        soapObject.addProperty("currentPg", currentPg);//当前下载的页数;
        soapObject.addProperty("tableName", tableName);//下载的表名称;
        soapObject.addProperty("para", param);//下载的参数;用户ID;

        envelop.setOutputSoapObject(soapObject);//在上文已经使用soap协议创建envelop对象 ,现在用envelop对象发送参数;
        try {
            httpSE.call(targetNameSpace + "ITDService/" + GETDATA_METHOD, envelop);

            // 得到远程方法返回的SOAP对象
            SoapObject sobj = (SoapObject) envelop.bodyIn;
            retParam.result = Integer.parseInt(sobj.getProperty("result").toString());//执行结果;
            retParam.totalPg = Integer.parseInt(sobj.getProperty("totalPg").toString());//还需下载的数据总页数;

            String protoStr = sobj.getProperty(0).toString();//第一排的集合数据;
            Log.e("", "-----------------------------------tableName=" + tableName + ",param=" + param + ",protoStr=" + protoStr);
            if (protoStr != null && protoStr.equals("anyType{}"))
                protoStr = "";
            return protoStr;
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (XmlPullParserException e) {
            //e.printStackTrace();
        }
        return null;
    }

    /**
     * 上传数据
     *
     * @param uploadStr 上传协议字符串
     * @return 返回上传结果（ 如果是空或者等于“上传成功”表示成功）
     */
    public String uploadData(String uploadStr) {
        SoapObject soapObject = new SoapObject(targetNameSpace,
                REQUEST_PROCESS_METHOD);
        soapObject.addProperty("message", "");
        soapObject.addProperty("datas", uploadStr);
        envelop.setOutputSoapObject(soapObject);
        try {
            httpSE.call(
                    targetNameSpace + "ITDService/" + REQUEST_PROCESS_METHOD,
                    envelop);

            // 得到远程方法返回的SOAP对象
            SoapObject sobj = (SoapObject) envelop.bodyIn;
            String resMessage = sobj.getPropertyAsString("message");
            String res = sobj.getProperty(0).toString();

            return res + "," + resMessage;
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (XmlPullParserException e) {
            //e.printStackTrace();
        }
        return null;
    }

    public void close() {
        httpSE.reset();
    }

    /**
     * 后期服务系统时间戳
     *
     * @return
     */
    public String getSysTimestamp() {
        SoapObject soapObject = new SoapObject(targetNameSpace,
                GET_TIME_METHOD);
        envelop.setOutputSoapObject(soapObject);
        try {
            httpSE.call(
                    targetNameSpace + "ITDService/" + GET_TIME_METHOD,
                    envelop);

            // 得到远程方法返回的SOAP对象
            SoapObject sobj = (SoapObject) envelop.bodyIn;
            String res = sobj.getProperty(0).toString();
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
            Date date = null;
            try {
                date = df.parse(res);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            Calendar cal = Calendar.getInstance();
            cal.setTime(date);
            long timestamp = cal.getTimeInMillis();
            return Long.toString(timestamp);
        } catch (IOException e) {
            //e.printStackTrace();
        } catch (XmlPullParserException e) {
            //e.printStackTrace();
        }
        return null;
    }

}