package com.poona.agrocart.data.network;

import static com.poona.agrocart.app.AppConstants.ADD_ADDRESS_API;
import static com.poona.agrocart.app.AppConstants.ADD_TO_BASKET;
import static com.poona.agrocart.app.AppConstants.ADD_TO_FAVOURITE;
import static com.poona.agrocart.app.AppConstants.ADD_TO_PRODUCT;
import static com.poona.agrocart.app.AppConstants.AREA_WITH_ID_API;
import static com.poona.agrocart.app.AppConstants.BASKET_DETAIL_API;
import static com.poona.agrocart.app.AppConstants.CHECK_VALID_PIN_CODE_API;
import static com.poona.agrocart.app.AppConstants.CITY_API;
import static com.poona.agrocart.app.AppConstants.CITY_WITH_ID_API;
import static com.poona.agrocart.app.AppConstants.CMS;
import static com.poona.agrocart.app.AppConstants.COUPON_API;
import static com.poona.agrocart.app.AppConstants.DELETE_ADDRESS_API;
import static com.poona.agrocart.app.AppConstants.FAQ;
import static com.poona.agrocart.app.AppConstants.FAVOURITE_LIST_API;
import static com.poona.agrocart.app.AppConstants.HOME_API;
import static com.poona.agrocart.app.AppConstants.HOME_BANNER_API;
import static com.poona.agrocart.app.AppConstants.HOME_BASKET_API;
import static com.poona.agrocart.app.AppConstants.HOME_BEST_SELLING_API;
import static com.poona.agrocart.app.AppConstants.HOME_CATEGORY_API;
import static com.poona.agrocart.app.AppConstants.HOME_EXCLUSIVE_API;
import static com.poona.agrocart.app.AppConstants.HOME_PRODUCT_LIST_API;
import static com.poona.agrocart.app.AppConstants.HOME_SEASONAL_LIST_API;
import static com.poona.agrocart.app.AppConstants.HOME_STORE_BANNER_API;
import static com.poona.agrocart.app.AppConstants.INTRO_SCREEN_API;
import static com.poona.agrocart.app.AppConstants.ISSUE_TICKET;
import static com.poona.agrocart.app.AppConstants.LOGIN_API;
import static com.poona.agrocart.app.AppConstants.MY_PROFILE_API;
import static com.poona.agrocart.app.AppConstants.ADDRESS_LIST_API;
import static com.poona.agrocart.app.AppConstants.PRODUCT_DETAIL_API;
import static com.poona.agrocart.app.AppConstants.PRODUCT_LIST_BY_API;
import static com.poona.agrocart.app.AppConstants.REGISTER_API;
import static com.poona.agrocart.app.AppConstants.REMOVE_FAVOURITE;
import static com.poona.agrocart.app.AppConstants.REPLY_TO_TICKET;
import static com.poona.agrocart.app.AppConstants.RESEND_OTP;
import static com.poona.agrocart.app.AppConstants.STATE_API;
import static com.poona.agrocart.app.AppConstants.STORE_DETAILS;
import static com.poona.agrocart.app.AppConstants.STORE_LIST;
import static com.poona.agrocart.app.AppConstants.SIGN_OUT_API;
import static com.poona.agrocart.app.AppConstants.TICKET_TYPE;
import static com.poona.agrocart.app.AppConstants.UPDATE_ADDRESS_API;
import static com.poona.agrocart.app.AppConstants.UPDATE_LOCATION_API;
import static com.poona.agrocart.app.AppConstants.UPDATE_MY_PROFILE_API;
import static com.poona.agrocart.app.AppConstants.VERIFY_OTP_API;
import static com.poona.agrocart.app.AppConstants.VIEW_CHATS;
import static com.poona.agrocart.app.AppConstants.VIEW_GALLERY;
import static com.poona.agrocart.app.AppConstants.VIEW_TICKET;

import com.poona.agrocart.data.network.responses.AddressesResponse;
import com.poona.agrocart.data.network.responses.AreaResponse;
import com.poona.agrocart.data.network.responses.BannerResponse;
import com.poona.agrocart.data.network.responses.BaseResponse;
import com.poona.agrocart.data.network.responses.BasketDetailsResponse;
import com.poona.agrocart.data.network.responses.BasketResponse;
import com.poona.agrocart.data.network.responses.BestSellingResponse;
import com.poona.agrocart.data.network.responses.CategoryResponse;
import com.poona.agrocart.data.network.responses.CityResponse;
import com.poona.agrocart.data.network.responses.CouponResponse;
import com.poona.agrocart.data.network.responses.ExclusiveResponse;
import com.poona.agrocart.data.network.responses.HomeResponse;
import com.poona.agrocart.data.network.responses.help_center_response.CreateTicketResponse;
import com.poona.agrocart.data.network.responses.help_center_response.SendMessageResponse;
import com.poona.agrocart.data.network.responses.help_center_response.TicketListResponse;
import com.poona.agrocart.data.network.responses.help_center_response.TicketTypeResponse;
import com.poona.agrocart.data.network.responses.IntroScreenResponse;
import com.poona.agrocart.data.network.responses.ProductDetailsResponse;
import com.poona.agrocart.data.network.responses.ProductListByResponse;
import com.poona.agrocart.data.network.responses.ProductListResponse;
import com.poona.agrocart.data.network.responses.ProfileResponse;
import com.poona.agrocart.data.network.responses.SeasonalProductResponse;
import com.poona.agrocart.data.network.responses.SignInResponse;
import com.poona.agrocart.data.network.responses.StateResponse;
import com.poona.agrocart.data.network.responses.StoreBannerResponse;
import com.poona.agrocart.data.network.responses.VerifyOtpResponse;
import com.poona.agrocart.data.network.responses.CmsResponse;
import com.poona.agrocart.data.network.responses.favoutiteResponse.FavouriteLisResponse;
import com.poona.agrocart.data.network.responses.galleryResponse.GalleryResponse;
import com.poona.agrocart.data.network.responses.help_center_response.recieveMessage.RecieveMessageResponse;
import com.poona.agrocart.data.network.reponses.AddressesResponse;
import com.poona.agrocart.data.network.reponses.AreaResponse;
import com.poona.agrocart.data.network.reponses.BannerResponse;
import com.poona.agrocart.data.network.reponses.BaseResponse;
import com.poona.agrocart.data.network.reponses.BasketDetailsResponse;
import com.poona.agrocart.data.network.reponses.BasketResponse;
import com.poona.agrocart.data.network.reponses.BestSellingResponse;
import com.poona.agrocart.data.network.reponses.CategoryResponse;
import com.poona.agrocart.data.network.reponses.CityResponse;
import com.poona.agrocart.data.network.reponses.CouponResponse;
import com.poona.agrocart.data.network.reponses.ExclusiveResponse;
import com.poona.agrocart.data.network.reponses.HomeResponse;
import com.poona.agrocart.data.network.reponses.favoutiteResponse.FavouriteListResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.CreateTicketResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketListResponse;
import com.poona.agrocart.data.network.reponses.help_center_response.TicketTypeResponse;
import com.poona.agrocart.data.network.reponses.IntroScreenResponse;
import com.poona.agrocart.data.network.reponses.ProductDetailsResponse;
import com.poona.agrocart.data.network.reponses.ProductListByResponse;
import com.poona.agrocart.data.network.reponses.ProductListResponse;
import com.poona.agrocart.data.network.reponses.ProfileResponse;
import com.poona.agrocart.data.network.reponses.SeasonalProductResponse;
import com.poona.agrocart.data.network.reponses.SignInResponse;
import com.poona.agrocart.data.network.reponses.StateResponse;
import com.poona.agrocart.data.network.reponses.StoreBannerResponse;
import com.poona.agrocart.data.network.reponses.VerifyOtpResponse;
import com.poona.agrocart.data.network.reponses.CmsResponse;
import com.poona.agrocart.data.network.reponses.galleryResponse.GalleryResponse;
import com.poona.agrocart.ui.nav_faq.model.FaqListResponse;
import com.poona.agrocart.ui.nav_stores.model.OurStoreListResponse;
import com.poona.agrocart.ui.nav_stores.model.store_details.OurStoreViewDataResponse;

import java.util.HashMap;

import io.reactivex.Completable;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.PartMap;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public interface ApiInterface {
    /*Login API*/
    @FormUrlEncoded
    @POST(LOGIN_API)
    Single<SignInResponse> getSignInResponse(@FieldMap HashMap<String, String> data);

    /*Verify otp API*/
    @FormUrlEncoded
    @POST(VERIFY_OTP_API)
    Single<VerifyOtpResponse> getVerifyOtpResponse(@FieldMap HashMap<String, String> data);

    //Resend OTP here
    @FormUrlEncoded
    @POST(RESEND_OTP)
    Single<SignInResponse> getResendOtpResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(REGISTER_API)
    Single<BaseResponse> getRegistrationResponse(@FieldMap HashMap<String, String> data);

    @GET(INTRO_SCREEN_API)
    Single<IntroScreenResponse> getIntroScreenResponse();

    @FormUrlEncoded
    @POST(MY_PROFILE_API)
    Observable<ProfileResponse> getProfileObservableResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(MY_PROFILE_API)
    Single<ProfileResponse> getProfileResponse(@FieldMap HashMap<String, String> data);

    @GET(ADDRESS_LIST_API)
    Single<AddressesResponse> getAddressesListResponse();

    @Multipart
    @POST(UPDATE_MY_PROFILE_API)
    Single<ProfileResponse> updateProfileResponse(@PartMap HashMap<String, RequestBody> data,
                                                  @Part MultipartBody.Part profilePhoto);

    @GET(STATE_API)
    Observable<StateResponse> getStateObservableResponse();

    @FormUrlEncoded
    @POST(CITY_WITH_ID_API)
    Observable<CityResponse> getCityObservableResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(AREA_WITH_ID_API)
    Observable<AreaResponse> getAreaObservableResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(CITY_WITH_ID_API)
    Single<CityResponse> getCityResponse(@FieldMap HashMap<String, String> data);

    @GET(CITY_API)
    Single<CityResponse> getCityResponse();

    @FormUrlEncoded
    @POST(AREA_WITH_ID_API)
    Single<AreaResponse> getAreaResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(ADD_ADDRESS_API)
    Single<BaseResponse> addAddressResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(UPDATE_ADDRESS_API)
    Single<BaseResponse> updateAddressResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(DELETE_ADDRESS_API)
    Single<BaseResponse> deleteAddressResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(UPDATE_LOCATION_API)
    Single<BaseResponse> updateLocationResponse(@FieldMap HashMap<String, String> data);

    @FormUrlEncoded
    @POST(CHECK_VALID_PIN_CODE_API)
    Single<BaseResponse> checkPinCodeAvailableResponse(@FieldMap HashMap<String, String> data);

    //Home Banner API
    @GET(HOME_BANNER_API)
    Single<BannerResponse> homeBannerResponse();

    //Home Category API
    @FormUrlEncoded
    @POST(HOME_CATEGORY_API)
    Single<CategoryResponse> homeCategoryResponse(@FieldMap HashMap<String, String> categoryParams);

    //Home Basket list API
    @FormUrlEncoded
    @POST(HOME_BASKET_API)
    Single<BasketResponse> homeBasketResponse(@FieldMap HashMap<String, String> categoryParams);

    //Home Product Offer API
    @FormUrlEncoded
    @POST(HOME_EXCLUSIVE_API)
    Single<ExclusiveResponse> homeExclusiveResponseSingle(@FieldMap HashMap<String, String> hashMap);

    //Home Best selling API
    @FormUrlEncoded
    @POST(HOME_BEST_SELLING_API)
    Single<BestSellingResponse> homeBestSellingResponseSingle(@FieldMap HashMap<String, String> hashMap);

    //Home Seasonal ProductOld API
    @FormUrlEncoded
    @POST(HOME_SEASONAL_LIST_API)
    Single<SeasonalProductResponse> homeSeasonalListResponse(@FieldMap HashMap<String, String> hashMap);

    //Home ProductOld list API
    @FormUrlEncoded
    @POST(HOME_PRODUCT_LIST_API)
    Single<ProductListResponse> homeProductListResponse(@FieldMap HashMap<String, String> hashMap);

    // Home Store Banner API
    @GET(HOME_STORE_BANNER_API)
    Single<StoreBannerResponse> homeStoreBannerResponse();

    /*Coupon API*/
    @FormUrlEncoded
    @POST(COUPON_API)
    Single<CouponResponse> couponListResponse(@FieldMap HashMap<String, String> hashMap);

    /*Store listing API*/
    @FormUrlEncoded
    @POST(STORE_LIST)
    Single<OurStoreListResponse> getOurStoreList(@FieldMap HashMap<String, String> ourStoreInputParameter);

    /*Logout API*/
    @FormUrlEncoded
    @POST(SIGN_OUT_API)
    Single<BaseResponse> signOutResponse(@FieldMap HashMap<String, String> hashMap);

    /*Product list by Category API*/
    @FormUrlEncoded
    @POST(PRODUCT_LIST_BY_API)
    Single<ProductListByResponse> productsByCategoryResponse(@FieldMap HashMap<String,String> hashMap);

    @FormUrlEncoded
    @POST(STORE_DETAILS)
    Single<OurStoreViewDataResponse> getOurStoreDetails(@FieldMap HashMap<String, String> ourStoreDetailsInputParameter);

    /*FAQ API here*/
    @GET(FAQ)
    Single<FaqListResponse> getAddFaqs();

    /*Cms API Here*/
    @GET(CMS)
    Single<CmsResponse> getCmsResponse();

    /*Product Details API*/
    @FormUrlEncoded
    @POST(PRODUCT_DETAIL_API)
    Single<ProductDetailsResponse> getProductDetailsResponse(@FieldMap HashMap<String,String> hashMap);

    /*Basket detail API*/
    @FormUrlEncoded
    @POST(BASKET_DETAIL_API)
    Single<BasketDetailsResponse> getBasketDetailsResponse(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ADD_TO_BASKET)
    Single<BaseResponse> addToBasketResponse(@FieldMap HashMap<String, String> hashMap);

    /*Add to favourite API*/
    @FormUrlEncoded
    @POST(ADD_TO_FAVOURITE)
    Single<BaseResponse> addToFavouriteResponse(@FieldMap HashMap<String,String> hashMap);

    /*Remove From Favourite*/
    @FormUrlEncoded
    @POST(REMOVE_FAVOURITE)
    Single<BaseResponse> removeFromFavouriteResponse(@FieldMap HashMap<String,String> hashMap);

    /*View Gallery ResponseData*/
    @GET(VIEW_GALLERY)
    Single<GalleryResponse> getGalleryReponse();

    /*Add to cart Product API*/
    @FormUrlEncoded
    @POST(ADD_TO_PRODUCT)
    Single<BaseResponse> addToCartProductResponse(@FieldMap HashMap<String,String> hashMap);

    /*Favourite ResponseData*/
    @GET(TICKET_TYPE)
    Single<TicketTypeResponse> getTicketType();

    @FormUrlEncoded
    @POST(VIEW_TICKET)
    Single<TicketListResponse> getTicketList(@FieldMap HashMap<String, String> ticketListInputParameter);

    /*Favourite Response*/
    @GET(FAVOURITE_LIST_API)
    Single<FavouriteListResponse> getFavouriteList();

    /*Home API*/
    @FormUrlEncoded
    @POST(HOME_API)
    Single<HomeResponse> getHomeAllData(@FieldMap HashMap<String, String> hashMap);

    @FormUrlEncoded
    @POST(ISSUE_TICKET)
    Single<CreateTicketResponse> getCreateTicket(@FieldMap HashMap<String, String> createTicketInputParameter);

    @FormUrlEncoded
    @POST(VIEW_CHATS)
    Single<RecieveMessageResponse> getReceiveMessage(@FieldMap HashMap<String, String> recieveMessageInputParameter);

    @FormUrlEncoded
    @POST(REPLY_TO_TICKET)
    Single<SendMessageResponse> getSenderMessage(@FieldMap HashMap<String, String> sendMessageParameters);;
}