package com.poona.agrocart.ui.nav_favourites;

import static com.poona.agrocart.app.AppConstants.STATUS_CODE_200;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_400;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_401;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_404;
import static com.poona.agrocart.app.AppConstants.STATUS_CODE_405;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.databinding.FragmentFavouriteItemsBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.util.ArrayList;

public class FavouriteItemsFragment extends BaseFragment
{
    private FragmentFavouriteItemsBinding fragmentFavouriteItemsBinding;
    private FavouriteViewModel favouriteViewModel;
    private RecyclerView rvFavouriteItems;
    private LinearLayoutManager linearLayoutManager;
    private FavouriteItemAdapter favouriteItemAdapter;
    private ArrayList<FavouriteListResponse.Favourite> favouriteItemsList = new ArrayList<>();

    View view;
    private String TAG= FavouriteItemsFragment.class.getSimpleName();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        fragmentFavouriteItemsBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_favourite_items, container, false);
        view = fragmentFavouriteItemsBinding.getRoot();
        favouriteViewModel=new ViewModelProvider(this).get(FavouriteViewModel.class);
        fragmentFavouriteItemsBinding.setFaqViewModel(favouriteViewModel);
        fragmentFavouriteItemsBinding.setLifecycleOwner(this);

         initTitleBar(getString(R.string.menu_favourite));
         initView();
         setAdaptor();


        return view;
    }


    @Override
    public void onResume() {
        super.onResume();

    }

    private void setAdaptor() {
        linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFavouriteItems.setHasFixedSize(true);
        rvFavouriteItems.setLayoutManager(linearLayoutManager);

        //Initializing our superheroes list
        favouriteItemsList = new ArrayList<>();

       // refreshLayout.setRefreshing(false);
        //initializing our adapter
        favouriteItemAdapter = new FavouriteItemAdapter(context, favouriteItemsList, this);

        //Adding adapter to recyclerview
        rvFavouriteItems.setAdapter(favouriteItemAdapter);

        //Calling method to get data to fetch data
        if (isConnectingToInternet(context)) {
            callFavouriteAPi(showCircleProgressDialog(context, ""));
        } else {
            showNotifyAlert(requireActivity(), context.getString(R.string.info), context.getString(R.string.internet_error_message), R.drawable.ic_no_internet);
        }
    }


    private void initView() {
        rvFavouriteItems=fragmentFavouriteItemsBinding.rvFavouriteItems;
    }

    private void callFavouriteAPi(ProgressDialog progressDialog) {
        @SuppressLint("NotifyDataSetChanged")
        Observer<FavouriteListResponse> favouriteLisResponseObserver = favouriteLisResponse -> {

            if (favouriteLisResponse != null){
                Log.e("Favourite List Response", new Gson().toJson(favouriteLisResponse));
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
                switch (favouriteLisResponse.getStatus()) {
                    case STATUS_CODE_200://success
                        if (favouriteLisResponse.getFavouriteList() != null
                                && favouriteLisResponse.getFavouriteList().size() > 0){

                            fragmentFavouriteItemsBinding.emptyLayout.setVisibility(View.GONE);
                            fragmentFavouriteItemsBinding.rlMain.setVisibility(View.VISIBLE);

                            favouriteItemsList.addAll(favouriteLisResponse.getFavouriteList());
                            favouriteItemAdapter.notifyDataSetChanged();
                        }
                        break;
                    case STATUS_CODE_400://Validation Errors
                        warningToast(context, favouriteLisResponse.getMessage());
                        break;
                    case STATUS_CODE_404://Record not Found
                        /* show empty screen message */
                        fragmentFavouriteItemsBinding.emptyLayout.setVisibility(View.VISIBLE);
                        fragmentFavouriteItemsBinding.rlMain.setVisibility(View.GONE);
                        break;
                    case STATUS_CODE_401://Unauthorized user
                        warningToast(context, favouriteLisResponse.getMessage());
                        goToAskSignInSignUpScreen();
                        break;
                    case STATUS_CODE_405://Method Not Allowed
                        infoToast(context, favouriteLisResponse.getMessage());
                        break;
                }
            }else{
                if (progressDialog !=null){
                    progressDialog.dismiss();
                }
            }
        };

        favouriteViewModel.favouriteLisResponseLiveData(progressDialog,FavouriteItemsFragment.this)
                .observe(getViewLifecycleOwner(), favouriteLisResponseObserver);
    }



}