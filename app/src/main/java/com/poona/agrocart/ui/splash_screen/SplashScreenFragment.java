package com.poona.agrocart.ui.splash_screen;

import static com.poona.agrocart.app.AppConstants.COUNTRY_CODE;
import static com.poona.agrocart.app.AppConstants.USER_ID;
import static com.poona.agrocart.app.AppConstants.USER_MOBILE;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentSplashScreenBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
@SuppressLint("CustomSplashScreen")
public class SplashScreenFragment extends BaseFragment implements View.OnClickListener {
    private static final String TAG = SplashScreenFragment.class.getName();
    private final int SPLASH_TIME_OUT = 3000;
    private Context context;
    private FragmentSplashScreenBinding fragmentSplashScreenBinding;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentSplashScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false);
        fragmentSplashScreenBinding.setLifecycleOwner(this);

        final View view = fragmentSplashScreenBinding.getRoot();

        //if(preferences.getFromLogOut())
        //{
        //preferences.setFromLogOut(false);
        //SPLASH_TIME_OUT = 300;
        //}

        initView(view);

        return view;
    }

    private void initView(View view) {
        fragmentSplashScreenBinding.btnRetry.setOnClickListener(this);
        fragmentSplashScreenBinding.btnCancel.setOnClickListener(this);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(600);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        FirebaseApp.initializeApp(context);

        startSplash();
    }

    private void startSplash() {
        getFirebaseToken();
        new Handler().postDelayed(() -> {
            if (isConnectingToInternet(context)) {
//                if (preferences.getIsLoggedIn())
//                {
//                    Intent intent = new Intent(context, DashboardActivity.class);
//                    startActivity(intent);
//                    (requireActivity()).finish();
//                    (requireActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                }
//                else
//                {
                //NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_SplashScreenFragment_to_vegetablesFragment);
                if (preferences.getIsLoggedIn()) {
                    startDashBoard();
                } else if (preferences.getIsIntroRead())
                    checkVerified();
                else
                    NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_SplashScreenFragment_to_introScreenFragment);
            } else {
                fragmentSplashScreenBinding.linearLayoutNoInternet.setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);
    }

    private void checkVerified() {
        if (preferences.isVerified()) {
            if (!preferences.getUid().isEmpty()) {
                if (!preferences.getUserAddress().isEmpty()) {
                    startDashBoard();
                } else
                    NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_SplashScreenFragment_to_selectLocationFragment);
            } else {
                Bundle bundle = new Bundle();
                bundle.putString(USER_ID, preferences.getUid());
                bundle.putString(USER_MOBILE, preferences.getUserMobile());
                bundle.putString(COUNTRY_CODE, preferences.getUserCountry());
                NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_SplashScreenFragment_to_signUpFragment, bundle);
            }
        } else {
            NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_SplashScreenFragment_to_signInFragment);
        }
    }

    private void startDashBoard() {
        Intent intent = new Intent(requireActivity(), HomeActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        requireActivity().finish();
        startActivity(intent);
    }

    private void getFirebaseToken() {
        /*option 1*/
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                if (!task.isSuccessful()) {
                    Log.w(TAG, "getInstanceId failed", task.getException());
                    return;
                }

                // Get new Instance ID token
                String token = task.getResult().getToken();

                //showInfoToast(SplashScreenActivity.this, token);

                preferences.setFCMToken(token);

                // Log and toast
                Activity activity = getActivity();
                String msg = "";
                if (activity != null && isAdded()) {
                    msg = getString(R.string.msg_token_fmt, token);
                }
                Log.d(TAG, msg);
            }
        });

        /*option 2*/
        /*FirebaseMessaging.getInstance().getToken().addOnSuccessListener(token -> {
            // Get new Instance ID token

            //AppUtils.showInfoToast(SplashScreenActivity.this, token);

            preferences.setFCMToken(token);

            // Log and toast
            Activity activity = getActivity();
            String msg = "";
            if(activity != null && isAdded()) {
                msg = getString(R.string.msg_token_fmt, token);
            }
            Log.d(TAG, msg);
        });*/
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRetry:
                //progressbar.setVisibility(View.VISIBLE);
                fragmentSplashScreenBinding.linearLayoutNoInternet.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    public void run() {
                        startSplash();
                    }
                }, 300);
                break;
            case R.id.btnCancel:
                getActivity().finish();
                getActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                break;
        }
    }
}