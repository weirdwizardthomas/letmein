package com.via.letmein.persistence.api;

import com.via.letmein.persistence.api.request.BiometricJson;
import com.via.letmein.persistence.api.request.CreateMemberJson;
import com.via.letmein.persistence.api.request.PromotionJson;
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
    int PORT = 8080;
    String HTTP = "http:/";
    String ADDRESS_PORT_DELIMITER = ":";
    String API_PATH = "/api/";
    String QUERY_DELIMITER = "?";
    String PARAMETER_DELIMITER = "=";

    String SESSION_ID = "session_id";
    String USER_NAME = "user_name";
    String PASSWORD = "password";
    String USER_ID = "userid";
    String NOTIFICATION_ID = "notification_id";
    String BEGIN_DATE = "begin_date";
    String END_DATE = "end_date";
    String IMAGE_ID = "imageid";

    @POST("register")
    Call<ApiResponse> register(@Body RegisterJson registerJson);

    @GET("login")
    Call<ApiResponse> login(@Query(USER_NAME) String username,
                            @Query(PASSWORD) String password);

    @POST("admin")
    Call<ApiResponse> promoteAdmin(@Body PromotionJson promotionJson);

    @GET("admin/confirm")
    Call<ApiResponse> acceptAdminPromotion(@Query(SESSION_ID) String sessionId);

    @POST("user")
    Call<ApiResponse> createUser(@Body CreateMemberJson createMemberJson);

    @POST("user/biometric")
    Call<ApiResponse> startBiometricData(@Body BiometricJson biometricJson);

    @GET("users")
    Call<ApiResponse> getUserList(@Query(SESSION_ID) String sessionId);

    @GET("user/{userid}/images")
    Call<ApiResponse> getUserImagesList(@Path(USER_ID) String username,
                                        @Query(SESSION_ID) String sessionId);

    @GET("user/{userid}/face/{imageid}")
    Call<ApiResponse> getFaceImage(@Path(USER_ID) int userId,
                                   @Path(IMAGE_ID) int imageId,
                                   @Query(SESSION_ID) String sessionId);

    @GET("user/{userid}/profile/image")
    Call<ApiResponse> getProfileImage(@Path(USER_ID) int userId,
                                      @Query(SESSION_ID) String sessionId);

    @POST("user/{userid}/profile/image")
    Call<ApiResponse> setProfileImage(@Path(USER_ID) int userId,
                                      @Query(SESSION_ID) String sessionId);

    @GET("door")
    Call<ApiResponse> openDoor(@Query(SESSION_ID) String sessionId);

    @GET("logs")
    Call<ApiResponse> getLog(@Query(SESSION_ID) String sessionId,
                             @Query(BEGIN_DATE) String startingDate,
                             @Query(END_DATE) String endingDate);

    @GET("notifications")
    Call<ApiResponse> getNotificationLog(@Query(SESSION_ID) String sessionId);

    @PUT("notification/{notification_id}")
    Call<ApiResponse> markNotificationAsRead(@Query(SESSION_ID) String sessionId,
                                             @Path(NOTIFICATION_ID) int notificationId);

}
