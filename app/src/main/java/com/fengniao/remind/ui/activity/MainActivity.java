package com.fengniao.remind.ui.activity;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.fengniao.remind.R;
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

    @Override
    public void initView() {
        super.initView();
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

    @OnClick(R.id.fab_main)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.fab_main:
                Intent intent = new Intent(this, ChooseSiteActivity.class);
                startActivityForResult(intent, TYPE_LOCATION);
                break;
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TYPE_LOCATION && resultCode == RESULT_OK) {
            LocationListFragment fragment = (LocationListFragment) fragments.get(1);
            if (fragment != null) {
                fragment.refreshList();
            }
        }
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
