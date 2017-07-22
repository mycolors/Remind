package com.fengniao.remind.ui.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import com.fengniao.remind.R;
import com.fengniao.remind.data.Location;

import java.util.List;


public class LocationListAdapter extends RecyclerView.Adapter<LocationListAdapter.RecyclerViewHolder> {

    private Context context;
    private List<Location> mItems;
    private OnItemClickListener mOnItemClickListener;

    public LocationListAdapter(Context context, List<Location> mItems) {
        this.context = context;
        this.mItems = mItems;
    }

    public void setOnItemClickListener(OnItemClickListener mOnItemClickListener) {
        this.mOnItemClickListener = mOnItemClickListener;
    }

    @Override
    public LocationListAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_list_event, parent, false);
        return new LocationListAdapter.RecyclerViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, int position) {
        holder.address.setText(mItems.get(position).getAddress());
        if (mItems.get(position).isActivate()){
            holder.cbActivate.setChecked(true);
        } else {
            holder.cbActivate.setChecked(false);
        }
        if (mOnItemClickListener!=null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mOnItemClickListener.onItemClick(holder.getAdapterPosition());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    class RecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView address;

        CheckBox cbActivate;

        private RecyclerViewHolder(View itemView) {
            super(itemView);
            address = (TextView) itemView.findViewById(R.id.text_address);
            cbActivate = (CheckBox) itemView.findViewById(R.id.cb_activate);
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int  position);
    }
}
