package com.example.loverapplication.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.loverapplication.Activity.LoginActivity;
import com.example.loverapplication.Activity.MainActivity;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    private static final String URL = "http://192.168.1.5:3000/api/";

    // add cookie
    public static LoverManagerServices managerServices() {
        Context context = MyAppContext.getContext();
        CookieManager cookieManager = new CookieManager(context);
        String cookie = cookieManager.getCookie().substring(1, cookieManager.getCookie().length() - 1 );

        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
        httpClient.addInterceptor(new AddCookieInterceptor(cookie));

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build();
        return retrofit.create(LoverManagerServices.class);
    }

    // not cookie ( register new account, login )
    public static LoverManagerServices servicesNoCookie() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        return retrofit.create(LoverManagerServices.class);
    }
}
