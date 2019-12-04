package com.via.letmein.persistence.api;

import com.via.letmein.persistence.api.request.CreateMemberJson;
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

    String SESSION_ID = "session_id";
    String USER_NAME = "user_name";
    String PASSWORD = "password";


    @POST("register")
    Call<ApiResponse> registerAdministrator(@Body RegisterJson registerJson);

    @GET("users")
    Call<ApiResponse> getUserList(@Query(SESSION_ID) String sessionId);

    @GET("user/{username}/images")
    Call<ApiResponse> getUserImagesList(@Query(USER_NAME) String username, @Query(SESSION_ID) String sessionId);

    @GET("login")
    Call<ApiResponse> loginAdministrator(@Query(USER_NAME) String username, @Query(PASSWORD) String password);

    @POST("user")
    Call<ApiResponse> createUser(@Body CreateMemberJson createMemberJson);

    @GET("door")
    Call<ApiResponse> openDoor(@Query(SESSION_ID) String sessionId);

    @GET("video")
    Call<ApiResponse> getStreamUrl(@Query(SESSION_ID) String sessionId);
}
