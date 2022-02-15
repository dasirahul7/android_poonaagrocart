package com.poona.agrocart.ui.nav_gallery.fragment;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.viewpager.widget.ViewPager;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.google.android.material.tabs.TabLayout;
import com.poona.agrocart.R;
import com.poona.agrocart.data.network.reponses.galleryResponse.GalleryImage;
import com.poona.agrocart.data.network.reponses.galleryResponse.GalleryVideo;
import com.poona.agrocart.databinding.FragmentGalleryBinding;
import com.poona.agrocart.ui.BaseFragment;
import com.poona.agrocart.ui.nav_gallery.viewModel.GalleryViewModel;
import com.poona.agrocart.ui.nav_gallery.adapter.GalleryFragmentAdapter;
import com.poona.agrocart.widgets.CustomTextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GalleryFragment extends BaseFragment {

    private GalleryViewModel mViewModel;
    private FragmentGalleryBinding galleryBinding;
    private ArrayList<String> tabsTitle;
    private ArrayList<Fragment> fragmentsName;
    private TabLayout tabLayout;
    private View mIndicator;
    private ViewPager mViewPager;
    private int indicatorWidth;
    private View fragmentView;
    private List<GalleryImage> photos = new ArrayList<>();
    private List<GalleryVideo> videos = new ArrayList<>();



    public static GalleryFragment newInstance() {
        return new GalleryFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        initViews();
        setFragmentItem();
        setTabsAction();

        Log.d("TAG", "onCreateView: ");
        return fragmentView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.d("TAG", "onResume: ");
        setFragmentItem();

        CustomTextView tabPhoto = (CustomTextView) LayoutInflater.from(getActivity()).inflate(R.layout.gallery_tab_item, null);
        tabPhoto.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gallery_photo, 0, 0, 0);
        tabPhoto.setText(R.string.photo);
        CustomTextView tabVideo = (CustomTextView) LayoutInflater.from(getActivity()).inflate(R.layout.gallery_tab_item, null);
        tabVideo.setCompoundDrawablesWithIntrinsicBounds(R.drawable.ic_gallery_video, 0, 0, 0);
        tabVideo.setText(R.string.video);
        Objects.requireNonNull(tabLayout.getTabAt(0)).setCustomView(tabPhoto);
        Objects.requireNonNull(tabLayout.getTabAt(1)).setCustomView(tabVideo);

       // setTabsAction();
    }

    private void setTabsAction() {
        //Determine indicator width at runtime
        tabLayout.post(new Runnable() {
            @Override
            public void run() {
                indicatorWidth = tabLayout.getWidth() / tabLayout.getTabCount();
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
                Objects.requireNonNull(tabLayout.getTabAt(0)).setCustomView(tabPhoto);
                Objects.requireNonNull(tabLayout.getTabAt(1)).setCustomView(tabVideo);

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
        GalleryFragmentAdapter adapter = new GalleryFragmentAdapter(getActivity().getSupportFragmentManager(),context);

        adapter.addFragment(PhotoGalleryFragment.newInstance(), getString(R.string.photo));
        adapter.addFragment(VideoGalleryFragment.newInstance(), getString(R.string.video));


        mViewPager.setAdapter(adapter);
        tabLayout.setupWithViewPager(mViewPager);
    }

    private void initViews() {
        initTitleBar(getString(R.string.menu_gallery));
        galleryBinding = FragmentGalleryBinding.inflate(LayoutInflater.from(context));
//        galleryBinding.setLifecycleOwner(this);
        mViewModel = new ViewModelProvider(this).get(GalleryViewModel.class);
        fragmentView = galleryBinding.getRoot();
        tabLayout = galleryBinding.tab;
        mIndicator = galleryBinding.indicator;
        mViewPager = galleryBinding.vpGallery;

        //makePhotoList();
       //makeVideoList();
        mViewModel.photoLiveData.setValue(photos);
        mViewModel.videoLiveData.setValue(videos);
    }

}