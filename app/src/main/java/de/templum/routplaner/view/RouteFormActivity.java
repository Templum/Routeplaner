package de.templum.routplaner.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.RelativeLayout;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.templum.routplaner.R;
import de.templum.routplaner.view.helper.RouteListAdapter;


public class RouteFormActivity extends AppCompatActivity {

    private static final Integer LOCATION_PICKER_INTENT = 1337;

    @Bind(R.id.form_route_list)
    RecyclerView mList;

    @Bind(R.id.form_submit_container)
    RelativeLayout mBottom;

    private RouteListAdapter mAdapter;

    /**
     * Lifecycle
     **/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_form);
        ButterKnife.bind(this);

        initialiseRouteList();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == LOCATION_PICKER_INTENT) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(this, data);
                mAdapter.addItem(place.getAddress().toString());
            } else {
                Snackbar.make(mBottom, R.string.error_something_went_wrong, BaseTransientBottomBar.LENGTH_LONG).show();
            }
        }
    }

    @OnClick(R.id.form_fab_add)
    public void addLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), LOCATION_PICKER_INTENT);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            Snackbar.make(mBottom, R.string.error_google_places_not_reachable, BaseTransientBottomBar.LENGTH_LONG).show();
        }
    }

    @OnClick(R.id.form_submit)
    public void calculateRoute() {
        if (mAdapter.getItemCount() > 3) {
            Intent intent = new Intent(this, RouteViewActivity.class);
            intent.putStringArrayListExtra(RouteViewActivity.ROUTE_LIST, mAdapter.getData());
            startActivity(intent);
        } else {
            Snackbar.make(mBottom, R.string.error_nothing_to_optimize, BaseTransientBottomBar.LENGTH_LONG).show();
        }

    }


    private void initialiseRouteList() {
        mAdapter = new RouteListAdapter(this);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);
        mList.setHasFixedSize(true);
    }
}