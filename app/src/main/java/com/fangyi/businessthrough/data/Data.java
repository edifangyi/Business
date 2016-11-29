package com.fangyi.businessthrough.data;

import android.content.Context;
import android.text.TextUtils;

import com.fangyi.businessthrough.application.FYApplication;
import com.fangyi.businessthrough.bean.business.Goods;
import com.fangyi.businessthrough.bean.business.OrderDetail;
import com.fangyi.businessthrough.bean.business.OrderMain;
import com.fangyi.businessthrough.bean.business.PrintOrderMain;
import com.fangyi.businessthrough.bean.business.PrintPurchaseOrderMain;
import com.fangyi.businessthrough.bean.business.PurchaseOrderMain;
import com.fangyi.businessthrough.bean.business.SEOrderMain;
import com.fangyi.businessthrough.dao.DBBusiness;
import com.fangyi.businessthrough.events.AddGoodsMessage;
import com.fangyi.businessthrough.events.AddGoodsPromotion;
import com.fangyi.businessthrough.utils.business.CalculationUtils;
import com.socks.library.KLog;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.fangyi.businessthrough.application.FYApplication.getContext;


/**
 * Created by FANGYI on 2016/11/5.
 */

public class Data {


    public static void saveOrder(String ID, String orderDate, String deliveryDate, String customerSySID, String wareHouseName,
                                 Map<String, String> mapWareHouse, String userSysID, String saveTime,
                                 String isUpLoad, String message, String deptID, Map<String, String> mapDepartment, String businessType,
                                 String FAreaPS, String FSaleType, Map<String, String> map, String allMoney, String FUpdateType,
                                 String FDelType, String printNum, String fyFSource,
                                 List<AddGoodsMessage> goodsInfos, List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos) {


        OrderMain orderMain = new OrderMain();

        orderMain.setID(ID);   //S+日期+业务编号+流水号
        orderMain.setOrderDate(orderDate);//单据业务日期
        orderMain.setDeliveryDate(deliveryDate);   //交货日期
        orderMain.setCustomerSySID(customerSySID);  //客户id即cusotmer里的sysid
        orderMain.setWareHouseName(wareHouseName);    //仓库名称

        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {

            if (stringStringEntry.getValue().equals(wareHouseName)) {
                orderMain.setWareHouseSysID(stringStringEntry.getKey().toString()); //仓库id
            } else {
                orderMain.setWareHouseSysID(""); //仓库id
            }
        }

        orderMain.setUserSysID(userSysID);   //业务员id kisid
        orderMain.setSaveTime(saveTime);  //单据保存日期
        orderMain.setIsUpLoad(isUpLoad);//是否上传
        orderMain.setMessage(message);//备注

        for (Map.Entry<String, String> stringStringEntry : mapDepartment.entrySet()) {
            if (stringStringEntry.getValue().equals(deptID)) {
                orderMain.setDeptID(stringStringEntry.getKey().toString()); //部门id
            }
        }

        orderMain.setBusinessType(businessType);//订单类型

        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {

            if (stringStringEntry.getValue().equals(FAreaPS)) {
                orderMain.setFAreaPS(stringStringEntry.getKey().toString());    //销售范围
            }

            if (stringStringEntry.getValue().equals(FSaleType)) {
                orderMain.setFSaleType(stringStringEntry.getKey().toString());  //销售方式
            }
        }


        orderMain.setAllMoney(allMoney);    //总金额
        orderMain.setFUpdateType(FUpdateType);  //不允许修改单据控制方式
        orderMain.setFDelType(FDelType);    //允许删除单据控制方式
        orderMain.setPrintNum(printNum);    //打印次数
        orderMain.setFyFSource(fyFSource);


        KLog.e("====000====" + orderMain.toString());

        /*********************************************************************************/

        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            KLog.e("====$$$$$===" + goodsInfo.toString());
        }

        if (goodsInfos != null && goodsInfos.size() != 0) {

            for (AddGoodsMessage goodsInfo : goodsInfos) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setID(ID); //S+日期+业务编号+流水号
                orderDetail.setGoodsSysID(goodsInfo.getGoodsSysID());//商品id  goods 里sysid
                orderDetail.setStandard(goodsInfo.getGoodsUom());//规格

                if (TextUtils.isEmpty(goodsInfo.getNotNum()) && TextUtils.isEmpty(goodsInfo.getOutNum())) {
                    orderDetail.setOrderNum(goodsInfo.getGoodsNum());//数量
                } else {
                    orderDetail.setOrderNum(goodsInfo.getNotNum());//数量
                }

                orderDetail.setOrderType(goodsInfo.getGoodsType());//标识
                orderDetail.setOrderMoney(goodsInfo.getGoodsSum());//总价
                orderDetail.setOrderPrice(goodsInfo.getGoodsPrice());//价格
                orderDetail.setUnitID(goodsInfo.getUnitID());//单位ID

                orderDetail.setOrderFree("");//方案标识
                orderDetail.setOrderbyGoodID("");//id  主键
                orderDetail.setNode("");//备注
                orderDetail.setListGoodsType("");

                if (TextUtils.isEmpty(goodsInfo.wareHouseSysId)) {
                    orderDetail.setWareHouseSysId("");
                } else {
                    orderDetail.setWareHouseSysId(goodsInfo.wareHouseSysId);
                }



                for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                    if (stringStringEntry.getValue().equals(wareHouseName)) {
                        orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());//仓库id
                    }
                }
                orderDetail.setBusinessType(businessType);//订单类型
                KLog.e("====$$$$$===" + orderDetail.toString());
                orderDetailArrayList.add(orderDetail);
            }
        }


        if (goodsPromotionInfos != null && goodsPromotionInfos.size() != 0) {


            ArrayList<ArrayList<AddGoodsPromotion>> aaa1 = new ArrayList<>();
            for (HashMap<String, AddGoodsPromotion> goodsPromotionInfo : goodsPromotionInfos) {
                ArrayList<AddGoodsPromotion> aaa2 = new ArrayList<>();
                for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : goodsPromotionInfo.entrySet()) {
                    aaa2.add(stringAddGoodsPromotionEntry.getValue());
                }
                aaa1.add(aaa2);
            }


            KLog.e("=====************====1111111111====" + aaa1.toString());


            for (ArrayList<AddGoodsPromotion> addGoodsPromotions : aaa1) {

                for (int i = 0; i < addGoodsPromotions.size() + 1; i++) {

                    if (i != 0 && i == addGoodsPromotions.size()) {

                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setID(ID);
                        orderDetail.setGoodsSysID(addGoodsPromotions.get(i - 1).getPromotionID());
                        orderDetail.setStandard(addGoodsPromotions.get(i - 1).getGoodsUnit());
                        orderDetail.setOrderNum(addGoodsPromotions.get(i - 1).getPromotionCount());
                        orderDetail.setUnitID(addGoodsPromotions.get(i - 1).getPromotionUnitID());
                        orderDetail.setOrderPrice("0");
                        orderDetail.setOrderMoney("0");
                        orderDetail.setOrderType("2");
                        orderDetail.setOrderFree(addGoodsPromotions.get(i - 1).getfName());
                        orderDetail.setOrderbyGoodID("");
                        orderDetail.setNode("");

                        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                            if (stringStringEntry.getValue().equals(addGoodsPromotions.get(i - 1).getPromotionWareHouseSysId())) {
                                orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());
                            } else {
                                orderMain.setWareHouseSysID(""); //仓库id
                            }
                        }

                        orderDetail.setBusinessType(businessType);
                        orderDetail.setListGoodsType(String.valueOf(addGoodsPromotions.get(i - 1).getListGoodsType()));

                        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                            if (stringStringEntry.getValue().equals(wareHouseName)) {
                                orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());
                            }
                        }

                        KLog.e("=====************====333333333333====" + orderDetail.toString());

                        orderDetailArrayList.add(orderDetail);

                    } else {

                        OrderDetail orderDetail = new OrderDetail();

                        orderDetail.setID(ID);
                        orderDetail.setGoodsSysID(addGoodsPromotions.get(i).getGoodsSysID());
                        orderDetail.setStandard(addGoodsPromotions.get(i).getGoodsUnit());
                        orderDetail.setOrderNum(addGoodsPromotions.get(i).getGoodsNumber());
                        orderDetail.setOrderPrice(addGoodsPromotions.get(i).getGoodsPrice());
                        orderDetail.setOrderMoney(addGoodsPromotions.get(i).getGoodsSumPrice());
                        orderDetail.setOrderType(addGoodsPromotions.get(i).getGoodsType());
                        orderDetail.setOrderFree(addGoodsPromotions.get(i).getfName());
                        orderDetail.setUnitID(addGoodsPromotions.get(i).getGoodsUnitID());
                        orderDetail.setOrderbyGoodID("");
                        orderDetail.setNode("");
                        orderDetail.setWareHouseSysId("");
                        orderDetail.setBusinessType(businessType);
                        orderDetail.setListGoodsType(String.valueOf(addGoodsPromotions.get(i).getListGoodsType()));

                        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                            if (stringStringEntry.getValue().equals(wareHouseName)) {
                                orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());
                            }
                        }
                        KLog.e("=====************====2222222222====" + orderDetail.toString());
                        orderDetailArrayList.add(orderDetail);

                    }

                }
            }
        }

        DBBusiness manager = new DBBusiness(FYApplication.getContext());
        manager.saveOrder(ID, orderMain, orderDetailArrayList);
        manager.closeDB();
    }

    public static void saveSEOrder(String ID, String orderDate, String deliveryDate, String customerSySID, String wareHouseName,
                                   Map<String, String> mapWareHouse, String userSysID, String saveTime,
                                   String isUpLoad, String message, String deptID, Map<String, String> mapDepartment, String businessType,
                                   String FAreaPS, String FSaleType, Map<String, String> map, String allMoney, String FUpdateType,
                                   String FDelType, String printNum, String fyFSource,
                                   List<AddGoodsMessage> goodsInfos, List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos) {


        OrderMain orderMain = new OrderMain();

        orderMain.setID(ID);   //S+日期+业务编号+流水号
        orderMain.setOrderDate(orderDate);//单据业务日期
        orderMain.setDeliveryDate(deliveryDate);   //交货日期
        orderMain.setCustomerSySID(customerSySID);  //客户id即cusotmer里的sysid
        orderMain.setWareHouseName(wareHouseName);    //仓库名称

        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
            if (stringStringEntry.getValue().equals(wareHouseName)) {
                orderMain.setWareHouseSysID(stringStringEntry.getKey().toString()); //仓库id
            }
        }

        orderMain.setUserSysID(userSysID);   //业务员id kisid
        orderMain.setSaveTime(saveTime);  //单据保存日期
        orderMain.setIsUpLoad(isUpLoad);//是否上传
        orderMain.setMessage(message);//备注

        for (Map.Entry<String, String> stringStringEntry : mapDepartment.entrySet()) {
            if (stringStringEntry.getValue().equals(deptID)) {
                orderMain.setDeptID(stringStringEntry.getKey().toString()); //部门id
            }
        }

        orderMain.setBusinessType(businessType);//订单类型

        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {

            if (stringStringEntry.getValue().equals(FAreaPS)) {
                orderMain.setFAreaPS(stringStringEntry.getKey().toString());    //销售范围
            }

            if (stringStringEntry.getValue().equals(FSaleType)) {
                orderMain.setFSaleType(stringStringEntry.getKey().toString());  //销售方式
            }
        }


        orderMain.setAllMoney(allMoney);    //总金额
        orderMain.setFUpdateType(FUpdateType);  //不允许修改单据控制方式
        orderMain.setFDelType(FDelType);    //允许删除单据控制方式
        orderMain.setPrintNum(printNum);    //打印次数
        orderMain.setFyFSource(fyFSource);


        /*********************************************************************************/

        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            KLog.e("====$$$$$===" + goodsInfo.toString());
        }

        if (goodsInfos != null && goodsInfos.size() != 0) {

            for (AddGoodsMessage goodsInfo : goodsInfos) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setID(ID); //S+日期+业务编号+流水号
                orderDetail.setGoodsSysID(goodsInfo.getGoodsSysID());//商品id  goods 里sysid
                orderDetail.setStandard(goodsInfo.getGoodsUom());//规格

                orderDetail.setOrderType(goodsInfo.getGoodsType());//标识
                orderDetail.setOrderMoney(goodsInfo.getGoodsSum());//总价
                orderDetail.setOrderPrice(goodsInfo.getGoodsPrice());//价格
                orderDetail.setUnitID(goodsInfo.getUnitID());//单位ID

                orderDetail.setOrderFree("");//方案标识
                orderDetail.setOrderbyGoodID("");//id  主键
                orderDetail.setNode("");//备注
                orderDetail.setListGoodsType("");
                orderDetail.setWareHouseSysId(goodsInfo.wareHouseSysId);


                for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                    if (stringStringEntry.getValue().equals(wareHouseName)) {
                        orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());//仓库id
                    }
                }
                orderDetail.setBusinessType(businessType);//订单类型
                KLog.e("====$$$$$===" + orderDetail.toString());
                orderDetailArrayList.add(orderDetail);
            }
        }


        if (goodsPromotionInfos != null && goodsPromotionInfos.size() != 0) {


            ArrayList<ArrayList<AddGoodsPromotion>> aaa1 = new ArrayList<>();
            for (HashMap<String, AddGoodsPromotion> goodsPromotionInfo : goodsPromotionInfos) {
                ArrayList<AddGoodsPromotion> aaa2 = new ArrayList<>();
                for (Map.Entry<String, AddGoodsPromotion> stringAddGoodsPromotionEntry : goodsPromotionInfo.entrySet()) {
                    aaa2.add(stringAddGoodsPromotionEntry.getValue());
                }
                aaa1.add(aaa2);
            }


            for (ArrayList<AddGoodsPromotion> addGoodsPromotions : aaa1) {

                for (int i = 0; i < addGoodsPromotions.size() + 1; i++) {

                    if (i != 0 && i == addGoodsPromotions.size()) {

                        OrderDetail orderDetail = new OrderDetail();
                        orderDetail.setID(ID);
                        orderDetail.setGoodsSysID(addGoodsPromotions.get(i - 1).getPromotionID());
                        orderDetail.setStandard(addGoodsPromotions.get(i - 1).getGoodsUnit());
                        orderDetail.setOrderNum(addGoodsPromotions.get(i - 1).getPromotionCount());
                        orderDetail.setUnitID(addGoodsPromotions.get(i - 1).getPromotionUnitID());
                        orderDetail.setOrderPrice("0");
                        orderDetail.setOrderMoney("0");
                        orderDetail.setOrderType("2");
                        orderDetail.setOrderFree(addGoodsPromotions.get(i - 1).getfName());
                        orderDetail.setOrderbyGoodID("");
                        orderDetail.setNode("");
                        orderDetail.setWareHouseSysId("");
                        orderDetail.setBusinessType(businessType);
                        orderDetail.setListGoodsType(String.valueOf(addGoodsPromotions.get(i - 1).getListGoodsType()));

                        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                            if (stringStringEntry.getValue().equals(wareHouseName)) {
                                orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());
                            }
                        }


                        orderDetailArrayList.add(orderDetail);

                    } else {

                        OrderDetail orderDetail = new OrderDetail();

                        orderDetail.setID(ID);
                        orderDetail.setGoodsSysID(addGoodsPromotions.get(i).getGoodsSysID());
                        orderDetail.setStandard(addGoodsPromotions.get(i).getGoodsUnit());
                        orderDetail.setOrderNum(addGoodsPromotions.get(i).getGoodsNumber());
                        orderDetail.setOrderPrice(addGoodsPromotions.get(i).getGoodsPrice());
                        orderDetail.setOrderMoney(addGoodsPromotions.get(i).getGoodsSumPrice());
                        orderDetail.setOrderType(addGoodsPromotions.get(i).getGoodsType());
                        orderDetail.setOrderFree(addGoodsPromotions.get(i).getfName());
                        orderDetail.setUnitID(addGoodsPromotions.get(i).getGoodsUnitID());
                        orderDetail.setOrderbyGoodID("");
                        orderDetail.setNode("");
                        orderDetail.setWareHouseSysId("");
                        orderDetail.setBusinessType(businessType);
                        orderDetail.setListGoodsType(String.valueOf(addGoodsPromotions.get(i).getListGoodsType()));

                        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                            if (stringStringEntry.getValue().equals(wareHouseName)) {
                                orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());
                            }
                        }

                        orderDetailArrayList.add(orderDetail);

                    }

                }
            }
        }

        DBBusiness manager = new DBBusiness(getContext());
        manager.saveOrder(ID, orderMain, orderDetailArrayList);
        manager.closeDB();
    }


    public static void ArrangeOrder(PrintOrderMain printOrderMain, List<AddGoodsMessage> goodsInfos, List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos) {

        List<AddGoodsPromotion> list1 = new ArrayList<>();

        for (Goods orderGood : printOrderMain.orderGoods) {

            KLog.e("======11111111111====" + orderGood.toString());

            if (TextUtils.isEmpty(orderGood.fName)) {

                AddGoodsMessage addGoodsMessage = new AddGoodsMessage();
                addGoodsMessage.setGoodsSysID(orderGood.goodsSysID);
                addGoodsMessage.setGoodsName(orderGood.goodsName);
                addGoodsMessage.setGoodsNum(orderGood.orderNum);
                addGoodsMessage.setGoodsPrice(orderGood.orderPrice);
                addGoodsMessage.setGoodsSum(orderGood.orderCounter);
                addGoodsMessage.setGoodsUom(orderGood.unit);
                addGoodsMessage.setGoodsType(orderGood.goodsType);
                addGoodsMessage.setUnitID(orderGood.unitID);

                KLog.e("=====222222222222222222=====" + addGoodsMessage.toString());

                goodsInfos.add(addGoodsMessage);

            } else {

                AddGoodsPromotion addGoodsPromotion = new AddGoodsPromotion();
                addGoodsPromotion.setGoodsSysID(orderGood.goodsSysID);
                addGoodsPromotion.setGoodsName(orderGood.goodsName);
                addGoodsPromotion.setGoodsNumber(orderGood.orderNum);
                addGoodsPromotion.setGoodsPrice(orderGood.orderPrice);
                addGoodsPromotion.setGoodsSumPrice(String.valueOf(CalculationUtils.muldd(Double.parseDouble(orderGood.orderPrice), Double.parseDouble(orderGood.orderNum))));
                addGoodsPromotion.setGoodsUnit(orderGood.unit);
                addGoodsPromotion.setGoodsUnitID(orderGood.unitID);
                addGoodsPromotion.setGoodsType(orderGood.goodsType);
                addGoodsPromotion.setfName(orderGood.fName);
                addGoodsPromotion.setListGoodsType(orderGood.listGoodsType);

                for (Goods presentGood : printOrderMain.presentGoods) {
                    if (orderGood.fName.equals(presentGood.fName) && orderGood.listGoodsType.equals(presentGood.listGoodsType)) {
                        addGoodsPromotion.setPromotionID(presentGood.goodsID);
                        addGoodsPromotion.setPromotionName(presentGood.goodsName);
                        addGoodsPromotion.setPromotionCount(presentGood.orderNum);
                        addGoodsPromotion.setPromotionUnit(presentGood.unit);
                    }
                }


                list1.add(addGoodsPromotion);
            }

        }


        for (Goods presentGood : printOrderMain.presentGoods) {

            if (TextUtils.isEmpty(presentGood.fName)) {
                AddGoodsMessage addGoodsMessage = new AddGoodsMessage();
                addGoodsMessage.setGoodsSysID(presentGood.goodsSysID);
                addGoodsMessage.setGoodsName(presentGood.goodsName);
                addGoodsMessage.setGoodsNum(presentGood.orderNum);
                addGoodsMessage.setGoodsPrice("");
                addGoodsMessage.setGoodsSum("");
                addGoodsMessage.setGoodsUom(presentGood.unit);
                addGoodsMessage.setGoodsType(presentGood.goodsType);
                addGoodsMessage.setUnitID(presentGood.unitID);

                KLog.e("=====222222222222222222=====" + addGoodsMessage.toString());

                goodsInfos.add(addGoodsMessage);
            }
        }

        HashMap<String, List<AddGoodsPromotion>> rt_sepert = new HashMap<>();

        for (AddGoodsPromotion item : list1) {
            if (rt_sepert.containsKey(item.getListGoodsType())) {
                rt_sepert.get(item.getListGoodsType()).add(item);
            } else {
                ArrayList<AddGoodsPromotion> typelist = new ArrayList<>();
                typelist.add(item);
                rt_sepert.put(item.getListGoodsType(), typelist);

            }
        }


        int kk = 0;
        for (Map.Entry<String, List<AddGoodsPromotion>> stringListEntry : rt_sepert.entrySet()) {
            kk++;
            int ll = 0;
            HashMap<String, AddGoodsPromotion> hashMap = new HashMap<>();
            for (AddGoodsPromotion addGoodsPromotion : stringListEntry.getValue()) {
                ll++;
                hashMap.put("" + kk + ll, addGoodsPromotion);
            }
            goodsPromotionInfos.add(hashMap);
        }

    }


    /**
     * 采购整理
     *
     * @param seOrderMain
     * @param goodsInfos
     */
    public static void ArrangeSEOrder(SEOrderMain seOrderMain, List<AddGoodsMessage> goodsInfos) {

        for (Goods orderGood : seOrderMain.getOrderGoods()) {
            AddGoodsMessage addGoodsMessage = new AddGoodsMessage();
            addGoodsMessage.setGoodsSysID(orderGood.goodsSysID);
            addGoodsMessage.setGoodsName(orderGood.goodsName);
            addGoodsMessage.setGoodsNum(orderGood.orderNum);
            addGoodsMessage.setGoodsPrice(orderGood.orderPrice);
            addGoodsMessage.setGoodsSum(orderGood.orderCounter);
            addGoodsMessage.setGoodsUom(orderGood.unit);
            addGoodsMessage.setGoodsType(orderGood.goodsType);
            addGoodsMessage.setUnitID(orderGood.unitID);
            addGoodsMessage.setOutNum(orderGood.outNum);
            addGoodsMessage.setNotNum(orderGood.notNum);

            goodsInfos.add(addGoodsMessage);
        }
    }


    /**
     * 销售整理
     *
     * @param SEOrderMain
     * @param goodsInfos
     * @param goodsPromotionInfos
     */
    public static void ArrangeSEOrder(SEOrderMain SEOrderMain, List<AddGoodsMessage> goodsInfos, List<HashMap<String, AddGoodsPromotion>> goodsPromotionInfos) {

        List<AddGoodsPromotion> list1 = new ArrayList<>();

        for (Goods orderGood : SEOrderMain.getOrderGoods()) {


            if (TextUtils.isEmpty(orderGood.fName)) {
                AddGoodsMessage addGoodsMessage = new AddGoodsMessage();
                addGoodsMessage.setGoodsSysID(orderGood.goodsSysID);
                addGoodsMessage.setGoodsName(orderGood.goodsName);
                addGoodsMessage.setGoodsNum(orderGood.orderNum);
                addGoodsMessage.setGoodsPrice(orderGood.orderPrice);
                addGoodsMessage.setGoodsSum(orderGood.orderCounter);
                addGoodsMessage.setGoodsUom(orderGood.unit);
                addGoodsMessage.setGoodsType(orderGood.goodsType);
                addGoodsMessage.setUnitID(orderGood.unitID);
                addGoodsMessage.setOutNum(orderGood.outNum);
                addGoodsMessage.setNotNum(orderGood.notNum);

                goodsInfos.add(addGoodsMessage);

            } else {
                KLog.e("===@@@@@@@==111111111111==" + orderGood.toString());

                AddGoodsPromotion addGoodsPromotion = new AddGoodsPromotion();
                addGoodsPromotion.setGoodsSysID(orderGood.goodsSysID);
                addGoodsPromotion.setGoodsName(orderGood.goodsName);
                addGoodsPromotion.setGoodsNumber(orderGood.orderNum);
                addGoodsPromotion.setGoodsPrice(orderGood.orderPrice);
                addGoodsPromotion.setGoodsSumPrice(String.valueOf(CalculationUtils.muldd(Double.parseDouble(orderGood.orderPrice), Double.parseDouble(orderGood.orderNum))));
                addGoodsPromotion.setGoodsUnit(orderGood.unit);
                addGoodsPromotion.setGoodsUnitID(orderGood.unitID);
                addGoodsPromotion.setGoodsType(orderGood.goodsType);
                addGoodsPromotion.setfName(orderGood.fName);
                addGoodsPromotion.setListGoodsType(orderGood.listGoodsType);
                addGoodsPromotion.setOutNum(orderGood.outNum);
                addGoodsPromotion.setNotNum(orderGood.notNum);

                for (Goods presentGood : SEOrderMain.getPresentGoods()) {
                    if (orderGood.fName.equals(presentGood.fName) && orderGood.listGoodsType.equals(presentGood.listGoodsType)) {
                        addGoodsPromotion.setPromotionID(presentGood.goodsSysID);
                        addGoodsPromotion.setPromotionName(presentGood.goodsName);
                        addGoodsPromotion.setPromotionCount(presentGood.orderNum);
                        addGoodsPromotion.setPromotionUnit(presentGood.unit);
                        addGoodsPromotion.setPromotionOutNum(presentGood.outNum);
                        addGoodsPromotion.setPromotionNotNum(presentGood.notNum);
                    }
                }

                KLog.e("===@@@@@@@==22222222222==" + addGoodsPromotion.toString());


                list1.add(addGoodsPromotion);
            }

        }


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            KLog.e("===#############=====" + goodsInfo.toString());
        }

        for (AddGoodsPromotion addGoodsPromotion : list1) {
            KLog.e("===@@@@@@@====" + addGoodsPromotion.toString());
        }

        HashMap<String, List<AddGoodsPromotion>> rt_sepert = new HashMap<>();

        for (AddGoodsPromotion item : list1) {
            if (rt_sepert.containsKey(item.getListGoodsType())) {
                rt_sepert.get(item.getListGoodsType()).add(item);
            } else {
                ArrayList<AddGoodsPromotion> typelist = new ArrayList<>();
                typelist.add(item);
                rt_sepert.put(item.getListGoodsType(), typelist);

            }
        }


        int kk = 0;
        for (Map.Entry<String, List<AddGoodsPromotion>> stringListEntry : rt_sepert.entrySet()) {
            kk++;
            int ll = 0;
            HashMap<String, AddGoodsPromotion> hashMap = new HashMap<>();
            for (AddGoodsPromotion addGoodsPromotion : stringListEntry.getValue()) {
                ll++;
                hashMap.put("" + kk + ll, addGoodsPromotion);
            }
            goodsPromotionInfos.add(hashMap);
        }

    }


    private static String uploadMainPro = "#userId#<6>#X#<1>#id#<4>#source#<4>#orderDate#<4>#deliveryDate#<4>#customerSysId#<4>#userSysId#<4>#saleType#<4>#areaPS#<4>#deptId#<4>#message#<2>";

    private static String uploadSubPro = "#goodsSysID#<4>#unitID#<4>#orderPrice#<4>#orderNum#<4>#orderCounter#<4>#wareHouseSysId#<4>#fName#<4>#listGoodsType#<4>#goodsType#<3>";


    public static String getUploadProStr(Context context, PrintOrderMain printOrderMain, String businessType) {


        String id = printOrderMain.id;
        String orderDate = printOrderMain.orderDate;
        String deliveryDate = printOrderMain.deliveryDate;
        String allMoney = printOrderMain.allMoney;
        String saveTime = printOrderMain.saveTime;
        String isUpLoad = printOrderMain.isUpLoad;
        String message = printOrderMain.message;
        String customerSysId = printOrderMain.customerSysId;
        String customerName = printOrderMain.customerName;
        String customerAddress = printOrderMain.customerAddress;
        String customerTel = printOrderMain.customerTel;
        String userSysId = printOrderMain.userSysId;
        String userId = printOrderMain.userId;
        String deptId = printOrderMain.deptId;
        String deptName = printOrderMain.deptName;
        String wareHouseSysId = printOrderMain.wareHouseSysId;
        String wareHouseName = printOrderMain.wareHouseName;
        String saleType = printOrderMain.saleType;
        String areaPS = printOrderMain.areaPS;
        String updateType = printOrderMain.updateType;
        String printNum = printOrderMain.printNum;
        String delType = printOrderMain.delType;
        String source = printOrderMain.source;


        List<Goods> orderGoods = printOrderMain.orderGoods;
        List<Goods> presentGoods = printOrderMain.presentGoods;

        KLog.e("==mainStr====111====" + businessType);

        if ("0".equals(businessType)) {//退货单
            businessType = "T";
        } else if ("1".equals(businessType)) {//订货单
            businessType = "X";
        } else if ("2".equals(businessType)) {//出库单
            businessType = "D";
        } else if ("3".equals(businessType)) {//退货通知
            businessType = "Z";
        }

        String mainStr = null;
        String goodsStr = null;

        KLog.e("==mainStr=====222===" + businessType);

        mainStr = uploadMainPro.replace("#userId#", userId);
        mainStr = mainStr.replace("#X#", businessType);

        KLog.e("==mainStr====333====" + mainStr);

        mainStr = mainStr.replace("#id#", id);
        mainStr = mainStr.replace("#source#", source);
        mainStr = mainStr.replace("#orderDate#", orderDate);
        mainStr = mainStr.replace("#deliveryDate#", deliveryDate);
        mainStr = mainStr.replace("#customerSysId#", customerSysId);
        mainStr = mainStr.replace("#userSysId#", userSysId);
        mainStr = mainStr.replace("#saleType#", saleType);
        mainStr = mainStr.replace("#areaPS#", areaPS);
        mainStr = mainStr.replace("#deptId#", deptId);
        mainStr = mainStr.replace("#message#", message);


        String temporaryStr_1 = "";
        for (Goods orderGood : orderGoods) {

            goodsStr = uploadSubPro.replace("#goodsSysID#", orderGood.goodsSysID);
            goodsStr = goodsStr.replace("#unitID#", orderGood.unitID);
            goodsStr = goodsStr.replace("#orderPrice#", orderGood.orderPrice);
            goodsStr = goodsStr.replace("#orderNum#", orderGood.orderNum);
            goodsStr = goodsStr.replace("#orderCounter#", orderGood.orderCounter);

            KLog.e("======1111111111111111==" + orderGood.wareHouseSysId);

            if (TextUtils.isEmpty(orderGood.wareHouseSysId)) {
                goodsStr = goodsStr.replace("#wareHouseSysId#", "");
            } else {
                goodsStr = goodsStr.replace("#wareHouseSysId#", orderGood.wareHouseSysId);
            }

            goodsStr = goodsStr.replace("#fName#", orderGood.fName);
            goodsStr = goodsStr.replace("#listGoodsType#", orderGood.listGoodsType);
            goodsStr = goodsStr.replace("#goodsType#", orderGood.goodsType);
            temporaryStr_1 += goodsStr;
        }
        mainStr = mainStr + temporaryStr_1;

        String temporaryStr_2 = "";

        for (Goods presentGood : presentGoods) {
            goodsStr = uploadSubPro.replace("#goodsSysID#", presentGood.goodsSysID);
            goodsStr = goodsStr.replace("#unitID#", presentGood.unitID);
            goodsStr = goodsStr.replace("#orderPrice#", presentGood.orderPrice);
            goodsStr = goodsStr.replace("#orderNum#", presentGood.orderNum);
            goodsStr = goodsStr.replace("#orderCounter#", presentGood.orderCounter);


            if (TextUtils.isEmpty(presentGood.wareHouseSysId)) {
                goodsStr = goodsStr.replace("#wareHouseSysId#", "");
            } else {
                goodsStr = goodsStr.replace("#wareHouseSysId#", presentGood.wareHouseSysId);
            }

            goodsStr = goodsStr.replace("#fName#", presentGood.fName);
            goodsStr = goodsStr.replace("#listGoodsType#", presentGood.listGoodsType);
            goodsStr = goodsStr.replace("#goodsType#", presentGood.goodsType);
            temporaryStr_2 += goodsStr;
        }
        mainStr = mainStr + temporaryStr_2;

        return mainStr.substring(0, mainStr.length() - 3);
    }

    /**
     * 保存采购
     * @param ID
     * @param strFyFFetchDayV
     * @param strFyFPlanFetchDay1
     * @param strFyFPlanFetchDay2
     * @param strFyFPOStyle
     * @param strFyFAreaPS
     * @param strFyFBizType
     * @param strFyFSource
     * @param strFyFSupplier
     * @param strFyFRequester
     * @param mapDepartment
     * @param strFyFDept
     * @param strFyFFManager
     * @param strFyFSManager
     * @param mapWareHouse
     * @param strFyFDCStock
     * @param strFyFDigest
     * @param strBusinessType
     * @param strIsUpLoad
     * @param strFUpdateType
     * @param strFDelType
     * @param strPrintNum
     * @param allMoney
     * @param goodsInfos
     * @param map
     */
    public static void savePurchaseOrder(String ID, String strFyFFetchDayV, String strFyFPlanFetchDay1, String strFyFPlanFetchDay2, String strFyFPOStyle, String strFyFAreaPS, String strFyFBizType, String strFyFSource, String strFyFSupplier, String strFyFRequester, Map<String, String> mapDepartment, String strFyFDept, String strFyFFManager, String strFyFSManager, Map<String, String> mapWareHouse, String strFyFDCStock, String strFyFDigest, String strBusinessType, String strIsUpLoad, String strFUpdateType, String strFDelType, String strPrintNum, String allMoney, List<AddGoodsMessage> goodsInfos, Map<String, String> map) {
        PurchaseOrderMain main = new PurchaseOrderMain();


        main.ID = ID;
        main.strFyFFetchDayV = strFyFFetchDayV;
        main.strFyFPlanFetchDay1 = strFyFPlanFetchDay1;
        main.strFyFPlanFetchDay2 = strFyFPlanFetchDay2;

        for (Map.Entry<String, String> stringStringEntry : map.entrySet()) {
            if (stringStringEntry.getValue().equals(strFyFPOStyle)) {
                main.strFyFPOStyle = stringStringEntry.getKey().toString();
            } else {
                main.strFyFPOStyle = "";
            }
            if (stringStringEntry.getValue().equals(strFyFAreaPS)) {
                main.strFyFAreaPS = stringStringEntry.getKey().toString();
            } else {
                main.strFyFAreaPS = "";
            }
            if (stringStringEntry.getValue().equals(strFyFBizType)) {
                main.strFyFBizType = stringStringEntry.getKey().toString();
            } else {
                main.strFyFBizType = "";
            }
        }


        if (TextUtils.isEmpty(strFyFSource)) {
            main.strFyFSource = "";
        } else {
            main.strFyFSource = strFyFSource;
        }

        main.strFyFRequester = strFyFRequester;

        for (Map.Entry<String, String> stringStringEntry : mapDepartment.entrySet()) {
            if (stringStringEntry.getValue().equals(strFyFDept)) {
                main.strFyFDept = stringStringEntry.getKey().toString();
            } else {
                main.strFyFDept = "";
            }
        }

        if (TextUtils.isEmpty(strFyFFManager)) {
            main.strFyFFManager = "";
        } else {
            main.strFyFFManager = strFyFFManager;
        }

        if (TextUtils.isEmpty(strFyFSManager)) {
            main.strFyFSManager = "";
        } else {
            main.strFyFSManager = strFyFSManager;
        }


        main.strFyFDCStock = strFyFDCStock;
        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
            if (stringStringEntry.getValue().equals(strFyFDept)) {
                main.strFyFDCStock = stringStringEntry.getKey().toString();
            }
        }


        main.strFyFDigest = strFyFDigest;
        main.strBusinessType = strBusinessType;
        main.strAllMoney = allMoney;
        main.strIsUpLoad = strIsUpLoad;
        main.strFUpdateType = strFUpdateType;
        main.strFDelType = strFDelType;
        main.strPrintNum = strPrintNum;


        /*********************************************************************************/

        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();


        for (AddGoodsMessage goodsInfo : goodsInfos) {
            KLog.e("====$$$$$===" + goodsInfo.toString());
        }

        if (goodsInfos != null && goodsInfos.size() != 0) {

            for (AddGoodsMessage goodsInfo : goodsInfos) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setID(ID); //S+日期+业务编号+流水号
                orderDetail.setGoodsSysID(goodsInfo.getGoodsSysID());//商品id  goods 里sysid
                orderDetail.setStandard(goodsInfo.getGoodsUom());//规格
                orderDetail.setOrderNum(goodsInfo.getGoodsNum());//数量
                orderDetail.setOrderType(goodsInfo.getGoodsType());//标识
                orderDetail.setOrderMoney(goodsInfo.getGoodsSum());//总价
                orderDetail.setOrderPrice(goodsInfo.getGoodsPrice());//价格
                orderDetail.setUnitID(goodsInfo.getUnitID());//单位ID

                orderDetail.setOrderFree("");//方案标识
                orderDetail.setOrderbyGoodID("");//id  主键
                orderDetail.setNode("");//备注
                orderDetail.setListGoodsType("");
                orderDetail.setWareHouseSysId(goodsInfo.wareHouseSysId);

                for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {
                    if (stringStringEntry.getValue().equals(strFyFDept)) {
                        orderDetail.setWareHouseSysId(stringStringEntry.getKey().toString());//仓库id
                    }
                }
                orderDetail.setBusinessType(strBusinessType);//订单类型
                KLog.e("====$$$$$===" + orderDetail.toString());
                orderDetailArrayList.add(orderDetail);
            }
        }

        DBBusiness manager = new DBBusiness(getContext());
        manager.savePurchaseOrder(ID, main, orderDetailArrayList);
        manager.closeDB();
    }

    public static String getUploadPurchaseProStr(Context context, PrintPurchaseOrderMain orderMain, String businessType) {


        return orderMain.toString() + "==============" + orderMain.orderGoods.toString();
    }

    /**
     * 仓库调拨单
     *
     * @param ID
     * @param orderDate
     * @param mapWareHouse
     * @param wareHouseName_1
     * @param wareHouseName_2
     * @param mapRequester
     * @param emp
     * @param FFManager
     * @param FSManager
     * @param userSysID
     * @param saveTime
     * @param isUpLoad
     * @param businessType
     * @param FUpdateType
     * @param FDelType
     * @param printNum
     * @param goodsInfos
     */
    public static void saveStockOrder(String ID, String orderDate, Map<String, String> mapWareHouse, String wareHouseName_1, String wareHouseName_2, Map<String, String> mapRequester, String emp, String FFManager, String FSManager, String userSysID, String saveTime, String isUpLoad, String businessType, String FUpdateType, String FDelType, String printNum, List<AddGoodsMessage> goodsInfos) {


        OrderMain orderMain = new OrderMain();

        orderMain.setID(ID);   //S+日期+业务编号+流水号
        orderMain.setOrderDate(orderDate);//单据业务日期

        orderMain.setDeliveryDate(wareHouseName_2);  //调出

        orderMain.setWareHouseName(wareHouseName_1); //调入

        for (Map.Entry<String, String> stringStringEntry : mapWareHouse.entrySet()) {

            if (stringStringEntry.getValue().equals(wareHouseName_1)) {
                orderMain.setWareHouseSysID(stringStringEntry.getKey().toString()); //调入
            } else {
                orderMain.setWareHouseSysID(""); //调入
            }

            if (stringStringEntry.getValue().equals(wareHouseName_2)) {
                orderMain.setCustomerSySID(stringStringEntry.getKey().toString()); //调出
            } else {
                orderMain.setCustomerSySID(""); //调出
            }
        }

        orderMain.setUserSysID(userSysID);   //业务员id kisid
        orderMain.setSaveTime(saveTime);  //单据保存日期
        orderMain.setIsUpLoad(isUpLoad);//是否上传
        orderMain.setBusinessType(businessType);//订单类型


        orderMain.setMessage(FFManager);//验收
        orderMain.setDeptID(FSManager); //保管
        for (Map.Entry<String, String> stringStringEntry : mapRequester.entrySet()) {

            if (stringStringEntry.getValue().equals(FFManager)) {
                orderMain.setFAreaPS(stringStringEntry.getKey().toString()); //验收
            } else {
                orderMain.setFAreaPS(""); //验收
            }

            if (stringStringEntry.getValue().equals(FSManager)) {
                orderMain.setFSaleType(stringStringEntry.getKey().toString()); //保管
            } else {
                orderMain.setFSaleType("");//保管
            }
        }


        orderMain.setAllMoney("");    //总金额
        orderMain.setFUpdateType(FUpdateType);  //不允许修改单据控制方式
        orderMain.setFDelType(FDelType);    //允许删除单据控制方式
        orderMain.setPrintNum(printNum);    //打印次数
        orderMain.setFyFSource("");


        /*********************************************************************************/

        ArrayList<OrderDetail> orderDetailArrayList = new ArrayList<>();

        if (goodsInfos != null && goodsInfos.size() != 0) {

            for (AddGoodsMessage goodsInfo : goodsInfos) {
                OrderDetail orderDetail = new OrderDetail();
                orderDetail.setID(ID); //S+日期+业务编号+流水号
                orderDetail.setGoodsSysID(goodsInfo.getGoodsSysID());//商品id  goods 里sysid
                orderDetail.setStandard(goodsInfo.getGoodsUom());//规格

                if (TextUtils.isEmpty(goodsInfo.getNotNum()) && TextUtils.isEmpty(goodsInfo.getOutNum())) {
                    orderDetail.setOrderNum(goodsInfo.getGoodsNum());//数量
                } else {
                    orderDetail.setOrderNum(goodsInfo.getNotNum());//数量
                }

                orderDetail.setOrderType(goodsInfo.getGoodsType());//标识
                orderDetail.setOrderMoney(goodsInfo.getGoodsSum());//总价
                orderDetail.setOrderPrice(goodsInfo.getGoodsPrice());//价格
                orderDetail.setUnitID(goodsInfo.getUnitID());//单位ID

                orderDetail.setOrderFree("");//方案标识
                orderDetail.setOrderbyGoodID("");//id  主键
                orderDetail.setNode("");//备注
                orderDetail.setListGoodsType("");

                if (TextUtils.isEmpty(goodsInfo.wareHouseSysId)) {
                    orderDetail.setWareHouseSysId("");
                } else {
                    orderDetail.setWareHouseSysId(goodsInfo.wareHouseSysId);
                }
                orderDetail.setBusinessType(businessType);//订单类型
                KLog.e("====$$$$$===" + orderDetail.toString());
                orderDetailArrayList.add(orderDetail);
            }
        }


        DBBusiness manager = new DBBusiness(getContext());
        manager.saveOrder(ID, orderMain, orderDetailArrayList);
        manager.closeDB();


    }


    public static String getUploadStockProStr(Context context, PrintOrderMain orderMain, String businessType) {


        return orderMain.toString() + "==============" + orderMain.orderGoods.toString();
    }
}
