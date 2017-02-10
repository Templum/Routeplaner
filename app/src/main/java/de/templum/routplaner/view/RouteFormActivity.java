package de.templum.routplaner.view;

import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.templum.routplaner.R;
import de.templum.routplaner.model.RoutePoint;
import de.templum.routplaner.util.AddressAutoCompleteHelper;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RouteFormActivity extends AppCompatActivity {

    @Bind(R.id.form_route_list)
    RecyclerView mList;
    @Bind(R.id.form_input)
    AutoCompleteTextView mInput;

    private final PublishSubject<String> mInputEventStream = PublishSubject.create();
    private List<RoutePoint> mRoute;
    private AddressAutoCompleteHelper mAddressAutoCompleteHelper;
    private Disposable mSubscription;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_form);
        ButterKnife.bind(this);
        // Maybe restore old state
        mList.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.list_content);
        mAddressAutoCompleteHelper = new AddressAutoCompleteHelper(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddressAutoCompleteHelper.onResume(mInputEventStream.throttleWithTimeout(3,TimeUnit.SECONDS));
        mAddressAutoCompleteHelper
                .getSuggestionStream()
                .observeOn(Schedulers.computation())
                .subscribeOn(AndroidSchedulers.mainThread())
                .subscribe(new LocationObserver());
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mSubscription != null) mSubscription.dispose();
        mAddressAutoCompleteHelper.onPause();
    }

    @OnClick(R.id.form_submit)
    public void addLocation(){
        if(mInput.getText() != null)
            Toast.makeText(this, "Addresse: " + mInput.getText().toString() + " hinzugefügt", Toast.LENGTH_LONG).show();
    }

    @OnTextChanged(value = R.id.form_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterEmailInput(final Editable editable) {
        Log.d("Hier","Triggered");
        mInputEventStream.onNext(editable != null ? editable.toString() : "");
    }


    private class LocationObserver implements Observer<Location>{

        @Override
        public void onSubscribe(Disposable d) {
            mSubscription = d;
        }

        @Override
        public void onNext(Location value) {
            Log.d("Test", "Value" + value.getProvider());
            mAdapter.add(value.getProvider());
        }

        @Override
        public void onError(Throwable e) {}

        @Override
        public void onComplete() {}
    }
}