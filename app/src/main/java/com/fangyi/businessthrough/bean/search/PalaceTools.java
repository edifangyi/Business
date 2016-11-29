package com.fangyi.businessthrough.bean.search;

/**
 * Created by FANGYI on 2016/9/21.
 */

public class PalaceTools {


    public PalaceTools() {
        super();
        // TODO Auto-generated constructor stub
    }
    public static double D(double y1,double y2,double x11,double x22){

        double x1=x11;//定位的位置纬度;
        double x2=x22;//定位的位置经度;
        double a ,b ,R;

        R=6378137;

        x2=x2*Math.PI/180.0;
        y2=y2*Math.PI/180.0;
        a = x2 - y2;
        b =	(x1 - y1 )*Math.PI/180.0;
        double d;

        double sa2,sb2;
        sa2=Math.sin(a/2.0);
        sb2=Math.sin(b/2.0);

        d=2*R*Math.asin
                (Math.sqrt(sa2*sa2+
                        Math.cos(x2)*Math.cos(y2)*sb2*sb2));
        return d;
    }
}