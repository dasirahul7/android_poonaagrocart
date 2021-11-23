package com.poona.agrocart.ui.my_orders;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentMyOrdersBinding;
import com.poona.agrocart.ui.BaseFragment;


public class MyOrdersFragment extends BaseFragment {

    private MyOrdersViewModel myOrdersViewModel;
    private FragmentMyOrdersBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        myOrdersViewModel = new ViewModelProvider(this).get(MyOrdersViewModel.class);

        binding = FragmentMyOrdersBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        final TextView textView = binding.textSlideshow;
        myOrdersViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });

        initTitleBar(getString(R.string.my_orders));
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}