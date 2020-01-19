package com.via.letmein.persistence.repository;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.api.request.BiometricJson;
import com.via.letmein.persistence.api.request.PromotionJson;
import com.via.letmein.persistence.model.AdminPromotion;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PromotionRepository {
    /**
     * Single instance of the class.
     */
    private static PromotionRepository instance;
    /**
     * API to which requests are sent.
     */
    private Api api;


    private PromotionRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
    }

    public static synchronized PromotionRepository getInstance(Session session) {
        if (instance == null)
            instance = new PromotionRepository(session);
        return instance;
    }

    public void setIpAddress(String ipAddress) {
        api = ServiceGenerator.getApi(ipAddress);
    }

    public LiveData<ApiResponse> requestPromotion(String sessionID, int userID) {
        MutableLiveData<ApiResponse> data = new MutableLiveData<>(new ApiResponse());
        Call<ApiResponse> call = api.promoteAdmin(new PromotionJson(userID, sessionID));
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

    public LiveData<ApiResponse> confirmPromotion(String sessionID) {
        MutableLiveData<ApiResponse> data = new MutableLiveData<>(new ApiResponse());

        Call<ApiResponse> call = api.confirmPromotion(sessionID);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    Gson gson = new GsonBuilder().create();

                    if (dummy.isError())
                        dummy.setContent(0);
                    else {
                        TypeToken<AdminPromotion> responseTypeToken = new TypeToken<AdminPromotion>() {
                        };
                        AdminPromotion content =
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

    public LiveData<ApiResponse> addBiometricData(int userID, String sessionID) {
        MutableLiveData<ApiResponse> data = new MutableLiveData<>(new ApiResponse());
        Call<ApiResponse> call = api.startBiometricData(new BiometricJson(userID, sessionID));
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();

                    Gson gson = new GsonBuilder().create();

                    //Check for error & resolve
                    if (dummy.isError())
                        dummy.setContent(0);
                    else {
                        TypeToken<String> responseTypeToken = new TypeToken<String>() {
                        };
                        String responseString = gson.fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                        dummy.setContent(responseString);
                    }
                    //save the value
                    data.setValue(dummy);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {

            }
        });

        return data;
    }
}
