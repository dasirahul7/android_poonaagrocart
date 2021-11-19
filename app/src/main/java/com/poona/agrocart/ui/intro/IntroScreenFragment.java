package com.poona.agrocart.ui.intro;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.content.ContextCompat;
import androidx.core.view.ViewCompat;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import android.os.Handler;
import android.text.Html;
import android.util.AndroidException;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.material.tabs.TabLayout;
import com.poona.agrocart.R;
import com.poona.agrocart.app.Intro;
import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;
import com.poona.agrocart.databinding.FragmentIntroScreenBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.splash_screen.OnBackPressedListener;
import com.poona.agrocart.ui.splash_screen.SplashScreenActivity;

import java.util.ArrayList;

import me.huseyinozer.TooltipIndicator;

public class IntroScreenFragment extends BaseFragment implements IntroPagerAdapter.OnChangeButtonCaptionListener, OnBackPressedListener {


    private final String TAG = "IntroScreenFragment";
    public int count = 0;
    private FragmentIntroScreenBinding fragmentIntroScreenBinding;
    private View view;
    public ViewPager vpIntro;
    private TooltipIndicator tbIndicator;
    private Button btnNext;
    ArrayList<Intro> introList;
    private IntroPagerAdapter introPagerAdapter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        fragmentIntroScreenBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_screen, container, false);
        fragmentIntroScreenBinding.setLifecycleOwner(this);
        view = fragmentIntroScreenBinding.introLayout;

        vpIntro = fragmentIntroScreenBinding.vpIntro;
        tbIndicator = fragmentIntroScreenBinding.tlIndicators;
        btnNext = fragmentIntroScreenBinding.btNext;
        introList = new ArrayList<>();
        Intro intro1 = new Intro("1", "Welcome to poona cart 1", "Ger your groceries in as fast as one hour", R.drawable.info_img);
        Intro intro2 = new Intro("2", "Welcome to poona cart 2", "Ger your groceries in as fast as one hour", R.drawable.info_img);
        Intro intro3 = new Intro("3", "Welcome to poona cart 3", "Ger your groceries in as fast as one hour", R.drawable.info_img);
        introList.add(intro1);
        introList.add(intro2);
        introList.add(intro3);
//        addDotsLine(0);
        if (introList.size() > 0) {
            count = introList.size();
            setViewPagerAdapterItems();
        } else {
            NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.introScreenFragment, true).build();
            NavHostFragment.findNavController(IntroScreenFragment.this)
                    .navigate(R.id.action_SplashScreenFragment_to_signInFragment, null, navOptions);
        }
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!btnNext.getText().toString().equalsIgnoreCase(getString(R.string.str_get_started))){
                    vpIntro.setCurrentItem(vpIntro.getCurrentItem() + 1);
                }
                else {
                    preferences.setIsLoggedIn(true);
                    NavHostFragment.findNavController(IntroScreenFragment.this).navigate(R.id.action_introScreenFragment_to_LoginFragment);
                }
            }
        });


        return view;
    }
    private void setViewPagerAdapterItems() {
        // set the viewPager
        introPagerAdapter = new IntroPagerAdapter(IntroScreenFragment.this, getChildFragmentManager(), this, introList);
        vpIntro.setAdapter(introPagerAdapter);
        introPagerAdapter.notifyDataSetChanged();

        vpIntro.addOnPageChangeListener(introPagerAdapter);

        // Set up tab indicators
        tbIndicator.setupViewPager(vpIntro);
    }

    @Override
    public void onAddText(int position) {
        Log.d(TAG, "onAddText: " + position);
        if (position == introList.size()-1){
            btnNext.setText(R.string.str_get_started);
        }
        else btnNext.setText(R.string.str_next);

    }

    @Override
    public void onBackPressed() {
        NavHostFragment.findNavController(IntroScreenFragment.this).popBackStack();
    }
}