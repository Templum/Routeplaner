package de.templum.routplaner.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import de.templum.routplaner.R;

public class RouteViewActivity extends AppCompatActivity {

    public static final String ROUTE_LIST = "Route_List";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);

        if(getIntent() != null && getIntent().getExtras() != null){
            List<String> routeList = getIntent().getExtras().getStringArrayList(ROUTE_LIST);

            for (String address: routeList) {
                Log.d(ROUTE_LIST, address);
            }
        }
    }

}
