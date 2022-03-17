package com.poona.agrocart.ui.payment;

import static com.poona.agrocart.app.AppConstants.ADDRESS_P_ID;
import static com.poona.agrocart.app.AppConstants.COUPON_ID;
import static com.poona.agrocart.app.AppConstants.DELIVERY_DATE;
import static com.poona.agrocart.app.AppConstants.PAYMENT_CURRENCY;
import static com.poona.agrocart.app.AppConstants.PAYMENT_MODE_ID;
import static com.poona.agrocart.app.AppConstants.PAYMENT_REFERENCE_ID;
import static com.poona.agrocart.app.AppConstants.PAYMENT_SECRET_KEY;
import static com.poona.agrocart.app.AppConstants.SLOT_ID;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.view.GravityCompat;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.splash_screen.SplashScreenActivity;
import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

import java.util.HashMap;

public class PaymentActivity extends AppCompatActivity implements PaymentResultListener {

    private AppSharedPreferences preferences;
    private PaymentViewModel paymentViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        preferences = new AppSharedPreferences(PaymentActivity.this);
        paymentViewModel = new ViewModelProvider(this).get(PaymentViewModel.class);
        Checkout.preload(this);
        startPayment();
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
        Checkout.preload(this);
        startPayment();
    }

    public void startPayment() {
        /*
          You need to pass current activity in order to let Razorpay create CheckoutActivity
         */
        final Activity activity = this;

        final Checkout checkout = new Checkout();
        HashMap<String,String> keyMap = new HashMap();
        keyMap = preferences.getRazorCredentials();
        checkout.setKeyID(keyMap.get(PAYMENT_SECRET_KEY));

        try {
            JSONObject options = new JSONObject();
            options.put("name", "Razorpay Corp");
            options.put("description", "Demoing Charges");
            options.put("send_sms_hash",true);
            options.put("allow_rotation", true);
            //You can omit the image option to fetch the image from dashboard
            options.put("image", "https://s3.amazonaws.com/rzp-mobile/images/rzp.png");
            options.put("currency", keyMap.get(PAYMENT_CURRENCY));
            options.put("amount", "100");

            JSONObject preFill = new JSONObject();
            preFill.put("email", "test@razorpay.com");
            preFill.put("contact", "9876543210");

            options.put("prefill", preFill);

            checkout.open(activity, options);
        } catch (Exception e) {
            Toast.makeText(activity, "Error in payment: " + e.getMessage(), Toast.LENGTH_SHORT)
                    .show();
            e.printStackTrace();
        }
    }

    @Override
    public void onPaymentSuccess(String paymentReference) {
        Toast.makeText(PaymentActivity.this,"SuccessFull Payment integrated"+paymentReference,Toast.LENGTH_LONG).show();
        if (paymentReference!=null){
            calPlaceOrderApi(showCircleProgressDialog(this,""),paymentReference);
        }
    }

    private void calPlaceOrderApi(ProgressDialog progressDialog,String paymentReference) {
        Observer<BaseResponse> baseResponseObserver = baseResponse -> {
            if (baseResponse!=null){
                if (progressDialog!=null)
                    progressDialog.dismiss();
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (baseResponse.getStatus() == 200) {
                            finish();
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
//                        warningToast(context, productListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                    case STATUS_CODE_405://Method Not Allowed
                        Toast.makeText(PaymentActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }

            }
        };
        paymentViewModel.getOrderPlaceAPIResponse(progressDialog,placeOrderMaps(paymentReference),
                PaymentActivity.this).observeForever(baseResponseObserver);
    }

    private HashMap<String, String> placeOrderMaps(String paymentReference) {
        HashMap<String, String> map = new HashMap<>();
        map.put(ADDRESS_P_ID, preferences.getDeliveryAddressId());
        map.put(DELIVERY_DATE, preferences.getDeliveryDate());
        map.put(SLOT_ID, preferences.getDeliverySlot());
        map.put(PAYMENT_MODE_ID, preferences.getPaymentMode());
        map.put(COUPON_ID, preferences.getCouponId());
        map.put(PAYMENT_REFERENCE_ID, paymentReference);
        return map;
    }
    @Override
    public void onPaymentError(int i, String paymentReference) {
        Toast.makeText(PaymentActivity.this,"Failed Payment integrated"+paymentReference,Toast.LENGTH_LONG).show();
    }

    public ProgressDialog showCircleProgressDialog(Context context, String message) {
        ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(context, R.style.CustomProgressDialog));
        try {
            dialog.show();
        } catch (WindowManager.BadTokenException e) {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.circular_progresbar);
        // dialog.setAmenitiesName(Message);
        return dialog;
    }

    @Override
    public void onBackPressed() {
        Toast.makeText(PaymentActivity.this,"Complete you payment",Toast.LENGTH_SHORT).show();
        recreate();
    }
}