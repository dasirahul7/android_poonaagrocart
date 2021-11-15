package com.poona.agrocart.ui.splash_screen;

import android.app.ActionBar;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.widget.Toolbar;
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


    Toolbar toolbar;




   /* //handling click events of action bar
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Take appropriate action for each action item click
        switch (item.getItemId()) {
            case R.id.item_action_back:
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    //inflating menu for action bar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_login_toolbar, menu);
        return super.onCreateOptionsMenu(menu);
    }*/




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

        toolbar=findViewById(R.id.toolbar_login);
        toolbar.setTitle("");
        setSupportActionBar(toolbar);

        getSupportActionBar().hide();




        // get action bar
        /*ActionBar actionBar = getActionBar();

        // Enabling Up / Back navigation
        //actionBar.setDisplayHomeAsUpEnabled(true);*/


        //check in app update
        checkForAppUpdate();
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        onBackPressed();
        return true;
    }
}