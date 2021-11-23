package com.poona.agrocart.ui.home;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.res.ResourcesCompat;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavHostController;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentHomeBinding;
import com.poona.agrocart.ui.BaseFragment;

public class HomeFragment extends BaseFragment {

    private HomeViewModel homeViewModel;
    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);
        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();
        try {
            initHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }

//        ((AppCompatActivity) getActivity()).getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        return root;
    }

    @Override
    public void onResume() {
        try {
            initHomeTitleBar();
        } catch (Exception e) {
            e.printStackTrace();
        }
        super.onResume();
    }

    private void initHomeTitleBar() {
        ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.post(() -> {
            Drawable d = ResourcesCompat.getDrawable(getResources(),
                    R.drawable.ic_poona_agrocart_logo_final, null);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationIcon(d);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setPadding(30,0,0,0);
            ((HomeActivity) requireActivity()).binding.appBarHome.toolbar.setNavigationOnClickListener(
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requireActivity().onBackPressed();
                        }
                    }
            );
        });
        if (((HomeActivity)requireActivity()).binding.appBarHome.textTitle.getVisibility()==View.VISIBLE)
        ((HomeActivity)requireActivity()).binding.appBarHome.textTitle.setVisibility(View.GONE);
        ((HomeActivity)requireActivity()).binding.appBarHome.textView.setVisibility(View.VISIBLE);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}