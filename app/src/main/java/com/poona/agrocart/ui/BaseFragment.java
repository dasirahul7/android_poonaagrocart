package com.poona.agrocart.ui;

import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LOGOUT;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.ParseException;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.view.ContextThemeWrapper;
import androidx.core.content.res.ResourcesCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;
import com.poona.agrocart.app.DrawerLocker;
import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.home.model.ProductOld;
import com.poona.agrocart.ui.splash_screen.SplashScreenActivity;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.custom_alert.Alerter;
import com.poona.agrocart.widgets.toast.CustomToast;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okio.Buffer;

/**
 * Created by Rahul Dasi on 6/10/2020
 */

public abstract class BaseFragment extends Fragment implements DrawerLocker {
    private static final String TAG = BaseFragment.class.getSimpleName();
    public Context context;
    public AppSharedPreferences preferences;
    /**
     * Method checks Network Exceptions
     */
    private OnDialogRetryClickListener onDialogRetryClickListener;
    private LocationManager locationManager;
    private RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);


    public static String get_24_to_12_time(String temp_date) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat outputFormat = new SimpleDateFormat("dd MMM yyyy, hh:mm a");
        Date date = null;
        try {
            date = inputFormat.parse(temp_date);
        } catch (ParseException | java.text.ParseException e) {
            e.printStackTrace();
        }
        String date_time = outputFormat.format(date);
        return date_time;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;

        preferences = new AppSharedPreferences(context);
    }

    //Title and app logo on actionBar
    @SuppressLint("ResourceType")
    protected void initTitleBar(String title) {
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(0, 0, 0, 0);
        });
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.logImg.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setText(title);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setLayoutParams(layoutParams);
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setBackgroundResource(R.color.white);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setTextColor(Color.parseColor(context.getString(R.color.black)));
        setDrawerLocked(false);
    }

    @SuppressLint("ResourceType")
    protected void initTitleWithBackBtn(String title) {
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_back, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(0, 0, 0, 0);
        });
        ((HomeActivity) requireActivity()).setSupportActionBar(((HomeActivity) requireActivity()).binding.appBarHome.toolbar);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setVisibility(View.VISIBLE);
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.basketMenu.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.rlProductTag.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.tvAddress.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.logImg.setVisibility(View.GONE);
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setBackgroundResource(R.color.white);
        layoutParams.addRule(RelativeLayout.CENTER_HORIZONTAL);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setText(title);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setLayoutParams(layoutParams);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setTextColor(Color.parseColor(context.getString(R.color.black)));
        setDrawerLocked(true);
    }

    @SuppressLint("ResourceType")
    protected void initGreenTitleBar(String title) {
        initTitleBar(title);
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setBackgroundResource(R.color.colorPrimary);
        ((HomeActivity) requireActivity()).binding.appBarHome.textTitle.setTextColor(Color.parseColor(context.getString(R.color.white)));
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle_white, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
        });
    }

    public void loadingImage(Context context, String url, ImageView imageView) {
        try {
            Glide.with(context)
                    .load(url)
                    .placeholder(R.drawable.placeholder)
                    .error(R.drawable.background_bottom_navigation_screen).into(imageView);
        } catch (Exception e) {
            imageView.setImageResource(R.drawable.placeholder);
        }
    }

    protected void successToast(Context context, String message) {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    protected void errorToast(Context context, String message) {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.error(context, message, Toast.LENGTH_LONG, true).show();
    }

    protected void infoToast(Context context, String message) {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.info(context, message, Toast.LENGTH_LONG, true).show();
    }

    protected void warningToast(Context context, String message) {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.warning(context, message, Toast.LENGTH_LONG, true).show();
    }

    protected void showNotifyAlert(Activity activity, String dialogTitle, String dialogMessage, int icon) {
        Alerter.create(activity)
                .setTitle(dialogTitle)
                .setIcon(icon)
                .setDuration(5000)
                .setText(dialogMessage)
                .show();
    }

    protected void showConfirmPopup(Activity activity, String dialogMessage, CustomDialogInterface dialogInterface) {
        final boolean[] flag = new boolean[1];
        Dialog dialog = new Dialog(activity);
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;
        dialog.setContentView(R.layout.dialog_logout);

        CustomTextView tvTitle = dialog.findViewById(R.id.tv_heading);
        CustomButton btnYes = dialog.findViewById(R.id.btn_yes);
        CustomButton btnNo = dialog.findViewById(R.id.btn_no);
        btnNo.setOnClickListener(v -> {
            dialogInterface.onNoClick();
            dialog.dismiss();
        });
        btnYes.setOnClickListener(v -> {
            dialog.dismiss();
            dialogInterface.onYesClick();
        });
        tvTitle.setText(dialogMessage);

        dialog.show();
    }

    /*convert date format, we have to pass from & to date format*/
    public String formatDate(String date,
                             String initDateFormat,
                             String endDateFormat) throws java.text.ParseException {

        Date initDate = new SimpleDateFormat(initDateFormat).parse(date);
        SimpleDateFormat formatter = new SimpleDateFormat(endDateFormat);
        String parsedDate = formatter.format(initDate);

        return parsedDate;
    }

    protected String getCurrentDate() {
        SimpleDateFormat formDate = new SimpleDateFormat("dd-MM-yyyy");
        String strDate = formDate.format(new Date()); // option 2
        return strDate;
    }

    public void hideKeyBoard(Activity activity) {
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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


        // Get screen width and height in pixels
//        DisplayMetrics displayMetrics = new DisplayMetrics();
//        ((AppCompatActivity)context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
//        // The absolute width of the available display size in pixels.
//        int displayWidth = displayMetrics.widthPixels;
//        // The absolute height of the available display size in pixels.
//        int displayHeight = displayMetrics.heightPixels;
//
//        // Initialize a new window manager layout parameters
//        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();
//
//        // Copy the alert dialog window attributes to new layout parameter instance
//        layoutParams.copyFrom(dialog.getWindow().getAttributes());
//
//        // Set the alert dialog window width and height
//        // Set alert dialog width equal to screen width 90%
//        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
//        // Set alert dialog height equal to screen height 90%
//        // int dialogWindowHeight = (int) (displayHeight * 0.9f);
//
//        // Set alert dialog width equal to screen width 70%
//        int dialogWindowWidth = (int) (displayWidth * 1.0f);
//        // Set alert dialog height equal to screen height 70%
//        int dialogWindowHeight = (int) (displayHeight * 1.0f);
//
//        // Set the width and height for the layout parameters
//        // This will bet the width and height of alert dialog
//        layoutParams.width = dialogWindowWidth;
//        layoutParams.height = dialogWindowHeight;
//
//        // Apply the newly created layout parameters to the alert dialog window
//        dialog.getWindow().setAttributes(layoutParams);

        return dialog;
    }

    /**
     * Method checks for Internet connectivity
     *
     * @param _context
     * @return - true - If Internet is available
     * false - If Internet is not available
     */
    protected boolean isConnectingToInternet(Context _context) {
        boolean checkConnection = false;
        ConnectivityManager cm = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        if (activeNetwork != null) {
            // connected to the internet
            switch (activeNetwork.getType()) {
                case ConnectivityManager.TYPE_WIFI:
                case ConnectivityManager.TYPE_MOBILE:
                    // connected to mobile data
                    // connected to wifi
                    checkConnection = true;
                    break;
                default:
                    break;
            }
        } else {
            // not connected to the internet
            checkConnection = false;
        }
        return checkConnection;
    }

    protected void goToAskSignInSignUpScreen(String message, Context context) {
        goToAskSignInSignUpScreenDialog(message, context);
    }
    protected void goToAskSelectLocationScreen(String message, Context context) {
        goToAskSelectLocationDialog(message, context);
    }

    protected void goToAskAndDismiss(String message, Context context) {
        goToAskAndDismissDialog(message, context);
    }

    private void goToAskSignInSignUpScreenDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(new ContextThemeWrapper(context,
                R.style.CustomAlertDialog));

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_text_with_button, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        CustomTextView tvHeading = dialogView.findViewById(R.id.tv_heading);
        tvHeading.setText(message);
        // tvHeading.setText(message + "\n\n" + preferences.getAuthorizationToken());
        tvHeading.setTextIsSelectable(true);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        CustomButton customButton = dialogView.findViewById(R.id.bt_ok);

        customButton.setOnClickListener(v -> {
            dialog.dismiss();
            goToAskSignInSignUpScreen();
        });

        /*dialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                playClickSound();
            }
            return true;
        });*/

        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 100%
        //int dialogWindowHeight = (int) (displayHeight * 0.8f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        //layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
    }
    private void goToAskSelectLocationDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(new ContextThemeWrapper(context,
                R.style.CustomAlertDialog));

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_text_with_button, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        CustomTextView tvHeading = dialogView.findViewById(R.id.tv_heading);
        tvHeading.setText(message);
        // tvHeading.setText(message + "\n\n" + preferences.getAuthorizationToken());
        tvHeading.setTextIsSelectable(true);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        CustomButton customButton = dialogView.findViewById(R.id.bt_ok);

        customButton.setOnClickListener(v -> {
            dialog.dismiss();
            goToAskSelectLocationScreen();
        });

        /*dialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                playClickSound();
            }
            return true;
        });*/

        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 100%
        //int dialogWindowHeight = (int) (displayHeight * 0.8f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        //layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void goToAskAndDismissDialog(String message, Context context) {
        AlertDialog.Builder builder = new AlertDialog
                .Builder(new ContextThemeWrapper(context,
                R.style.CustomAlertDialog));

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_text_with_button, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        CustomTextView tvHeading = dialogView.findViewById(R.id.tv_heading);
        tvHeading.setText(message);
        // tvHeading.setText(message + "\n\n" + preferences.getAuthorizationToken());
        tvHeading.setTextIsSelectable(true);

        final AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        CustomButton customButton = dialogView.findViewById(R.id.bt_ok);

        customButton.setOnClickListener(v -> {
            dialog.dismiss();
        });

        /*dialog.setOnKeyListener((arg0, keyCode, event) -> {
            // TODO Auto-generated method stub
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog.dismiss();
                playClickSound();
            }
            return true;
        });*/

        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 0.8f);
        // Set alert dialog height equal to screen height 100%
        //int dialogWindowHeight = (int) (displayHeight * 0.8f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        //layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void goToAskSignInSignUpScreen() {
        preferences.clearSharedPreferences(context);
        preferences.setFromLogOut(true);

        Intent intent = new Intent(requireActivity(), SplashScreenActivity.class);
        intent.putExtra(FROM_SCREEN, LOGOUT);
        startActivity(intent);
        requireActivity().finish();
    }
    private void goToAskSelectLocationScreen() {
        Intent intent = new Intent(requireActivity(), SplashScreenActivity.class);
        intent.putExtra(FROM_SCREEN, LOGOUT);
        startActivity(intent);
        requireActivity().finish();
    }

    public void setOnItemClickListener(OnDialogRetryClickListener listener) {
        onDialogRetryClickListener = listener;
    }

    protected void showServerErrorDialog(String title, Fragment fragment, final OnDialogRetryClickListener listener, Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(context, R.style.StyleDataConfirmationDialog));

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_server_error, null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        CustomTextView tvHeading = dialogView.findViewById(R.id.tv_heading);
        CustomButton btRetry = dialogView.findViewById(R.id.bt_retry);
        btRetry.setVisibility(View.VISIBLE);

        tvHeading.setText(title);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        btRetry.setOnClickListener(v -> {
            dialog.dismiss();
            if (listener != null) {
                listener.onDialogRetryClick();
            }
        });

        dialog.setOnKeyListener((dialog1, keyCode, event) -> {
            if (keyCode == KeyEvent.KEYCODE_BACK) {
                dialog1.dismiss();
                NavHostFragment.findNavController(fragment).popBackStack();
            }
            return true;
        });

        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        requireActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        //int displayWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        //int displayHeight = Resources.getSystem().getDisplayMetrics().heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

        // Set the alert dialog window width and height
        // Set alert dialog width equal to screen width 90%
        // int dialogWindowWidth = (int) (displayWidth * 0.9f);
        // Set alert dialog height equal to screen height 90%
        // int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set alert dialog width equal to screen width 100%
        int dialogWindowWidth = (int) (displayWidth * 1.0f);
        // Set alert dialog height equal to screen height 100%
        int dialogWindowHeight = (int) (displayHeight * 1.0f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
    }

    protected boolean checkGpsStatus() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
    }

    // This method  converts String to RequestBody
    protected RequestBody toRequestBody(String value) {
        return RequestBody.create(value, MultipartBody.FORM);
    }

    protected String stringifyRequestBody(RequestBody request) {
        if (request.contentType() != null) {
            try {
                final RequestBody copy = request;
                final Buffer buffer = new Buffer();
                copy.writeTo(buffer);
                return buffer.readUtf8();
            } catch (final IOException e) {
                Log.w(TAG, "Failed to stringify request body: " + e.getMessage());
            }
        }
        return "";
    }

    /*animated expand view VISIBILITY VISIBLE*/
    public void expand(final View v) {
        int matchParentMeasureSpec = View.MeasureSpec.makeMeasureSpec(((View) v.getParent()).getWidth(), View.MeasureSpec.EXACTLY);
        int wrapContentMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
        v.measure(matchParentMeasureSpec, wrapContentMeasureSpec);
        final int targetHeight = v.getMeasuredHeight();

        // Older versions of android (pre API 21) cancel animations for views with a height of 0.
        v.getLayoutParams().height = 1;
        v.setVisibility(View.VISIBLE);
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                v.getLayoutParams().height = interpolatedTime == 1
                        ? ViewGroup.LayoutParams.WRAP_CONTENT
                        : (int) (targetHeight * interpolatedTime);
                v.requestLayout();
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Expansion speed of 1dp/ms
        a.setDuration((int) (targetHeight / v.getContext().getResources().getDisplayMetrics().density + 300));
        v.startAnimation(a);
    }

    /*animated collapse view VISIBILITY GONE*/
    public void collapse(final View v) {
        final int initialHeight = v.getMeasuredHeight();
        Animation a = new Animation() {
            @Override
            protected void applyTransformation(float interpolatedTime, android.view.animation.Transformation t) {
                super.applyTransformation(interpolatedTime, t);

                if (interpolatedTime == 1) {
                    v.setVisibility(View.GONE);
                } else {
                    v.getLayoutParams().height = initialHeight - (int) (initialHeight * interpolatedTime);
                    v.requestLayout();
                }
            }

            @Override
            public boolean willChangeBounds() {
                return true;
            }
        };

        // Collapse speed of 1dp/ms
        a.setDuration((int) (initialHeight / v.getContext().getResources().getDisplayMetrics().density + 300));
        v.startAnimation(a);
    }
    public interface OnDialogRetryClickListener {
        void onDialogRetryClick();
    }

    public static String getCompletedAddress(AddressesResponse.Address address){
        StringBuilder fullAddressSb = new StringBuilder();
        if (address.getHouseNo() != null && !TextUtils.isEmpty(address.getHouseNo()))
            fullAddressSb.append(address.getHouseNo() + ", ");
        if (address.getAppartmentName() != null && !TextUtils.isEmpty(address.getAppartmentName()))
            fullAddressSb.append(address.getAppartmentName() + ", ");
        if (address.getStreet() != null && !TextUtils.isEmpty(address.getStreet()))
            fullAddressSb.append(address.getStreet() + ", ");
        if (address.getLandmark() != null && !TextUtils.isEmpty(address.getLandmark()))
            fullAddressSb.append(address.getLandmark() + ", ");
        if (address.getAreaName() != null && !TextUtils.isEmpty(address.getAreaName()))
            fullAddressSb.append(address.getAreaName() + ", ");
        if (address.getCityName() != null && !TextUtils.isEmpty(address.getCityName()))
            fullAddressSb.append(address.getCityName() + ", ");
        if (address.getPincode() != null && !TextUtils.isEmpty(address.getPincode()))
            fullAddressSb.append(address.getPincode());
        address.setFullAddress(fullAddressSb.toString());
        return address.getFullAddress();
    }
    public static AddressesResponse.Address setCompletedAddress(AddressesResponse.Address address){
        StringBuilder fullAddressSb = new StringBuilder();
        if (address.getHouseNo() != null && !TextUtils.isEmpty(address.getHouseNo()))
            fullAddressSb.append(address.getHouseNo() + ", ");
        if (address.getAppartmentName() != null && !TextUtils.isEmpty(address.getAppartmentName()))
            fullAddressSb.append(address.getAppartmentName() + ", ");
        if (address.getStreet() != null && !TextUtils.isEmpty(address.getStreet()))
            fullAddressSb.append(address.getStreet() + ", ");
        if (address.getLandmark() != null && !TextUtils.isEmpty(address.getLandmark()))
            fullAddressSb.append(address.getLandmark() + ", ");
        if (address.getAreaName() != null && !TextUtils.isEmpty(address.getAreaName()))
            fullAddressSb.append(address.getAreaName() + ", ");
        if (address.getCityName() != null && !TextUtils.isEmpty(address.getCityName()))
            fullAddressSb.append(address.getCityName() + ", ");
        if (address.getPincode() != null && !TextUtils.isEmpty(address.getPincode()))
            fullAddressSb.append(address.getPincode());
        address.setFullAddress(fullAddressSb.toString());
        return address;
    }

    public void setDrawerLocked(boolean enabled){
        if(enabled){
            ((HomeActivity) requireActivity()).binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED);
        }else{
            ((HomeActivity) requireActivity()).binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED);
        }

    }
}