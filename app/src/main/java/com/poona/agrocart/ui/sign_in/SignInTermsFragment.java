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

public class SignInTermsFragment extends BaseFragment {
    private FragmentTermsConditionBinding termsConditionBinding;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        termsConditionBinding = FragmentTermsConditionBinding.inflate(getLayoutInflater());
        termsConditionBinding.setLifecycleOwner(this);
        final View termsView = termsConditionBinding.getRoot();
        initView(termsView);

        return termsView;
    }

    private void initView(View termsView) {
        Objects.requireNonNull(((AppCompatActivity) requireActivity()).getSupportActionBar()).show();
        ivBack.setOnClickListener(v -> {
            hideKeyBoard(requireActivity());
            Navigation.findNavController(termsView).popBackStack();
        });
        title.setText(getString(R.string.menu_terms_conditions));
        termsConditionBinding.tvContent.setText(R.string.sample_terms);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
