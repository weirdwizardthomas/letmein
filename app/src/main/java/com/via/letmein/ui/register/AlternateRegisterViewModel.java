package com.via.letmein.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.PromotionRepository;
import com.via.letmein.persistence.repository.SessionRepository;

public class AlternateRegisterViewModel extends AndroidViewModel {

    private final SessionRepository sessionRepository;
    private final PromotionRepository promotionRepository;

    public AlternateRegisterViewModel(@NonNull Application application) {
        super(application);
        sessionRepository = SessionRepository.getInstance(Session.getInstance(application));
        promotionRepository = PromotionRepository.getInstance(Session.getInstance(application));
    }

    public AlternateRegisterViewModel setIpAddress(String ipAddress) {
        sessionRepository.setIpAddress(ipAddress);
        //Update the api's ip address
        promotionRepository.setIpAddress(ipAddress);
        return this;
    }

    public AlternateRegisterViewModel setSessionId(String sessionId) {
        sessionRepository.setSessionID(sessionId);
        return this;
    }


    public LiveData<ApiResponse> confirmPromotion() {
        return promotionRepository.confirmPromotion(sessionRepository.getSessionID());
    }

    public AlternateRegisterViewModel setPassword(String password) {
        sessionRepository.setPassword(password);
        return this;
    }

    public AlternateRegisterViewModel setRegistered() {
        sessionRepository.setRegistered();
        return this;
    }

    public String getUsername() {
        return sessionRepository.getUsername();
    }

    public String getSessionID() {
        return sessionRepository.getSessionID();
    }

    public int getId() {
        return sessionRepository.getUserID();
    }

    public LiveData<ApiResponse> addBiometricData() {
        return promotionRepository.addBiometricData(sessionRepository.getUserID(), sessionRepository.getSessionID());
    }

    public AlternateRegisterViewModel setUsername(String username) {
        sessionRepository.setUsername(username);
        return this;
    }

    public AlternateRegisterViewModel setUserID(int id) {
        sessionRepository.setUserID(id);
        return this;
    }
}