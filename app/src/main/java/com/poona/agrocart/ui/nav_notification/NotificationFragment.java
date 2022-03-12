package com.poona.agrocart.ui.nav_notification;

import static com.poona.agrocart.app.AppConstants.LIMIT;
import static com.poona.agrocart.app.AppConstants.OFFSET;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.NetworkExceptionListener;
import com.poona.agrocart.data.network.responses.notification.DeleteNotificationResponse;
import com.poona.agrocart.data.network.responses.notification.NotificationListResponse;
import com.poona.agrocart.databinding.FragmentNotificationBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.HomeActivity;
import com.poona.agrocart.ui.nav_help_center.HelpCenterFragment;
import com.poona.agrocart.widgets.CustomButton;

import java.util.ArrayList;
import java.util.HashMap;

public class NotificationFragment extends BaseFragment implements NetworkExceptionListener {

    private NotificationViewModel mViewModel;
    private FragmentNotificationBinding fragmentNotificationBinding;
    private NotificationAdapter notificationAdapter;
    private ArrayList<NotificationListResponse.NotificationList> notificationLists = new ArrayList<>();
    private AlertDialog dialog;
    private SwipeRefreshLayout refreshLayout;

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
        fragmentNotificationBinding = FragmentNotificationBinding.inflate(getLayoutInflater());
        mViewModel = new ViewModelProvider(this).get(NotificationViewModel.class);
        View view = fragmentNotificationBinding.getRoot();
        initTitleBar(getString(R.string.menu_notification));
        ((HomeActivity) requireActivity()).binding.appBarHome.imgDelete.setVisibility(View.VISIBLE);
        refreshLayout = fragmentNotificationBinding.rlMain;
        if (isConnectingToInternet(context)){
            setNotificationItems();
        }else{
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }

        refreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                refreshLayout.setRefreshing(true);
                if (isConnectingToInternet(context)){
                    setNotificationItems();
                }else{
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            }
        });

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



            layoutManager = new LinearLayoutManager(getContext());
            fragmentNotificationBinding.rvNotification.setLayoutManager(layoutManager);
            refreshLayout.setRefreshing(false);
            callNotificationApi(showCircleProgressDialog(context,""),"RecyclerView");
            notificationAdapter = new NotificationAdapter(notificationLists,context);
            fragmentNotificationBinding.rvNotification.setHasFixedSize(true);
            fragmentNotificationBinding.rvNotification.setAdapter(notificationAdapter);

            //Pagination in scroll view
            setScrollListener();



    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    private void setScrollListener() {
        fragmentNotificationBinding.rvNotification.setNestedScrollingEnabled(true);
        NestedScrollView nestedScrollView = fragmentNotificationBinding.nvMain;

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
            try {
                if (isConnectingToInternet(context)){

                    callDeleteNotificationApi(showCircleProgressDialog(context,""));
                    notificationLists.clear();

                }else{
                    showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            dialog.dismiss();
        });

        buttonNo.setOnClickListener(view -> {
            dialog.dismiss();
        });

        dialog.dismiss();
        dialog.show();
    }

    private void callNotificationApi(ProgressDialog progressDialog, String fromFunction){
        if (fromFunction.equals("onScrolled")) {
            offset = offset + 1;
        } else {
            offset = 0;
        }

        @SuppressLint("NotifyDataSetChanged") Observer<NotificationListResponse> notificationListResponseObserver = notificationListResponse -> {
            if (notificationListResponse != null) {
                Log.e("Notification list Api Response", new Gson().toJson(notificationListResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (notificationListResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (offset == 0)
                            notificationLists.clear();

                       totalCount = notificationListResponse.getNotificationCount();
                        if (notificationListResponse.getData() != null && notificationListResponse.getData().size() > 0) {
                            notificationLists.addAll(notificationListResponse.getData());
                            notificationAdapter.notifyDataSetChanged();

                            fragmentNotificationBinding.llMain.setVisibility(View.VISIBLE);
                            fragmentNotificationBinding.llEmptyScreen.setVisibility(View.GONE);
                        }else{
                            fragmentNotificationBinding.llEmptyScreen.setVisibility(View.VISIBLE);
                            fragmentNotificationBinding.llMain.setVisibility(View.GONE);
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, notificationListResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found

                        fragmentNotificationBinding.llEmptyScreen.setVisibility(View.VISIBLE);
                        fragmentNotificationBinding.llMain.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(notificationListResponse.getMessage(),context);
//                        errorToast(context, notificationListResponse.getMessage());
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

    private void callDeleteNotificationApi(ProgressDialog progressDialog){

        Observer<DeleteNotificationResponse> deleteNotificationResponseObserver = deleteNotificationResponse -> {
            if (deleteNotificationResponse != null) {
                Log.e("Notification list Api Response", new Gson().toJson(deleteNotificationResponse));
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
                switch (deleteNotificationResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        successToast(context, deleteNotificationResponse.getMessage());
                        setNotificationItems();
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, deleteNotificationResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        //llEmptyLayout.setVisibility(View.VISIBLE);
                        //llMainLayout.setVisibility(View.INVISIBLE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        goToAskSignInSignUpScreen(deleteNotificationResponse.getMessage(),context);
//                        errorToast(context, deleteNotificationResponse.getMessage());
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, deleteNotificationResponse.getMessage());
                        break;
                }
            } else {
                if (progressDialog != null) {
                    progressDialog.dismiss();
                }
            }


        };
        mViewModel.getDeleteNotification(progressDialog, context, NotificationFragment.this)
                .observe(getViewLifecycleOwner(), deleteNotificationResponseObserver);

    }

    @Override
    public void onNetworkException(int from, String type) {

        showServerErrorDialog(getString(R.string.for_better_user_experience), NotificationFragment.this, () -> {
            if (isConnectingToInternet(context)) {
                hideKeyBoard(requireActivity());
                if (from == 0) {
                    callNotificationApi(showCircleProgressDialog(context,""),"RecyclerView");
                } else if (from == 1) {
                    callDeleteNotificationApi(showCircleProgressDialog(context,""));
                }
            } else {
                showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
            }
        }, context);
    }
}