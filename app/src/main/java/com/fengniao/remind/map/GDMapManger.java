package com.fengniao.remind.map;


import android.content.Context;
import android.view.MotionEvent;

import com.amap.api.location.AMapLocation;
import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationClientOption;
import com.amap.api.location.AMapLocationListener;
import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdate;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.AMapGestureListener;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

//高德MapManager
public class GDMapManger extends MapManager {

    private MapView mMapView;

    private AMap mAMap;

    private Context mContext;

    //声明mlocationClient对象
    private AMapLocationClient mlocationClient;
    //声明mLocationOption对象
    private AMapLocationClientOption mLocationOption;



    public GDMapManger(Context context) {
        mContext = context;
    }


    public GDMapManger(Context context, MapView mapView) {
        mContext = context;
        mMapView = mapView;
        init();
    }


    @Override
    void init() {
        if (mMapView != null) {
            mAMap = mMapView.getMap();
            mAMap.setTrafficEnabled(true);// 显示实时交通状况
            //地图模式可选类型：MAP_TYPE_NORMAL,MAP_TYPE_SATELLITE,MAP_TYPE_NIGHT
            mAMap.setMapType(AMap.MAP_TYPE_NORMAL);
        }
    }

    @Override
    public void enableLocation(boolean enable) {

        if (mlocationClient == null)
            mlocationClient = new AMapLocationClient(mContext);
        if (mLocationOption == null) {
            mLocationOption = new AMapLocationClientOption();
            //设置定位模式为高精度模式，Battery_Saving为低功耗模式，Device_Sensors是仅设备模式
            mLocationOption.setLocationMode(AMapLocationClientOption.AMapLocationMode.Hight_Accuracy);
            //设置定位间隔,单位毫秒,默认为2000ms
            mLocationOption.setInterval(2000);
            //设置定位参数
            mlocationClient.setLocationOption(mLocationOption);
            // 此方法为每隔固定时间会发起一次定位请求，为了减少电量消耗或网络流量消耗，
            // 注意设置合适的定位时间的间隔（最小间隔支持为1000ms），并且在合适时间调用stopLocation()方法来取消定位请求
            // 在定位结束后，在合适的生命周期调用onDestroy()方法
            // 在单次定位情况下，定位无论成功与否，都无需调用stopLocation()方法移除请求，定位sdk内部会移除
        }

        if (enable) {
            //启动定位
            mlocationClient.startLocation();
        } else {
            mlocationClient.onDestroy();
            mlocationClient = null;
            mLocationOption = null;
        }


    }

    //设置位置监听器，用户获取当前位置，只有开启定位是才有效果
    public void setLocationListener(AMapLocationListener listener) {
        if (mlocationClient == null) return;
        mlocationClient.setLocationListener(listener);
    }

    /**
     * 设置定位样式
     *
     * @param locationType myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_SHOW);//只定位一次。
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATE) ;//定位一次，且将视角移动到地图中心点。
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW) ;//连续定位、且将视角移动到地图中心点，定位蓝点跟随设备移动。（1秒1次定位）
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE);//连续定位、且将视角移动到地图中心点，地图依照设备方向旋转，定位点会跟随设备移动。（1秒1次定位）
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）默认执行此种模式。
     *                     //以下三种模式从5.1.0版本开始提供
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，定位点依照设备方向旋转，并且蓝点会跟随设备移动。
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_FOLLOW_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，并且蓝点会跟随设备移动。
     *                     myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_MAP_ROTATE_NO_CENTER);//连续定位、蓝点不会移动到地图中心点，地图依照设备方向旋转，并且蓝点会跟随设备移动。
     */
    public void setLocationType(int locationType) {
        if (mAMap == null) return;
        MyLocationStyle myLocationStyle = mAMap.getMyLocationStyle();
        if (myLocationStyle == null) return;
        myLocationStyle.myLocationType(locationType);
        mAMap.setMyLocationStyle(myLocationStyle);

    }

    @Override
    public void enableFollow(boolean enable) {
        if (enable) {
            setLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);
        } else {
            setLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE_NO_CENTER);
        }
    }

    @Override
    public void showMyLocation(boolean enable) {
        MyLocationStyle myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类
        myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        myLocationStyle.showMyLocation(enable);  //是否显示定位蓝点
        if (mAMap != null) {
            mAMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
            mAMap.getUiSettings().setMyLocationButtonEnabled(true); //设置默认定位按钮是否显示，非必需设置。
            mAMap.setMyLocationEnabled(enable);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。
        }
    }

    //设置缩放级别
    public void setZoomLevel(Float level) {
        if (mAMap == null) return;
        if (level == null) level = 18f;
        CameraUpdate update = CameraUpdateFactory.zoomTo(level);
        animateCamera(update);
    }

    public void animateCamera(LatLng latLng) {
        if (mAMap == null) return;
        if (latLng == null) return;
        CameraUpdate update = CameraUpdateFactory.newCameraPosition(new CameraPosition(
                latLng, 18, 0, 30));
        mAMap.animateCamera(update);
    }


    public void animateCamera(CameraUpdate update) {
        if (mAMap == null) return;
        if (update == null) return;
        mAMap.animateCamera(update);
    }


    public void setOnCameraChangeListener(AMap.OnCameraChangeListener listener) {
        if (mAMap == null) return;
        mAMap.setOnCameraChangeListener(listener);
    }



    /**
     * 通过关键字搜索
     *
     * @param keyword 关键字
     * @param type    poi搜索类型
     * @param area    poi搜索区域（空字符串代表全国）
     */
    public static void searchByKeyword(Context context, String keyword, String type, String area, PoiSearch.OnPoiSearchListener listener) {
        int currentPage = 0;
        PoiSearch.Query query = new PoiSearch.Query(keyword, type, area);
        query.setPageSize(10); //设置每页返回多少条poi item
        query.setPageNum(currentPage);// 设置查第一页

        PoiSearch poiSearch = new PoiSearch(context, query);
        poiSearch.setOnPoiSearchListener(listener);
        poiSearch.searchPOIAsyn();
    }


    /**
     * 给地图设置touch时间
     *
     * @param listener
     */
    public void setOnMapTouchListener(AMap.OnMapTouchListener listener) {
        if (mAMap != null) {
            mAMap.setOnMapTouchListener(listener);
        }
    }


    public void setOnMyLocationChangeListener(AMap.OnMyLocationChangeListener listener) {
        if (mAMap != null) {
            mAMap.setOnMyLocationChangeListener(listener);
        }
    }


    public void onResume() {
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        if (mMapView != null)
            mMapView.onResume();
    }

    public void onPause() {
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        if (mMapView != null)
            mMapView.onPause();
    }

    public void onDestroy() {
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        if (mMapView != null)
            mMapView.onDestroy();
        if (mlocationClient != null)
            mlocationClient.onDestroy();
    }


}
