package com.poona.agrocart.data.network;

import static com.poona.agrocart.app.AppConstants.AUTH_ACCESS_TOKEN;
import static com.poona.agrocart.app.AppConstants.BASE_URL;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.poona.agrocart.data.shared_preferences.AppSharedPreferences;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Rahul Dasi on 6/10/2020
 */
public class ApiClientAuth
{
    private static Retrofit retrofit = null;
    private static OkHttpClient okHttpClient;
    private static AppSharedPreferences appSharedPreferences;

    public static Retrofit getClient(Context context)
    {
        appSharedPreferences = new AppSharedPreferences(context);

        if(okHttpClient == null)
            initOkHttp(context);

        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }

    private static void initOkHttp(final Context context)
    {
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder()
                .callTimeout(2, TimeUnit.MINUTES)
                .connectTimeout(20, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .addInterceptor(new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY))
                .addInterceptor(chain ->
                {
                    Request original = chain.request();
                    Request.Builder requestBuilder = original.newBuilder()
                            .addHeader("Accept", "application/json")
                            .addHeader("Content-Type", "application/json");

                    // Adding Authorization token (API Key)
                    // Requests will be denied without API key
                    if (appSharedPreferences.getAuthorizationToken() != null
                            && !TextUtils.isEmpty(appSharedPreferences.getAuthorizationToken()))
                    {
                        Log.e(AUTH_ACCESS_TOKEN, appSharedPreferences.getAuthorizationToken());
                        requestBuilder.addHeader(AUTH_ACCESS_TOKEN, appSharedPreferences.getAuthorizationToken());
                    }

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                });


        okHttpClient = httpClient.build();
    }
}