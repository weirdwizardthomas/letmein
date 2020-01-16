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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionRepository {
    /**
     * Single instance of the class.
     */
    private static PromotionRepository instance;
    /**
     * Retrieved response data.
     */
    private final MutableLiveData<ApiResponse> data;
    /**
     * API to which requests are sent.
     */
    private final Api api;

    private PromotionRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
        data = new MutableLiveData<>(new ApiResponse());
    }

    public static synchronized PromotionRepository getInstance(Session session) {
        if (instance == null)
            instance = new PromotionRepository(session);
        return instance;
    }

    public LiveData<ApiResponse> getPromoteAdmin(String sessionId, int userId) {
        Call<ApiResponse> call = api.promoteAdmin(new PromotionJson(userId, sessionId));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    Gson gson = new GsonBuilder().create();

                    if (dummy.isError())
                        dummy.setContent(0);
                    else {
                        TypeToken<String> responseTypeToken = new TypeToken<String>() {
                        };
                        String content =
                                gson.fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                        dummy.setContent(content);
                    }

                    data.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

        return data;
    }
}
