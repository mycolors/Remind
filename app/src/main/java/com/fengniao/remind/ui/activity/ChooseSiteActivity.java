package com.fengniao.remind.ui.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.amap.api.maps.MapView;
import com.amap.api.maps.model.LatLng;
import com.amap.api.services.core.PoiItem;
import com.fengniao.remind.R;
import com.fengniao.remind.map.GDMapManger;
import com.fengniao.remind.service.RemindService;
import com.fengniao.remind.ui.adapter.SearchListAdapter;
import com.fengniao.remind.ui.base.BaseActivity;
import com.fengniao.remind.ui.present.ChooseSiteAtPresent;
import com.fengniao.remind.ui.view.ChooseSiteAtI;
import com.fengniao.remind.util.PopupWindowUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class ChooseSiteActivity extends BaseActivity<ChooseSiteAtI, ChooseSiteAtPresent> implements ChooseSiteAtI {

    private static final String TAG = ChooseSiteActivity.class.getSimpleName();


    @BindView(R.id.edit_search)
    EditText editSearch;

    @BindView(R.id.search_list)
    RecyclerView searchList;

    @BindView(R.id.img_marker)
    ImageView imgMarker;

    @BindView(R.id.text_address)
    TextView textAddress;

    @BindView(R.id.map_view)
    MapView mapView;

    private PopupWindow mPopup;

    private SearchListAdapter mAdapter;



    @Override
    public void onViewCreated(Bundle savedInstanceState) {
        super.onViewCreated(savedInstanceState);
        mapView.onCreate(savedInstanceState);
    }

    @Override
    public void initView() {
        super.initView();
        getLocationPermission();
        mPresenter.start();

        searchList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchListAdapter(this);
        searchList.setAdapter(mAdapter);

        editSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!TextUtils.isEmpty(editSearch.getText())) {
                    showSearchList();
                }
            }
        });
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                mPresenter.getSearchResult(editSearch.getText().toString());
            }
        });
        mAdapter.setOnItemClickListener(new SearchListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(PoiItem poiItem) {
                //在地图中心显示选择的地址
                mPresenter.getMapManger().animateCamera(new LatLng(poiItem.getLatLonPoint().getLatitude(),
                        poiItem.getLatLonPoint().getLongitude()));
//                //关闭地图跟随
                mPresenter.getMapManger().enableFollow(false);
                //显示marker
                showMarker();
                hideSearchList();
                //收起虚拟键盘
                closeVirtualKeyboard();
            }
        });
    }


    @OnClick(R.id.my_location)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.my_location:
                mPresenter.getMapManger().enableFollow(true);
                mPresenter.getMapManger().setZoomLevel(18f);
                hideMarker();
                break;
        }
    }

    public void getLocationPermission() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            //用于用户已经拒绝一次申请权限，这里可写一些为何申请此权限，获取用户的理解
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION)) {
                Toast.makeText(this, "请同意定位权限", Toast.LENGTH_SHORT).show();
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        1);
            }
        } else {
//            mManager.enableLocation(true);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//            mManager.enableLocation(true);
        } else {
            Toast.makeText(this, "获取权限失败", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void initPopupWindow() {
        View view = getLayoutInflater().inflate(R.layout.popup_search_list, null);
        RecyclerView listView = (RecyclerView) view.findViewById(R.id.search_list);
        listView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SearchListAdapter(this);
        listView.setAdapter(mAdapter);
        mPopup = PopupWindowUtils.getPopupWindowAsDropDownParent(view, WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.WRAP_CONTENT);
        mPopup.setFocusable(false);
    }

    @Override
    public void showPopupWindow() {
        if (!mPopup.isShowing())
            mPopup.showAsDropDown(editSearch);
    }

    @Override
    public void hidePopupWindow() {
        if (mPopup.isShowing())
            mPopup.dismiss();
    }

    @Override
    public void updateSearchList(List<PoiItem> list) {
        mAdapter.updateList(list);
    }

    @Override
    public void clearEditText() {
        editSearch.setText("");
    }

    @Override
    public void showSearchList() {
        searchList.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideSearchList() {
        searchList.setVisibility(View.GONE);
    }

    @Override
    public void showMarker() {
        imgMarker.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideMarker() {
        imgMarker.setVisibility(View.GONE);
    }

    @Override
    public void setAddressText(String addres) {
        textAddress.setText(addres);
    }

    @Override
    public void closeVirtualKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        if (imm != null) {
            imm.hideSoftInputFromWindow(getWindow().getDecorView().getWindowToken(), 0);
        }
    }

    @Override
    public MapView getMapView() {
        return mapView;
    }

    @Override
    public void updateAddress(String address) {
        textAddress.setText(address);
    }


    @Override
    public ChooseSiteAtPresent createPresent() {
        return new ChooseSiteAtPresent(this);
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_choose_site;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_determine, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.menu_determine) {
            if (!imgMarker.isShown()) {
                Toast.makeText(this, "请选择目的地", Toast.LENGTH_SHORT).show();
                return false;
            }
            if (mPresenter.saveLocation()) {
//                mBinder.startRemind();
                setResult(RESULT_OK);
                finish();
            } else {
                Toast.makeText(this, "选择目的地失败", Toast.LENGTH_SHORT).show();

            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView. onResume ()，实现地图生命周期管理
        mPresenter.getMapManger().onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView. onPause ()，实现地图生命周期管理
        mPresenter.getMapManger().onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，实现地图生命周期管理
        mPresenter.getMapManger().onDestroy();
        Intent intent = new Intent(this, RemindService.class);
        stopService(intent);
    }

    @Override
    public void onBackPressed() {
        if (!TextUtils.isEmpty(editSearch.getText().toString())) {
            clearEditText();
            hideMarker();
            return;
        }
        super.onBackPressed();
    }
}
