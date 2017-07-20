package com.fengniao.remind.ui.fragment;


import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.fengniao.remind.R;
import com.fengniao.remind.ui.adapter.EventListAdapter;
import com.fengniao.remind.ui.base.BaseFragment;
import com.fengniao.remind.ui.base.BasePresenter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * A simple {@link Fragment} subclass.
 */
public class EventListFragment extends BaseFragment {
    @BindView(R.id.event_list)
    RecyclerView eventList;

    public EventListFragment() {
        // Required empty public constructor
    }

    @Override
    protected BasePresenter createPresenter() {
        return null;
    }

    @Override
    protected int provideContentViewId() {
        return R.layout.fragment_event_list;
    }

    @Override
    public void initView(View rootView) {
        super.initView(rootView);
        List<String> list = new ArrayList<>();
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        list.add("");
        EventListAdapter adapter = new EventListAdapter(getContext(), list);
        eventList.setLayoutManager(new LinearLayoutManager(getContext()));
        eventList.setAdapter(adapter);
    }
}
