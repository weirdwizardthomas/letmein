package com.via.letmein.persistence.repository;

import androidx.core.util.Pair;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.Log;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LogRepository {

    private static LogRepository instance;

    private final Api api;

    private MutableLiveData<ApiResponse> data;

    private LogRepository(Session session) {
        // api = ServiceGenerator.getApi(session.getIpAddress());
        api = ServiceGenerator.getMockupApi();
        data = new MutableLiveData<>(new ApiResponse());
    }

    public static synchronized LogRepository getInstance(Session session) {
        if (instance == null)
            instance = new LogRepository(session);
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
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    Gson gson = new GsonBuilder().create();

                    //Check for error & resolve
                    if (dummy.isError())
                        dummy.setContent(0);
                    else {
                        TypeToken<List<Log>> responseTypeToken = new TypeToken<List<Log>>() {
                        };
                        List<Log> token = gson.fromJson(
                                gson.toJson(dummy.getContent()),
                                responseTypeToken.getType());
                        dummy.setContent(token);
                    }
                    data.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
