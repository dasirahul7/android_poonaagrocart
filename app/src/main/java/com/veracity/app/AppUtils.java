package com.veracity.app;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import androidx.appcompat.view.ContextThemeWrapper;

import com.veracity.R;
import com.veracity.widgets.custom_alert.Alerter;
import com.veracity.widgets.toast.CustomToast;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class AppUtils {

    public static boolean isConnectingToInternet(Context _context) {
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

    public static ProgressDialog showCircleProgressDialog(Context context, String message)
    {
        ProgressDialog dialog = new ProgressDialog(new ContextThemeWrapper(context, R.style.CustomProgressDialog));
        try
        {
            dialog.show();
        }
        catch (WindowManager.BadTokenException e)
        {

        }
        dialog.setCancelable(false);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.setContentView(R.layout.circular_progresbar);

        return dialog;
    }

    public static void successToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.success(context, message, Toast.LENGTH_SHORT, true).show();
    }

    public static void errorToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.error(context, message, Toast.LENGTH_LONG, true).show();
    }

    public static void infoToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.info(context, message, Toast.LENGTH_LONG, true).show();
    }

    public static void warningToast(Context context, String message)
    {
        CustomToast.Config.getInstance()
                .setToastTypeface(Typeface.createFromAsset(context.getAssets(), "fonts/poppins/poppins_regular.ttf"))
                .apply();
        CustomToast.warning(context, message, Toast.LENGTH_LONG, true).show();
    }

    public static void showNotifyAlert(Activity activity, String dialogTitle, String dialogMessage, int icon) {
        Alerter.create(activity)
                .setTitle(dialogTitle)
                .setIcon(icon)
                .setDuration(5000)
                .setText(dialogMessage)
                .show();
    }

    public static void hideKeyBoard(Activity activity)
    {
        View view = activity.getCurrentFocus();
        if (view != null)
        {
            InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }
}