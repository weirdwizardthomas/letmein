package com.via.letmein.ui.live;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.CameraStreamRepository;
import com.via.letmein.persistence.repository.LockRepository;

public class LiveViewModel extends AndroidViewModel {
    private final LockRepository lockRepository;
    private final CameraStreamRepository cameraStreamRepository;

    public LiveViewModel(@NonNull Application application) {
        super(application);
        lockRepository = LockRepository.getInstance(Session.getInstance(application));
        cameraStreamRepository = CameraStreamRepository.getInstance(Session.getInstance(application));
    }

    public LiveData<ApiResponse> openDoor(String sessionId) {
        return lockRepository.openDoor(sessionId);
    }

    /**
     * Gets the Url on which the camera feed is streamed
     *
     * @param sessionId Current session's id
     * @return Observable Live data for responses
     */
    public LiveData<ApiResponse> getStreamUrl(String sessionId) {
        return cameraStreamRepository.getStreamUrl(sessionId);
    }
}
