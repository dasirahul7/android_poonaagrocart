package com.poona.agrocart.ui.home;

import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.ActivityHomeBinding;
import com.poona.agrocart.ui.BaseActivity;

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
        backBtn = binding.appBarHome.backImg;
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
        binding.appBarHome.backImg.setVisibility(View.GONE);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    private void initNavigation() {
        drawer = binding.drawerLayout;
        navigationView = binding.navView;
        bottomNavigationView=binding.appBarHome.bottomNavigationView;

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home,
                R.id.nav_profile,
                R.id.nav_orders,
                R.id.nav_explore,
                R.id.nav_cart,
                R.id.nav_favourite,
                R.id.nav_profile,
                R.id.nav_store,
                R.id.nav_basket,
                R.id.nav_address)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView,navController);

        binding.appBarHome.toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!HomeActivity.this.binding.drawerLayout.isDrawerOpen((int) GravityCompat.START)) {
                    HomeActivity.this.binding.drawerLayout.openDrawer((int) GravityCompat.START);

                } else {
                    HomeActivity.this.binding.drawerLayout.closeDrawer((View) binding.drawerLayout);
                }
            }
        });
    }

    private void setCustomDrawerIconInFragments() {
        binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_poona_agrocart_logo_final, null);
            binding.appBarHome.toolbar.setNavigationIcon(d);
            binding.appBarHome.toolbar.setPadding(30, 0, 0, 0);
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
}