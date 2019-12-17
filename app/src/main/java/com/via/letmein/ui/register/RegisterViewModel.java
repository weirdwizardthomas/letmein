package com.via.letmein.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

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
    private final HouseholdMemberRepository householdMemberRepository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        sessionRepository = SessionRepository.getInstance(Session.getInstance(application));
        householdMemberRepository = HouseholdMemberRepository.getInstance(Session.getInstance(application));
    }

    public LiveData<ApiResponse> register(String username, String serialNumber) {
        return sessionRepository.register(username, serialNumber);
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
        return sessionRepository.getSessionID(username, password);
    }

    public void setSessionID(String sessionID) {
        sessionRepository.setSessionID(sessionID);
    }

    public LiveData<ApiResponse> addBiometricData(String username, String sessionId) {
        return householdMemberRepository.addBiometricData(username, sessionId);
    }

    public String getUsername() {
        return sessionRepository.getUsername();
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
}
