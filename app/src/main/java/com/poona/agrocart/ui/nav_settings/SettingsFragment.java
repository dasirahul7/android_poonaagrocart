package com.poona.agrocart.ui.nav_settings;

import static com.poona.agrocart.app.AppConstants.APP_NOTIFICATION_STATUS;
import static com.poona.agrocart.app.AppConstants.EMAIL_NOTIFICATION_STATUS;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.appcompat.widget.SwitchCompat;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.settingResponse.UpdateConfigurationResponse;
import com.poona.agrocart.data.network.responses.settingResponse.ViewConfigurationResponse;
import com.poona.agrocart.databinding.FragmentSettingsBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.HashMap;

public class SettingsFragment extends BaseFragment
{
    private FragmentSettingsBinding fragmentSettingsBinding;
    private SettingViewModel settingViewModel;
    private View view;
    private SwitchCompat emailNotification, appNotification;
    private Integer strAppNotification , strEmailNotification;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentSettingsBinding= DataBindingUtil.inflate(inflater,R.layout.fragment_settings, container, false);
        fragmentSettingsBinding.setLifecycleOwner(this);
         settingViewModel=new ViewModelProvider(this).get(SettingViewModel.class);
         fragmentSettingsBinding.setSettingViewModel(settingViewModel);
         view = fragmentSettingsBinding.getRoot();

        initTitleBar(getString(R.string.settings));

        initView();
        setClickListener();

        if (isConnectingToInternet(context)) {
           // callUpdatedNotificationApi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        return view;
    }

    private void setClickListener() {
        appNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Toast.makeText(context, ""+strAppNotification, Toast.LENGTH_SHORT).show();
                if (compoundButton.getId() == R.id.sc_app_notification) {
                    if (isChecked){
                        strAppNotification = 1;
                        callNotificationUpdate(showCircleProgressDialog(context, ""));
                    } else {
                        strAppNotification = 0;
                        callNotificationUpdate(showCircleProgressDialog(context, ""));
                    }
                }

            }
        });

        emailNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                Toast.makeText(context, ""+strEmailNotification, Toast.LENGTH_SHORT).show();

                if (compoundButton.getId() == R.id.sc_email_notification) {
                    if (isChecked){
                        strEmailNotification = 1;
                        callNotificationUpdate(showCircleProgressDialog(context, ""));
                    } else {
                        strEmailNotification = 0;
                        callNotificationUpdate(showCircleProgressDialog(context, ""));
                    }
                }
            }
        });

    }

    private void initView() {
        appNotification = fragmentSettingsBinding.scAppNotification;
        emailNotification = fragmentSettingsBinding.scEmailNotification;
    }

    private void callUpdatedNotificationApi(ProgressDialog progressDialog){
        Observer<ViewConfigurationResponse> viewConfigurationResponseObserver = viewConfigurationResponse -> {
            if (viewConfigurationResponse != null){
                Log.e("UpdatedNotification Api ResponseData", new Gson().toJson(viewConfigurationResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (viewConfigurationResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if(viewConfigurationResponse.getData() != null){
                            Integer strEmail = Integer.valueOf(viewConfigurationResponse.getData().get(0).getEmailNotificationStatus());
                            Integer strApp = Integer.valueOf(viewConfigurationResponse.getData().get(0).getAppNotificationStatus());

                            if(strEmail.equals(1)){
                                strEmailNotification = 1;
                                emailNotification.setChecked(true);
                            }else {
                                strEmailNotification = 0;
                                emailNotification.setChecked(false);
                            }

                            if(strApp.equals(1)){
                                strAppNotification = 1;
                                appNotification.setChecked(true);
                            }else {
                                strAppNotification = 0;
                                appNotification.setChecked(false);
                            }

                            /*String strAndroid = viewConfigurationResponse.getData().get(0).getConfigurationName();
                            if(strAndroid.equalsIgnoreCase("Android App Version")) {
                                settingViewModel.appVersion.setValue(viewConfigurationResponse.getData().get(0).getConfigurationName());
                                settingViewModel.version.setValue(viewConfigurationResponse.getData().get(0).getConfigurationValue());
                            }*/
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, viewConfigurationResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, viewConfigurationResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, viewConfigurationResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, viewConfigurationResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };
        settingViewModel.getUpdatedNotification(progressDialog, context, SettingsFragment.this)
                .observe(getViewLifecycleOwner(), viewConfigurationResponseObserver);
    }

    private void callNotificationUpdate (ProgressDialog progressDialog){
        Observer<UpdateConfigurationResponse> updateConfigurationResponseObserver = updateConfigurationResponse -> {

            if (updateConfigurationResponse != null){
                Log.e("callNotificationUpdate Api ResponseData", new Gson().toJson(updateConfigurationResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (updateConfigurationResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        callUpdatedNotificationApi(showCircleProgressDialog(context, ""));
                        successToast(context, updateConfigurationResponse.getMessage());
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, updateConfigurationResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        errorToast(context, updateConfigurationResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, updateConfigurationResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, updateConfigurationResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }

        };
        settingViewModel.getNotificationUpdate(progressDialog, context, NotificationInputParameter(), SettingsFragment.this)
                .observe(getViewLifecycleOwner(), updateConfigurationResponseObserver);
    }

    private HashMap<String, Integer> NotificationInputParameter() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(EMAIL_NOTIFICATION_STATUS, strEmailNotification);
        map.put(APP_NOTIFICATION_STATUS, strAppNotification);

        return map;
    }
}