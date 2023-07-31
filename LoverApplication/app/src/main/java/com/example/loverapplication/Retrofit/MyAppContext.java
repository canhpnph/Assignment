package com.example.loverapplication.Retrofit;

import android.content.Context;

public class MyAppContext {
    private static Context context;

    public static void setContext(Context ctx) {
        context = ctx.getApplicationContext();
    }

    public static Context getContext() {
        return context;
    }
}
