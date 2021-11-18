package com.poona.agrocart.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.navigation.NavOptions;
import androidx.navigation.fragment.NavHostFragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.dashboard.DashBoardActivity;
import com.poona.agrocart.ui.intro.IntroScreenFragment;
import com.poona.agrocart.ui.splash_screen.OnBackPressedListener;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class LogInFragment extends BaseFragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(requireActivity(), DashBoardActivity.class);
                startActivity(intent);
            }
        },1000);
        return inflater.inflate(R.layout.fragment_log_in, container, false);
    }
}