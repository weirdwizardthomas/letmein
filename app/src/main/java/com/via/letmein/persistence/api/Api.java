package com.via.letmein.persistence.api;

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

    String PASSWORD = "password";
    String SERIAL_ID = "serial_id";
    String SESSION_ID = "session_id";
    String USERNAME = "username";

    @POST("register")
    Call<ApiResponse> registerAdministrator(@Query(USERNAME) String username, @Query(SERIAL_ID) String serialNo);

    @GET("users")
    Call<ApiResponse> getUserList(@Query(SESSION_ID) String sessionId);

    @GET("user/{username}/images")
    Call<ApiResponse> getUserImagesList(@Path(USERNAME) String username, @Query(SESSION_ID) String sessionId);

    @GET("login")
    Call<ApiResponse> loginAdministrator(@Query(USERNAME) String username, @Query(PASSWORD) String password);
}
