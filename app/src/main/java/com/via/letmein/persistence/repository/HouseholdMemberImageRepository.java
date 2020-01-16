package com.via.letmein.persistence.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.api.request.PromotionJson;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for the image URLs of images on the server.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HouseholdMemberImageRepository {

    /**
     * Single instance of the class
     */
    private static HouseholdMemberImageRepository instance;
    /**
     * Retrieved data
     */
    private final MutableLiveData<ApiResponse> imagePathsData;
    private final MutableLiveData<ApiResponse> promotionData;
    /**
     * API to which requests are sent.
     */
    private final Api api;

    private HouseholdMemberImageRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
        imagePathsData = new MutableLiveData<>(new ApiResponse());
        promotionData = new MutableLiveData<>(new ApiResponse());
    }

    /**
     * Returns the class' single instance.
     *
     * @return Singleton instance of this class.
     */
    public static synchronized HouseholdMemberImageRepository getInstance(Session session) {
        if (instance == null)
            instance = new HouseholdMemberImageRepository(session);
        return instance;
    }

    /**
     * Retrieves all the user's images urls
     *
     * @param username  Username of the logged in user
     * @param sessionId Generated sessionID from server
     * @return {@see ApiResponse} having {@see ApiResponse#content} of {@see List} of URL {@see String} if there was no {@see ApiResponse#error}, 0 otherwise
     */
    public LiveData<ApiResponse> getImagePaths(String username, String sessionId) {
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
                        List<String> content =
                                gson.fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                        dummy.setContent(content);
                    } else
                        dummy.setContent(0);

                    imagePathsData.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

        return imagePathsData;
    }


}
