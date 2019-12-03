package com.via.letmein.ui.administration;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.via.letmein.persistence.api.ApiResponse;
import com.via.letmein.persistence.api.Session;
import com.via.letmein.persistence.api.pojo.response.HouseholdMember;
import com.via.letmein.persistence.repository.HouseholdMemberRepository;

/**
 * View model of the Administration fragment.
 *
 * @author Tomas Koristka: 291129@via.dk
 */
public class AdministrationViewModel extends AndroidViewModel {
    //todo document
    private HouseholdMemberRepository repository;

    public AdministrationViewModel(@NonNull Application application) {
        super(application);
        repository = HouseholdMemberRepository.getInstance(Session.getInstance(application));
    }

    /**
     * Retrieves data from the repository
     *
     * @param sessionId ID of the current session
     * @return All {@see HouseholdMember} retrieved from the server.
     */
    public LiveData<ApiResponse> getAllHouseholdMembers(String sessionId) {
        return repository.getAllHouseholdMembers(sessionId);
    }

    //todo document
    public void delete(HouseholdMember householdMember) {
        //TODO send a delete request
    }
}
