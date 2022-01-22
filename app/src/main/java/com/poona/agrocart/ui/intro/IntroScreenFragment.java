package com.poona.agrocart.ui.intro;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;
import androidx.viewpager.widget.ViewPager;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.reponses.IntroScreenResponse;
import com.poona.agrocart.databinding.FragmentIntroScreenBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.splash_screen.OnBackPressedListener;
import com.poona.agrocart.ui.verify_otp.VerifyOtpFragment;
import com.tbuonomo.viewpagerdotsindicator.DotsIndicator;

import java.util.ArrayList;

public class IntroScreenFragment extends BaseFragment implements IntroPagerAdapter.OnChangeButtonCaptionListener, OnBackPressedListener, NetworkExceptionListener {
    private final String TAG = "IntroScreenFragment";
    public int count = 0;
    private FragmentIntroScreenBinding fragmentIntroScreenBinding;
    private View view;
    public ViewPager vpIntro;
    private DotsIndicator dotsIndicator;
    private Button btnNext;
    ArrayList<Intro> introList;
    private IntroPagerAdapter introPagerAdapter;
    private View introView;
    private IntroScreenViewModel introScreenViewModel;

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
        introView = fragmentIntroScreenBinding.introLayout;

        vpIntro = fragmentIntroScreenBinding.vpIntro;
        dotsIndicator = fragmentIntroScreenBinding.dotsIndicator;
        btnNext = fragmentIntroScreenBinding.btNext;
        introList = new ArrayList<>();
        initView();
//        Intro intro1 = new Intro("1", "Welcome to poona cart 1", "Ger your groceries in as fast as one hour", R.drawable.info_img);
//        Intro intro2 = new Intro("2", "Welcome to poona cart 2", "Ger your groceries in as fast as one hour", R.drawable.info_img);
//        Intro intro3 = new Intro("3", "Welcome to poona cart 3", "Ger your groceries in as fast as one hour", R.drawable.info_img);
//        introList.add(intro1);
//        introList.add(intro2);
//        introList.add(intro3);
//        addDotsLine(0);

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    preferences.setIsIntroRead(true);
                    NavHostFragment.findNavController(IntroScreenFragment.this).navigate(R.id.action_introScreenFragment_to_SignInFragment);
            }
        });

        return introView;
    }

    private void initView() {
        introScreenViewModel = new ViewModelProvider(this).get(IntroScreenViewModel.class);
        callIntroScreenApi(showCircleProgressDialog(context,""));
    }

    private void callIntroScreenApi(ProgressDialog progressDialog) {
        Observer<IntroScreenResponse> introScreenResponseObserver = introScreenResponse -> {
            if (introScreenResponse!=null){
                progressDialog.dismiss();
                if (introScreenResponse.getIntroScreenItem()!=null){
                    introList = introScreenResponse.getIntroScreenItem();
                    if (introList.size() > 0) {
                        count = introList.size();
                        setViewPagerAdapterItems();
                    } else {
                        NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.introScreenFragment, true).build();
                        NavHostFragment.findNavController(IntroScreenFragment.this)
                                .navigate(R.id.action_SplashScreenFragment_to_signInFragment, null, navOptions);
                    }
                }
            }
        };
        introScreenViewModel.getIntroScreenResponse(progressDialog,IntroScreenFragment.this)
                .observe(getViewLifecycleOwner(),introScreenResponseObserver);
    }

    @Override
    public void onNetworkException(int from) {
        showServerErrorDialog(getString(R.string.for_better_user_experience), IntroScreenFragment.this,() -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if(from == 0) {
                    callIntroScreenApi(showCircleProgressDialog(context, ""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
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