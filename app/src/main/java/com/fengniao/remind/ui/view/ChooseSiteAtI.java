package com.fengniao.remind.ui.view;


import com.baidu.mapapi.search.core.PoiInfo;
import com.fengniao.remind.map.MapManager;

import java.util.List;

public interface ChooseSiteAtI {

    void initPopupWindow();

    void showPopupWindow();

    void hidePopupWindow();

    void updateSearchList(List<PoiInfo> list);

    void clearEditText();

    MapManager getMapManager();

    void showSearchList();

    void hideSearchList();

    void showMarker();

    void hideMarker();

    void setAddressText(String addres);

    void closeVirtualKeyboard();


}
