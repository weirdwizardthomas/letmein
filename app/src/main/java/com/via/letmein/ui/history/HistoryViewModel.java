package com.via.letmein.ui.history;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.core.util.Pair;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.repository.LogRepository;

/**
 * ViewModel for {@See HistoryFragment}
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class HistoryViewModel extends AndroidViewModel {

    private final LogRepository repository;

    public HistoryViewModel(@NonNull Application application) {
        super(application);
        repository = LogRepository.getInstance(Session.getInstance(application));
    }

    public LiveData<ApiResponse> getVisits(String sessionId, Pair<Long, Long> dateRange) {
        return repository.getVisits(sessionId, dateRange);
    }

}
