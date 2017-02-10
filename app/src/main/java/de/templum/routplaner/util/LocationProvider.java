package de.templum.routplaner.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;


/**
 * Created by simon on 09.02.2017.
 */

public class LocationProvider {
    private Geocoder mGeocode;
    private LocationManager mLocationManager;
    private Observer<String> mInputObserver;
    private PublishSubject<Location> mSuggestionStream;
    private Disposable mSubscription;

    public LocationProvider(Context ctx) {
        mGeocode = new Geocoder(ctx);
        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
        mInputObserver = new InputObserver();
        mSuggestionStream = PublishSubject.create();
    }

    private void searchByAddress(final String address) {
        try {
            List<Address> findings = mGeocode.getFromLocationName(address, 10);
            for (Address found : findings) {
                Location location = new Location(address);
                location.setLatitude(found.getLatitude());
                location.setLongitude(found.getLongitude());
                location.setTime(new Date().getTime());
                mSuggestionStream.onNext(location);
            }
        } catch (IOException e) {}
    }

    public Observable<Location> registerInputObserver(Observable<String> inputEvent){
        if(mSubscription != null) mSubscription.dispose();
        inputEvent.observeOn(Schedulers.computation()).subscribe(mInputObserver);
        return mSuggestionStream;
    }

    public void unregisterInputObserver(){
        if(mSubscription != null) mSubscription.dispose();
    }

    private class InputObserver implements Observer<String>{

        @Override
        public void onSubscribe(Disposable d) {
            mSubscription = d;
        }

        @Override
        public void onNext(String value) {
            Log.d("Test", "Value: " + value);
            searchByAddress(value);
        }

        @Override
        public void onError(Throwable e) {}

        @Override
        public void onComplete() {}
    }
}
