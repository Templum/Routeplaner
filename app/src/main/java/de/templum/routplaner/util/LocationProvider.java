package de.templum.routplaner.util;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;


/**
 * Created by simon on 09.02.2017.
 */

public class LocationProvider {
    private Geocoder mGeocode;
    private LocationManager mLocationManager;

    public LocationProvider(Context ctx) {
        mGeocode = new Geocoder(ctx);
        mLocationManager = (LocationManager) ctx.getSystemService(Context.LOCATION_SERVICE);
    }

    public Observable<Location> searchByAddress(final String address) {
        return new Observable<Location>() {
            @Override
            protected void subscribeActual(Observer<? super Location> observer) {
                try {
                    List<Address> findings = mGeocode.getFromLocationName(address, 10);
                    for (Address found : findings) {
                        Location location = new Location(address);
                        location.setLatitude(found.getLatitude());
                        location.setLongitude(found.getLongitude());
                        location.setTime(new Date().getTime());
                        observer.onNext(location);
                    }
                    observer.onComplete();
                } catch (IOException e) {
                    observer.onError(e);
                }

            }
        };
    }

    public Observable<Location> searchByPosition() {
        return new Observable<Location>() {
            @Override
            protected void subscribeActual(final Observer<? super Location> observer) {
                final LocationListener locationListener = new LocationListener() {
                    @Override
                    public void onLocationChanged(Location location) {
                        observer.onNext(location);
                        observer.onComplete();
                    }

                    @Override
                    public void onStatusChanged(String provider, int status, Bundle extras) {
                    }

                    @Override
                    public void onProviderEnabled(String provider) {
                    }

                    @Override
                    public void onProviderDisabled(String provider) {
                    }
                };
                try {
                    mLocationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, locationListener, Looper.myLooper());
                }catch (SecurityException e){
                    observer.onError(e); //TODO: Later Use an Runtime Library like dexter to check
                }
            }
        };
    }
}
