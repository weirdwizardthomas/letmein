package com.via.letmein.ui.live;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.LockRepository;

/**
 * ViewModel for the {@See LiveFragment}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class LiveViewModel extends AndroidViewModel {
    private final LockRepository lockRepository;

    public LiveViewModel(@NonNull Application application) {
        super(application);
        lockRepository = LockRepository.getInstance(Session.getInstance(application));
    }

    /**
     * Sends a request to the server to open the connected roo
     *
     * @param sessionId ID of the current session
     * @return Observable LiveData of server's response
     */
    public LiveData<ApiResponse> openDoor(String sessionId) {
        return lockRepository.openDoor(sessionId);
    }
}
