package com.via.letmein.persistence.api;

import com.via.letmein.persistence.pojo.request.LoginJson;
import com.via.letmein.persistence.pojo.request.RegisterJson;
import com.via.letmein.persistence.pojo.request.SessionIdJson;
import com.via.letmein.persistence.pojo.request.UserListJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * An interface for Retrofit to implement
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public interface Api {

    @POST("register")
    Call<ApiResponse> registerAdministrator(@Body RegisterJson registerJson);

    @GET("users")
    Call<ApiResponse> getUserList(@Body SessionIdJson sessionIdJson);

    @GET("user/{username}/images")
    Call<ApiResponse> getUserImagesList(@Body UserListJson userListJson);

    @GET("login")
    Call<ApiResponse> loginAdministrator(@Body LoginJson loginJson);
}
