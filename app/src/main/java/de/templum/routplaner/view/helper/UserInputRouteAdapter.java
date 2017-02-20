package de.templum.routplaner.view.helper;

import android.content.Context;
import android.support.annotation.UiThread;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.templum.routplaner.R;

/**
 * An Adapter for displaying the route during creation.
 * Created by simon on 11.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class UserInputRouteAdapter extends RecyclerView.Adapter<UserInputRouteAdapter.RouteItem> {

    private final int START_OR_END = 0;
    private final int STATION = 1;
    private ArrayList<String> mData;
    private Context mCtx;


    public UserInputRouteAdapter(Context context) {
        mData = new ArrayList<>();
        mCtx = context;
    }

    @Override
    public UserInputRouteAdapter.RouteItem onCreateViewHolder(ViewGroup parent, int viewType) {
        RouteItem container = new RouteItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item, parent, false));
        if (viewType == START_OR_END) {
            container.setDrawable(START_OR_END);
        } else {
            container.setDrawable(STATION);
        }

        return container;
    }

    @Override
    public void onBindViewHolder(RouteItem holder, int position) {
        holder.setText(mData.get(position));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return (position == 0 || position == mData.size() - 1) ? START_OR_END : STATION;
    }

    @UiThread
    public void addItem(String item) {
        int insert = mData.size();
        if (insert < 1) {
            mData.add(item);
            mData.add(item);
            notifyItemRangeInserted(0, 2);
        } else {
            appendItem(item);
        }

    }

    @UiThread
    public void updateItem(int position, String item){
        mData.remove(position);
        mData.add(position, item);
        notifyItemChanged(position);
    }

    @UiThread
    public void removeItem(int position){
        mData.remove(position);
        notifyItemRemoved(position);
    }

    @UiThread
    public void clearItems(){
        mData.clear();
        notifyDataSetChanged();
    }

    private void appendItem(String item) {
        mData.remove(mData.size() - 1);
        mData.add(item);
        mData.add(mData.get(0));

        notifyDataSetChanged();
    }


    public ArrayList<String> getData() {
        return mData;
    }

    class RouteItem extends RecyclerView.ViewHolder {

        @Bind(R.id.route_item_address)
        TextView mText;
        @Bind(R.id.route_item_icon)
        ImageView mIcon;

        RouteItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setText(final String text) {
            mText.setText(text);
        }

        public void setDrawable(final int type) {
            if (type == START_OR_END)
                mIcon.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.icon_start));
            else
                mIcon.setImageDrawable(ContextCompat.getDrawable(mCtx, R.drawable.icon_station));
        }
    }
}
