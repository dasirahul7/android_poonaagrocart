package com.poona.agrocart.ui.nav_gallery.fragment;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.poona.agrocart.R;
import com.poona.agrocart.databinding.FragmentGalleryBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.viewModel.GalleryViewModel;
import com.poona.agrocart.ui.nav_gallery.model.Photos;
import com.poona.agrocart.ui.nav_gallery.model.Videos;
import com.poona.agrocart.ui.nav_gallery.adapter.GalleryFragmentAdapter;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.Objects;

public class GalleryFragment extends BaseFragment {

    private GalleryViewModel mViewModel;
    private FragmentGalleryBinding galleryBinding;
    private TabLayout mTabs;
    private View mIndicator;
    private ViewPager mViewPager;
    private int indicatorWidth;
    private View fragmentView;
    private ArrayList<Photos> photos = new ArrayList<>();
    private ArrayList<Videos> videos = new ArrayList<>();



    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initViews();
        setFragmentItem();
        setTabsAction();
        return fragmentView;
    }


    private void setTabsAction() {
        //Determine indicator width at runtime
        mTabs.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = mTabs.getWidth() / mTabs.getTabCount();
                //Assign new width
                FrameLayout.LayoutParams indicatorParams = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                indicatorParams.width = indicatorWidth-30;
                indicatorParams.setMargins(10, 5, 20, 5);
                mIndicator.setLayoutParams(indicatorParams);
                CustomTextView tabPhoto = (CustomTextView) LayoutInflater.from(getActivity()).inflate(R.layout.gallery_tab_item, null);
                tabPhoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gallery_photo, 0, 0, 0);
                tabPhoto.setText(R.string.photo);
                CustomTextView tabVideo = (CustomTextView) LayoutInflater.from(getActivity()).inflate(R.layout.gallery_tab_item, null);
                tabVideo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gallery_video, 0, 0, 0);
                tabVideo.setText(R.string.video);
                Objects.requireNonNull(mTabs.getTabAt(0)).setCustomView(tabPhoto);
                Objects.requireNonNull(mTabs.getTabAt(1)).setCustomView(tabVideo);

            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            //To move the indicator as the user scroll, we will need the scroll offset values
            //positionOffset is a value from [0..1] which represents how far the page has been scrolled
            //see https://developer.android.com/reference/android/support/v4/view/ViewPager.OnPageChangeListener
            @Override
            public void onPageScrolled(int i, float positionOffset, int positionOffsetPx) {
                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) mIndicator.getLayoutParams();
                //Multiply positionOffset with indicatorWidth to get translation
                float translationOffset = (positionOffset + i) * indicatorWidth;
                params.leftMargin = (int) translationOffset+20;
                params.setMargins((int) translationOffset+20, 5, 20, 5);
                mIndicator.setLayoutParams(params);
            }

            @Override
            public void onPageSelected(int i) {

            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    private void setFragmentItem() {
        //Set up the view pager and fragments
        GalleryFragmentAdapter adapter = new GalleryFragmentAdapter(getActivity().getSupportFragmentManager(), 1);
        adapter.addFragment(PhotoGalleryFragment.newInstance(), getString(R.string.photo));
        adapter.addFragment(VideoGalleryFragment.newInstance(), getString(R.string.video));
        mViewPager.setAdapter(adapter);
        mTabs.setupWithViewPager(mViewPager);
    }

    private void initViews() {
        initTitleBar(getString(R.string.menu_gallery));
        galleryBinding = FragmentGalleryBinding.inflate(LayoutInflater.from(context));
//        galleryBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        fragmentView = galleryBinding.getRoot();
        mTabs = galleryBinding.tab;
        mIndicator = galleryBinding.indicator;
        mViewPager = galleryBinding.vpGallery;
//        makePhotoList();
//        makeVideoList();
        mViewModel.photoLiveData.setValue(photos);
        mViewModel.videoLiveData.setValue(videos);
    }

}