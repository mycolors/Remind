package com.fengniao.remind.ui.fragment;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.fengniao.remind.R;
import com.fengniao.remind.data.Location;
import com.fengniao.remind.data.local.LocalDataSource;
import com.fengniao.remind.ui.activity.MainActivity;
import com.fengniao.remind.ui.adapter.LocationListAdapter;
import com.fengniao.remind.ui.base.BaseFragment;
import com.fengniao.remind.ui.base.BasePresenter;
import com.fengniao.remind.ui.wediget.ItemTouchHelperCallback;

import java.util.Collections;
import java.util.List;

import butterknife.BindView;

import static com.fengniao.remind.app.Constant.REMIND_FINISHED;

/**
 * A simple {@link Fragment} subclass.
 */
public class LocationListFragment extends BaseFragment {
    @BindView(R.id.location_list)
    RecyclerView locationList;

    private List<Location> mList;

    private LocationListAdapter mAdapter;

    private Receiver mReceiver;

    @Override
    public void onDestroy() {
        super.onDestroy();
        getActivity().unregisterReceiver(mReceiver);
    }

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
        initBroadcast();
        mList = LocalDataSource.getInstance(getContext()).getAllLocation();
        locationList.setLayoutManager(new LinearLayoutManager(getContext()));
        mAdapter = new LocationListAdapter(getContext(), mList);
        locationList.setAdapter(mAdapter);
        enableSwipeAndDrag();
        mAdapter.setOnItemClickListener(new LocationListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                if (mList.get(position).isActivate()) {
                    if (LocalDataSource.getInstance(getContext()).arrivedLocation(
                            mList.get(position))) {
                        mList.get(position).setActivate(!mList.get(position).isActivate());
                        refreshList();
                        startRemind();
                    }
                } else {
                    if (LocalDataSource.getInstance(getContext()).activateLocation(
                            mList.get(position))) {
                        mList.get(position).setActivate(!mList.get(position).isActivate());
                        refreshList();
                        startRemind();
                    }
                }
            }
        });
    }

    public void initBroadcast() {
        mReceiver = new Receiver();
        IntentFilter filter = new IntentFilter(REMIND_FINISHED);
        getActivity().registerReceiver(mReceiver, filter);
    }

    //开启列表滑动删除和拖动变换位置
    public void enableSwipeAndDrag(){
        ItemTouchHelperCallback callback = new ItemTouchHelperCallback(new ItemTouchHelperCallback.OnItemTouchCallbackListener() {
            @Override
            public void onSwiped(int positon) {
                if (LocalDataSource.getInstance(getContext()).deleteLocation(
                        mList.get(positon))) {
                    if (mList.get(positon).isActivate()) {
                        startRemind();
                    }
                    mList.remove(positon);
                    mAdapter.notifyItemRemoved(positon);
                }
            }

            @Override
            public boolean onMove(int srcPos, int targetPos) {
                Collections.swap(mList,srcPos,targetPos);
                //这里不要用全部刷新
                mAdapter.notifyItemMoved(srcPos,targetPos);
                return false;
            }
        });
        callback.setDragEnable(true);
        callback.setSwipeEnable(true);
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        itemTouchHelper.attachToRecyclerView(locationList);
    }

    public void resetList() {
        List<Location> list = LocalDataSource.getInstance(getContext()).getAllLocation();
        mList.clear();
        mList.addAll(list);
        refreshList();
    }

    public void refreshList() {
        mAdapter.notifyDataSetChanged();
    }

    public class Receiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            long id = intent.getLongExtra("id", -1);
            if (id != -1) {
                for (Location l : mList) {
                    if (l.getId() == id) {
                        l.setActivate(false);
                        break;
                    }
                }
                refreshList();

            }
        }
    }

    public void startRemind() {
        if (getActivity() instanceof MainActivity)
            ((MainActivity) getActivity()).startRemind();
    }


}
