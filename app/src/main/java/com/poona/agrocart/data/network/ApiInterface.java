package com.poona.agrocart.data.network;

import static com.poona.agrocart.app.AppConstants.AREA_API;
import static com.poona.agrocart.app.AppConstants.CITY_API;
import static com.poona.agrocart.app.AppConstants.HOME_BANNER_API;
import static com.poona.agrocart.app.AppConstants.HOME_BASKET_API;
import static com.poona.agrocart.app.AppConstants.HOME_CATEGORY_API;
import static com.poona.agrocart.app.AppConstants.HOME_EXCLUSIVE_API;
import static com.poona.agrocart.app.AppConstants.INTRO_SCREEN_API;
import static com.poona.agrocart.app.AppConstants.LOGIN_API;
import static com.poona.agrocart.app.AppConstants.REGISTER_API;
import static com.poona.agrocart.app.AppConstants.UPDATE_LOCATION_API;
import static com.poona.agrocart.app.AppConstants.VERIFY_OTP_API;

import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.IntroScreenResponse;
import com.poona.agrocart.data.network.reponses.SignInResponse;
import com.poona.agrocart.data.network.reponses.VerifyOtpResponse;

import java.util.HashMap;

import io.reactivex.rxjava3.core.Single;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public interface ApiInterface {
    @FormUrlEncoded
    @POST(LOGIN_API)
    Single<SignInResponse> getSignInResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(VERIFY_OTP_API)
    Single<VerifyOtpResponse> getVerifyOtpResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(REGISTER_API)
    Single<BaseResponse> getRegistrationResponse(@FieldMap HashMap<String, String> data);

    @GET(INTRO_SCREEN_API)
    Single<IntroScreenResponse> getIntroScreenResponse();

    @GET(AREA_API)
    Single<AreaResponse> getAreaResponse();

    @GET(CITY_API)
    Single<CityResponse> getCityResponse();

    @FormUrlEncoded
    @POST(UPDATE_LOCATION_API)
    Single<BaseResponse> updateLocationResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(HOME_BANNER_API)
    Single<BannerResponse> homeBannerResponse(@FieldMap HashMap<String, String> bannerParams);

    @FormUrlEncoded
    @POST(HOME_CATEGORY_API)
    Single<CategoryResponse> homeCategoryResponse(@FieldMap HashMap<String, String> categoryParams);

    @FormUrlEncoded
    @POST(HOME_BASKET_API)
    Single<BasketResponse> homeBasketResponse(@FieldMap HashMap<String, String> categoryParams);

    @FormUrlEncoded
    @POST(HOME_EXCLUSIVE_API)
    Single<ExclusiveResponse> homeExclusiveResponseSingle(@FieldMap HashMap<String, String> hashMap);
}