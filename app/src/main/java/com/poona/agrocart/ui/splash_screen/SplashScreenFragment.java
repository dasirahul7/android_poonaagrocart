package com.poona.agrocart.ui.splash_screen;

import static com.poona.agrocart.app.AppConstants.COUNTRY_CODE;
import static com.poona.agrocart.app.AppConstants.FCM_TOKEN;
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
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
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
        try {
            loadingImage(context,getString(R.string.splash_gif),fragmentSplashScreenBinding.ivBackgroundMain);
        } catch (Exception exception) {
            exception.printStackTrace();
        }
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
                if (!preferences.getUserAddress().isEmpty()&&preferences.getUserAddress()!=null) {
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
/*
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
                Log.e(TAG, "onComplete: "+ token);
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
*/

        FirebaseMessaging.getInstance().getToken()
                .addOnCompleteListener(new OnCompleteListener<String>() {
                    @Override
                    public void onComplete(@NonNull Task<String> task) {
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "Fetching FCM registration token failed", task.getException());
                            return;
                        }

                        // Get new FCM registration token
                        String token = task.getResult();
                        preferences.setFCMToken(token);

                        // Log and toast
                        String msg = getString(R.string.msg_token_fmt, token);
                        Log.d(TAG, msg);
//                        Toast.makeText(S.this, msg, Toast.LENGTH_SHORT).show();
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

    /*Splash Screen API here*/
//    private void callSplashScreenAPI() {
//        ApiClientAuth.getClient(this.context )
//                .create(ApiInterface.class)
//                .getSplashScreenResponse()
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribeWith(new DisposableSingleObserver<SplashResponse>() {
//                    @Override
//                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull SplashResponse splashResponse) {
//                        if (splashResponse!=null){
//                            try {
//                                if (splashResponse.getSplashData().getGifImage()!=null
//                                        && !splashResponse.getSplashData().getGifImage().equals(""))
//                                    loadingImage(context,splashResponse.getSplashData().getGifImage(),fragmentSplashScreenBinding.ivBackgroundMain);
//                                if (splashResponse.getSplashData().getLogoImage()!=null
//                                        && !splashResponse.getSplashData().getLogoImage().equals(""))
//                                    loadingImage(context,splashResponse.getSplashData().getLogoImage(),fragmentSplashScreenBinding.ivLogo);
//                                fragmentSplashScreenBinding.linearLayoutCompat.setVisibility(View.VISIBLE);
//                            } catch (NullPointerException exception) {
//                                exception.printStackTrace();
//                            }
//                        }
//                    }
//
//                    @Override
//                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
//                        Gson gson = new GsonBuilder().create();
//                        SplashResponse splashResponse = new SplashResponse();
//                        try {
//                            splashResponse = gson.fromJson(((HttpException) e).response().errorBody().string(),
//                                    SplashResponse.class);
//                        } catch (Exception exception) {
//                            Log.e(TAG, exception.getMessage());
//                        }
//                        infoToast(context,splashResponse.getMessage());
//                        fragmentSplashScreenBinding.linearLayoutCompat.setVisibility(View.VISIBLE);
//                    }
//                });
//    }
}