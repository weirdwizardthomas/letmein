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
import com.via.letmein.persistence.api.request.RegisterJson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionRepository {

    /**
     * Single instance of the class.
     */
    private static SessionRepository instance;
    /**
     * Retrieved registration response.
     */
    private final MutableLiveData<ApiResponse> registrationData;
    /**
     * Retrieved login response.
     */
    private final MutableLiveData<ApiResponse> loginData;
    /**
     * Application's session data
     */
    private final Session session;

    private SessionRepository(Session session) {
        this.session = session;
        registrationData = new MutableLiveData<>(new ApiResponse());
        loginData = new MutableLiveData<>(new ApiResponse());
    }

    public static synchronized SessionRepository getInstance(Session session) {
        if (instance == null)
            instance = new SessionRepository(session);
        return instance;
    }

    public LiveData<ApiResponse> register(String username, String serialNumber) {
        Api api = ServiceGenerator.getApi(session.getIpAddress());
        refresh(api.register(new RegisterJson(username, serialNumber)), registrationData);
        return registrationData;
    }

    public LiveData<ApiResponse> getSessionID(String username, String password) {
        Api api = ServiceGenerator.getApi(session.getIpAddress());
        refresh(api.login(username, password), loginData);
        return loginData;
    }

    private void refresh(Call<ApiResponse> call, final MutableLiveData<ApiResponse> target) {
        call.enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(@NonNull Call<ApiResponse> call, @NonNull Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse dummy = response.body();
                    Gson gson = new GsonBuilder().create();

                    if (dummy.isError()) {
                        dummy.setContent(0);
                    } else {
                        TypeToken<String> responseTypeToken = new TypeToken<String>() {
                        };
                        String content = gson.fromJson(
                                gson.toJson(dummy.getContent()),
                                responseTypeToken.getType());
                        dummy.setContent(content);
                    }

                    target.setValue(dummy);
                }
            }

            @Override
            public void onFailure(@NonNull Call<ApiResponse> call, @NonNull Throwable t) {
            }
        });


    }

    public void setIpAddress(String ipAddress) {
        session.setIpAddress(ipAddress);
    }

    public void setPassword(String password) {
        session.setPassword(password);
    }

    public void setRegistered() {
        session.setRegistered(true);
    }

    public void setUsername(String username) {
        session.setUsername(username);
    }

    public void setSessionID(String sessionID) {
        session.setSessionId(sessionID);
    }

    public void wipeSession() {
        session.wipeSession();
    }

    public String getUsername() {
        return session.getUsername();
    }

    public String getPassword() {
        return session.getPassword();
    }
}
