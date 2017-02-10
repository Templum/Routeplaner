package de.templum.routplaner.view;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Toast;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnTextChanged;
import de.templum.routplaner.R;
import de.templum.routplaner.model.RoutePoint;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.internal.operators.observable.ObservableFromPublisher;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;

public class RouteFormActivity extends AppCompatActivity {

    @Bind(R.id.form_route_list)
    RecyclerView mList;
    @Bind(R.id.form_input)
    AutoCompleteTextView mInput;

    private List<RoutePoint> mRoute;
    private final PublishSubject<String> mInputEvent = PublishSubject.create();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_route_form);
        ButterKnife.bind(this);
        // Maybe restore old state
        mList.setLayoutManager(new LinearLayoutManager(this));
        inputTest();
    }

    @OnClick(R.id.form_submit)
    public void addLocation(){
        if(mInput.getText() != null)
            Toast.makeText(this, "Addresse: " + mInput.getText().toString() + " hinzugefügt", Toast.LENGTH_LONG).show();
    }

    @OnTextChanged(value = R.id.form_input, callback = OnTextChanged.Callback.AFTER_TEXT_CHANGED)
    public void afterEmailInput(final Editable editable) {
        mInputEvent.onNext(editable != null ? editable.toString() : "");
    }


    private void inputTest(){
        mInputEvent
                .throttleWithTimeout(1, TimeUnit.SECONDS, AndroidSchedulers.mainThread())
                .subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(String value) {
                Toast.makeText(RouteFormActivity.this, value, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }
}