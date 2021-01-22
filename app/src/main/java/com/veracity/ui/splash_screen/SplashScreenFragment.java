package com.veracity.ui.splash_screen;

import android.app.Activity;
import android.content.Context;
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
import androidx.databinding.ViewDataBinding;
import androidx.navigation.fragment.NavHostFragment;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.installations.FirebaseInstallations;
import com.google.firebase.installations.InstallationTokenResult;
import com.google.firebase.messaging.FirebaseMessaging;
import com.veracity.R;
import com.veracity.databinding.FragmentSplashScreenBinding;
import com.veracity.ui.BaseFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class SplashScreenFragment extends BaseFragment implements View.OnClickListener
{
    private int SPLASH_TIME_OUT = 3000;

    private static final String TAG = SplashScreenFragment.class.getName();

    //private DotProgressBar progressbar;

    private Context context;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        this.context = context;
    }

    private FragmentSplashScreenBinding fragmentSplashScreenBinding;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState)
    {
        fragmentSplashScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_splash_screen, container, false);
        fragmentSplashScreenBinding.setLifecycleOwner(this);

        final View view = ((ViewDataBinding) fragmentSplashScreenBinding).getRoot();

        //if(preferences.getFromLogOut())
        //{
            //preferences.setFromLogOut(false);
            //SPLASH_TIME_OUT = 300;
        //}

        initView(view);

        return view;
    }

    private void initView(View view)
    {
        //progressbar = view.findViewById(R.id.progressbar_splashActivity);

        fragmentSplashScreenBinding.btnRetry.setOnClickListener(this);
        fragmentSplashScreenBinding.btnCancel.setOnClickListener(this);

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(600);
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(Animation.INFINITE);

        FirebaseApp.initializeApp(context);

        //progressbar.setVisibility(View.VISIBLE);

        startSplash();
    }

    private void startSplash()
    {
        getFirebaseToken();

        new Handler().postDelayed(() ->
        {
            if (isConnectingToInternet(context))
            {
                //progressbar.setVisibility(View.VISIBLE);

//                if (preferences.getIsLoggedIn())
//                {
//                    Intent intent = new Intent(context, DashboardActivity.class);
//                    startActivity(intent);
//                    (requireActivity()).finish();
//                    (requireActivity()).overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
//                }
//                else
//                {
                    NavHostFragment.findNavController(SplashScreenFragment.this).navigate(R.id.action_SplashScreenFragment_to_LoginFragment);
//                }
            }
            else
            {
                //progressbar.setVisibility(View.INVISIBLE);
                fragmentSplashScreenBinding.linearLayoutNoInternet.setVisibility(View.VISIBLE);
            }
        }, SPLASH_TIME_OUT);
    }

    private void getFirebaseToken()
    {
        /*option 1*/
        FirebaseInstallations.getInstance().getToken(true).addOnCompleteListener(new OnCompleteListener<InstallationTokenResult>() {
            @Override
            public void onComplete(@NonNull Task<InstallationTokenResult> task) {
                if (!task.isSuccessful())
                {
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
                if(activity != null && isAdded()) {
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
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.btnRetry:
                //progressbar.setVisibility(View.VISIBLE);
                fragmentSplashScreenBinding.linearLayoutNoInternet.setVisibility(View.GONE);
                Handler handler = new Handler();
                handler.postDelayed(new Runnable()
                {
                    public void run()
                    {
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