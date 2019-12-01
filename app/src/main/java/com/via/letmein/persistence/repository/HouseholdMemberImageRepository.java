package com.via.letmein.persistence.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for the image URLs of images on the server.
 */
public class HouseholdMemberImageRepository {

    private static final String ERROR_MISSING_REQUEST_PARAMETERS = "missing_request_parameters";
    private static final String ERROR_EXPIRED_SESSION_ID = "expired_session_id";
    private static final String ERROR_DATABASE_ERROR = "database_error";

    /**
     * Single instance of the class
     */
    private static HouseholdMemberImageRepository instance;
    /**
     * Retrieved data
     */
    private MutableLiveData<ApiResponse> data;
    /**
     * API to which requests are sent.
     */
    private Api api;

    private HouseholdMemberImageRepository() {
        api = ServiceGenerator.getApi();
        ApiResponse dummy = new ApiResponse();
        data = new MutableLiveData<>(dummy);
    }

    /**
     * Returns the class' single instance.
     *
     * @return Singleton instance of this class.
     */
    public static synchronized HouseholdMemberImageRepository getInstance() {
        if (instance == null)
            instance = new HouseholdMemberImageRepository();
        return instance;
    }

    /**
     * Sends an asynchronous call to the server to retrieve user's images urls
     *
     * @param username  Username of the logged in user
     * @param sessionId Generated sessionID from server
     */
    private void refresh(String username, String sessionId) {

        Call<ApiResponse> call = api.getUserImagesList(username, sessionId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    Gson gson = new GsonBuilder().create();

                    if (!dummy.isError()) {
                        TypeToken<List<String>> responseTypeToken = new TypeToken<List<String>>() {
                        };
                        List<String> content = gson
                                .fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                        dummy.setContent(content);
                    } else
                        dummy.setContent(0);

                    data.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }

    //TODO SEND ID?

    /**
     * Retrieves all the user's images urls
     *
     * @param username  Username of the logged in user
     * @param sessionId Generated sessionID from server
     * @return {@see ApiResponse} having {@see ApiResponse#content} of {@see List} of URL {@see String} if there was no {@see ApiResponse#error}, 0 otherwise
     */
    public LiveData<ApiResponse> getImagePaths(String username, String sessionId) {
        refresh(username, sessionId);
        return data;
    }

}
