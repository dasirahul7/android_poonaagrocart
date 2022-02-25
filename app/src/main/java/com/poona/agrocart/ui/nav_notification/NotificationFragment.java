package com.poona.agrocart.ui.nav_notification;

import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.core.widget.NestedScrollView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.notification.NotificationListResponse;
import com.poona.agrocart.databinding.FragmentNotificationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.widgets.CustomButton;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationFragment extends BaseFragment {

    private NotificationViewModel mViewModel;
    private FragmentNotificationBinding notificationBinding;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationListResponse.NotificationList> notificationLists = new ArrayList<>();
    private AlertDialog dialog;

    /*View.OnClickListener,*/
    private float scale;
    private int offset = 0;
    private int visibleItemCount = 0;
    private int totalCount = 0;
    private LinearLayoutManager layoutManager;
    private final int limit = 10;


    /* Pagination adding to the recycler view */

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
            callNotificationApi(showCircleProgressDialog(context,""),"RecyclerView");
             layoutManager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
            notificationBinding.rvNotification.setLayoutManager(layoutManager);
            notificationBinding.rvNotification.setHasFixedSize(true);
            notificationBinding.rvNotification.setAdapter(notificationAdapter);
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setScrollListener() {
        notificationBinding.rvNotification.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = notificationBinding.nvMain;

        nestedScrollView.setOnScrollChangeListener((NestedScrollView.OnScrollChangeListener) (v, scrollX, scrollY, oldScrollX, oldScrollY) -> {
            if (v.getChildAt(v.getChildCount() - 1) != null) {
                visibleItemCount = layoutManager.getItemCount();

                if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount != totalCount) {
                    callNotificationApi(showCircleProgressDialog(context, ""), "onScrolled");
                } else if ((scrollY >= (v.getChildAt(v.getChildCount() - 1).getMeasuredHeight() - v.getMeasuredHeight())) && scrollY > oldScrollY
                        && visibleItemCount == totalCount) {
                    infoToast(getActivity(), getString(R.string.no_more_records));
                }
            }
        });

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

    private void callNotificationApi(ProgressDialog progressDialog, String fromFunction){
        if (fromFunction.equals("onScrolled")) {
            offset = offset + 1;
        } else {
            offset = 0;
        }

        Observer<NotificationListResponse> notificationListResponseObserver = notificationListResponse -> {
            if (notificationListResponse != null) {
                Log.e("Notification list Api Response", new Gson().toJson(notificationListResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (notificationListResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (offset == 0)
                            notificationLists.clear();

                        //totalCount = Integer.parseInt(notificationListResponse.getData());
                        if (notificationListResponse.getData() != null) {
                            notificationLists.addAll(notificationListResponse.getData());
                            notificationAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, notificationListResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        //llEmptyLayout.setVisibility(View.VISIBLE);
                        //llMainLayout.setVisibility(View.INVISIBLE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen();
                        errorToast(context, notificationListResponse.getMessage());
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, notificationListResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }
        };
        mViewModel.getNotification(progressDialog,context, notificationInputParameter(), NotificationFragment.this)
                .observe(getViewLifecycleOwner(), notificationListResponseObserver);
    }

    private HashMap<String, String> notificationInputParameter(){
        HashMap<String, String> map = new HashMap<>();

        map.put(LIMIT, String.valueOf(limit));
        map.put(OFFSET, String.valueOf(offset));

        return map;
    }

}