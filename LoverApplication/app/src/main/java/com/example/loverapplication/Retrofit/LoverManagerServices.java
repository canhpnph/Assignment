package com.example.loverapplication.Retrofit;

import com.example.loverapplication.Model.Lover;
import com.example.loverapplication.Model.LoverType;
import com.example.loverapplication.Model.ResMessage;
import com.example.loverapplication.Model.User;

import java.util.List;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface LoverManagerServices {
    // method of lover model
    @GET("list-lover")
    Call<List<Lover>> getListLover(@Query("_id") String id);

    @GET("list-lover")
    Call<List<Lover>> getLoverWithIdUser(
            @Query("loverOf") String id_User );

    @Multipart
    @POST("add-lover")
    Call<ResMessage> addNewLover(
            @Part MultipartBody.Part name,
            @Part MultipartBody.Part phone,
            @Part MultipartBody.Part age,
            @Part MultipartBody.Part weight,
            @Part MultipartBody.Part height,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part about,
            @Part MultipartBody.Part type,
            @Part MultipartBody.Part loverOf
    );

    @Multipart
    @PUT("update-lover-{id}")
    Call<ResMessage> updateLover(
            @Path("id") String id,
            @Part MultipartBody.Part name,
            @Part MultipartBody.Part phone,
            @Part MultipartBody.Part age,
            @Part MultipartBody.Part weight,
            @Part MultipartBody.Part height,
            @Part MultipartBody.Part image,
            @Part MultipartBody.Part about,
            @Part MultipartBody.Part type
    );

    @DELETE("delete-lover-{id}")
    Call<ResMessage> deleteLover(
            @Path("id") String id
    );


    // method of lover type model
    @GET("list-loverType")
    Call<List<LoverType>> getListLoverType();

    @POST("add-loverType")
    Call<ResMessage> addNewLoverType(@Body LoverType loverType);

    @PUT("update-loverType-{id}")
    Call<ResMessage> updateLoverType(
            @Path("id") String id,
            @Body LoverType loverType);

    @DELETE("delete-loverType-{id}")
    Call<ResMessage> deleteLoverType(
            @Path("id") String id
    );

    // method of user model
    @GET("list-user")
    Call<List<User>> getListUser(@Query("username") String username);

//    @POST("check-phone")
//    Call<ResMessage> checkPhoneNumber(@Body User user);

    @POST("check-username")
    Call<ResMessage> checkUserName(
            @Body User user);

    @POST("register")
    Call<ResMessage> register(@Body User user);

    @POST("login")
    Call<User> login(@Body User user);

    @GET("profile")
    Call<User> profile(@Header("Authorization") String token);

    @POST("logout")
    Call<ResMessage> logout(@Header("Authorization") String token);

    @Multipart
    @PUT("update-info-{id}")
    Call<ResMessage> updateInfo(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Part MultipartBody.Part fullname,
            @Part MultipartBody.Part date,
            @Part MultipartBody.Part image
    );

    @PUT("update-password-{id}")
    Call<ResMessage> updatePassword(
            @Header("Authorization") String token,
            @Path("id") String id,
            @Body User user
    );

    @POST("login-success")
    Call<ResMessage> login_success(
            @Body User user
    );

}
