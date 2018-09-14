package com.fengniao.remind.map;


import com.amap.api.maps.AMapUtils;
import com.amap.api.maps.model.LatLng;
import com.autonavi.amap.mapcore.AMapEngineUtils;

public class MapUtils {

    //计算两坐标之间的距离
    public static double calculateDistance(LatLng start, LatLng end) {
        return AMapUtils.calculateLineDistance(start, end);
    }
}
