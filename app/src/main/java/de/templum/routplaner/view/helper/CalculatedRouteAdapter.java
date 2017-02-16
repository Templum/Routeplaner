package de.templum.routplaner.view.helper;

import android.support.annotation.UiThread;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.templum.routplaner.R;
import de.templum.routplaner.model.RoutePoint;

/**
 * An adapter which displays the optimized route.
 * Created by simon on 14.02.2017.
 * Copyright (c) 2017 simon All rights reserved.
 */

public class CalculatedRouteAdapter extends RecyclerView.Adapter<CalculatedRouteAdapter.RouteItem> {

    private List<RoutePoint> mData = new ArrayList<>();

    public CalculatedRouteAdapter() {
    }

    @Override
    public CalculatedRouteAdapter.RouteItem onCreateViewHolder(ViewGroup parent, int viewType) {
        return new CalculatedRouteAdapter.RouteItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.calculated_route_item, parent, false));
    }

    @Override
    public void onBindViewHolder(CalculatedRouteAdapter.RouteItem holder, int position) {
        RoutePoint routePoint = mData.get(position);

        holder.setText(routePoint.toString());
        holder.setPosition(position + 1); // Only it guys can handle a list which start at 0
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @UiThread
    public void addAll(List<RoutePoint> data) {
        int size = mData.size();
        mData.addAll(data);
        notifyItemRangeInserted(size, size + data.size());
    }

    class RouteItem extends RecyclerView.ViewHolder {

        @Bind(R.id.route_item_address)
        TextView mText;

        @Bind(R.id.icon_position)
        TextView mPosition;

        RouteItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setText(final String text) {
            mText.setText(text);
        }

        public void setPosition(final Integer position) {
            setPosition(position.toString());
        }

        private void setPosition(final String position) {
            mPosition.setText(position);
        }
    }
}
