package com.poona.agrocart.ui.our_stores;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentStoreDetailBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.our_stores.model.Store;

public class StoreDetailFragment extends BaseFragment {

    private static final String TAG = "StoreDetailFragment";
    private StoreDetailViewModel mViewModel;
    private FragmentStoreDetailBinding storeBinding;
    private static Store storeValue;

    public static StoreDetailFragment newInstance(Store store) {
        storeValue = store;
        return new StoreDetailFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(StoreDetailViewModel.class);
        storeBinding = FragmentStoreDetailBinding.inflate(inflater,container,false);
        storeBinding.setLifecycleOwner(this);
        View root = storeBinding.getRoot();
        initTitleWithBackBtn(getString(R.string.store_location));
        initViews();
        return root;
    }

    private void initViews() {
        mViewModel.storeMutableLiveData.setValue(storeValue);
        Log.d(TAG, "initViews: "+mViewModel.storeMutableLiveData.getValue().getName());
//        storeBinding.tvStoreName.setText(mViewModel.storeMutableLiveData.getValue().name);
        storeBinding.setStoreDetailViewModel(mViewModel);
    }


}