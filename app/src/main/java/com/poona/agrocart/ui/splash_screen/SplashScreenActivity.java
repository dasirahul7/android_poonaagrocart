package com.poona.agrocart.ui.splash_screen;

import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.LOGOUT;

import android.animation.ObjectAnimator;
import android.animation.StateListAnimator;
import android.os.Build;
import android.os.Bundle;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import com.google.android.material.appbar.AppBarLayout;
import com.poona.agrocart.R;
import com.poona.agrocart.ui.BaseActivity;
import com.poona.agrocart.widgets.CustomTextView;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class SplashScreenActivity extends BaseActivity
{
    private static final String TAG = SplashScreenActivity.class.getSimpleName();

    private CoordinatorLayout coordinatorLayoutMain;


    Toolbar toolbar;
    public static ImageView ivBack;
    public static CustomTextView title;
    private AppBarLayout appBarLayout;





    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
//        }



        if (getIntent().getExtras() != null)
        {
            if(getIntent().getExtras().getString(FROM_SCREEN) != null
                    && getIntent().getExtras().getString(FROM_SCREEN).equals(LOGOUT)/*
                    || getIntent().getExtras().getString(FROM_SCREEN).equals(PUSH_NOTIFICATIONS)*/)
            {

            }
        }


        findCompo();
        coordinatorLayoutMain = findViewById(R.id.coordinatorLayout_main);

        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().setElevation(0);
        getSupportActionBar().hide();


        //to remove shadows of the app bar
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            StateListAnimator stateListAnimator = new StateListAnimator();
            stateListAnimator.addState(new int[0], ObjectAnimator.ofFloat(appBarLayout, "elevation", 0.1f));
            appBarLayout.setStateListAnimator(stateListAnimator);
        }




        //check in app update
        checkForAppUpdate();
    }


    private void findCompo()
    {
        ivBack=findViewById(R.id.iv_back);
        title=findViewById(R.id.tv_title);
        toolbar=findViewById(R.id.toolbar_login);
        appBarLayout=findViewById(R.id.app_bar_layout);

    }


    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}