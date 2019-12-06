package com.via.letmein.persistence.repository;

import android.util.Pair;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class VisitRepository2 {

    private static VisitRepository2 instance;

    private final Api api;

    private MutableLiveData<ApiResponse> data;

    private VisitRepository2(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
        data = new MutableLiveData<>(new ApiResponse());
    }

    public static synchronized VisitRepository2 getInstance(Session session) {
        if (instance == null)
            instance = new VisitRepository2(session);
        return instance;
    }

    public LiveData<ApiResponse> getVisits(String sessionId, Pair<Long, Long> dateRange) {
        refresh(sessionId, dateRange);
        return data;
    }

    private void refresh(String sessionId, Pair<Long, Long> dateRange) {
        Call<ApiResponse> call = api.getLog(sessionId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse dummy = response.body();
                Gson gson = new GsonBuilder().create();

                //Check for error & resolve
                if (dummy.isError())
                    dummy.setContent(0);
                else {
                   /* TypeToken<?> responseTypeToken = new TypeToken<?>() {
                    };
                    ? token = gson.fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());
                    dummy.setContent(token);
                */
                }
                data.setValue(dummy);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
