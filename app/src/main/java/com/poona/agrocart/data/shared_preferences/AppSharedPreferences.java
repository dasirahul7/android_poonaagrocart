package com.poona.agrocart.data.shared_preferences;

import static com.poona.agrocart.app.AppConstants.AUTHORIZATION_TOKEN;
import static com.poona.agrocart.app.AppConstants.COUNTRY_CODE;
import static com.poona.agrocart.app.AppConstants.FCM_TOKEN;
import static com.poona.agrocart.app.AppConstants.FROM_LOG_OUT;
import static com.poona.agrocart.app.AppConstants.GOOGLE_API_KEY;
import static com.poona.agrocart.app.AppConstants.IS_LOGGED_IN;
import static com.poona.agrocart.app.AppConstants.IS_READ_INTRO;
import static com.poona.agrocart.app.AppConstants.IS_VERIFIED;
import static com.poona.agrocart.app.AppConstants.PREFERENCES_NAME;
import static com.poona.agrocart.app.AppConstants.USERNAME;
import static com.poona.agrocart.app.AppConstants.USERPROFILE;
import static com.poona.agrocart.app.AppConstants.USER_ADDRESS;
import static com.poona.agrocart.app.AppConstants.USER_ID;
import static com.poona.agrocart.app.AppConstants.USER_MOBILE;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.poona.agrocart.R;
import com.poona.agrocart.ui.home.model.ProductOld;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class AppSharedPreferences {
    //Shared Preferences
    SharedPreferences preferences;
    //Editor for Shared preferences
    SharedPreferences.Editor editor;
    //Context
    Context context;
    //Shared preferences mode
    int PRIVATE_MODE = 0;
    String googleApiKey;
    private String fireBaseToken, authorizationToken, uid, userMobile, userCountry, userAddress, baseAuthUsername, baseAuthPassword, userName, userProfile;
    private int newOrderCount;
    private boolean isLoggedIn, isIntroRead, fromLogOut, isVerified;
    private String userType;

    //Constructor
    public AppSharedPreferences(Context context) {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public String getFCMToken() {
        return this.preferences.getString(FCM_TOKEN, "");
    }

    public void setFCMToken(String deviceToken) {
        this.fireBaseToken = deviceToken;
        this.editor.putString(FCM_TOKEN, deviceToken);
        this.editor.commit();
    }

    public boolean getIsLoggedIn() {
        return this.preferences.getBoolean(IS_LOGGED_IN, false);
    }

    public void setIsLoggedIn(boolean isLoggedIn) {
        this.isLoggedIn = isLoggedIn;
        this.editor.putBoolean(IS_LOGGED_IN, isLoggedIn);
        this.editor.commit();
    }

    public boolean getIsIntroRead() {
        return this.preferences.getBoolean(IS_READ_INTRO, false);
    }

    public void setIsIntroRead(boolean isIntroRead) {
        this.isIntroRead = isIntroRead;
        this.editor.putBoolean(IS_READ_INTRO, isIntroRead);
        this.editor.commit();
    }

    public boolean isVerified() {
        return this.preferences.getBoolean(IS_VERIFIED, false);
    }

    public void seIstVerified(boolean verified) {
        this.isVerified = verified;
        this.editor.putBoolean(IS_VERIFIED, verified);
        this.editor.commit();
    }

    public boolean getFromLogOut() {
        return this.preferences.getBoolean(FROM_LOG_OUT, false);
    }

    public void setFromLogOut(boolean fromLogOut) {
        this.fromLogOut = fromLogOut;
        this.editor.putBoolean(FROM_LOG_OUT, fromLogOut);
        this.editor.commit();
    }

    public String getAuthorizationToken() {
        /*
         * default value is app name, if it is empty then app will crash
         * */
        return this.preferences.getString(AUTHORIZATION_TOKEN, context.getResources().getString(R.string.app_name));
    }

    public void setAuthorizationToken(String authorizationToken) {
        this.authorizationToken = authorizationToken;
        this.editor.putString(AUTHORIZATION_TOKEN, authorizationToken);
        this.editor.commit();
    }

    public String getUid() {
        return preferences.getString(USER_ID, "");
    }

    public void setUid(String uid) {
        this.uid = uid;
        this.editor.putString(USER_ID, uid);
        this.editor.apply();
    }

    public String getUserMobile() {
        return preferences.getString(USER_MOBILE, "");
    }

    public void setUserMobile(String userMobile) {
        this.userMobile = userMobile;
        this.editor.putString(USER_MOBILE, userMobile);
        this.editor.commit();
    }

    public String getUserCountry() {
        return preferences.getString(COUNTRY_CODE, "");
    }

    public void setUserCountry(String userCountry) {
        this.userCountry = userCountry;
        this.editor.putString(COUNTRY_CODE, userCountry);
        this.editor.commit();
    }

    public String getUserName() {
        return this.preferences.getString(USERNAME, "");
    }

    public void setUserName(String userName) {
        this.userName = userName;
        this.editor.putString(USERNAME, userName);
        this.editor.commit();
    }

    public String getUserProfile() {
        return this.preferences.getString(USERPROFILE, "");
    }

    public void setUserProfile(String userProfile) {
        this.userProfile = userProfile;
        this.editor.putString(USERPROFILE, userProfile);
        this.editor.commit();
    }

    public String getUserAddress() {
        return preferences.getString(USER_ADDRESS, "");
    }

    public void setUserAddress(String userAddress) {
        this.userAddress = userAddress;
        this.editor.putString(USER_ADDRESS, userAddress);
        this.editor.commit();
    }

    public void clearSharedPreferences(Context context) {
        File dir = new File(context.getFilesDir().getParent() + "/shared_prefs/");
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().apply();

        //delete the file
        if (dir.exists() && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                if (children[i].replace(".xml", "").equals(PREFERENCES_NAME)) {
                    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().apply();
                    new File(dir, children[i]).delete();
                }
            }
        }
    }

    public void saveCartArrayList(ArrayList<ProductOld> list, String key) {

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public void saveFavArrayList(ArrayList<ProductOld> list, String key) {

        Gson gson = new Gson();
        String json = gson.toJson(list);
        editor.putString(key, json);
        editor.apply();     // This line is IMPORTANT !!!
    }

    public ArrayList<ProductOld> getSavedCartList(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        Type type = new TypeToken<ArrayList<ProductOld>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public ArrayList<ProductOld> getSavedFavList(String key) {
        Gson gson = new Gson();
        String json = preferences.getString(key, null);
        Type type = new TypeToken<ArrayList<ProductOld>>() {
        }.getType();
        return gson.fromJson(json, type);
    }

    public String getGoogleApiKey() {
        return this.preferences.getString(GOOGLE_API_KEY, "");
    }

    public void setGoogleApiKey(String googleApiKey) {
        this.googleApiKey = googleApiKey;
        this.editor.putString(GOOGLE_API_KEY, googleApiKey);
        this.editor.commit();
    }

    public void removeAll() {
        this.editor.clear();
        this.editor.commit();
    }
}