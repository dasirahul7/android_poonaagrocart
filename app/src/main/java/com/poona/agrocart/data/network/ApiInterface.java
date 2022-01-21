package com.poona.agrocart.data.network;

import static com.poona.agrocart.app.AppConstants.KEY_MOBILE;
import static com.poona.agrocart.app.AppConstants.LOGIN_API;

import android.database.Observable;


import com.poona.agrocart.ui.sign_in.model.LoginResponse;

import io.reactivex.Single;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public interface ApiInterface
{
    @FormUrlEncoded
    @POST(LOGIN_API)
    Call<LoginResponse> getLoginResponse(@Field(KEY_MOBILE) String UA_mobile);

   }