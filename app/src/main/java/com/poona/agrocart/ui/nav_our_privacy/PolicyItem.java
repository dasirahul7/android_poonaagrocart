package com.poona.agrocart.ui.nav_our_privacy;

import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.poona.agrocart.R;

public class PolicyItem {
    String policyTitle;
    int icon;

    public String getPolicyTitle() {
        return policyTitle;
    }

    public void setPolicyTitle(String policyTitle) {
        this.policyTitle = policyTitle;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public PolicyItem(String policyTitle, int icon) {
        this.policyTitle = policyTitle;
        this.icon = icon;
    }

    @BindingAdapter("setIcon")
    public static void setImage(ImageView view, int img){
        Glide.with(view.getContext())
                .load(img)
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.placeholder).into(view);
    }}
