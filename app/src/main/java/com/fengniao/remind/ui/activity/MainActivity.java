package com.fengniao.remind.ui.activity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.IBinder;
import android.os.RemoteException;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import com.amap.api.maps.Projection;
import com.fengniao.remind.R;
import com.fengniao.remind.RemindServiceInf;
import com.fengniao.remind.service.ProtectService;
import com.fengniao.remind.service.RemindService;
import com.fengniao.remind.ui.adapter.FragmentAdapter;
import com.fengniao.remind.ui.base.BaseActivity;
import com.fengniao.remind.ui.base.BasePresenter;
import com.fengniao.remind.ui.fragment.EventListFragment;
import com.fengniao.remind.ui.fragment.LocationListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class MainActivity extends BaseActivity {
    public static final int TYPE_EVENT = 1;

    public static final int TYPE_LOCATION = 2;

    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.tab_layout_main)
    TabLayout tabLayout;
    @BindView(R.id.view_pager_main)
    ViewPager viewPager;
    @BindView(R.id.fab_main)
    FloatingActionButton fabMain;

    private List<Fragment> fragments;



    private RemindServiceInf mBinder;

    private ServiceConnection connection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mBinder =RemindServiceInf.Stub.asInterface(service);
            startRemind();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    @Override
    public void initView() {
        super.initView();
        initService();
        setSupportActionBar(toolbar);
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_tab_main_1));
        titles.add(getString(R.string.title_tab_main_2));
        viewPager.setOffscreenPageLimit(2);
        fragments = new ArrayList<>();
        fragments.add(new EventListFragment());
        fragments.add(new LocationListFragment());
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(mFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);
    }


    public void initService() {
        Intent intent = new Intent(this, RemindService.class);
        startService(intent);
        bindService(intent, connection, BIND_AUTO_CREATE);
        Intent intent1 = new Intent(this, ProtectService.class);
        startService(intent1);
    }


    public void locationListChanged() {
        try {
            mBinder.startRemind();
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.fab_main)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_main:
                Intent intent = new Intent(this, ChooseSiteActivity.class);
                startActivityForResult(intent, TYPE_LOCATION);
                break;
        }
    }

    public void startRemind() {
        if (mBinder != null){
            try {
                mBinder.startRemind();
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE_LOCATION && resultCode == RESULT_OK) {
            LocationListFragment fragment = (LocationListFragment) fragments.get(1);
            if (fragment != null) {
                fragment.resetList();
            }
        }
        startRemind();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(connection);
    }

    @Override
    public BasePresenter createPresent() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.activity_main;
    }


}
