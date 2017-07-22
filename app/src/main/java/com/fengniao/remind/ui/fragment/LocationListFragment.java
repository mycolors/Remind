package com.fengniao.remind.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fengniao.remind.R;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.ui.adapter.LocationListAdapter;
import com.fengniao.remind.ui.base.BaseFragment;
import com.fengniao.remind.ui.base.BasePresenter;

import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationListFragment extends BaseFragment {
    @BindView(R.id.location_list)
    RecyclerView locationList;

    private List<Location> mList;

    private LocationListAdapter mAdapter;

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

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        mList = LocalDataSource.getInstance(getContext()).getAllLocation();
        locationList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LocationListAdapter(getContext(), mList);
        locationList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mList.get(position).isActivate()) {
                    if (LocalDataSource.getInstance(getContext()).arrivedLocation(
                            mList.get(position))) {
                            mList.get(position).setActivate(!mList.get(position).isActivate());
                    }
                } else {
                    if (LocalDataSource.getInstance(getContext()).activateLocation(
                            mList.get(position))) {
                        mList.get(position).setActivate(!mList.get(position).isActivate());
                    }
                }
                refreshList();
            }
        });
    }


    public void resetList(){
        mList = LocalDataSource.getInstance(getContext()).getAllLocation();
        refreshList();
    }

    public void refreshList() {
        mAdapter.notifyDataSetChanged();
    }

}
