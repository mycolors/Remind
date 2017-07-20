package com.fengniao.remind.ui.fragment;


import android.support.v4.app.Fragment;

import com.fengniao.remind.R;
import com.fengniao.remind.ui.base.BaseFragment;
import com.fengniao.remind.ui.base.BasePresenter;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationListFragment extends BaseFragment {


    public LocationListFragment() {
        // Required empty public constructor
    }


    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_location_list;
    }

}
