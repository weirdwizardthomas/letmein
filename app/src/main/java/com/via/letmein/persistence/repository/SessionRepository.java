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
import com.via.letmein.persistence.api.request.RegisterJson;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SessionRepository {

    public static final String ERROR_MISSING_REQUIRED_PARAMETERS = "missing_request_parameters";
    public static final String ERROR_SHORT_USERNAME = "short_username_length";
    public static final String ERROR_DATABASE_ERROR = "database_error";
    public static final String ERROR_ADMIN_ALREADY_EXISTS = "admin_already_exists";
    public static final String ERROR_WRONG_SERIAL_ID = "wrong_serial_id";
    public static final String ERROR_NAME_ALREADY_IN_USE = "name_already_in_use";
    public static final String ERROR_MEMBER_IS_ALREADY_AN_ADMIN = "member_is_already_an_admin";
    public static final String ERROR_WRONG_USER_PASSWORD = "wrong_user_password";

    private static SessionRepository instance;
    private final MutableLiveData<ApiResponse> registrationData;
    private final MutableLiveData<ApiResponse> loginData;
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
        refresh(api.registerAdministrator(new RegisterJson(username, serialNumber)), registrationData);
        return registrationData;
    }

    public LiveData<ApiResponse> getSessionID(String username, String password) {
        Api api = ServiceGenerator.getApi(session.getIpAddress());
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

                    target.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
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
}
