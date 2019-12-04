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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CameraStreamRepository {

    private static CameraStreamRepository instance;

    private final Api api;
    private final MutableLiveData<ApiResponse> data;

    private CameraStreamRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
        data = new MutableLiveData<>(new ApiResponse());
    }

    public static synchronized CameraStreamRepository getInstance(Session session) {
        if (instance == null)
            instance = new CameraStreamRepository(session);
        return instance;
    }

    public LiveData<ApiResponse> getStreamUrl(String sessionId) {
        refresh(sessionId);
        return data;
    }

    private void refresh(String sessionId) {
        Call<ApiResponse> call = api.getStreamUrl(sessionId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse dummy = response.body();
                Gson gson = new GsonBuilder().create();

                //Check for error & resolve
                if (dummy.isError())
                    dummy.setContent(0);
                else {
                    TypeToken<String> responseTypeToken = new TypeToken<String>() {
                    };
                    String token = gson.fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                    dummy.setContent(token);
                }
                data.setValue(dummy);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
