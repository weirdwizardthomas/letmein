package com.via.letmein.persistence.api;

import com.via.letmein.persistence.api.request.RegisterJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

/**
 * An interface for Retrofit to implement
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public interface Api {

    @POST("register")
    Call<ApiResponse> registerAdministrator(@Body RegisterJson registerJson);

    @GET("users")
    Call<ApiResponse> getUserList(@Query("session_id") String session_id);

    @GET("user/{username}/images")
    Call<ApiResponse> getUserImagesList(@Query("user_name") String user_name, @Query("session_id") String session_id);

    @GET("login")
    Call<ApiResponse> loginAdministrator(@Query("user_name") String user_name, @Query("password") String password);
}
