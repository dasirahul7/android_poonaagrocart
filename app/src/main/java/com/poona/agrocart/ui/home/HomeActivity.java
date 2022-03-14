package com.poona.agrocart.ui.home;

import static com.poona.agrocart.app.AppConstants.CMS_TYPE;
import static com.poona.agrocart.app.AppConstants.CUSTOMER_ID;
import static com.poona.agrocart.app.AppConstants.FROM_SCREEN;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_403;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;
import static com.poona.agrocart.app.AppConstants.USER_ID;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomnavigation.BottomNavigationItemView;
import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.ApiClientAuth;
import com.poona.agrocart.data.network.ApiInterface;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.ProfileResponse;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.ActivityHomeBinding;
import com.poona.agrocart.ui.BaseActivity;
import com.poona.agrocart.ui.sign_in.SignInViewModel;
import com.poona.agrocart.ui.splash_screen.SplashScreenActivity;
import com.poona.agrocart.widgets.CustomButton;
import com.poona.agrocart.widgets.CustomTextView;
import com.poona.agrocart.widgets.imageview.CircularImageView;

import java.util.HashMap;
import java.util.Objects;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.observers.DisposableSingleObserver;
import io.reactivex.rxjava3.schedulers.Schedulers;


public class HomeActivity extends BaseActivity {

    private static final String TAG = HomeActivity.class.getSimpleName();
    public ActivityHomeBinding binding;
    public Toolbar toolbar;
    public ImageView backBtn;
    public CustomTextView tvUserName;
    public CircularImageView civProfilePhoto;
    private AppBarConfiguration mAppBarConfiguration;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private NavController navController;
    private BottomNavigationView bottomNavigationView;
    private AppSharedPreferences preferences;
    private TextView textCartItemCount;
    private int mCartItemCount;
    private Menu menu;
    private View menuCartView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferences = new AppSharedPreferences(HomeActivity.this);
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        toolbar = binding.appBarHome.toolbar;
//        backBtn = binding.appBarHome.backImg;
        initToolbar();
//        setUserProfile(showCircleProgressDialog(HomeActivity.this,""),profileParam(preferences.getUid()));
        initNavigation();
        setCustomDrawerIconInFragments();
        checkForAppUpdate();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    private HashMap<String, String> profileParam(String userId) {
        HashMap<String, String> map = new HashMap<>();
        map.put(CUSTOMER_ID, userId);
        return map;
    }

    private void setUserProfile(ProgressDialog progressDialog, HashMap<String, String> hashMap) {
        ApiClientAuth.getClient(HomeActivity.this)
                .create(ApiInterface.class)
                .getViewProfileResponse(hashMap)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableSingleObserver<ProfileResponse>() {
                    @Override
                    public void onSuccess(@io.reactivex.rxjava3.annotations.NonNull ProfileResponse profileResponse) {
                        if (profileResponse != null) {
                            if (progressDialog != null)
                                progressDialog.dismiss();
                            Log.e(TAG, "onSuccess: " + new Gson().toJson(profileResponse));
                            preferences.setUserProfile(profileResponse.getProfile().getImage());
                            preferences.setUserName(profileResponse.getProfile().getName());
                        }
                    }

                    @Override
                    public void onError(@io.reactivex.rxjava3.annotations.NonNull Throwable e) {
                        progressDialog.dismiss();
                        Gson gson = new GsonBuilder().create();
                        ProfileResponse response = new ProfileResponse();
                        Log.e(TAG, "onError: " + new Gson().toJson(response));
                    }
                });

    }

    private void initToolbar() {
        setSupportActionBar(binding.appBarHome.toolbar);
        Objects.requireNonNull(getSupportActionBar()).setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
    }


    private void initNavigation() {
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
                R.id.nav_faq,
                R.id.nav_help_center,
                R.id.nav_privacy,
                R.id.nav_setting,
                R.id.nav_store,
                R.id.nav_signout,
                //Bottom Menus
                R.id.nav_explore,
                R.id.nav_cart,
                R.id.nav_favourite
        ).setOpenableLayout(drawer).build();

        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_home);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);

        binding.appBarHome.toolbar.setNavigationOnClickListener(v -> {
            if (!HomeActivity.this.binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
                HomeActivity.this.binding.drawerLayout.openDrawer(GravityCompat.START);
            } else {
                HomeActivity.this.binding.drawerLayout.closeDrawer(binding.drawerLayout);
            }
        });

        /*add counter budge start here*/

        BottomNavigationMenuView mbottomNavigationMenuView =
                (BottomNavigationMenuView) bottomNavigationView.getChildAt(0);

        View view = mbottomNavigationMenuView.getChildAt(2);

        BottomNavigationItemView itemView = (BottomNavigationItemView) view;

        View cart_badge = LayoutInflater.from(this)
                .inflate(R.layout.action_layout_my_cart,
                        mbottomNavigationMenuView, false);

        textCartItemCount = (TextView) cart_badge.findViewById(R.id.cart_badge);
        itemView.addView(cart_badge);
        /*add counter budge finish here*/

        Menu m = navigationView.getMenu();
        MenuItem menuItemAboutUs = m.findItem(R.id.nav_cms);
        MenuItem signOut = m.findItem(R.id.nav_signout);

//        menuItemAboutUs.setOnMenuItemClickListener(menuItem -> {
//            redirectToCmsFragment(0);
//            return false;
//        });

        signOut.setOnMenuItemClickListener(item -> {
            drawer.closeDrawer(GravityCompat.START);
            showLogoutConfirmationDialog();
            return true;
        });
//        Menu mBottom = navigationView.getMenu();


        View headerView = navigationView.getHeaderView(0);
        RelativeLayout rlEditProfile = headerView.findViewById(R.id.rl_edit_profile);
        tvUserName = headerView.findViewById(R.id.tv_user_name);
        ImageView editImg = headerView.findViewById(R.id.edit_img);
        civProfilePhoto = headerView.findViewById(R.id.civ_profile_photo);
        Glide.with(HomeActivity.this)
                .load(preferences.getUserProfile())
                .placeholder(R.drawable.ic_profile_white)
                .into(civProfilePhoto);
        tvUserName.setSelected(true);
        tvUserName.setText("Hello! " + preferences.getUserName());
        editImg.setOnClickListener(v -> {
            drawer.closeDrawer(GravityCompat.START);
            initTitleBar(getString(R.string.menu_my_profile));
            navController.navigate(R.id.action_nav_home_to_nav_profile);
        });
    }

    private void signOutApiCall(ProgressDialog progressDialog) {
        SignInViewModel signInViewModel = new ViewModelProvider(this).get(SignInViewModel.class);
        Observer<BaseResponse> baseResponseObserver = baseResponse -> {
            if (baseResponse != null) {
                switch (baseResponse.getStatus()) {
                    case STATUS_CODE_200://Record Create/Update Successfully
                        if (baseResponse.getStatus() == 200) {
                            drawer.closeDrawer(GravityCompat.START);
                            Log.e("signOutApiCall: ", new Gson().toJson(baseResponse));
                            AppSharedPreferences preferences = new AppSharedPreferences(this);
                            preferences.clearSharedPreferences(this);
                            Intent intent = new Intent(this, SplashScreenActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                        }
                        break;
                    case STATUS_CODE_403://Validation Errors
                    case STATUS_CODE_400://Validation Errors
                    case STATUS_CODE_404://Validation Errors
//                        warningToast(context, productListResponse.getMessage());
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        Toast.makeText(HomeActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        Toast.makeText(HomeActivity.this, baseResponse.getMessage(), Toast.LENGTH_LONG).show();
                        break;
                }
            }

        };
        signInViewModel.signOutApiResponse(progressDialog, parameters(), HomeActivity.this)
                .observeForever(baseResponseObserver);

    }

    private HashMap<String, String> parameters() {
        HashMap<String, String> map = new HashMap<>();
        map.put(USER_ID, preferences.getUid());
        return map;
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
//        getMenuInflater().inflate(R.menu.bottom_menu, menu);
//        MenuItem cartMenu = menu.findItem(R.id.nav_cart);
//        View cartView = cartMenu.getActionView();
//        textCartItemCount = (TextView) cartView.findViewById(R.id.cart_badge);
//        MenuItem cartMenu = bottomNavigationView.getMenu().getItem(R.id.nav_cart);
//        textCartItemCount = cartMenu.getActionView().findViewById(R.id.cart_badge);
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

    private void redirectToCmsFragment(int from) {
        Bundle bundle = new Bundle();
        bundle.putString(FROM_SCREEN, TAG);
        bundle.putInt(CMS_TYPE, from);
        navController.navigate(R.id.action_nav_cms, bundle);
    }

    //Title and app logo on actionBar
    @SuppressLint("ResourceType")
    protected void initTitleBar(String title) {
        binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.menu_icon_toggle, null);
            binding.appBarHome.toolbar.setNavigationIcon(d);
            binding.appBarHome.toolbar.setPadding(0, 0, 0, 0);
        });
        binding.appBarHome.textTitle.setVisibility(View.VISIBLE);
        binding.appBarHome.imgDelete.setVisibility(View.GONE);
        binding.appBarHome.basketMenu.setVisibility(View.GONE);
        binding.appBarHome.tvAddress.setVisibility(View.GONE);
        binding.appBarHome.logImg.setVisibility(View.GONE);
        binding.appBarHome.rlProductTag.setVisibility(View.GONE);
        binding.appBarHome.textTitle.setText(title);
        binding.appBarHome.toolbar.setBackgroundResource(R.color.white);
        binding.appBarHome.textTitle.setTextColor(Color.parseColor(getString(R.color.black)));
    }

    private void showLogoutConfirmationDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this,
                R.style.StyleDataConfirmationDialog));

        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_logout, null);

        builder.setView(dialogView);
        builder.setCancelable(true);

        CustomTextView tvHeading = dialogView.findViewById(R.id.tv_heading);
        CustomButton customButtonYes = dialogView.findViewById(R.id.btn_yes);
        CustomButton customButtonNo = dialogView.findViewById(R.id.btn_no);


        tvHeading.setText(R.string.do_you_really_want_to_logout);

        final AlertDialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.StyleDialogUpDownAnimation;

        customButtonYes.setOnClickListener(v -> {
            try {
                signOutApiCall(showCircleProgressDialog(HomeActivity.this, ""));
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });
        customButtonNo.setOnClickListener(v -> {
            dialog.dismiss();
        });


        dialog.show();

        // Get screen width and height in pixels
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        // The absolute width of the available display size in pixels.
        int displayWidth = displayMetrics.widthPixels;
        // The absolute height of the available display size in pixels.
        int displayHeight = displayMetrics.heightPixels;

        // Initialize a new window manager layout parameters
        WindowManager.LayoutParams layoutParams = new WindowManager.LayoutParams();

        // Copy the alert dialog window attributes to new layout parameter instance
        layoutParams.copyFrom(dialog.getWindow().getAttributes());

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

    public void setCountBudge(int count) {
        mCartItemCount = count;
        try {
            if (textCartItemCount != null) {
                if (mCartItemCount == 0) {
                    if (textCartItemCount.getVisibility() != View.GONE) {
                        textCartItemCount.setVisibility(View.GONE);
                    }
                } else {
                    if (mCartItemCount > 99){
                        textCartItemCount.setText("99+");
                    }else {
                        textCartItemCount.setText(String.valueOf(Math.min(mCartItemCount, 99)));
                    }
                    if (textCartItemCount.getVisibility() != View.VISIBLE) {
                        textCartItemCount.setVisibility(View.VISIBLE);
                    }
                }
            }
        }catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
}