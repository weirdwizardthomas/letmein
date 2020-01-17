package com.via.letmein.ui.promotion;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.model.HouseholdMember;
import com.via.letmein.persistence.repository.SessionRepository;

public class PromotionViewModel extends AndroidViewModel {

    private final SessionRepository sessionRepository;

    private String sessionID;
    private HouseholdMember householdMember;

    public PromotionViewModel(@NonNull Application application) {
        super(application);
        sessionRepository = SessionRepository.getInstance(Session.getInstance(application));
        householdMember = new HouseholdMember();
    }

    public void setSessionID(String sessionID) {
        this.sessionID = sessionID;
    }

    public String getSessionID() {
        return sessionID;
    }

    public void setMember(HouseholdMember householdMember) {
        this.householdMember = householdMember;
    }

    public String getIpAddress(){
        return sessionRepository.getIpAddress();
    }
}
