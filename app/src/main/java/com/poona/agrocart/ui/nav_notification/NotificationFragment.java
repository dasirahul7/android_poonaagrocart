package com.poona.agrocart.ui.nav_notification;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentNotificationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;

public class NotificationFragment extends BaseFragment {

    private NotificationViewModel mViewModel;
    private FragmentNotificationBinding notificationBinding;
    private NotificationAdapter notificationAdapter;

    public static NotificationFragment newInstance() {
        return new NotificationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        notificationBinding = FragmentNotificationBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        View view = notificationBinding.getRoot();
        initTitleBar(getString(R.string.menu_notification));
        ((HomeActivity)requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
        setNotificationItems();
        return view;
    }

    private void setNotificationItems() {
        mViewModel.arrayListMutableLiveData.observe(requireActivity(),notifications -> {
           notificationAdapter = new NotificationAdapter(notifications,getActivity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false);
            notificationBinding.rvNotification.setLayoutManager(layoutManager);
            notificationBinding.rvNotification.setHasFixedSize(true);
           notificationBinding.rvNotification.setAdapter(notificationAdapter);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

}