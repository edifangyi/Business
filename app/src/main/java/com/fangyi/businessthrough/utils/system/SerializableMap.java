package com.fangyi.businessthrough.utils.system;


import com.fangyi.businessthrough.events.AddGoodsPromotion;

import java.io.Serializable;
import java.util.HashMap;

/**
 * 序列化map供Bundle传递map使用
 * Created by FANGYI on 2016/10/6.
 */

public class SerializableMap implements Serializable {

    private HashMap<String, String> mapNumber;
    private HashMap<String, AddGoodsPromotion> mapAddGoodsPromotion;

    public HashMap<String, String> getMap() {
        return mapNumber;
    }

    public HashMap<String, AddGoodsPromotion> getMapAddGoodsPromotion() {
        return mapAddGoodsPromotion;
    }


    public void setMap(HashMap<String, String> mapNumber) {
        this.mapNumber = mapNumber;
    }

    public void setMapAddGoodsPromotion(HashMap<String, AddGoodsPromotion> mapAddGoodsPromotion) {
        this.mapAddGoodsPromotion = mapAddGoodsPromotion;
    }
}