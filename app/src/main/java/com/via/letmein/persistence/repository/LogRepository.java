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
import com.via.letmein.persistence.model.LoggedAction;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for retrieving logs from the server
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class LogRepository {

    public static final String DATE_FORMAT = "dd/MM/yyyy-HH:mm:ss";

    /**
     * Single instance of the class.
     */
    private static LogRepository instance;
    /**
     * API to which requests are sent.
     */
    private final Api api;

    private LogRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
    }

    public static synchronized LogRepository getInstance(Session session) {
        if (instance == null)
            instance = new LogRepository(session);
        return instance;
    }

    public LiveData<ApiResponse> getVisits(String sessionId, Pair<Long, Long> dateRange) {
        SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
        Pair<String, String> timestampRange = new Pair<>(
                sdf.format(new Timestamp(dateRange.first)),
                sdf.format(new Timestamp(dateRange.second)));

        MutableLiveData<ApiResponse> data = new MutableLiveData<>(new ApiResponse());

        Call<ApiResponse> call = api.getLog(sessionId, timestampRange.first, timestampRange.second);

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
                        TypeToken<List<LoggedAction>> responseTypeToken = new TypeToken<List<LoggedAction>>() {
                        };
                        List<LoggedAction> token = gson.fromJson(
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
        return data;
    }

}
