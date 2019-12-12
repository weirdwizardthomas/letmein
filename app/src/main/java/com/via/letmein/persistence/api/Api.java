package com.via.letmein.persistence.api;

import com.via.letmein.persistence.api.request.CreateMemberJson;
import com.via.letmein.persistence.api.request.RegisterJson;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
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
    String USER_ID = "userid";

    int PORT = 8080;
    String HTTP = "http:/";
    String ADDRESS_PORT_DELIMITER = ":";
    String API_PATH = "/api/";
    String QUERY_DELIMITER = "?";
    String PARAMETER_DELIMITER = "=";
    String NOTIFICATION_ID = "notification_id";

    @POST("register")
    Call<ApiResponse> register(@Body RegisterJson registerJson);

    @GET("login")
    Call<ApiResponse> login(@Query(USER_NAME) String username, @Query(PASSWORD) String password);

    @GET("users")
    Call<ApiResponse> getUserList(@Query(SESSION_ID) String sessionId);

    @GET("user/{userid}/images")
    Call<ApiResponse> getUserImagesList(@Path(USER_ID) String username, @Query(SESSION_ID) String sessionId);

    @POST("user")
    Call<ApiResponse> createUser(@Body CreateMemberJson createMemberJson);

    @GET("door")
    Call<ApiResponse> openDoor(@Query(SESSION_ID) String sessionId);

    @GET("video")
    Call<ApiResponse> getStreamUrl(@Query(SESSION_ID) String sessionId);

    @GET("logs")
    Call<ApiResponse> getLog(@Query(SESSION_ID) String sessionId, @Query("begin_date") String startingDate, @Query("end_date") String endingDate);

    @GET("notifications")
    Call<ApiResponse> getNotificationLog(@Query(SESSION_ID) String sessionId);

    @PUT("notification/{notification_id}")
    Call<ApiResponse> markNotificationAsRead(@Query(SESSION_ID) String sessionId, @Path(NOTIFICATION_ID) int notificationId);
}
