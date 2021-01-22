package com.veracity.ui;

import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.view.ContextThemeWrapper;

import com.google.android.play.core.appupdate.AppUpdateInfo;
import com.google.android.play.core.appupdate.AppUpdateManager;
import com.google.android.play.core.appupdate.AppUpdateManagerFactory;
import com.google.android.play.core.install.InstallStateUpdatedListener;
import com.google.android.play.core.install.model.AppUpdateType;
import com.google.android.play.core.install.model.InstallStatus;
import com.google.android.play.core.install.model.UpdateAvailability;
import com.google.android.play.core.tasks.Task;
import com.veracity.R;
import com.veracity.data.firebase.PushNotification;
import com.veracity.ui.splash_screen.SplashScreenActivity;
import com.veracity.widgets.CustomButton;
import com.veracity.widgets.CustomTextView;

import static com.veracity.app.AppConstants.FROM_SCREEN;
import static com.veracity.app.AppConstants.LOGOUT;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public abstract class BaseActivity extends AppCompatActivity
{
    private static final int REQ_CODE_VERSION_UPDATE = 530;
    private static String TAG = BaseActivity.class.getSimpleName();
    private AppUpdateManager appUpdateManager;
    private InstallStateUpdatedListener installStateUpdatedListener;

    protected PushNotification pushNotification;

    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        checkNewAppVersionState();
    }

    @Override
    public void onActivityResult(int requestCode, final int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        switch (requestCode) {

            case REQ_CODE_VERSION_UPDATE:
                if (resultCode != RESULT_OK) { //RESULT_OK / RESULT_CANCELED / RESULT_IN_APP_UPDATE_FAILED
                    System.out.println("Update flow failed! Result code: " + resultCode);
                    // If the update is cancelled or fails,
                    // you can request to start the update again.
                    unregisterInstallStateUpdListener();
                }

                break;
        }
    }

    @Override
    protected void onDestroy() {
        unregisterInstallStateUpdListener();
        super.onDestroy();
    }

    protected void checkForAppUpdate() {
        // Creates instance of the manager.
        appUpdateManager = AppUpdateManagerFactory.create(this);

        // Returns an intent object that you use to check for an update.
        Task<AppUpdateInfo> appUpdateInfoTask = appUpdateManager.getAppUpdateInfo();

        // Create a listener to track request state updates.
        installStateUpdatedListener = installState -> {
            // Show module progress, log state, or install the update.
            if (installState.installStatus() == InstallStatus.DOWNLOADED)
                // After the update is downloaded, show a notification
                // and request user confirmation to restart the app.
                popupForCompleteUpdateAndUnregister();
        };

        // Checks that the platform will allow the specified type of update.
        appUpdateInfoTask.addOnSuccessListener(appUpdateInfo -> {
            if (appUpdateInfo.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE) {
                // Request the update.
                if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.FLEXIBLE)) {

                    // Before starting an update, register a listener for updates.
                    appUpdateManager.registerListener(installStateUpdatedListener);
                    // Start an update.
                    startAppUpdateFlexible(appUpdateInfo);
                } else if (appUpdateInfo.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE) ) {
                    // Start an update.
                    startAppUpdateImmediate(appUpdateInfo);
                }
            }
        }).addOnFailureListener(Throwable::printStackTrace);
    }

    private void startAppUpdateImmediate(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.IMMEDIATE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    BaseActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
        }
    }

    private void startAppUpdateFlexible(AppUpdateInfo appUpdateInfo) {
        try {
            appUpdateManager.startUpdateFlowForResult(
                    appUpdateInfo,
                    AppUpdateType.FLEXIBLE,
                    // The current activity making the update request.
                    this,
                    // Include a request code to later monitor this update request.
                    BaseActivity.REQ_CODE_VERSION_UPDATE);
        } catch (IntentSender.SendIntentException e) {
            e.printStackTrace();
            unregisterInstallStateUpdListener();
        }
    }

    /**
     * Displays the snackbar notification and call to action.
     * Needed only for Flexible app update
     */
    protected void popupForCompleteUpdateAndUnregister()
    {
        /*Snackbar snackbar = Snackbar.make(coordinatorLayoutMain, getString(R.string.update_downloaded), Snackbar.LENGTH_INDEFINITE);
        snackbar.setAction(R.string.restart, (View.OnClickListener) view -> appUpdateManager.completeUpdate());
        snackbar.setActionTextColor(ContextCompat.getColor(this, R.color.colorRed));
        snackbar.show();*/

        showUpdateSuccessPopup();
        unregisterInstallStateUpdListener();
    }

    /**
     * Checks that the update is not stalled during 'onResume()'.
     * However, you should execute this check at all app entry points.
     */
    private void checkNewAppVersionState() {
        appUpdateManager
                .getAppUpdateInfo()
                .addOnSuccessListener(
                        appUpdateInfo -> {
                            //FLEXIBLE:
                            // If the update is downloaded but not installed,
                            // notify the user to complete the update.
                            if (appUpdateInfo.installStatus() == InstallStatus.DOWNLOADED) {
                                popupForCompleteUpdateAndUnregister();
                            }

                            //IMMEDIATE:
                            if (appUpdateInfo.updateAvailability()
                                    == UpdateAvailability.DEVELOPER_TRIGGERED_UPDATE_IN_PROGRESS) {
                                // If an in-app update is already running, resume the update.
                                startAppUpdateImmediate(appUpdateInfo);
                            }
                        })
                .addOnFailureListener(Throwable::printStackTrace);
    }

    /**
     * Needed only for FLEXIBLE update
     */
    private void unregisterInstallStateUpdListener() {
        if (appUpdateManager != null && installStateUpdatedListener != null)
            appUpdateManager.unregisterListener(installStateUpdatedListener);
    }

    private void showUpdateSuccessPopup()
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.StyleDataConfirmationDialog));

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_logout,null);

        builder.setView(dialogView);
        builder.setCancelable(false);

        CustomTextView
                tvHeading = dialogView.findViewById(R.id.tv_heading);
        CustomButton customButtonYes = dialogView.findViewById(R.id.btn_yes);
        CustomButton customButtonNo = dialogView.findViewById(R.id.btn_no);

        tvHeading.setText(R.string.update_downloaded);
        customButtonYes.setText(R.string.restart);

        customButtonNo.setVisibility(View.GONE);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        customButtonYes.setOnClickListener(v -> {
            dialog.dismiss();
            appUpdateManager.completeUpdate();
            //doRestart();
        });

        customButtonNo.setOnClickListener(v -> dialog.dismiss());

        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
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
        //int dialogWindowHeight = (int) (displayHeight * 0.9f);

        // Set the width and height for the layout parameters
        // This will bet the width and height of alert dialog
        layoutParams.width = dialogWindowWidth;
        //layoutParams.height = dialogWindowHeight;

        // Apply the newly created layout parameters to the alert dialog window
        dialog.getWindow().setAttributes(layoutParams);
    }

    private void doRestart() {
        Intent intent = new Intent(this, SplashScreenActivity.class);
        intent.putExtra(FROM_SCREEN, LOGOUT);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        this.finish();
    }
}