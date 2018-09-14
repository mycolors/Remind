package com.fengniao.remind.ui.view;


import com.amap.api.maps.MapView;
import com.amap.api.services.core.PoiItem;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.map.GDMapManger;

import java.util.List;

public interface ChooseSiteAtI {

    void initPopupWindow();

    void showPopupWindow();

    void hidePopupWindow();

    void updateSearchList(List<PoiItem> list);

    void clearEditText();

    void showSearchList();

    void hideSearchList();

    void showMarker();

    void hideMarker();

    void setAddressText(String addres);

    void closeVirtualKeyboard();

    MapView getMapView();

    void updateAddress(String address);

}
