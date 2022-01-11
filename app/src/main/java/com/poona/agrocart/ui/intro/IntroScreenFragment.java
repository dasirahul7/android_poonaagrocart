package com.poona.agrocart.ui.intro;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentIntroScreenBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.splash_screen.OnBackPressedListener;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class IntroScreenFragment extends BaseFragment implements IntroPagerAdapter.OnChangeButtonCaptionListener, OnBackPressedListener {


    private final String TAG = "IntroScreenFragment";
    public int count = 0;
    private FragmentIntroScreenBinding fragmentIntroScreenBinding;
    private View view;
    public ViewPager vpIntro;
    private DotsIndicator dotsIndicator;
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
        dotsIndicator = fragmentIntroScreenBinding.dotsIndicator;
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
                    preferences.setIsIntroRead(true);
                    NavHostFragment.findNavController(IntroScreenFragment.this).navigate(R.id.action_introScreenFragment_to_SignInFragment);
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
        dotsIndicator.setViewPager(vpIntro);
    }

    @Override
    public void onAddText(int position) {
//            btnNext.setText(R.string.str_get_started);
    }

    @Override
    public void onBackPressed() {
        NavHostFragment.findNavController(IntroScreenFragment.this).popBackStack();
    }
}