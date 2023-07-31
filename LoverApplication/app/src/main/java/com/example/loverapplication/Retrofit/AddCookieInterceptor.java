package com.example.loverapplication.Retrofit;

import java.io.IOException;
import java.util.List;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class AddCookieInterceptor implements Interceptor {
    String sessionCookie;

    public AddCookieInterceptor(String sessionCookie) {
        this.sessionCookie = sessionCookie;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request originalRequest = chain.request();

        Request.Builder requestBuilder = originalRequest.newBuilder()
                .header("Cookie", sessionCookie)
                .method(originalRequest.method(), originalRequest.body());

        Request request = requestBuilder.build();
        return chain.proceed(request);

    }
}
