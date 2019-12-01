package com.via.letmein.ui.main_activity;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.repository.SessionRepository;

public class MainActivityViewModel extends AndroidViewModel {

    private SessionRepository repository;

    public MainActivityViewModel(@NonNull Application application) {
        super(application);
        repository = SessionRepository.getInstance();
    }

    public LiveData<ApiResponse> getSessionID(String username, String password) {
        return repository.getSessionID(username, password);
    }

}
