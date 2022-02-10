package com.poona.agrocart.ui.sign_in;

import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.ivBack;
import static com.poona.agrocart.ui.splash_screen.SplashScreenActivity.title;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.Navigation;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentTermsConditionBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.Objects;

public class SignInPrivacyFragment extends BaseFragment {
    private FragmentTermsConditionBinding fragmentPrivacyPolicyBinding;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        fragmentPrivacyPolicyBinding = FragmentTermsConditionBinding.inflate(getLayoutInflater());
        fragmentPrivacyPolicyBinding.setLifecycleOwner(this);
        View privacyView = fragmentPrivacyPolicyBinding.getRoot();
        initView(privacyView);

        return privacyView;
    }

    private void initView(View privacyView) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(privacyView).popBackStack();
        });
        title.setText(getString(R.string.privacy_policy));
        fragmentPrivacyPolicyBinding.tvContent.setText(getString(R.string.sample_policy));
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
