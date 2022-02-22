package com.poona.agrocart.ui.intro;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;

import com.poona.agrocart.R;
import com.poona.agrocart.data.network.responses.IntroScreenResponse;
import com.poona.agrocart.databinding.FragmentIntroScreenItemBinding;
import com.poona.agrocart.ui.BaseFragment;

import java.util.ArrayList;

public class IntroItemFragment extends BaseFragment {
    private static final String POSITION = "position";
    private static Context context;
    private static ArrayList<IntroScreenResponse.Intro> introList;
    private FragmentIntroScreenItemBinding fragmentIntroItemBinding;
    private View view;
    private SharedPreferences getPreferences;
    private ImageView introImg;
    private TextView introTitle;
    private TextView introDesc;

    public static IntroItemFragment newInstance(IntroScreenFragment introScreenFragment, int pos, ArrayList<IntroScreenResponse.Intro> intros) {
        context = introScreenFragment.getContext();
        introList = intros;
        IntroItemFragment fragment = new IntroItemFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(POSITION, pos);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final int position = this.getArguments().getInt(POSITION);
        fragmentIntroItemBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_intro_screen_item, container, false);
        fragmentIntroItemBinding.setLifecycleOwner(this);
        view = fragmentIntroItemBinding.itemLayout;
        introImg = fragmentIntroItemBinding.itemImg;
        introTitle = fragmentIntroItemBinding.introTitle;
        introDesc = fragmentIntroItemBinding.introDesc;

        introTitle.setText(introList.get(position).getTitle());
        introDesc.setText(introList.get(position).getDescription());
//        introImg.setImageResource(introList.get(position).getImgFile());
        loadingImage(context, introList.get(position).getImgFile(), fragmentIntroItemBinding.itemImg);
        return view;
    }
}
