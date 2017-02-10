package de.templum.routplaner.util;


import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


/**
 * Created by simon on 10.02.2017.
 */

public class AddressAutoCompleteHelper implements Observer<String> {

    public interface IAutoComplete {
        void suggestion(List<String> suggestions);
    }

    private final IAutoComplete mListener;
    private final Geocoder mGeocode;

    public AddressAutoCompleteHelper(Context ctx, IAutoComplete listener) {
        mGeocode = new Geocoder(ctx, Locale.getDefault());
        mListener = listener;
    }


    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(String value) {
        searchForAddressWith(value);
    }

    @Override
    public void onError(Throwable e) {
    }

    @Override
    public void onComplete() {
    }

    private void searchForAddressWith(final String userInput) {
        List<String> suggestions = new ArrayList<>();
        try {
            List<Address> results = mGeocode.getFromLocationName(userInput, 10);

            for (Address result : results) {
                StringBuilder stringBuilder = new StringBuilder();
                for (int i = 0; i < result.getMaxAddressLineIndex() + 1; i++) {
                    stringBuilder.append(result.getAddressLine(i));
                    if(i < result.getMaxAddressLineIndex()) stringBuilder.append(',');
                }
                suggestions.add(stringBuilder.toString());
            }
        } catch (IOException e) {
            suggestions.add(Messages.NO_RESULT_FOUND);
        }
        mListener.suggestion(suggestions);
    }
}
