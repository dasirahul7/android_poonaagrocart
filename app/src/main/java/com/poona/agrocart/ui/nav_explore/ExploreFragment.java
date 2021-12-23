package com.poona.agrocart.ui.nav_explore;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;

import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentExploreBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_explore.adapter.ExploreItemAdapter;

public class ExploreFragment extends BaseFragment {

    private ExploreViewModel mViewModel;
    private FragmentExploreBinding exploreFragmentBinding;
    private ExploreItemAdapter exploreItemAdapter;

    public static ExploreFragment newInstance() {
        return new ExploreFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        exploreFragmentBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_explore,container,false);
        exploreFragmentBinding.setLifecycleOwner(this);
        View root = exploreFragmentBinding.getRoot();
        mViewModel = new ViewModelProvider(this).get(ExploreViewModel.class);
        initTitleBar(getString(R.string.menu_explore));
        setExploreList(root);
        return root;
    }

    private void setExploreList(View root) {
        mViewModel.exploreMutableLiveData.observe(requireActivity(),exploreItems -> {
            exploreItemAdapter = new ExploreItemAdapter(getActivity(),exploreItems,root);
            GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),2);
            exploreFragmentBinding.rvExplore.setLayoutManager(gridLayoutManager);
            exploreFragmentBinding.rvExplore.setHasFixedSize(true);
            exploreFragmentBinding.rvExplore.setAdapter(exploreItemAdapter);
        });
    }

}