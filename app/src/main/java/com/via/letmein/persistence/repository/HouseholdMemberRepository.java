package com.via.letmein.persistence.repository;

import androidx.lifecycle.MutableLiveData;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.via.letmein.persistence.api.Api;
import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.ServiceGenerator;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.HouseholdMember;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Repository for Household members from the server.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HouseholdMemberRepository {

    public static final String ERROR_MISSING_REQUIRED_PARAMETERS = "missing_request_parameters";
    public static final String ERROR_EXPIRED_SESSION_ID = "expired_session_id";
    public static final String ERROR_DATABASE_ERROR = "database_error";

    /**
     * Single instance of the class.
     */
    private static HouseholdMemberRepository instance;

    /**
     * Retrieved data.
     */
    private MutableLiveData<ApiResponse> data;

    /**
     * API to which requests are sent.
     */
    private Api api;

    private HouseholdMemberRepository(Session session) {
        api = ServiceGenerator.getApi(session.getIpAddress());
        ApiResponse dummy = new ApiResponse();
        data = new MutableLiveData<>(dummy);
    }

    /**
     * Returns the class' single instance.
     *
     * @return Singleton instance of this class.
     */
    public static synchronized HouseholdMemberRepository getInstance(Session session) {
        if (instance == null)
            instance = new HouseholdMemberRepository(session);
        return instance;
    }

    /**
     * Retrieves all received household members
     *
     * @param sessionId ID of the current session
     * @return {@see ApiResponse} with content of list of  if there is no error,
     */
    public MutableLiveData<ApiResponse> getAllHouseholdMembers(String sessionId) {
        refreshUsers(sessionId);
        return data;
    }

    /**
     * Sends an asynchronous call to the server to retrieve users
     *
     * @param sessionId current session ID
     */
    private void refreshUsers(String sessionId) {
        Call<ApiResponse> call = api.getUserList(sessionId);

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
                        //Convert 'content' to List<HouseholdMember>
                        TypeToken<List<HouseholdMember>> responseTypeToken = new TypeToken<List<HouseholdMember>>() {
                        };
                        List<HouseholdMember> responseList = gson
                                .fromJson(gson.toJson(dummy.getContent()), responseTypeToken.getType());

                        dummy.setContent(responseList);
                    }
                    //save the value
                    data.setValue(dummy);
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {

            }
        });

    }
}
