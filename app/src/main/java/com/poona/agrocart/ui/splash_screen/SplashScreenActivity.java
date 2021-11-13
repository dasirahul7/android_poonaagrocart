package com.poona.agrocart.ui.splash_screen;

import android.os.Build;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;

import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.content.ContextCompat;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.BaseActivity;

import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LOGOUT;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class SplashScreenActivity extends BaseActivity
{
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private CoordinatorLayout coordinatorLayoutMain;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(ContextCompat.getColor(this, R.color.colorPrimary));
        }

        if (getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString(FROM_SCREEN) != null
                    && getIntent().getExtras().getString(FROM_SCREEN).equals(LOGOUT)/*
                    || getIntent().getExtras().getString(FROM_SCREEN).equals(PUSH_NOTIFICATIONS)*/)
            {

            }
        }

        coordinatorLayoutMain = findViewById(R.id.coordinatorLayout_main);

        /*check in app update*/
        checkForAppUpdate();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}