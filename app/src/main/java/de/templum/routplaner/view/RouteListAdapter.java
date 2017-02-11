package de.templum.routplaner.view;

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
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.templum.routplaner.R;

/**
 * Created by simon on 11.02.2017.
 */

public class RouteListAdapter extends RecyclerView.Adapter<RouteListAdapter.RouteItem> {

    private List<String> mData;
    private Context mCtx;
    private final int START_OR_END = 0;
    private final int STATION = 1;


    public RouteListAdapter(Context context){
        mData = new ArrayList<>();
        mCtx = context;
    }

    @Override
    public RouteItem onCreateViewHolder(ViewGroup parent, int viewType) {
        RouteItem container = new RouteItem(LayoutInflater.from(parent.getContext()).inflate(R.layout.route_item,parent,false));
        if(viewType == START_OR_END){
            container.setDrawable(START_OR_END);
        }else{
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
        return (position == 0 || position == mData.size() -1) ? START_OR_END : STATION;
    }

    @UiThread
    public void addItem(String item){
        int insert = mData.size();
        mData.add(item);
        notifyItemInserted(insert);
    }

    public class RouteItem extends RecyclerView.ViewHolder{

        @Bind(R.id.route_item_address)
        TextView mText;
        @Bind(R.id.route_item_icon)
        ImageView mIcon;

        public RouteItem(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }

        public void setText(final String text){
            mText.setText(text);
        }

        public void setDrawable(final int type){
            if(type == START_OR_END)
                mIcon.setImageDrawable(ContextCompat.getDrawable(mCtx,R.drawable.icon_start));
            else
                mIcon.setImageDrawable(ContextCompat.getDrawable(mCtx,R.drawable.icon_station));
        }
    }
}
