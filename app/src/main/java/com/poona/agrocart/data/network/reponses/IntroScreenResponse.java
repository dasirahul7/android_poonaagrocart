package com.poona.agrocart.data.network.reponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.poona.agrocart.ui.intro.Intro;

import java.util.ArrayList;

public class IntroScreenResponse extends BaseResponse{
    @SerializedName("data")
    @Expose
    private ArrayList<Intro> introScreenItem;

    public ArrayList<Intro> getIntroScreenItem() {
        return introScreenItem;
    }

    public void setIntroScreenItem(ArrayList<Intro> introScreenItem) {
        this.introScreenItem = introScreenItem;
    }
}
