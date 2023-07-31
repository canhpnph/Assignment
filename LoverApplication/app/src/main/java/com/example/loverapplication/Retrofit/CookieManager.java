package com.example.loverapplication.Retrofit;

import android.content.Context;
import android.content.SharedPreferences;

public class CookieManager {
    private static final String SHARED_PREF_NAME = "user-login";
    private static final String COOKIE_KEY = "cookie";

    private SharedPreferences sharedPreferences;

    public CookieManager(Context context) {
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
    }

    public void saveCookie(String cookie) {
        sharedPreferences.edit().putString(COOKIE_KEY, cookie).commit();
    }

    public String getCookie() {
        return sharedPreferences.getString(COOKIE_KEY, "");
    }
}
