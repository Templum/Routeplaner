package de.templum.routplaner.util;

import android.content.Context;
import android.location.Location;
import android.util.Log;

import io.reactivex.Notification;
import io.reactivex.Observable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;

/**
 * Created by simon on 10.02.2017.
 */

public class AddressAutoCompleteHelper {
    private final LocationProvider mProvider;
    private Disposable mSubscription;
    private Observable<Location> mSuggestionStream;

    public AddressAutoCompleteHelper(final Context ctx){
        mProvider = new LocationProvider(ctx);
    }

    public void onResume(Observable<String> inputEventStream){
        mSuggestionStream = mProvider.registerInputObserver(inputEventStream.doOnEach(new Consumer<Notification<String>>() {
            @Override
            public void accept(Notification<String> stringNotification) throws Exception {
                Log.d("Teste", stringNotification.getValue());
            }
        }));
    }

    public void onPause(){
        if(mSubscription != null) mSubscription.dispose();
        mProvider.unregisterInputObserver();
    }

    public Observable<Location> getSuggestionStream(){
        return mSuggestionStream;
    }
}
