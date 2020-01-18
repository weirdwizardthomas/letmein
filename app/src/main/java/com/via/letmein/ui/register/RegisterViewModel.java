package com.via.letmein.ui.register;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.HouseholdMemberRepository;
import com.via.letmein.persistence.repository.SessionRepository;

/**
 * ViewModel for {@see RegisterActivity}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class RegisterViewModel extends AndroidViewModel {
    private final SessionRepository sessionRepository;
    private HouseholdMemberRepository householdMemberRepository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        sessionRepository = SessionRepository.getInstance(Session.getInstance(application));
        householdMemberRepository = null/*HouseholdMemberRepository.getInstance(Session.getInstance(application))*/;
    }

    public LiveData<ApiResponse> register(String username, String serialNumber) {
        MutableLiveData<ApiResponse> liveData = new MutableLiveData<>(new ApiResponse());
        sessionRepository.register(username, serialNumber, liveData);
        return liveData;
    }

    public RegisterViewModel setIpAddress(String ipAddress) {
        sessionRepository.setIpAddress(ipAddress);
        return this;
    }

    public RegisterViewModel setRegistered() {
        sessionRepository.setRegistered();
        return this;
    }

    public LiveData<ApiResponse> getSessionID(String username, String password) {
        MutableLiveData<ApiResponse> liveData = new MutableLiveData<>(new ApiResponse());
        sessionRepository.getSessionID(username, password, liveData);
        return liveData;
    }

    public String getSessionID() {
        return sessionRepository.getSessionID();
    }

    public void setSessionID(String sessionID) {
        sessionRepository.setSessionID(sessionID);
    }

    public LiveData<ApiResponse> addBiometricData(int userId, String sessionId) {
        MutableLiveData<ApiResponse> liveData = new MutableLiveData<>(new ApiResponse());
        householdMemberRepository.addBiometricData(userId, sessionId, liveData);
        return liveData;
    }

    public String getUsername() {
        return sessionRepository.getUsername();
    }

    public int getId() {
        return sessionRepository.getUserID();
    }

    public RegisterViewModel setUsername(String username) {
        sessionRepository.setUsername(username);
        return this;
    }

    public String getPassword() {
        return sessionRepository.getPassword();
    }

    public RegisterViewModel setPassword(String password) {
        sessionRepository.setPassword(password);
        return this;
    }

    public void addHouseholdRepository(Context context) {
        householdMemberRepository = HouseholdMemberRepository.getInstance(Session.getInstance(context));
    }

    public RegisterViewModel setId(int id) {
        sessionRepository.setUserID(id);
        return this;
    }

}
