package de.templum.routplaner.view.helper;

import android.support.annotation.Nullable;
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
 * Created by simon on 14.02.2017.
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
        holder.setPosition(position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    @UiThread
    public void clearData() {
        int size = mData.size();
        mData.clear();
        notifyItemRangeRemoved(0, size);
    }

    public List<RoutePoint> getData(){
        return mData;
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

        public RouteItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setText(final String text) {
            mText.setText(text);
        }

        private void setPosition(final String position) {
            mPosition.setText(position);
        }

        public void setPosition(final Integer position) {
            setPosition(position.toString());
        }
    }
}
