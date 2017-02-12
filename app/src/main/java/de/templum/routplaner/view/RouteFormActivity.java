package de.templum.routplaner.view;


import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

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

    @Bind(R.id.form_route_list)
    RecyclerView mList;

    private RouteListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_form);
        ButterKnife.bind(this);

        initialiseRouteList();
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("Place: %s", place.getAddress());
                mAdapter.addItem(toastMsg);
            }
        }
    }

    @OnClick(R.id.form_fab_add)
    public void addLocation() {
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try {
            startActivityForResult(builder.build(this), 10);
        } catch (GooglePlayServicesRepairableException | GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }
    }

    @OnClick(R.id.form_submit)
    public void calculateRoute(){
        Toast.makeText(this,"Calculating Route", Toast.LENGTH_SHORT).show();
    }


    private void initialiseRouteList() {
        mAdapter = new RouteListAdapter(this);
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);
        mList.setHasFixedSize(true);
    }
}