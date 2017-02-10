package de.templum.routplaner.view;


import android.support.annotation.UiThread;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;

import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Toast;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.templum.routplaner.R;
import de.templum.routplaner.util.AddressAutoCompleteHelper;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RouteFormActivity extends AppCompatActivity implements AddressAutoCompleteHelper.IAutoComplete {

    @Bind(R.id.form_route_list)
    RecyclerView mList;
    @Bind(R.id.form_input)
    AutoCompleteTextView mInput;

    private final PublishSubject<String> mInputEventStream = PublishSubject.create();
    private List<String> mRoute = new ArrayList<>();
    private AddressAutoCompleteHelper mAddressAutoCompleteHelper;
    private ArrayAdapter<String> mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_form);
        ButterKnife.bind(this);

        initialiseAutoComplete();
        initialiseRouteList();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mAddressAutoCompleteHelper = new AddressAutoCompleteHelper(this,this);
        mInputEventStream
                .debounce(500, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.computation())
                .subscribe(mAddressAutoCompleteHelper);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mAddressAutoCompleteHelper = null;
    }

    @OnClick(R.id.form_submit)
    public void addLocation() {
        if (mInput.getText() != null)
            Toast.makeText(this, "Addresse: " + mInput.getText().toString() + " hinzugefügt", Toast.LENGTH_LONG).show();
    }

    @OnTextChanged(value = R.id.form_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterEmailInput(final Editable editable) {
        mInputEventStream.onNext(editable != null ? editable.toString() : "");
    }

    private void initialiseAutoComplete() {
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        mAdapter.setNotifyOnChange(true);
        mInput.setAdapter(mAdapter);
    }

    private void initialiseRouteList() {
        mList.setLayoutManager(new LinearLayoutManager(this));
    }

    @UiThread
    @Override
    public void suggestion(final List<String> suggestions) {
        mAdapter.clear();
        mAdapter.addAll(suggestions);
        mAdapter.notifyDataSetChanged();
        mInput.showDropDown();
        Log.d("Test","Sollte jetzt eine neues shit zeigen");
    }

}