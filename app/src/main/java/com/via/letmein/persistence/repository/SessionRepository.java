package com.via.letmein.persistence.repository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.response.ApiResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionRepository {

    private static SessionRepository instance;
    private Api api;
    private MutableLiveData<ApiResponse> registrationData;
    private MutableLiveData<ApiResponse> loginData;

    private SessionRepository() {
        api = ServiceGenerator.getApi();
        registrationData = new MutableLiveData<>(new ApiResponse());
        loginData = new MutableLiveData<>(new ApiResponse());
    }

    public static synchronized SessionRepository getInstance() {
        if (instance == null)
            instance = new SessionRepository();
        return instance;
    }

    public LiveData<ApiResponse> getRegistration(String username, String serialNumber) {
        refresh(api.registerAdministrator(username, serialNumber), registrationData);
        return registrationData;
    }

    public LiveData<ApiResponse> getSessionID(String username, String password) {
        refresh(api.loginAdministrator(username, password), loginData);
        return loginData;
    }

    private void refresh(Call<ApiResponse> call, final MutableLiveData<ApiResponse> target) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    Gson gson = new GsonBuilder().create();

                    if (dummy.isError()) {
                        dummy.setContent(0);
                    } else {
                        TypeToken<String> responseTypeToken = new TypeToken<String>() {
                        };
                        String content = gson
                                .fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                        dummy.setContent(content);
                    }

                    //todo refresh the other one you retard
                    target.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });


    }
}
