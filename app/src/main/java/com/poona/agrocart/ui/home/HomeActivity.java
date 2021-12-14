package com.poona.agrocart.ui.home;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.navigation.NavigationView;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.ActivityHomeBinding;
import com.poona.agrocart.ui.BaseActivity;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.Objects;

public class HomeActivity extends BaseActivity {

    private AppBarConfiguration mAppBarConfiguration;
    public ActivityHomeBinding binding;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    public Toolbar toolbar;
    public ImageView backBtn;
    private ActionBarDrawerToggle actionBarDrawerToggle;
    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = binding.appBarHome.toolbar;
//        backBtn = binding.appBarHome.backImg;
        initToolbar();
        initNavigation();
        setCustomDrawerIconInFragments();
        checkForAppUpdate();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private void initToolbar() {
        setSupportActionBar(binding.appBarHome.toolbar);
//        binding.appBarHome.backImg.setVisibility(View.GONE);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    private void initNavigation() {
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(ContextCompat.getColor(this, R.color.white));
//        }
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        bottomNavigationView = binding.appBarHome.bottomNavigationView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_orders,
                R.id.nav_address,
                R.id.nav_basket,
                R.id.nav_wallet,
                R.id.nav_offer,
                R.id.nav_notification,
                R.id.nav_about,
                R.id.nav_help_center,
                R.id.nav_privacy,
                R.id.nav_terms,
                R.id.nav_setting,
                R.id.nav_store,
                R.id.nav_signout,
                //Bottom New Menus
                R.id.nav_explore,
                R.id.nav_cart,
                R.id.nav_favourite
        )
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        binding.appBarHome.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!HomeActivity.this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                    HomeActivity.this.binding.drawerLayout.openDrawer(GravityCompat.START);
                } else {
                    HomeActivity.this.binding.drawerLayout.closeDrawer(binding.drawerLayout);
                }
            }
        });

        View headerView = navigationView.getHeaderView(0);
        CustomTextView tvUserName = headerView.findViewById(R.id.tv_user_name);
        ImageView editImg = headerView.findViewById(R.id.edit_img);
        tvUserName.setText("Hello ! Ranju");
        editImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawer.closeDrawer(GravityCompat.START);
                navController.navigate(R.id.nav_profile);
            }
        });

    }

    private void setCustomDrawerIconInFragments() {
        binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle, null);
            binding.appBarHome.toolbar.setNavigationIcon(d);
            binding.appBarHome.toolbar.setPadding(10, 0, 0, 0);
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.nav_home:
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }


    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            if (binding.appBarHome.basketMenu.getVisibility() == View.VISIBLE)
                binding.appBarHome.basketMenu.setVisibility(View.GONE);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}