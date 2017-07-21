package com.fengniao.remind.ui.present;


import android.text.TextUtils;
import android.widget.Toast;

import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiIndoorResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.map.MapManager;
import com.fengniao.remind.ui.base.BaseActivity;
import com.fengniao.remind.ui.base.BasePresenter;
import com.fengniao.remind.ui.view.ChooseSiteAtI;

import java.util.ArrayList;
import java.util.List;

public class ChooseSiteAtPresent extends BasePresenter<ChooseSiteAtI> {

    private List<PoiInfo> mList;
    private PoiSearch mPoiSearch;
    private GeoCoder mGeoCoder = null;
    private LatLng mLatLng;
    private Location mLocation;

    public ChooseSiteAtPresent(BaseActivity mContext) {
        super(mContext);
    }


    public void init() {
        mList = new ArrayList<>();
        mPoiSearch = PoiSearch.newInstance();
        mGeoCoder = GeoCoder.newInstance();
        getView().getMapManager().setOnMapStatusChangeListener(new MapManager.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus var1) {

            }

            @Override
            public void onMapStatusChange(MapStatus var1) {

            }

            @Override
            public void onMapStatusChangeFinish(MapStatus var1) {
                //获取屏幕中间中间的经纬度
                mLatLng = new LatLng(var1.target.latitude, var1.target.longitude);
                mGeoCoder.reverseGeoCode(new ReverseGeoCodeOption().location(mLatLng));
            }
        });
        mGeoCoder.setOnGetGeoCodeResultListener(new OnGetGeoCoderResultListener() {
            @Override
            public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

            }

            //根据屏幕中间的经纬度获取地址
            @Override
            public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
                if (reverseGeoCodeResult == null ||
                        reverseGeoCodeResult.error != SearchResult.ERRORNO.NO_ERROR) {
                    Toast.makeText(mContext, "未能找到结果", Toast.LENGTH_SHORT).show();
                    return;
                }
                getView().getMapManager().clear();
                getView().getMapManager().setMapStatus(MapStatusUpdateFactory.newLatLng(reverseGeoCodeResult.getLocation()));
                getView().setAddressText(reverseGeoCodeResult.getAddress());
                if (mLocation == null) {
                    mLocation = new Location();
                }
                mLocation.setName(reverseGeoCodeResult.getAddressDetail().countryName);
                mLocation.setAddress(reverseGeoCodeResult.getAddress());
                mLocation.setCity(reverseGeoCodeResult.getAddressDetail().city);
                mLocation.setLatitude(reverseGeoCodeResult.getLocation().latitude);
                mLocation.setLongitude(reverseGeoCodeResult.getLocation().longitude);
            }
        });
    }

    @Override
    public void start() {
        super.start();
        init();
    }


    public void getSearchResult(String key) {
        //如果传入值为空，就清空列表
        if (TextUtils.isEmpty(key)) {
            getView().hideSearchList();
//            getView().updateSearchList(new ArrayList<PoiInfo>());
//            getView().hidePopupWindow();
            return;
        }
        OnGetPoiSearchResultListener poiSearchResultListener = new OnGetPoiSearchResultListener() {
            @Override
            public void onGetPoiResult(PoiResult poiResult) {
                mList.clear();
                if (poiResult.getAllPoi() != null) {
                    mList.addAll(poiResult.getAllPoi());
//                    getView().showPopupWindow();
                    getView().showSearchList();
                }
                getView().updateSearchList(mList);
            }

            @Override
            public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {

            }

            @Override
            public void onGetPoiIndoorResult(PoiIndoorResult poiIndoorResult) {

            }
        };
        mPoiSearch.setOnGetPoiSearchResultListener(poiSearchResultListener);
        String city = "";
        if (getView().getMapManager().getMyLocation() != null) {
            city = getView().getMapManager().getMyLocation().getCity();
        }
        if (TextUtils.isEmpty(city)) {
            city = "北京";
        }
        mPoiSearch.searchInCity(new PoiCitySearchOption().city(city).keyword(key));
    }


    public LatLng getLatLng() {
        return mLatLng;
    }

    public boolean saveLocation() {
        if (mLocation != null) {
            mLocation.setActivate(true);
            return LocalDataSource.getInstance(mContext).saveLocation(mLocation);
        } else {
            return false;
        }
    }

}
