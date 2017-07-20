package com.fengniao.remind.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.fengniao.remind.R;

import java.util.List;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.RecyclerViewHolder> {


    private Context context;
    private List mItems;

    public LocationListAdapter(Context context, List mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public LocationListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_event, parent, false);
        return new LocationListAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {

    }


    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        private View mView;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
        }
    }
}
