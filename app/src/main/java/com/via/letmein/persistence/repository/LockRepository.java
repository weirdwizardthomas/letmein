package com.via.letmein.persistence.repository;

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

public class LockRepository {
    /**
     * Single instance of the class.
     */
    private static LockRepository instance;

    /**
     * Retrieved memberListLiveData.
     */
    private final MutableLiveData<ApiResponse> responseData;

    /**
     * API to which requests are sent.
     */
    private final Api api;

    private LockRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
        responseData = new MutableLiveData<>(new ApiResponse());
    }

    /**
     * Returns the class' single instance.
     *
     * @return Singleton instance of this class.
     */
    public static synchronized LockRepository getInstance(Session session) {
        if (instance == null)
            instance = new LockRepository(session);
        return instance;
    }


    public LiveData<ApiResponse> openDoor(String sessionId) {
        refresh(sessionId);
        return responseData;
    }

    private void refresh(String sessionId) {
        Call<ApiResponse> call = api.openDoor(sessionId);
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                ApiResponse dummy = response.body();
                Gson gson = new GsonBuilder().create();

                //Check for error & resolve
                if (dummy.isError())
                    dummy.setContent(0);
                else
                    dummy.setContent("");

                responseData.setValue(dummy);
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });
    }
}
