package com.poona.agrocart.ui.nav_addresses.map_view;

import android.app.IntentService;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.os.ResultReceiver;
import android.text.TextUtils;
import android.util.Log;

import com.poona.agrocart.R;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class FetchAddressIntentService extends IntentService {

    public static final String TAG = FetchAddressIntentService.class.getSimpleName();
    protected ResultReceiver receiver;

    private String state = "";
    private String city = "";
    private String area = "";
    private String pincode = "";

    private String houseNumber = "";
    private String street = "";
    private String landmark = "";

    /**
     * Creates an IntentService.  Invoked by your subclass's constructor.
     */
    public FetchAddressIntentService() {
        super("FetchAddressIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent == null) {
            return;
        }

        receiver = intent.getParcelableExtra(SimplePlacePicker.RECEIVER);
        double latitude = intent.getDoubleExtra(SimplePlacePicker.LOCATION_LAT_EXTRA, -1);
        double longitude = intent.getDoubleExtra(SimplePlacePicker.LOCATION_LNG_EXTRA, -1);
        String language = intent.getStringExtra(SimplePlacePicker.LANGUAGE);


        String errorMessage = "";
        List<Address> addresses = null;

        Locale locale = new Locale(language);
        Geocoder geocoder = new Geocoder(this, locale);
        try {
            addresses = geocoder.getFromLocation(
                    latitude,
                    longitude,
                    5
            );
        } catch (IOException ioException) {
            // Catch network or other I/O problems.
            errorMessage = getString(R.string.service_not_available);
            Log.e(TAG, errorMessage, ioException);
        }
        // Handle case where no address was found.
        if (addresses == null || addresses.size() == 0) {
            if (errorMessage.isEmpty()) {
                errorMessage = getString(R.string.no_address_found);
                Log.e(TAG, errorMessage);
            }
            deliverResultToReceiver(SimplePlacePicker.FAILURE_RESULT, errorMessage);
        } else {
            StringBuilder result = new StringBuilder();
            Address address = addresses.get(0);

            // Fetch the address lines using getAddressLine,
            // join them, and send them to the thread.
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                if (i == address.getMaxAddressLineIndex()) {
                    result.append(address.getAddressLine(i));
                } else {
                    result.append(address.getAddressLine(i) + ",");
                }
            }

            state = address.getAdminArea();
            city = address.getLocality();
            area = address.getSubLocality();
            pincode = address.getPostalCode();

            Log.i(TAG, getString(R.string.address_found));
            if (address.getFeatureName() != null && !TextUtils.isEmpty(address.getFeatureName())) {
                houseNumber = address.getFeatureName();
            } else {
                if (address.getSubThoroughfare() != null && !TextUtils.isEmpty(address.getSubThoroughfare())) {
                    houseNumber = address.getSubThoroughfare();
                } else {
                    houseNumber = "";
                }
            }

            if (address.getThoroughfare() != null && !TextUtils.isEmpty(address.getThoroughfare())) {
                street = address.getThoroughfare();
                landmark = address.getThoroughfare() + ", " + area;
            } else {
                street = "";
                landmark = "";
            }

            Log.i(TAG, "house number: " + houseNumber);
            Log.i(TAG, "street: " + street);
            Log.i(TAG, "landmark: " + landmark);

            Log.i(TAG, "name: " + address.getLocality());
            Log.i(TAG, "address : " + result);
            Log.i(TAG, "state : " + state);
            Log.i(TAG, "city : " + city);
            Log.i(TAG, "area : " + area);
            Log.i(TAG, "pincode : " + pincode);

            deliverResultToReceiver(SimplePlacePicker.SUCCESS_RESULT, result.toString());
        }

    }

    private void deliverResultToReceiver(int resultCode, String message) {
        Bundle bundle = new Bundle();
        bundle.putString(SimplePlacePicker.RESULT_DATA_KEY, message);
        bundle.putString(SimplePlacePicker.SELECTED_HOUSE_NUMBER, houseNumber);
        bundle.putString(SimplePlacePicker.SELECTED_STREET, street);
        bundle.putString(SimplePlacePicker.SELECTED_LANDMARK, landmark);
        bundle.putString(SimplePlacePicker.SELECTED_STATE, state);
        bundle.putString(SimplePlacePicker.SELECTED_CITY, city);
        bundle.putString(SimplePlacePicker.SELECTED_AREA, area);
        bundle.putString(SimplePlacePicker.SELECTED_PIN_CODE, pincode);
        receiver.send(resultCode, bundle);
    }
}