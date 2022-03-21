package com.poona.agrocart.app;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class AppConstants {
    /*m-staging URL*/
    /*Development URL*/
    public static final String BASE_URL = "https://cmsweb.m-staging.in/poona_agro_testing_dev/";

    /*Testing environment URL*/
//    public static final String BASE_URL = "https://cmsweb.m-staging.in/poona_agro_testing/";

    /*m-staging Base URL for Image, Document*/
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
    public static final String CITY_WITH_ID_API = "city-by-state-id";
    public static final String AREA_WITH_ID_API = "area-by-city-id";
    public static final String MY_PROFILE_API = "profile-view-customer";
    public static final String UPDATE_MY_PROFILE_API = "profile-update-customer";
    public static final String VIEW_PROFILE_API = "view-profile";
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
    public static final String REMOVE_FAVOURITE = "remove-from-favourite";

    /*Favourite API's*/
    public static final String FAVOURITE_LIST_API = "view-favourite-list";
    public static final String REMOVE_FAVOURITE_ITEM_API = "remove-from-favourite";

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
    public static final String VIEW_CHATS = "view-chats";
    public static final String REPLY_TO_TICKET = "reply-to-ticket";

    /*Setting Api*/
    public static final String UPDATE_CONFIGURATION = "update-configuration";
    public static final String VIEW_CONFIGURATION = "view-configuration";

    /*Cart lis api*/
    public static final String CART_LIST_API = "view-cart";
    public static final String DELETE_CART_ITEM_API = "delete-cart-by-id";
    public static final String DELETE_CART_LIST_API = "delete-cart-all";

    /*Product list API*/
    public static final String PRODUCT_LIST_BY_API = "product-by-category-list";
    public static final String RATE_TO_PRODUCT = "rate-product";

    /*Basket detail API*/
    public static final String BASKET_DETAIL_API = "basket-details";
    public static final String PRODUCT_DETAIL_API = "product-details";
    public static final String RATE_TO_BASKET = "rate-basket";

    /*Addresses screen apis*/
    public static final String ADDRESS_LIST_API = "address-view";
    public static final String ADD_ADDRESS_API = "address-add";
    public static final String UPDATE_ADDRESS_API = "address-update";
    public static final String DELETE_ADDRESS_API = "address-delete";
    public static final String CHECK_VALID_PIN_CODE_API = "check-pincode-availablity";
    public static final String SET_DEFAULT_ADDRESS_API = "make-address-default";

    /*Search API*/
    public static final String COMMON_SEARCH="common-search";

    /*Filter Listing API*/
    public static final String FILTER_LIST="filter-list";

    /*Search api Keys*/
    public static final String SEARCH_VALUE = "search_value";

    /*Payment related APIs*/
    public static final String PAYMENT_CRED_API = "payment-credentials";

    /*Favourite Api Keys*/
    public static final String ITEM_TYPE = "item_type";

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
    public static final String USERPROFILE = "profile";
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
    public static final String BASKET_ID = "basket_id";
    /*Add to cart APIs*/
    public static final String ADD_TO_BASKET = "add-to-cart-basket";
    public static final String ADD_TO_PRODUCT = "add-to-cart-product";
    public static final String REMOVE_FROM_CART_PRODUCT = "remove-cart-by-product-id";
    public static final String REMOVE_FROM_CART_BASKET = "remove-cart-by-product-id";

    /*Product API Keys*/
    public static final String PRODUCT_ID = "product_id";

    public static final String PU_ID = "pu_id";
    public static final String QUANTITY = "quantity";

    /*Notification*/
    public static final String MY_NOTIFICATION = "my-notifications";
    public static final String DELETE_NOTIFICATION = "delete-notifications";

    /*Order Summary Api*/
    public static final String ORDER_SUMMARY_API = "order-summary-customer";
    public static final String ORDER_PLACE_API = "place-order-customer";
    public static final String APPLY_COUPON_API = "apply-coupon";
    public static final String SLOT_BY_DATE_API = "get-available-slot-by-date";

    /*Order Summary API KEYS*/
    public static final String ORDER_SUMMARY ="order-summary";
    public static final String DELIVERY_ADDRESS ="delivery-address";
    public static final String COUPON_ID ="coupon_id";
    public static final String COUPON_CODE ="coupon_code";
    public static final String ADDRESS_P_ID = "address_primary_id";
    public static final String DELIVERY_DATE = "delivery_date";
    public static final String SLOT_ID = "slot_id";
    public static final String PAYMENT_MODE_ID = "payment_mode_id";
    public static final String PAYMENT_AMOUNT = "payment_amount";
    public static final String PAYMENT_SECRET_KEY = "payment_secret_key";
    public static final String PAYMENT_TYPE = "payment_type";
    public static final String PAYMENT_CURRENCY = "currency";
    public static final String PAYMENT_REFERENCE_ID = "payment_reference_id";
    public static final String PAYMENT_STATUS = "payment_status";
    /*Order view API*/
    public static final String RATE_ORDER = "rate-order";
    public static final String ORDER_CANCEL_REASON = "order-cancel-reason";
    public static final String ORDER_CANCEL = "order-cancel";
    public static final String CUSTOMER_ORDER_LIST = "customer-order-list";
    public static final String MY_ORDER_Details_CUSTOMER = "my-order-details-customer";
    public static final String MY_SUBSCRIBE_BASKET_LIST_CUSTOMER = "subscribe-basket-list-customer";
    public static final String MY_SUBSCRIBE_BASKET_DETAILS_CUSTOMER = "my-subscribe-basket-details-customer";
    public static final String SUBSCRIBE_BASKET_PRODUCTS = "subscribed-basket-products";
    public static final String SUBSCRIBE_BASKET_CUSTOMER = "subscribe-basket-customer";
    public static final String ORDER_TRACK = "order-track";

    /*Seasonal APIs*/
    public static final String SEASONAL_REGISTER ="seasonal-product-registeration";
    public static final String SEASONAL_DETAILS ="seasonal-product-details";

   /* Rating and Review */
   public static final String RATING = "rating";
   public static final String REVIEW = "review";
   public static final String REVIEW_LIST = "review_list";

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
    public static final String ADD_UPDATE_ADDRESS_DETAILS = "add_update_address_details";
    public static final String ADD_ADDRESS_DETAILS = "add_address_details";
    public static final String UPDATE_ADDRESS_DETAILS = "update_address_details";
    public static final String ADDRESS_DETAILS = "address_details";
    public static final String ADDRESS_ID = "address_primary_id";
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
    * Cart Screen Parameters
    * */
    public static final String CART_ID = "cart_id";

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
    //public static final String TICKET_ID = "TICKET_ID";
    public static final String STATUS = "STATUS";
    public static final String REMARK = "REMARK";
    public static final String DATE = "DATE";
    public static final String SUBJECT = "SUBJECT";

    /*Help center parameter and data*/
    public static final String ISSUE_ID = "issue_id";
    public static final String TICKET_SUBJECT = "subject";
    public static final String TICKET_REMARK = "remark";
    public static final String TICKET_ID = "ticket_id";
    public static final String MESSAGE = "message";
    public static final String pId = "PAC";
    /*Search Types here*/
    public static final String SEARCH_PRODUCT = "0";
    public static final String SEARCH_CATEGORY = "1";
    public static final String SEARCH_BASKET = "2";
    //SharedPreferences keys
    public static String CART_LIST = "CART_LIST";
    public static String FAV_LIST = "FAV_LIST";
    /*Store Parameter*/
    public static String STORE_ID = "store_id";

    /*Setting Parameter*/

    public static String EMAIL_NOTIFICATION_STATUS = "email_notification_status";
    public static String APP_NOTIFICATION_STATUS = "app_notification_status";

    /*Order Module Parameter*/
    public static String ORDER_ID = "order_id";
    public static String ORDER_SUBSCRIPTION_ID = "order_subscription_id";
    public static String CANCEL_ID = "cancel_id";
    public static final String ITEM_LIST = "item_list";

    public static final String SUBSCRIPTION_TYPE = "subscription_type";
    public static final String NO_OF_SUBSCRIPTION = "no_of_subscription";
    public static final String START_DATE = "start_date";
    //public static final String SLOT_ID = "slot_id";
   // public static final String PAYMENT_MODE_ID = "payment_mode_id";
   // public static final String BASKET_ID = "basket_id";
    public static final String SLOT_START_TIME = "slot_start_time";
    public static final String SLOT_END_TIME = "slot_end_time";

}

