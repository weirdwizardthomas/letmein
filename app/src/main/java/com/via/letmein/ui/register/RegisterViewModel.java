package com.via.letmein.ui.register;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.SessionRepository;

/**
 * ViewModel for {@see RegisterActivity}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class RegisterViewModel extends AndroidViewModel {
    private final SessionRepository repository;

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        repository = SessionRepository.getInstance(Session.getInstance(application));
    }

    public LiveData<ApiResponse> register(String username, String serialNumber) {
        return repository.register(username, serialNumber);
    }

    public RegisterViewModel setIpAddress(String ipAddress) {
        repository.setIpAddress(ipAddress);
        return this;
    }

    public RegisterViewModel setPassword(String password) {
        repository.setPassword(password);
        return this;
    }

    public RegisterViewModel setRegistered() {
        repository.setRegistered();
        return this;
    }

    public RegisterViewModel setUsername(String username) {
        repository.setUsername(username);
        return this;
    }
}
