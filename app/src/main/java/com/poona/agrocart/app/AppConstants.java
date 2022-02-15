package com.poona.agrocart.app;

import com.google.gson.JsonObject;

import org.json.JSONArray;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class AppConstants {
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
    public static final String IS_VERIFIED = "verify";
    public static final String GOOGLE_API_KEY = "google_api_key";
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
    public static final int STATUS_CODE_402 = 402; //fail response code
    public static final int STATUS_CODE_403 = 403; //record or data not found response code
    public static final int STATUS_CODE_404 = 404; //record or data not found response code
    public static final int STATUS_CODE_405 = 405; //record or data not found response code
    public static final int STATUS_CODE_2 = 2; //inactive user
    public static final int STATUS_CODE_1 = 1;

    /*API names here*/
    /*login & register APIs*/
    public static final String FROM_SCREEN = "from_screen";
    public static final String LOGIN_API = "api-login-customer";
    public static final String VERIFY_OTP_API = "verify-otp-customer";
    public static final String RESEND_OTP = "resend-otp";
    public static final String REGISTER_API = "register-customer";
    public static final String INTRO_SCREEN_API = "screen-customer";
    public static final String STATE_API = "state";
    public static final String CITY_API = "city";
    public static final String AREA_API = "area";
    public static final String AREA_WITH_ID_API = "area-by-city-id";
    public static final String MY_PROFILE_API = "profile-view-customer";
    public static final String ADDRESS_LIST_API = "address-view";
    public static final String ADD_ADDRESS_API = "address-add";
    public static final String UPDATE_MY_PROFILE_API = "profile-update-customer";
    public static final String UPDATE_LOCATION_API = "update-location-customer";
    /*Home screen APIs*/
    public static final String HOME_API = "home";
    public static final String HOME_BANNER_API = "home-screen-banner";
    public static final String HOME_CATEGORY_API = "category-list";
    public static final String HOME_BASKET_API = "basket-list";
    public static final String HOME_BEST_SELLING_API = "best-selling-product-list";
    public static final String HOME_EXCLUSIVE_API = "exclusive-product-list";
    public static final String HOME_SEASONAL_LIST_API = "seasonal-product-list";
    public static final String HOME_PRODUCT_LIST_API = "product-list";
    public static final String HOME_STORE_BANNER_API = "store-banner";
    public static final String SIGN_OUT_API = "logout-customer";
    public static final String ADD_TO_FAVOURITE = "add-to-favourite";

    /*Favourite API's*/
    public static final String FAVOURITE_LIST_API = "view-favourite-list";

    /*Offer & Coupon APIs*/
    public static final String COUPON_API = "coupon-list";

    /*Our Store Api */
    public static final String STORE_LIST = "store-list";
    public static final String STORE_DETAILS = "store-details";

    /*Faq Api*/
    public static final String FAQ = "faq";
    public static final String CMS = "cms";

    /*Gallery Api*/
    public static final String VIEW_GALLERY = "view-gallery";

    /*Help Center Api*/
    public static final String TICKET_TYPE = "ticket-type";
    public static final String VIEW_TICKET = "view-ticket";
    public static final String ISSUE_TICKET = "issue-ticket";


    /*Product list API*/
    public static final String PRODUCT_LIST_BY_API = "product-by-category-list";

    /*Basket detail API*/
    public static final String BASKET_DETAIL_API="basket-details";
    public static final String PRODUCT_DETAIL_API="product-details";

    /*Favourite Api Keys*/
    public static final String ITEM_TYPE ="item_type";

    /*See all Listing from home keys*/
    public static final String AllBasket = "Basket";
    public static final String AllSelling = "Best selling";
    public static final String AllExclusive = "Exclusive";

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
    public static final String CMS_TYPE = "cms_type";
    public static final String CMS_ASSIGN_ID = "cms_assign_id";
    public static final String ABOUT_US = "about-us";
    public static final String TERMS_CONDITIONS = "term-and-condition";
    public static final String CONTACT_US = "contact-us";
    public static final String QUERY = "query";
    public static final String LIMIT = "limit";
    public static final String OFFSET = "offset";
    public static final String SEARCH = "search";

    /*API KEYS*/
    public static final String MOBILE_NUMBER = "mobile";
    public static final String OTP = "otp";
    public static final String USERNAME = "username";
    public static final String EMAIL = "email";
    public static final String STATE_ID = "state_id";
    public static final String AREA_ID = "area_id";
    public static final String CITY_ID = "city_id";
    public static final String SEARCH_KEY = "search_key";
    public static final String SEARCH_TYPE = "search_type";
    public static final String USER_ID = "user_id";
    public static final String USER_ADDRESS = "user_address";

    /*Home parameters*/
    public static final String CATEGORY_ID = "category_id";
    public static final String LIST_TITLE = "list_title";
    public static final String LIST_TYPE = "list_type";

    /*Basket API Keys*/
    public static final String BASKET_ID ="basket_id";
    public static final String ADD_TO_BASKET ="add-to-cart-basket";

    /*Product API Keys*/
    public static final String PRODUCT_ID ="product_id";
    public static final String ADD_TO_PRODUCT ="add-to-cart-product";
    public static final String PU_ID ="pu_id";
    public static final String QUANTITY ="quantity";

    /*Category types*/
    public static final String BASKET = "0";
    public static final String PRODUCT = "1";

    /*profile api parameters*/
    public static final String CUSTOMER_ID = "customer_id";
    public static final String PROFILE_IMAGE = "image";
    public static final String NAME = "name";
    public static final String ALTERNATE_MOBILE_NUMBER = "alternate_mobile";
    public static final String GENDER = "gender";
    public static final String DATE_OF_BIRTH = "date_of_birth";

    /*add address post parameters*/
    public static final String ADDRESS_TYPE = "address_type";
    public static final String MOBILE = "mobile";
    public static final String CITY_ = "city_id_fk";
    public static final String AREA_ = "area_id_fk";
    public static final String PIN_CODE = "pincode";
    public static final String APARTMENT_NAME = "appartment_name";
    public static final String HOUSE_NO = "house_no";
    public static final String STREET = "street";
    public static final String LANDMARK = "landmark";
    public static final String LATITUDE = "latitude";
    public static final String LONGITUDE = "longitude";
    public static final String GOOGLE_MAP_ADDRESS = "map_address";

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
    public static final String COUNTRY_CODE = "COUNTRY_CODE";
    public static final String USER_OTP = "USER_OTP";
    public static final String USER_VERIFIED = "USER_VERIFIED";
    public static final Object IS_NOTIFICATION_RECEIVED = "IS_NOTIFICATION_RECEIVED";

    //Help center
    public static final String TICKET_ID = "TICKET_ID";
    public static final String STATUS = "STATUS";
    public static final String REMARK = "REMARK";
    public static final String DATE = "DATE";
    public static final String SUBJECT = "SUBJECT";

    /*Help center parameter and data*/
    public static final String ISSUE_ID = "issue_id";
    public static final String TICKET_SUBJECT = "subject";
    public static final String TICKET_REMARK = "remark";

    //SharedPreferences keys
    public static String CART_LIST = "CART_LIST";
    public static String FAV_LIST = "FAV_LIST";
    public static final String pId = "PAC";

    /*Search Types here*/
    public static final String SEARCH_PRODUCT = "0";
    public static final String SEARCH_CATEGORY = "1";
    public static final String SEARCH_BASKET = "2";

    /*Store Parameter*/
    public static String STORE_ID = "store_id";
}