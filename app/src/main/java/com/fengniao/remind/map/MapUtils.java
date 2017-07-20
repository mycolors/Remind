package com.fengniao.remind.map;


import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.utils.DistanceUtil;

public class MapUtils {

    //计算两坐标之间的距离
    public static double calculateDistance(LatLng latLng1, LatLng latLng2) {
        return DistanceUtil.getDistance(latLng1, latLng2);
    }

}
