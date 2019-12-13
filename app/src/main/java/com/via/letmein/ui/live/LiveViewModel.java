package com.via.letmein.ui.live;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.LockRepository;

public class LiveViewModel extends AndroidViewModel {
    private final LockRepository lockRepository;

    public LiveViewModel(@NonNull Application application) {
        super(application);
        lockRepository = LockRepository.getInstance(Session.getInstance(application));
    }

    public LiveData<ApiResponse> openDoor(String sessionId) {
        return lockRepository.openDoor(sessionId);
    }
}
