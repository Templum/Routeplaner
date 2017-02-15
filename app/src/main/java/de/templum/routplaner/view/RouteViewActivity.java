package de.templum.routplaner.view;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.support.design.widget.BaseTransientBottomBar;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.ybq.android.spinkit.SpinKitView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import butterknife.Bind;
import butterknife.ButterKnife;
import de.templum.routplaner.R;
import de.templum.routplaner.computing.RouteFactory;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.Helper;
import de.templum.routplaner.view.helper.CalculatedRouteAdapter;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RouteViewActivity extends AppCompatActivity implements Observer<List<RoutePoint>> {

    public static final String ROUTE_LIST = "Route_List";
    private final List<RoutePoint> mInitialRoute = new ArrayList<>();
    @Bind(R.id.route_view_list)
    RecyclerView mList;
    @Bind(R.id.route_view_footer)
    RelativeLayout mFooter;
    @Bind(R.id.route_footer_value)
    TextView mValue;
    @Bind(R.id.route_view_progress)
    SpinKitView mProgress;

    private RouteFactory mFactory = new RouteFactory(this);
    private Disposable mSubscription;
    private CalculatedRouteAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_view);
        ButterKnife.bind(this);

        if (getIntent() != null && getIntent().getExtras() != null) {
            List<String> routeList = getIntent().getExtras().getStringArrayList(ROUTE_LIST);
            mFactory.optimizeGivenRoute(routeList)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(this);
            init(routeList);
        }

        initialiseCalculatedRouteList();
    }

    @Override
    public void onSubscribe(Disposable d) {
        mSubscription = d;
        mProgress.setVisibility(View.VISIBLE);
    }

    @Override
    public void onNext(List<RoutePoint> value) {
        mAdapter.clearData();
        mAdapter.addAll(value);
    }

    @Override
    public void onError(Throwable e) {
        Snackbar.make(mFooter, e.getMessage(), BaseTransientBottomBar.LENGTH_LONG).show();
    }

    @Override
    public void onComplete() {
        if (mSubscription != null) mSubscription.dispose();
        mProgress.setVisibility(View.GONE);
        mList.setVisibility(View.VISIBLE);
        calculateDifference();
    }

    private void initialiseCalculatedRouteList() {
        mAdapter = new CalculatedRouteAdapter();
        mList.setLayoutManager(new LinearLayoutManager(this));
        mList.setAdapter(mAdapter);
        mList.setHasFixedSize(true);
    }

    private void init(final List<String> initialAddresses) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                for (String address : initialAddresses) {
                    Location location = Helper.searchBy(RouteViewActivity.this, address);
                    if (location != null)
                        mInitialRoute.add(new RoutePoint(location, location.getProvider()));
                }
            }
        }).start();
    }

    private void calculateDifference(){
        Double lengthOriginal = Helper.calculateRouteLength(mInitialRoute);
        Double lengthCurrent = Helper.calculateRouteLength(mAdapter.getData());

        Double signum = Math.signum(lengthOriginal - lengthCurrent);
        Double dif = Math.abs(lengthOriginal - lengthCurrent) / 1000; //Convert to Kilometer

        Log.d("Difference", "Original: " + lengthOriginal + " Current: " + lengthCurrent);
        Log.d("Difference", "Signum: " + signum + " Difference: " + dif);

        if(signum > 0){
            mValue.setTextColor(Color.GREEN);
            mValue.setText(String.format(Locale.getDefault(), "%1$,.2f KM", dif));
        }else{
            mValue.setTextColor(Color.RED);
            mValue.setText(String.format(Locale.getDefault(), "%1$,.2f KM", dif));
        }
    }
}
