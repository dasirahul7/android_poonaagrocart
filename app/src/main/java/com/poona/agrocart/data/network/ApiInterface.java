package com.poona.agrocart.data.network;

import static com.poona.agrocart.app.AppConstants.LOGIN_API;

import com.poona.agrocart.data.network.reponses.SignInResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public interface ApiInterface {
    @FormUrlEncoded
    @POST(LOGIN_API)
    Single<SignInResponse> getSignInResponse(@FieldMap HashMap<String, String> data);
}