package com.poona.agrocart.app;

import com.google.gson.JsonObject;

import org.json.JSONArray;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class AppConstants
{
    /*Development URL*/
    //public static final String BASE_URL = "";

    /*Live URL*/
    public static final String BASE_URL = "http://cmsweb.m-staging.in/poona_agro_testing/";

    /*Live Base URL for Image, Document*/
    public static final String IMAGE_DOC_BASE_URL = "";

    /*API methods*/
    public static final String GET = "GET";
    public static final String POST = "POST";
    public static final String DELETE = "DELETE";
    public static final String PUT = "PUT";

    /* Shared Preferences Parameters*/
    public static final String PREFERENCES_NAME = "PoonaAgroCartSharedPreferences";
    public static final String ANDROID_ID = "android_id";
    public static final String FCM_TOKEN = "fcm_token";
    public static final String IS_LOGGED_IN = "isLoggedIn";
    public static final String IS_READ_INTRO = "isIntroRead";
    public static final String FROM_LOG_OUT = "fromLogOut";
    public static final String IS_SKIP_BANK_DETAILS = "isSkippedBankDetails";
    public static final String IS_REMEMBER = "isRemember";
    public static final String BASE_AUTH_USERNAME = "BaseAuthUsername";
    public static final String BASE_AUTH_PASSWORD = "BaseAuthPassword";
    public static final String AUTHORIZATION_TOKEN = "AuthorizationToken";
    public static final String USER_TYPE = "user_type";
    public static final String PORTRAIT = "portrait";
    public static final String LANDSCAPE = "landscape";

    /*
    * api header parameters
    * */
    public static final String AUTH_ACCESS_TOKEN = "Access-Token";

    /*
     * api response codes
     * */
    public static final int STATUS_CODE_200 = 200; //success response code
    public static final int STATUS_CODE_400 = 400; //fail response code
    public static final int STATUS_CODE_401 = 401; //fail response code
    public static final int STATUS_CODE_403 = 403; //record or data not found response code
    public static final int STATUS_CODE_404 = 404; //record or data not found response code
    public static final int STATUS_CODE_405 = 405; //record or data not found response code
    public static final int STATUS_CODE_2 = 2; //inactive user
    public static final int STATUS_CODE_1 = 1;

    /*API names here*/
    public static final String FROM_SCREEN = "from_screen";
    public static final String LOGIN_API = "api-login-customer";
    public static final String VERIFY_OTP_API = "verify-otp-customer";
    public static final String REGISTER_API = "register-customer";
    public static final String INTRO_SCREEN_API = "screen-customer";
    public static final String AREA_API = "area";
    public static final String CITY_API = "city";
    public static final String UPDATE_LOCATION_API = "update-location-customer";
    public static final String HOME_BANNER_API = "home-screen-banner";
    public static final String HOME_CATEGORY_API = "category-list";
    public static final String HOME_BASKET_API = "basket-list";
    public static final String HOME_BEST_SELLING_API = "best-selling-product-list";
    public static final String HOME_EXCLUSIVE_API = "exclusive-product-list";
    public static final String HOME_SEASONAL_LIST_API = "seasonal-product-list";

    public static final String TAB_INDEX = "index";
    public static final String TOOLBAR_TITLE = "toolbar_title";
    public static final String LOGOUT = "logout";
    public static final String PUSH_NOTIFICATIONS = "push_notifications";

    public static final String ZERO = "0";
    public static final String ONE = "1";
    public static final String TWO = "2";
    public static final String THREE = "3";
    public static final String FOUR = "4";

    /*
    * if vendor is inactive or unauthorized then redirect to login page
    * */
    public static final String UNAUTHORIZED = "unauthorized";

    /*
    * cms content api parameters
    */
    public static final String CMS_NAME = "cms_name";
    public static final String CMS_ASSIGN_ID = "cms_assign_id";
    public static final String ABOUT_US = "about-us";
    public static final String TERMS_CONDITIONS = "term-and-condition";
    public static final String CONTACT_US = "contact-us";
    public static final String QUERY = "query";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";

    /*API KEYS*/
    public static final String MOBILE_NUMBER ="mobile";
    public static final String OTP ="otp";
    public static final String USERNAME ="username";
    public static final String EMAIL ="email";
    public static final String AREA_ID ="area_id";
    public static final String CITY_ID ="city_id";


    /*
    * id to handle the notification in the notification tray
    * */
    public static final int NOTIFICATION_RANDOM_ID = 0;
    public static final int NOTIFICATION_ID_BIG_IMAGE = 101;
    public static final String PUSH_NOTIFICATION = "com.poona.agrocart.NOTIFICATION";
    public static final String ADMIN = "admin";
    public static final String APP = "app";

    /*SignInFragment keys*/
    public static final String USER_MOBILE = "MOBILE_NO";
    public static final String COUNTRY_CODE ="COUNTRY_CODE";
    public static final String USER_OTP ="USER_OTP";
    public static final Object IS_NOTIFICATION_RECEIVED = "IS_NOTIFICATION_RECEIVED";

    //Help center
    public static final String TICKET_ID="TICKET_ID";
    public static final String STATUS="STATUS";
    public static final String REMARK="REMARK";
    public static final String DATE="DATE";
    public static final String SUBJECT="SUBJECT";

    //SharedPreferences keys
    public static String CART_LIST = "CART_LIST";
    public static String FAV_LIST = "FAV_LIST";
    public static final String pId="PAC";

}