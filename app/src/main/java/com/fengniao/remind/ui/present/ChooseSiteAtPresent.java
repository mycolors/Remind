package com.fengniao.remind.ui.present;


import android.text.TextUtils;
import android.view.MotionEvent;

import com.amap.api.maps.AMap;
import com.amap.api.maps.model.CameraPosition;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.map.GDMapManger;
import com.fengniao.remind.ui.activity.ChooseSiteActivity;
import com.fengniao.remind.ui.base.BaseActivity;
import com.fengniao.remind.ui.base.BasePresenter;
import com.fengniao.remind.ui.view.ChooseSiteAtI;
import com.fengniao.remind.util.PzLogUtil;

import java.util.ArrayList;
import java.util.List;

public class ChooseSiteAtPresent extends BasePresenter<ChooseSiteAtI> {

    private static final String TAG = ChooseSiteActivity.class.getSimpleName();

    private List<PoiItem> mList;


    private GDMapManger gdMapManger;

    private RegeocodeResult mRegeocodeResult;

    //地理编码
    private GeocodeSearch geocoderSearch;

    public ChooseSiteAtPresent(BaseActivity mContext) {
        super(mContext);
    }


    public void init() {
        mList = new ArrayList<>();
        gdMapManger = new GDMapManger(mContext, getView().getMapView());
        gdMapManger.showMyLocation(true);
        gdMapManger.setZoomLevel(18f);

        geocoderSearch = new GeocodeSearch(mContext);
        geocoderSearch.setOnGeocodeSearchListener(new GeocodeSearch.OnGeocodeSearchListener() {
            @Override
            public void onRegeocodeSearched(RegeocodeResult regeocodeResult, int i) {
                PzLogUtil.i(TAG, "center address is " + regeocodeResult.getRegeocodeAddress().getFormatAddress());
                mRegeocodeResult = regeocodeResult;
                getView().updateAddress(regeocodeResult.getRegeocodeAddress().getFormatAddress());
            }

            @Override
            public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

            }
        });

        final GDMapManger gdMapManger = getMapManger();
        if (gdMapManger != null) {
            gdMapManger.setOnMapTouchListener(new AMap.OnMapTouchListener() {
                @Override
                public void onTouch(MotionEvent motionEvent) {
                    if (motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
                        gdMapManger.enableFollow(false);
                    }
                }
            });
            gdMapManger.setOnCameraChangeListener(new AMap.OnCameraChangeListener() {
                @Override
                public void onCameraChange(CameraPosition cameraPosition) {
                    PzLogUtil.i(TAG, "center lat/lng is " + cameraPosition.target.toString());
                    RegeocodeQuery query = new RegeocodeQuery(new LatLonPoint(cameraPosition.target.latitude,
                            cameraPosition.target.longitude), 1000f, GeocodeSearch.AMAP);
                    geocoderSearch.getFromLocationAsyn(query);
                }

                @Override
                public void onCameraChangeFinish(CameraPosition cameraPosition) {

                }
            });

        }

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
        //根据关键子搜索地图poi
        GDMapManger.searchByKeyword(mContext, key, "", getCity(), new PoiSearch.OnPoiSearchListener() {
            @Override
            public void onPoiSearched(PoiResult poiResult, int i) {
                PzLogUtil.i(TAG, "map search count is " + i);
                mList.clear();
                mList.addAll(poiResult.getPois());
                getView().showSearchList();
                getView().updateSearchList(mList);
            }

            @Override
            public void onPoiItemSearched(PoiItem poiItem, int i) {

            }
        });
    }

    private String getCity() {
        if (mRegeocodeResult == null)
            return "";
        return mRegeocodeResult.getRegeocodeAddress().getCity();
    }


    public boolean saveLocation() {
        if (mRegeocodeResult != null) {
            Location location = new Location();
            location.setName(mRegeocodeResult.getRegeocodeAddress().getDistrict());
            location.setAddress(mRegeocodeResult.getRegeocodeAddress().getFormatAddress());
            location.setCity(mRegeocodeResult.getRegeocodeAddress().getCity());
            location.setLatitude(mRegeocodeResult.getRegeocodeQuery().getPoint().getLatitude());
            location.setLongitude(mRegeocodeResult.getRegeocodeQuery().getPoint().getLongitude());
            location.setPostCode(mRegeocodeResult.getRegeocodeAddress().getCityCode());
            location.setActivate(true);
            return LocalDataSource.getInstance(mContext).saveLocation(location);
        }
        return false;
    }

    public GDMapManger getMapManger() {
        return gdMapManger;
    }


}
