package com.via.letmein.persistence.api;

import com.via.letmein.persistence.api.response.ApiResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * An interface for Retrofit to implement
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public interface Api {

    String SESSION_ID = "session_id";

    @POST("register")
    Call<ApiResponse> registerAdministrator(@Query("user_name") String username, @Query("serial_id") String serialNo);

    @GET("users")
    Call<ApiResponse> getUserList(@Query(SESSION_ID) String sessionId);

    @GET("user/{username}/images")
    Call<ApiResponse> getUserImagesList(@Path("username") String username, @Query(SESSION_ID) String sessionId);

    @GET("login")
    Call<ApiResponse> loginAdministrator(@Query("user_name") String username, @Query("password") String password);
}
