package com.via.letmein.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.response.ApiResponse;
import com.via.letmein.persistence.repository.SessionRepository;

public class RegisterViewModel extends AndroidViewModel {
    private SessionRepository repository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = SessionRepository.getInstance();
    }

    public LiveData<ApiResponse> register(String username, String serialNumber) {
        return repository.getRegistration(username, serialNumber);
    }
}
