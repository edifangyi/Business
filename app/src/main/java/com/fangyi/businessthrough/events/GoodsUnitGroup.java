package com.fangyi.businessthrough.events;

/**
 * Created by FANGYI on 2016/9/28.
 */

public class GoodsUnitGroup {
    private String Uom;
    private String Conversionb;

    public String getUom() {
        return Uom;
    }

    public void setUom(String uom) {
        Uom = uom;
    }

    public String getConversionb() {
        return Conversionb;
    }

    public void setConversionb(String conversionb) {
        Conversionb = conversionb;
    }

    @Override
    public String toString() {
        return "GoodsUnitGroup{" +
                "Uom='" + Uom + '\'' +
                ", Conversionb='" + Conversionb + '\'' +
                '}';
    }
}
