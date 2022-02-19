package com.poona.agrocart.ui.nav_notification;

import android.app.Dialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentNotificationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.widgets.CustomButton;

public class NotificationFragment extends BaseFragment {

    private NotificationViewModel mViewModel;
    private FragmentNotificationBinding notificationBinding;
    private NotificationAdapter notificationAdapter;
    private AlertDialog dialog;

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
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
        setNotificationItems();

        OnsetClickListener();
        return view;
    }

    private void OnsetClickListener() {

        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setOnClickListener(view1 -> {
            ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setClickable(false);
            NotificationDeleteDialog();

            new Handler().postDelayed(() -> {
                ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setClickable(true);
            }, 500);
        });
    }

    private void setNotificationItems() {
        mViewModel.arrayListMutableLiveData.observe(requireActivity(), notifications -> {
            notificationAdapter = new NotificationAdapter(notifications, getActivity());
            LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            notificationBinding.rvNotification.setLayoutManager(layoutManager);
            notificationBinding.rvNotification.setHasFixedSize(true);
            notificationBinding.rvNotification.setAdapter(notificationAdapter);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    /*Delete notification Dialogue*/
    public void NotificationDeleteDialog() {
        Dialog dialog = new Dialog(getActivity());
        dialog.getWindow().addFlags(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimationUp;
        dialog.setContentView(R.layout.dialog_delete_notification);
        ImageView closeImg = dialog.findViewById(R.id.iv_cross);
        CustomButton buttonYes = dialog.findViewById(R.id.yes_btn);
        CustomButton buttonNo = dialog.findViewById(R.id.no_btn);

        closeImg.setOnClickListener(view -> {
            dialog.dismiss();
        });

        buttonYes.setOnClickListener(view -> {
            dialog.dismiss();
        });

        buttonNo.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.show();
    }

}