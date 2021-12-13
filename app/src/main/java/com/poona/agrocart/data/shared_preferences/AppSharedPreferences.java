package com.poona.agrocart.data.shared_preferences;

import static com.poona.agrocart.app.AppConstants.AUTHORIZATION_TOKEN;
import static com.poona.agrocart.app.AppConstants.FCM_TOKEN;
import static com.poona.agrocart.app.AppConstants.FROM_LOG_OUT;
import static com.poona.agrocart.app.AppConstants.IS_LOGGED_IN;
import static com.poona.agrocart.app.AppConstants.IS_READ_INTRO;
import static com.poona.agrocart.app.AppConstants.PREFERENCES_NAME;

import android.content.Context;
import android.content.SharedPreferences;

import com.poona.agrocart.R;

import java.io.File;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class AppSharedPreferences
{
    //Shared Preferences
    SharedPreferences preferences;
    //Editor for Shared preferences
    SharedPreferences.Editor editor;
    //Context
    Context context;
    //Shared preferences mode
    int PRIVATE_MODE = 0;

    private String fireBaseToken, authorizationToken,uid, baseAuthUsername, baseAuthPassword;
    private int newOrderCount;
    private boolean isLoggedIn,isIntroRead, fromLogOut, isSkipBankForm;
    private String userType;

    //Constructor
    public AppSharedPreferences(Context context)
    {
        this.context = context;
        preferences = this.context.getSharedPreferences(PREFERENCES_NAME, PRIVATE_MODE);
        editor = preferences.edit();
    }

    public String getFCMToken() {
        return this.preferences.getString(FCM_TOKEN, "");
    }

    public void setFCMToken(String deviceToken)
    {
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

    public void clearSharedPreferences(Context context)
    {
        File dir = new File(context.getFilesDir().getParent() + "/shared_prefs/");
        context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().apply();

        //delete the file
        if (dir.exists() && dir.isDirectory())
        {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++)
            {
                if(children[i].replace(".xml", "").equals(PREFERENCES_NAME))
                {
                    context.getSharedPreferences(PREFERENCES_NAME, Context.MODE_PRIVATE).edit().clear().apply();
                    new File(dir, children[i]).delete();
                }
            }
        }
    }
}