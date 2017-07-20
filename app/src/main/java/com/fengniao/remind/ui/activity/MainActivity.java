package com.fengniao.remind.ui.activity;

import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;

import com.fengniao.remind.R;
import com.fengniao.remind.ui.adapter.FragmentAdapter;
import com.fengniao.remind.ui.base.BaseActivity;
import com.fengniao.remind.ui.base.BasePresenter;
import com.fengniao.remind.ui.fragment.EventListFragment;
import com.fengniao.remind.ui.fragment.LocationListFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MainActivity extends BaseActivity {
    @BindView(R.id.toolbar_main)
    Toolbar toolbar;
    @BindView(R.id.tab_layout_main)
    TabLayout tabLayout;
    @BindView(R.id.view_pager_main)
    ViewPager viewPager;
    @BindView(R.id.fab_main)
    FloatingActionButton fabMain;

    @Override
    public void initView() {
        super.initView();
        setSupportActionBar(toolbar);
        List<String> titles = new ArrayList<>();
        titles.add(getString(R.string.title_tab_main_1));
        titles.add(getString(R.string.title_tab_main_2));
        viewPager.setOffscreenPageLimit(2);
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new EventListFragment());
        fragments.add(new LocationListFragment());
        FragmentAdapter mFragmentAdapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        viewPager.setAdapter(mFragmentAdapter);
        tabLayout.setupWithViewPager(viewPager);

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
