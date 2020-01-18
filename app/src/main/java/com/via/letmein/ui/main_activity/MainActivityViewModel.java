package com.via.letmein.ui.main_activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.SessionRepository;

/**
 * ViewModel for {@see MainActivity}
 */
public class MainActivityViewModel extends AndroidViewModel {

    private final SessionRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = SessionRepository.getInstance(Session.getInstance(application));
    }

    public LiveData<ApiResponse> getSessionID(String username, String password) {
        MutableLiveData<ApiResponse> liveData = new MutableLiveData<>(new ApiResponse());
        repository.getSessionID(username, password, liveData);
        return liveData;
    }

    public void setSessionID(String sessionID) {
        repository.setSessionID(sessionID);
    }


    public String getUsername() {
        return repository.getUsername();
    }

    public String getPassword() {
        return repository.getPassword();
    }

    /**
     * Removes all session related data
     */
    public void wipeSession() {
        repository.wipeSession();
    }
}
